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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmsfacades.common.populator.impl.DefaultLocalizedPopulator;
import de.hybris.platform.cmsfacades.data.SimpleBannerComponentData;
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

import jersey.repackaged.com.google.common.collect.Maps;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SimpleBannerComponentModelPopulatorTest
{
	private static final String FR = "fr";
	private static final String EN = "en";
	private static final String MEDIA_CODE_EN = "mediaCodeEN";
	private static final String MEDIA_CODE_FR = "mediaCodeFR";
	private static final String URL_LINK = "http://something-to-test.com";

	@InjectMocks
	private final SimpleBannerComponentModelPopulator populator = new SimpleBannerComponentModelPopulator();

	@InjectMocks
	private final DefaultLocalizedPopulator localizedPopulator = new DefaultLocalizedPopulator();

	@Mock
	private LanguageFacade languageFacade;
	@Mock
	private CommonI18NService commonI18NService;

	@Mock
	private MediaModel mediaModelEN;
	@Mock
	private MediaModel mediaModelFR;
	@Mock
	private CatalogVersionModel catalogVersionModel;
	@Mock
	private SimpleBannerComponentModel bannerModel;

	private SimpleBannerComponentData bannerDto;

	@Before
	public void setUp()
	{
		populator.setLocalizedPopulator(localizedPopulator);

		bannerDto = new SimpleBannerComponentData();
		bannerDto.setMedia(Maps.newHashMap());
		when(bannerModel.getMedia(Locale.ENGLISH)).thenReturn(mediaModelEN);
		when(bannerModel.getMedia(Locale.FRENCH)).thenReturn(mediaModelFR);
		when(Boolean.valueOf(bannerModel.isExternal())).thenReturn(Boolean.TRUE);
		when(bannerModel.getUrlLink()).thenReturn(URL_LINK);

		when(mediaModelEN.getCode()).thenReturn(MEDIA_CODE_EN);
		when(mediaModelFR.getCode()).thenReturn(MEDIA_CODE_FR);

		final LanguageData languageEN = new LanguageData();
		languageEN.setIsocode(EN);
		final LanguageData languageFR = new LanguageData();
		languageFR.setIsocode(FR);
		when(languageFacade.getLanguages()).thenReturn(Lists.newArrayList(languageEN, languageFR));
		when(commonI18NService.getLocaleForIsoCode(EN)).thenReturn(ENGLISH);
		when(commonI18NService.getLocaleForIsoCode(FR)).thenReturn(FRENCH);
	}

	@Test
	public void shouldPopulateNonLocalizedAttributes()
	{
		populator.populate(bannerModel, bannerDto);

		assertThat(bannerDto.getExternal(), equalTo(Boolean.TRUE));
		assertThat(bannerDto.getUrlLink(), equalTo(URL_LINK));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_NullMediaMap()
	{
		bannerDto.setMedia(null);
		populator.populate(bannerModel, bannerDto);

		assertThat(bannerDto.getMedia().get(EN), equalTo(MEDIA_CODE_EN));
		assertThat(bannerDto.getMedia().get(FR), equalTo(MEDIA_CODE_FR));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_AllLanguages()
	{
		populator.populate(bannerModel, bannerDto);

		assertThat(bannerDto.getMedia().get(EN), equalTo(MEDIA_CODE_EN));
		assertThat(bannerDto.getMedia().get(FR), equalTo(MEDIA_CODE_FR));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_SingleLanguages()
	{
		when(bannerModel.getMedia(Locale.FRENCH)).thenReturn(null);
		populator.populate(bannerModel, bannerDto);

		assertThat(bannerDto.getMedia().get(EN), equalTo(MEDIA_CODE_EN));
		assertThat(bannerDto.getMedia().get(FR), nullValue());
	}

}
