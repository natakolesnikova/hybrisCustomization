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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.data.MediaContainerData;
import de.hybris.platform.cmsfacades.media.service.CMSMediaFormatService;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.collection.IsArrayContaining;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class MediaContainerReversePopulatorTest
{

	private static final String WIDESCREEN = "widescreen";
	private static final String DESKTOP = "desktop";
	private static final String MOBILE = "mobile";
	private static final String TABLET = "tablet";
	private static final String MEDIA_CODE_WIDESCREEN = "MEDIA-CODE-1";
	private static final String MEDIA_CODE_DESKTOP = "MEDIA-CODE-2";
	private static final String MEDIA_CODE_MOBILE = "MEDIA-CODE-3";
	private static final String MEDIA_CODE_TABLET = "MEDIA-CODE-4";


	@Mock
	private MediaService mediaService;
	@Mock
	private CMSAdminSiteService cmsAdminSiteService;
	@Mock
	private CMSMediaFormatService mediaFormatService;

	@Mock
	private CatalogVersionModel catalogVersionModel;

	@InjectMocks
	private MediaContainerReversePopulator populator;

	private MediaContainerModel model;

	private MediaModel mediaWidescreen;
	private MediaModel mediaDesktop;
	private MediaModel mediaTablet;
	private MediaModel mediaMobile;

	@Before
	public void setup()
	{
		model = new MediaContainerModel();

		mediaWidescreen = createMedia(MEDIA_CODE_WIDESCREEN);
		mediaDesktop = createMedia(MEDIA_CODE_DESKTOP);
		mediaTablet = createMedia(MEDIA_CODE_TABLET);
		mediaMobile = createMedia(MEDIA_CODE_MOBILE);

		when(cmsAdminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersionModel);

		when(mediaService.getMedia(catalogVersionModel, MEDIA_CODE_WIDESCREEN)).thenReturn(mediaWidescreen);
		when(mediaService.getMedia(catalogVersionModel, MEDIA_CODE_DESKTOP)).thenReturn(mediaDesktop);
		when(mediaService.getMedia(catalogVersionModel, MEDIA_CODE_MOBILE)).thenReturn(mediaMobile);
		when(mediaService.getMedia(catalogVersionModel, MEDIA_CODE_TABLET)).thenReturn(mediaTablet);

	}

	@Test
	public void testPopulateWholeStructure()
	{
		final MediaContainerData data = new MediaContainerData();
		data.setFormatMediaCodeMap(getLocalizedMultiValueMap());

		populator.populate(data, model);

		assertThat(model, is(not(nullValue())));
		assertThat(model.getMedias(), is(not(nullValue())));
		assertThat(model.getMedias().toArray(), IsArrayContaining.hasItemInArray(mediaWidescreen));
		assertThat(model.getMedias().toArray(), IsArrayContaining.hasItemInArray(mediaDesktop));
		assertThat(model.getMedias().toArray(), IsArrayContaining.hasItemInArray(mediaTablet));
		assertThat(model.getMedias().toArray(), IsArrayContaining.hasItemInArray(mediaMobile));

	}

	protected Map<String, String> getLocalizedMultiValueMap()
	{
		final Map<String, String> formatMap = new HashMap<>();
		formatMap.put(WIDESCREEN, MEDIA_CODE_WIDESCREEN);
		formatMap.put(DESKTOP, MEDIA_CODE_DESKTOP);
		formatMap.put(TABLET, MEDIA_CODE_TABLET);
		formatMap.put(MOBILE, MEDIA_CODE_MOBILE);
		return formatMap;
	}

	protected MediaModel createMedia(final String mediaCode)
	{
		final MediaModel media = new MediaModel();
		media.setCode(mediaCode);
		return media;
	}

}
