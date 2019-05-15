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

import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.cmsfacades.common.populator.LocalizedPopulator;
import de.hybris.platform.cmsfacades.data.SimpleResponsiveBannerComponentData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default simple responsive banner component implementation of {@link Populator}.
 */
public class SimpleResponsiveBannerComponentModelPopulator
		implements Populator<SimpleResponsiveBannerComponentModel, SimpleResponsiveBannerComponentData>
{
	private LocalizedPopulator localizedPopulator;

	@Override
	public void populate(final SimpleResponsiveBannerComponentModel source, final SimpleResponsiveBannerComponentData target)
	{
		final Map<String, Map<String, String>> mediaMap = Optional.ofNullable(target.getMedia())
				.orElseGet(() -> getNewMediaMap(target));

		final Function<Locale, Map<String, String>> mediaCodePerFormatSourceGetter = locale -> getMediaCodePerFormat(source, locale)
				.orElse(Collections.emptyMap());
		final BiConsumer<Locale, Map<String, String>> mediaCodeFormatTargetSetter = (locale, value) -> mediaMap
				.put(getLocalizedPopulator().getLanguage(locale), value);

		getLocalizedPopulator().populate(mediaCodeFormatTargetSetter, mediaCodePerFormatSourceGetter);

		target.setUrlLink(source.getUrlLink());
	}

	protected Map<String, Map<String, String>> getNewMediaMap(final SimpleResponsiveBannerComponentData target)
	{
		target.setMedia(new LinkedHashMap<>());
		return target.getMedia();
	}

	/**
	 * Creates the MediaFormat Map, mapping the media format to the assigned media code.
	 *
	 * @param source
	 *           the SimpleResponsiveBannerComponentModel holding the media container
	 * @param locale
	 *           the current Locale to get the correct Media Container
	 * @return - the {@code Map<String, String>} containing the formats and codes for the simple responsive banner
	 *         component and locale.
	 */
	protected Optional<Map<String, String>> getMediaCodePerFormat(final SimpleResponsiveBannerComponentModel source,
			final Locale locale)
	{
		return Optional.ofNullable(source.getMedia(locale)) //
				.map(mediaContainer -> mediaContainer.getMedias()) //
				.map(medias -> medias.stream() //
						.collect(Collectors.toMap((final MediaModel media) -> media.getMediaFormat().getQualifier(),
								MediaModel::getCode)));
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
