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

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cmsfacades.common.populator.LocalizedPopulator;
import de.hybris.platform.cmsfacades.data.CMSParagraphComponentData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * This populator will populate the {@link CMSParagraphComponentModel#setContent(String, Locale)} with the attribute
 * content from the {@link CMSParagraphComponentData}.
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class ParagraphComponentDataPopulator implements Populator<CMSParagraphComponentData, CMSParagraphComponentModel>
{
	private LocalizedPopulator localizedPopulator;

	@Override
	public void populate(final CMSParagraphComponentData source, final CMSParagraphComponentModel target)
			throws ConversionException
	{
		Optional.ofNullable(source.getContent()) //
		.ifPresent(content -> getLocalizedPopulator().populate( //
				(locale, value) -> target.setContent(value, locale), //
				(locale) -> content.get(getLocalizedPopulator().getLanguage(locale))));
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

}
