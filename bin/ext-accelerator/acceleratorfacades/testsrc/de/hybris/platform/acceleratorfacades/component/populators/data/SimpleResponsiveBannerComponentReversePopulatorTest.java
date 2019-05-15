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

import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.common.populator.impl.DefaultLocalizedPopulator;
import de.hybris.platform.cmsfacades.data.MediaContainerData;
import de.hybris.platform.cmsfacades.data.SimpleResponsiveBannerComponentData;
import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.hamcrest.collection.IsArrayContaining;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SimpleResponsiveBannerComponentReversePopulatorTest
{

	private static final String MEDIA_CODE_WIDESCREEN = "MEDIA-CODE-1";
	private static final String MEDIA_CODE_DESKTOP = "MEDIA-CODE-2";
	private static final String MEDIA_CODE_MOBILE = "MEDIA-CODE-3";
	private static final String MEDIA_CODE_TABLET = "MEDIA-CODE-4";
	private static final String URL_LINK = "url-link1";
	private static final String COMPONENT_UID = "simple-responsive-uid";
	private static final String EN = Locale.ENGLISH.getLanguage();
	private static final String FR = Locale.FRENCH.getLanguage();
	private static final String WIDESCREEN = "widescreen";
	private static final String DESKTOP = "desktop";
	private static final String TABLET = "tablet";
	private static final String MOBILE = "mobile";

	@Mock
	private CMSAdminSiteService cmsAdminSiteService;
	@Mock
	private CMSAdminComponentService adminComponentService;
	@Mock
	private CatalogVersionModel catalogVersionModel;
	@Mock
	private Populator<MediaContainerData, MediaContainerModel> mediaContainerModelDataPopulator;
	@Mock
	private LanguageFacade languageFacade;
	@Mock
	private CommonI18NService commonI18NService;
	@InjectMocks
	private DefaultLocalizedPopulator localizedPopulator;

	@InjectMocks
	private SimpleResponsiveBannerComponentReversePopulator populator;

	private SimpleResponsiveBannerComponentData data;

	private MediaContainerModel mediaContainerModel;

	private MediaModel mediaWidescreen;
	private MediaModel mediaDesktop;
	private MediaModel mediaTablet;
	private MediaModel mediaMobile;
	private SimpleResponsiveBannerComponentModel model;
	private MediaFormatModel widescreenFormat;
	private MediaFormatModel desktopFormat;
	private MediaFormatModel tabletFormat;
	private MediaFormatModel mobileFormat;

	@Before
	public void setup()
	{
		mediaContainerModel = new MediaContainerModel();

		widescreenFormat = createMediaFormat(WIDESCREEN);
		desktopFormat = createMediaFormat(DESKTOP);
		tabletFormat = createMediaFormat(TABLET);
		mobileFormat = createMediaFormat(MOBILE);
		mediaWidescreen = createMedia(MEDIA_CODE_WIDESCREEN, widescreenFormat);
		mediaDesktop = createMedia(MEDIA_CODE_DESKTOP, desktopFormat);
		mediaTablet = createMedia(MEDIA_CODE_TABLET, tabletFormat);
		mediaMobile = createMedia(MEDIA_CODE_MOBILE, mobileFormat);



		data = createSimpleResponsiveBannerComponentData();
		when(cmsAdminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersionModel);

		model = new SimpleResponsiveBannerComponentModel();
		model.setMedia(mediaContainerModel, Locale.ENGLISH);
		when(adminComponentService.getCMSComponentForId(COMPONENT_UID)).thenReturn(model);

		doAnswer((Answer<Void>) invocationOnMock -> {
			mediaContainerModel.setMedias(Arrays.asList(mediaWidescreen, mediaDesktop, mediaTablet, mediaMobile));
			return null;
		}).when(mediaContainerModelDataPopulator).populate(any(), any());

		final Map<String, Boolean> localeMap = new HashMap<>();
		localeMap.put(Locale.ENGLISH.getLanguage(), Boolean.TRUE);

		final LanguageData languageEN = new LanguageData();
		languageEN.setIsocode(EN);
		final LanguageData languageFR = new LanguageData();
		languageFR.setIsocode(FR);
		when(languageFacade.getLanguages()).thenReturn(Lists.newArrayList(languageEN, languageFR));
		when(commonI18NService.getLocaleForIsoCode(EN)).thenReturn(ENGLISH);
		when(commonI18NService.getLocaleForIsoCode(FR)).thenReturn(FRENCH);

		populator.setLocalizedPopulator(localizedPopulator);
	}

	@Test
	public void testPopulateWholeStructure()
	{
		populator.populate(data, model);

		assertThat(model.getMedia(Locale.ENGLISH), is(not(nullValue())));
		assertThat(model.getMedia(Locale.ENGLISH).getMedias(), is(not(nullValue())));
		assertThat(model.getMedia(Locale.ENGLISH).getMedias().toArray(), IsArrayContaining.hasItemInArray(mediaWidescreen));
		assertThat(model.getMedia(Locale.ENGLISH).getMedias().toArray(), IsArrayContaining.hasItemInArray(mediaDesktop));
		assertThat(model.getMedia(Locale.ENGLISH).getMedias().toArray(), IsArrayContaining.hasItemInArray(mediaTablet));
		assertThat(model.getMedia(Locale.ENGLISH).getMedias().toArray(), IsArrayContaining.hasItemInArray(mediaMobile));

		assertThat(model.getUrlLink(), is(URL_LINK));
	}

	protected SimpleResponsiveBannerComponentData createSimpleResponsiveBannerComponentData()
	{
		final SimpleResponsiveBannerComponentData data = new SimpleResponsiveBannerComponentData();
		data.setUid(COMPONENT_UID);
		data.setUrlLink(URL_LINK);
		final Map<String, Map<String, String>> mediaMap = new HashMap<>();
		final Map<String, String> mediaFormatMap = new HashMap<>();
		mediaFormatMap.put(WIDESCREEN, MEDIA_CODE_WIDESCREEN);
		mediaFormatMap.put(DESKTOP, MEDIA_CODE_DESKTOP);
		mediaFormatMap.put(TABLET, MEDIA_CODE_TABLET);
		mediaFormatMap.put(MOBILE, MEDIA_CODE_MOBILE);
		mediaMap.put(Locale.ENGLISH.getLanguage(), mediaFormatMap);
		data.setMedia(mediaMap);

		return data;
	}

	protected MediaFormatModel createMediaFormat(final String mediaFormatQualifier)
	{
		final MediaFormatModel mediaFormat = mock(MediaFormatModel.class);
		when(mediaFormat.getQualifier()).thenReturn(mediaFormatQualifier);
		return mediaFormat;
	}

	protected MediaModel createMedia(final String mediaCode, final MediaFormatModel mediaFormat)
	{
		final MediaModel media = new MediaModel();
		media.setCode(mediaCode);
		media.setMediaFormat(mediaFormat);
		return media;
	}

}
