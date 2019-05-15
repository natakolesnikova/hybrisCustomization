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

import de.hybris.platform.acceleratorcms.model.components.AbstractMediaContainerComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.common.populator.LocalizedPopulator;
import de.hybris.platform.cmsfacades.data.MediaContainerData;
import de.hybris.platform.cmsfacades.data.SimpleResponsiveBannerComponentData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * This populator will populate the {@link SimpleResponsiveBannerComponentModel} from
 * {@link SimpleResponsiveBannerComponentData}.
 */
public class SimpleResponsiveBannerComponentReversePopulator
		implements Populator<SimpleResponsiveBannerComponentData, SimpleResponsiveBannerComponentModel>
{

	protected static final String MEDIA_CONTAINER_QUALIFIER = "_mediaContainer_";

	private CMSAdminSiteService cmsAdminSiteService;
	private CMSAdminComponentService componentAdminService;
	private Populator<MediaContainerData, MediaContainerModel> mediaContainerPopulator;
	private LocalizedPopulator localizedPopulator;

	@Override
	public void populate(final SimpleResponsiveBannerComponentData source, final SimpleResponsiveBannerComponentModel target)
	{
		final BiConsumer<Locale, Map<String, String>> mediaContainerSetter = getMediaContainerBiConsumer(source, target);

		Optional.ofNullable(source.getMedia()) //
				.ifPresent(media -> getLocalizedPopulator().populate( //
						mediaContainerSetter, locale -> media.get(getLocalizedPopulator().getLanguage(locale))));

		target.setUrlLink(source.getUrlLink());
	}

	/**
	 * Get the Consumer function to populate media container models and set then on the correct locale.
	 *
	 * @param source
	 *           the {@link SimpleResponsiveBannerComponentData} source data
	 * @param target
	 *           the {@link SimpleResponsiveBannerComponentModel} target model
	 * @return the function to populate the media container model and set it on the correct media container locale
	 */
	protected BiConsumer<Locale, Map<String, String>> getMediaContainerBiConsumer(final SimpleResponsiveBannerComponentData source,
			final SimpleResponsiveBannerComponentModel target)
	{
		return (locale, formatMediaCodeMap) -> {
			if (!MapUtils.isEmpty(formatMediaCodeMap))
			{
				final MediaContainerModel mediaContainer = findOrCreateMediaContainerModel(source.getUid(), locale);
				final MediaContainerData mediaContainerData = new MediaContainerData();
				mediaContainerData.setFormatMediaCodeMap(formatMediaCodeMap);

				getMediaContainerPopulator().populate(mediaContainerData, mediaContainer);
				target.setMedia(mediaContainer, locale);
			}
		};
	}

	/**
	 * Method to return the MediaContainerModel, if it is present, or create a new MediaContainerModel if it is not
	 * present in the target.
	 *
	 * @param componentUid
	 *           the component uid that will be used to set the media container qualifier
	 * @param locale
	 *           the locale this media container can be.s
	 * @return the new MediaContainerModel created.
	 */
	protected MediaContainerModel findOrCreateMediaContainerModel(final String componentUid, final Locale locale)
	{
		final AbstractMediaContainerComponentModel componentModel = (AbstractMediaContainerComponentModel) getComponentAdminService()
				.getCMSComponentForId(componentUid);

		final MediaContainerModel mediaContainer = componentModel.getMedia(locale);
		if (mediaContainer != null)
		{
			return mediaContainer;
		}
		else
		{
			return createMediaContainerModel(componentUid, locale);
		}
	}

	/**
	 * Builds the media container model
	 *
	 * @param componentUid
	 *           the component uid
	 * @return the media container model created. Still not attached.
	 */
	protected MediaContainerModel createMediaContainerModel(final String componentUid, final Locale locale)
	{
		final MediaContainerModel mediaContainerModel = new MediaContainerModel();
		mediaContainerModel.setQualifier(componentUid + MEDIA_CONTAINER_QUALIFIER + locale.getLanguage());
		mediaContainerModel.setCatalogVersion(getCmsAdminSiteService().getActiveCatalogVersion());
		return mediaContainerModel;
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

	protected Populator<MediaContainerData, MediaContainerModel> getMediaContainerPopulator()
	{
		return mediaContainerPopulator;
	}

	@Required
	public void setMediaContainerPopulator(final Populator<MediaContainerData, MediaContainerModel> mediaContainerPopulator)
	{
		this.mediaContainerPopulator = mediaContainerPopulator;
	}

	protected CMSAdminComponentService getComponentAdminService()
	{
		return componentAdminService;
	}

	@Required
	public void setComponentAdminService(final CMSAdminComponentService componentAdminService)
	{
		this.componentAdminService = componentAdminService;
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
