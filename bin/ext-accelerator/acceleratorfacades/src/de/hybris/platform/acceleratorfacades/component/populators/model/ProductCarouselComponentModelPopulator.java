/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.acceleratorfacades.component.populators.model;

import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.cmsfacades.common.populator.LocalizedPopulator;
import de.hybris.platform.cmsfacades.data.ProductCarouselComponentData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * This populator will populate the {@link ProductCarouselComponentModel} from {@link ProductCarouselComponentData}.
 */
public class ProductCarouselComponentModelPopulator
implements Populator<ProductCarouselComponentModel, ProductCarouselComponentData>
{
	private LocalizedPopulator localizedPopulator;
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Override
	public void populate(final ProductCarouselComponentModel source, final ProductCarouselComponentData target)
			throws ConversionException
	{
		final Map<String, String> titleMap = Optional.ofNullable(target.getTitle()).orElseGet(() -> getNewTitleMap(target));
		getLocalizedPopulator().populate( //
				(locale, value) -> titleMap.put(getLocalizedPopulator().getLanguage(locale), value),
				(locale) -> source.getTitle(locale));

		final List<String> productsData = source.getProducts().stream()
				.map(productData -> {
					return getUniqueItemIdentifierService().getItemData(productData).map(itemData -> {
						return itemData.getItemId();
					}).get();
				}).collect(Collectors.toList());
		target.setProducts(productsData);

		final List<String> categoriesData = source.getCategories().stream()
				.map(categoryData -> {
					return getUniqueItemIdentifierService().getItemData(categoryData).map(itemData -> {
						return itemData.getItemId();
					}).get();
				}).collect(Collectors.toList());
		target.setCategories(categoriesData);
	}

	protected Map<String, String> getNewTitleMap(final ProductCarouselComponentData target)
	{
		target.setTitle(new LinkedHashMap<>());
		return target.getTitle();
	}

	protected LocalizedPopulator getLocalizedPopulator()
	{
		return localizedPopulator;
	}

	@Required
	public void setLocalizedPopulator(final LocalizedPopulator localizedPopulator)
	{
		this.localizedPopulator = localizedPopulator;
	}

	protected UniqueItemIdentifierService getUniqueItemIdentifierService()
	{
		return uniqueItemIdentifierService;
	}

	@Required
	public void setUniqueItemIdentifierService(final UniqueItemIdentifierService uniqueItemIdentifierService)
	{
		this.uniqueItemIdentifierService = uniqueItemIdentifierService;
	}
}
