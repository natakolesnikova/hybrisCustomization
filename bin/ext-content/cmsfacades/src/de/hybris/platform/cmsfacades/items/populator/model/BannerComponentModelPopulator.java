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
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.cmsfacades.common.populator.LocalizedPopulator;
import de.hybris.platform.cmsfacades.data.BannerComponentData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default banner component implementation of {@link Populator}.
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class BannerComponentModelPopulator implements Populator<BannerComponentModel, BannerComponentData>
{
	private LocalizedPopulator localizedPopulator;

	@Override
	public void populate(final BannerComponentModel source, final BannerComponentData target) throws ConversionException
	{
		final Map<String, String> mediaMap = Optional.ofNullable(target.getMedia()).orElseGet(() -> getNewMediaMap(target));
		getLocalizedPopulator().populate( //
				(locale, value) -> mediaMap.put(getLocalizedPopulator().getLanguage(locale), value), //
				(locale) -> Optional.ofNullable(source.getMedia(locale)).map(media -> media.getCode()).orElse(null));

		final Map<String, String> contentMap = Optional.ofNullable(target.getContent()).orElseGet(() -> getNewContentMap(target));
		getLocalizedPopulator().populate( //
				(locale, value) -> contentMap.put(getLocalizedPopulator().getLanguage(locale), value), //
				(locale) -> source.getContent(locale));

		final Map<String, String> headlineMap = Optional.ofNullable(target.getHeadline())
				.orElseGet(() -> getNewHeadlineMap(target));
		getLocalizedPopulator().populate( //
				(locale, value) -> headlineMap.put(getLocalizedPopulator().getLanguage(locale), value), //
				(locale) -> source.getHeadline(locale));

		target.setExternal(source.isExternal());
		target.setUrlLink(source.getUrlLink());
	}

	protected Map<String, String> getNewMediaMap(final BannerComponentData target)
	{
		target.setMedia(new LinkedHashMap<>());
		return target.getMedia();
	}

	protected Map<String, String> getNewContentMap(final BannerComponentData target)
	{
		target.setContent(new LinkedHashMap<>());
		return target.getContent();
	}

	protected Map<String, String> getNewHeadlineMap(final BannerComponentData target)
	{
		target.setHeadline(new LinkedHashMap<>());
		return target.getHeadline();
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
