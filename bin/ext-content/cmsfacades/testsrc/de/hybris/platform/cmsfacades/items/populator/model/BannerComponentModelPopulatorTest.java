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

import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.cmsfacades.common.populator.impl.DefaultLocalizedPopulator;
import de.hybris.platform.cmsfacades.data.BannerComponentData;
import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class BannerComponentModelPopulatorTest
{
	private static final String MEDIA_CODE_EN = "testMediaUid-EN";
	private static final String MEDIA_CODE_FR = "testMediaUid-FR";
	private static final String CONTENT_EN = "content-EN";
	private static final String CONTENT_FR = "content-FR";
	private static final String HEADLINE_EN = "headline-EN";
	private static final String HEADLINE_FR = "headline-FR";
	private static final String URL_LINK = "test-url-link";
	private static final String EN = Locale.ENGLISH.getLanguage();
	private static final String FR = Locale.FRENCH.getLanguage();

	@Mock
	private BannerComponentModel bannerModel;
	@Mock
	private MediaModel mediaEN;
	@Mock
	private MediaModel mediaFR;
	@Mock
	private LanguageFacade languageFacade;
	@Mock
	private CommonI18NService commonI18NService;

	@InjectMocks
	private DefaultLocalizedPopulator localizedPopulator;
	@InjectMocks
	private BannerComponentModelPopulator populator;

	private BannerComponentData bannerDto;

	@Before
	public void setUp()
	{
		bannerDto = new BannerComponentData();
		bannerDto.setMedia(Maps.newHashMap());
		when(bannerModel.getContent(Locale.ENGLISH)).thenReturn(CONTENT_EN);
		when(bannerModel.getContent(Locale.FRENCH)).thenReturn(CONTENT_FR);
		when(bannerModel.getHeadline(Locale.ENGLISH)).thenReturn(HEADLINE_EN);
		when(bannerModel.getHeadline(Locale.FRENCH)).thenReturn(HEADLINE_FR);
		when(bannerModel.getMedia(Locale.ENGLISH)).thenReturn(mediaEN);
		when(bannerModel.getMedia(Locale.FRENCH)).thenReturn(mediaFR);
		when(bannerModel.isExternal()).thenReturn(Boolean.TRUE);
		when(bannerModel.getUrlLink()).thenReturn(URL_LINK);
		when(mediaEN.getCode()).thenReturn(MEDIA_CODE_EN);
		when(mediaFR.getCode()).thenReturn(MEDIA_CODE_FR);

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
	public void shouldPopulateNonLocalizedAttributes()
	{
		populator.populate(bannerModel, bannerDto);

		assertThat(bannerDto.getExternal(), equalTo(Boolean.TRUE));
		assertThat(bannerDto.getUrlLink(), equalTo(URL_LINK));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_NullMaps()
	{
		bannerDto.setMedia(null);
		bannerDto.setContent(null);
		bannerDto.setHeadline(null);

		populator.populate(bannerModel, bannerDto);

		assertThat(bannerDto.getContent().get(EN), equalTo(CONTENT_EN));
		assertThat(bannerDto.getContent().get(FR), equalTo(CONTENT_FR));
		assertThat(bannerDto.getHeadline().get(EN), equalTo(HEADLINE_EN));
		assertThat(bannerDto.getHeadline().get(FR), equalTo(HEADLINE_FR));
		assertThat(bannerDto.getMedia().get(EN), equalTo(MEDIA_CODE_EN));
		assertThat(bannerDto.getMedia().get(FR), equalTo(MEDIA_CODE_FR));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_AllLanguages()
	{
		populator.populate(bannerModel, bannerDto);

		assertThat(bannerDto.getContent().get(EN), equalTo(CONTENT_EN));
		assertThat(bannerDto.getContent().get(FR), equalTo(CONTENT_FR));
		assertThat(bannerDto.getHeadline().get(EN), equalTo(HEADLINE_EN));
		assertThat(bannerDto.getHeadline().get(FR), equalTo(HEADLINE_FR));
		assertThat(bannerDto.getMedia().get(EN), equalTo(MEDIA_CODE_EN));
		assertThat(bannerDto.getMedia().get(FR), equalTo(MEDIA_CODE_FR));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_SingleLanguages()
	{
		when(bannerModel.getContent(Locale.FRENCH)).thenReturn(null);
		when(bannerModel.getHeadline(Locale.FRENCH)).thenReturn(null);
		when(bannerModel.getMedia(Locale.FRENCH)).thenReturn(null);

		populator.populate(bannerModel, bannerDto);

		assertThat(bannerDto.getContent().get(EN), equalTo(CONTENT_EN));
		assertThat(bannerDto.getContent().get(FR), nullValue());
		assertThat(bannerDto.getHeadline().get(EN), equalTo(HEADLINE_EN));
		assertThat(bannerDto.getHeadline().get(FR), nullValue());
		assertThat(bannerDto.getMedia().get(EN), equalTo(MEDIA_CODE_EN));
		assertThat(bannerDto.getMedia().get(FR), nullValue());
	}
}
