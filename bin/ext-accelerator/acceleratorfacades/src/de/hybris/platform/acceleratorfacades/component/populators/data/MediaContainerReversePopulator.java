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

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.trim;

import de.hybris.platform.acceleratorcms.model.components.AbstractMediaContainerComponentModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.data.MediaContainerData;
import de.hybris.platform.cmsfacades.media.service.CMSMediaFormatService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * This populator will populate the {@link MediaContainerModel} from the localized value
 */
public class MediaContainerReversePopulator implements Populator<MediaContainerData, MediaContainerModel>
{

	private MediaService mediaService;
	private CMSAdminSiteService cmsAdminSiteService;
	private CMSMediaFormatService mediaFormatService;

	@Override
	public void populate(final MediaContainerData source, final MediaContainerModel target)
	{
		// transforms the list of media format into a map
		final Map<String, MediaFormatModel> mediaFormats = createMediaFormatMap(
				getMediaFormatService().getMediaFormatsByComponentType(AbstractMediaContainerComponentModel.class));

		getMediaContainerBiConsumer(target, mediaFormats).accept(source.getFormatMediaCodeMap());
	}

	/**
	 * Get the Media Container Bi Consumer function to populate the Component Model with the containers and media list.
	 *
	 * @param target
	 *           the {@link MediaContainerModel} target model.
	 * @param mediaFormats
	 *           the media format map.
	 * @return the function to populate the component model.
	 */
	protected Consumer<Map<String, String>> getMediaContainerBiConsumer(final MediaContainerModel target,
			final Map<String, MediaFormatModel> mediaFormats)
	{
		// function to consume the localized value data
		return formatMediaCodeMap ->
		// populate the media container
		Optional.ofNullable(formatMediaCodeMap).ifPresent(map -> {
			if (isFormatMediaMapTheSameAsBefore(target.getMedias(), formatMediaCodeMap))
			{
				return;
			}
			final List<MediaModel> mediaList = new ArrayList<>();
			map.entrySet().stream().forEach(entry -> {
				final MediaFormatModel mediaFormat = mediaFormats.get(entry.getKey());
				getMediaModel(entry.getValue()).ifPresent(media -> {
					media.setMediaFormat(mediaFormat);
					mediaList.add(media);
				});

			});
			target.setMedias(mediaList);
		});
	}

	/**
	 * Determines if the media list has been modified
	 *
	 * @param medias
	 *           the original media list on the target media container
	 * @param formatMediaCodeMap
	 *           the new format/media map being requested for change
	 * @return {@code true} if the media list has been modified; {@code false} otherwise.
	 */
	protected boolean isFormatMediaMapTheSameAsBefore(final Collection<MediaModel> medias,
			final Map<String, String> formatMediaCodeMap)
	{
		if (medias == null)
		{
			return false;
		}
		final Map<String, String> originalMap = medias.stream().collect(Collectors
				.toMap(media -> media.getMediaFormat().getQualifier(), media -> media.getCode(), (entry1, entry2) -> entry1));
		return originalMap.equals(formatMediaCodeMap);
	}

	/**
	 * Returns a {@code Map<String, MediaFormatModel>} where the key is the format qualifier and the value is the media
	 * format itself.
	 *
	 * @param mediaFormats
	 *           the list of media formats
	 * @return the {@code Map<String, MediaFormatModel>} with all the media formats and the qualifiers.
	 */
	protected Map<String, MediaFormatModel> createMediaFormatMap(final Collection<MediaFormatModel> mediaFormats)
	{
		return mediaFormats.stream().collect(Collectors.toMap(MediaFormatModel::getQualifier, Function.identity()));
	}

	/**
	 * Returns the MediaModel if the mediaCode is present. Otherwise returns null
	 *
	 * @param mediaCode
	 *           - The media code assigned to the MediaModel
	 * @return the mediaModel, if there is a corresponding mediaCode
	 */
	protected Optional<MediaModel> getMediaModel(final String mediaCode)
	{
		if (isEmpty(trim(mediaCode)))
		{
			return Optional.empty();
		}
		final MediaModel mediaModel;
		try
		{
			mediaModel = getMediaService().getMedia(getCmsAdminSiteService().getActiveCatalogVersion(), mediaCode);
			return Optional.of(mediaModel);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new ConversionException(String.format("Unable to find media for code '%s'", mediaCode), e.getCause());
		}
	}

	protected MediaService getMediaService()
	{
		return mediaService;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	protected CMSAdminSiteService getCmsAdminSiteService()
	{
		return cmsAdminSiteService;
	}

	@Required
	public void setCmsAdminSiteService(final CMSAdminSiteService cmsAdminSiteService)
	{
		this.cmsAdminSiteService = cmsAdminSiteService;
	}

	protected CMSMediaFormatService getMediaFormatService()
	{
		return mediaFormatService;
	}

	@Required
	public void setMediaFormatService(final CMSMediaFormatService mediaFormatService)
	{
		this.mediaFormatService = mediaFormatService;
	}
}
