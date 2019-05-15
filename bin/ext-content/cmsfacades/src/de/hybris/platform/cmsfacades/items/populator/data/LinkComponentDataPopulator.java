/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cmsfacades.items.populator.data;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.enums.LinkTargets;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmsfacades.common.populator.LocalizedPopulator;
import de.hybris.platform.cmsfacades.data.CMSLinkComponentData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Strings;


/**
 * This populator will populate the {@link CMSLinkComponentModel#setLinkName(String, Locale)} with attribute linkName
 * {@link CMSLinkComponentData}. <br>
 * The external attribute is defaulted to false if not provided.
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class LinkComponentDataPopulator implements Populator<CMSLinkComponentData, CMSLinkComponentModel>
{
	private LocalizedPopulator localizedPopulator;
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Override
	public void populate(final CMSLinkComponentData dto, final CMSLinkComponentModel model) throws ConversionException
	{
		Optional.ofNullable(dto.getLinkName()) //
		.ifPresent(linkName -> getLocalizedPopulator().populate( //
				(locale, value) -> model.setLinkName(value, locale),
				(locale) -> linkName.get(getLocalizedPopulator().getLanguage(locale))));

		// Reset model
		model.setProduct(null);
		model.setCategory(null);
		model.setContentPage(null);
		model.setUrl(null);

		if (Objects.nonNull(dto.getCategory()))
		{
			getUniqueItemIdentifierService().getItemModel(dto.getCategory(), CategoryModel.class)
			.ifPresent(category -> model.setCategory(category));
		}
		else if (Objects.nonNull(dto.getContentPage()))
		{
			getUniqueItemIdentifierService().getItemModel(dto.getContentPage(), ContentPageModel.class)
			.ifPresent(contentPage -> model.setContentPage(contentPage));
		}
		else if (Objects.nonNull(dto.getProduct()))
		{
			getUniqueItemIdentifierService().getItemModel(dto.getProduct(), ProductModel.class)
			.ifPresent(product -> model.setProduct(product));
		}
		else
		{
			model.setUrl(dto.getUrl());
		}

		// when url is provided, then external is TRUE; FALSE otherwise
		model.setExternal(Strings.isNullOrEmpty(dto.getUrl()) ? Boolean.FALSE : Boolean.TRUE);
		model.setTarget(dto.isTarget() ? LinkTargets.NEWWINDOW : LinkTargets.SAMEWINDOW);
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
