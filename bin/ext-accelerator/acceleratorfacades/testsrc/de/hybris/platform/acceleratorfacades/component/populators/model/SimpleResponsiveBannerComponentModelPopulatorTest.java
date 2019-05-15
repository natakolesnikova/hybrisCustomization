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

import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.cmsfacades.common.populator.impl.DefaultLocalizedPopulator;
import de.hybris.platform.cmsfacades.data.SimpleResponsiveBannerComponentData;
import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SimpleResponsiveBannerComponentModelPopulatorTest
{

	private static final String WIDESCREEN = "widescreen";
	private static final String DESKTOP = "desktop";
	private static final String MOBILE = "mobile";
	private static final String TABLET = "tablet";
	private static final String MEDIA_CODE_WIDESCREEN = "MEDIA-CODE-1";
	private static final String MEDIA_CODE_DESKTOP = "MEDIA-CODE-2";
	private static final String MEDIA_CODE_MOBILE = "MEDIA-CODE-3";
	private static final String MEDIA_CODE_TABLET = "MEDIA-CODE-4";
	private static final String URL_LINK = "url-link1";
	private static final String EN = Locale.ENGLISH.getLanguage();
	private static final String FR = Locale.FRENCH.getLanguage();

	@InjectMocks
	private SimpleResponsiveBannerComponentModelPopulator populator;

	@InjectMocks
	private DefaultLocalizedPopulator localizedPopulator;

	@Mock
	private LanguageFacade languageFacade;

	@Mock
	private CommonI18NService commonI18NService;

	@Mock
	private SimpleResponsiveBannerComponentModel model;

	@Mock
	private MediaModel mediaModel;

	@Before
	public void setup()
	{
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
	public void shouldPopulateTheWholeStructureWithMultiLanguage()
	{
		final SimpleResponsiveBannerComponentData target = new SimpleResponsiveBannerComponentData();

		final MediaContainerModel mediaContainer = mock(MediaContainerModel.class);
		final MediaModel widescreenMedia = createMedia(WIDESCREEN, MEDIA_CODE_WIDESCREEN);
		final MediaModel desktopMedia = createMedia(DESKTOP, MEDIA_CODE_DESKTOP);
		final MediaModel mobileMedia = createMedia(MOBILE, MEDIA_CODE_MOBILE);
		final MediaModel tabletMedia = createMedia(TABLET, MEDIA_CODE_TABLET);
		final Collection<MediaModel> mediaList = Arrays.asList(widescreenMedia, desktopMedia, mobileMedia, tabletMedia);

		when(model.getMedia(Locale.ENGLISH)).thenReturn(mediaContainer);
		when(mediaContainer.getMedias()).thenReturn(mediaList);
		when(model.getUrlLink()).thenReturn(URL_LINK);

		populator.populate(model, target);

		assertThat(target.getMedia().get(Locale.ENGLISH.getLanguage()), hasEntry(WIDESCREEN, MEDIA_CODE_WIDESCREEN));
		assertThat(target.getMedia().get(Locale.ENGLISH.getLanguage()), hasEntry(DESKTOP, MEDIA_CODE_DESKTOP));
		assertThat(target.getMedia().get(Locale.ENGLISH.getLanguage()), hasEntry(MOBILE, MEDIA_CODE_MOBILE));
		assertThat(target.getMedia().get(Locale.ENGLISH.getLanguage()), hasEntry(TABLET, MEDIA_CODE_TABLET));
		assertThat(target.getUrlLink(), is(URL_LINK));
	}

	@Test
	public void shouldPopulateTheWholeStructureWithEmptyContainerFormats()
	{
		final SimpleResponsiveBannerComponentData target = new SimpleResponsiveBannerComponentData();

		when(model.getUrlLink()).thenReturn(URL_LINK);

		populator.populate(model, target);

		assertThat(Integer.valueOf(target.getMedia().get(EN).size()), is(Integer.valueOf(0)));
		assertThat(target.getUrlLink(), is(URL_LINK));
	}

	@Test
	public void shouldPopulateTheWholeStructureWithSingleLanguageAndPartialMediaContainer()
	{
		final SimpleResponsiveBannerComponentData target = new SimpleResponsiveBannerComponentData();

		final MediaContainerModel mediaContainer = mock(MediaContainerModel.class);
		final MediaModel widescreenMedia = createMedia(WIDESCREEN, MEDIA_CODE_WIDESCREEN);
		final MediaModel desktopMedia = createMedia(DESKTOP, MEDIA_CODE_DESKTOP);
		final Collection<MediaModel> mediaList = Arrays.asList(widescreenMedia, desktopMedia);

		when(model.getMedia(any())).thenReturn(mediaContainer);
		when(mediaContainer.getMedias()).thenReturn(mediaList);
		when(model.getUrlLink()).thenReturn(URL_LINK);

		populator.populate(model, target);

		assertThat(Integer.valueOf(target.getMedia().get(EN).size()), is(Integer.valueOf(2)));
		assertThat(target.getMedia().get(EN), hasEntry(WIDESCREEN, MEDIA_CODE_WIDESCREEN));
		assertThat(target.getMedia().get(EN), hasEntry(DESKTOP, MEDIA_CODE_DESKTOP));
		assertThat(target.getUrlLink(), is(URL_LINK));
	}

	protected MediaModel createMedia(final String mediaFormat, final String mediaCode)
	{
		final MediaModel media = mock(MediaModel.class);
		when(media.getCode()).thenReturn(mediaCode);
		final MediaFormatModel mediaFormatModel = mock(MediaFormatModel.class);
		when(mediaFormatModel.getQualifier()).thenReturn(mediaFormat);
		when(media.getMediaFormat()).thenReturn(mediaFormatModel);
		return media;
	}

}
