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
package de.hybris.platform.cmsfacades.items.populator.model;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cmsfacades.common.populator.LocalizedPopulator;
import de.hybris.platform.cmsfacades.data.CMSParagraphComponentData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default cms paragraph component implementation of {@link Populator}.
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class CmsParagraphComponentModelPopulator implements Populator<CMSParagraphComponentModel, CMSParagraphComponentData>
{
	private LocalizedPopulator localizedPopulator;

	@Override
	public void populate(final CMSParagraphComponentModel source, final CMSParagraphComponentData target)
			throws ConversionException
	{
		final Map<String, String> contentMap = Optional.ofNullable(target.getContent()).orElseGet(() -> getNewContentMap(target));
		getLocalizedPopulator().populate( //
				(locale, value) -> contentMap.put(getLocalizedPopulator().getLanguage(locale), value),
				(locale) -> source.getContent(locale));
	}

	protected Map<String, String> getNewContentMap(final CMSParagraphComponentData target)
	{
		target.setContent(new LinkedHashMap<>());
		return target.getContent();
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
