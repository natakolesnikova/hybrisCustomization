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
package de.hybris.platform.acceleratorfacades.component.populators.data;

import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.cmsfacades.common.populator.LocalizedPopulator;
import de.hybris.platform.cmsfacades.common.service.ProductCatalogItemModelFinder;
import de.hybris.platform.cmsfacades.data.ProductCarouselComponentData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * This populator will populate the {@link ProductCarouselComponentModel} from {@link ProductCarouselComponentData}. The
 * list of products and categories are retrieved by disabling the search restrictions temporarily and then enabling them
 * back once done.
 */
public class ProductCarouselComponentReversePopulator
implements Populator<ProductCarouselComponentData, ProductCarouselComponentModel>
{

	private LocalizedPopulator localizedPopulator;
	private ProductCatalogItemModelFinder productCatalogItemModelFinder;

	@Override
	public void populate(final ProductCarouselComponentData source, final ProductCarouselComponentModel target)
			throws ConversionException
	{
		Optional.ofNullable(source.getTitle()) //
		.ifPresent(title -> getLocalizedPopulator().populate( //
				(locale, value) -> target.setTitle(value, locale), //
				(locale) -> title.get(getLocalizedPopulator().getLanguage(locale))));

		target.setProducts(getProductCatalogItemModelFinder().getProductsForCompositeKeys(source.getProducts()));
		target.setCategories(getProductCatalogItemModelFinder().getCategoriesForCompositeKeys(source.getCategories()));
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

	protected ProductCatalogItemModelFinder getProductCatalogItemModelFinder()
	{
		return productCatalogItemModelFinder;
	}

	@Required
	public void setProductCatalogItemModelFinder(final ProductCatalogItemModelFinder productCatalogItemModelFinder)
	{
		this.productCatalogItemModelFinder = productCatalogItemModelFinder;
	}
}
