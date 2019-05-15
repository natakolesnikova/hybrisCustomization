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

import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.cmsfacades.common.populator.LocalizedPopulator;
import de.hybris.platform.cmsfacades.data.SimpleBannerComponentData;
import de.hybris.platform.converters.Populator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default simple banner component implementation of {@link Populator}.
 */
public class SimpleBannerComponentModelPopulator implements Populator<SimpleBannerComponentModel, SimpleBannerComponentData>
{
	private LocalizedPopulator localizedPopulator;

	@Override
	public void populate(final SimpleBannerComponentModel source, final SimpleBannerComponentData target)
	{
		final Map<String, String> mediaMap = Optional.ofNullable(target.getMedia()).orElseGet(() -> getNewMediaMap(target));

		getLocalizedPopulator().populate( //
				(locale, value) -> mediaMap.put(getLocalizedPopulator().getLanguage(locale), value),
				locale -> Optional.ofNullable(source.getMedia(locale)).map(media -> media.getCode()).orElse(null));

		target.setUrlLink(source.getUrlLink());
		target.setExternal(Boolean.valueOf(source.isExternal()));
	}

	protected Map<String, String> getNewMediaMap(final SimpleBannerComponentData target)
	{
		target.setMedia(new LinkedHashMap<>());
		return target.getMedia();
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
