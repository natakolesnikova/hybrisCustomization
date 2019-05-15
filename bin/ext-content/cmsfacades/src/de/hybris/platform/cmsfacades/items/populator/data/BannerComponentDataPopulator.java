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
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.cmsfacades.common.populator.LocalizedPopulator;
import de.hybris.platform.cmsfacades.data.BannerComponentData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * This populator will populate the {@link BannerComponentModel#setContent(String, Locale)} and the
 * {@link BannerComponentModel#setHeadline(String, Locale)} with attributes content and headline from
 * {@link BannerComponentData}. <br>
 * The external attribute is defaulted to false if not provided. </br>
 * <br>
 * The populator relies on the {@link SessionService} to set the locale and the {@link MediaService} in order to set the
 * media {@link BannerComponentModel#setMedia(de.hybris.platform.core.model.media.MediaModel)} </br>
 *
 * The populator relies on the {@link SessionService} to get the locale.
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class BannerComponentDataPopulator implements Populator<BannerComponentData, BannerComponentModel>
{
	private MediaService mediaService;
	private CMSAdminSiteService cmsAdminSiteService;
	private LocalizedPopulator localizedPopulator;

	@Override
	public void populate(final BannerComponentData source, final BannerComponentModel target) throws ConversionException
	{
		Optional.ofNullable(source.getContent()) //
		.ifPresent(content -> getLocalizedPopulator().populate( //
				(locale, value) -> target.setContent(value, locale),
				(locale) -> content.get(getLocalizedPopulator().getLanguage(locale))));

		Optional.ofNullable(source.getHeadline()) //
		.ifPresent(headline -> getLocalizedPopulator().populate( //
				(locale, value) -> target.setHeadline(value, locale),
				(locale) -> headline.get(getLocalizedPopulator().getLanguage(locale))));

		Optional.ofNullable(source.getMedia()) //
		.ifPresent(media -> getLocalizedPopulator().populate( //
				(locale, value) -> target.setMedia(value, locale),
				(locale) -> getMediaModel(media.get(getLocalizedPopulator().getLanguage(locale)))));

		target.setExternal(source.getExternal() != null ? source.getExternal() : Boolean.FALSE);
		target.setUrlLink(source.getUrlLink());
	}


	/**
	 * Return the MediaModel
	 *
	 * @param mediaCode
	 *           - The media code assigned to the MediaModel
	 * @return the mediaModel, if there is a corresponding mediaCode
	 */
	protected MediaModel getMediaModel(final String mediaCode)
	{
		if (mediaCode == null)
		{
			return null;
		}
		final MediaModel mediaModel;
		try
		{
			mediaModel = getMediaService().getMedia(getCmsAdminSiteService().getActiveCatalogVersion(), mediaCode);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new ConversionException(String.format("Unable to find media for code '%s'", mediaCode), e.getCause());
		}
		return mediaModel;
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
