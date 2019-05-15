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

import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.common.populator.LocalizedPopulator;
import de.hybris.platform.cmsfacades.data.SimpleBannerComponentData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * This populator will populate the {@link SimpleBannerComponentModel#setExternal(boolean)} and the
 * {@link SimpleBannerComponentModel#setUrlLink(String)} with attributes content and headline from
 * {@link SimpleBannerComponentData}. <br>
 * The external attribute is defaulted to false if not provided. </br>
 *
 * The populator relies on the {@link SessionService} to get the locale and the {@link MediaService} in order to set the
 * media {@link SimpleBannerComponentModel#setMedia(de.hybris.platform.core.model.media.MediaModel)}.
 */
public class SimpleBannerComponentReversePopulator implements Populator<SimpleBannerComponentData, SimpleBannerComponentModel>
{
	private MediaService mediaService;
	private CMSAdminSiteService cmsAdminSiteService;
	private LocalizedPopulator localizedPopulator;

	@Override
	public void populate(final SimpleBannerComponentData source, final SimpleBannerComponentModel target)
	{
		Optional.ofNullable(source.getMedia()) //
				.ifPresent(media -> getLocalizedPopulator().populate( //
						(locale, value) -> target.setMedia(value, locale),
						locale -> getMediaModel(media.get(getLocalizedPopulator().getLanguage(locale)))));

		target.setExternal(source.getExternal() != null ? source.getExternal().booleanValue() : false);
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
