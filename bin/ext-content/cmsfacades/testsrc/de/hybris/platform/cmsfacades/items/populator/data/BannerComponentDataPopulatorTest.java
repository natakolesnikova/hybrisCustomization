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

import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.cmsfacades.common.populator.impl.DefaultLocalizedPopulator;
import de.hybris.platform.cmsfacades.data.BannerComponentData;
import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class BannerComponentDataPopulatorTest
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
	private MediaService mediaService;
	@Mock
	private CMSAdminSiteService cmsAdminSiteService;
	@Mock
	private MediaModel mediaEN;
	@Mock
	private MediaModel mediaFR;
	@Mock
	private CatalogVersionModel catalogVersion;
	@Mock
	private LanguageFacade languageFacade;
	@Mock
	private CommonI18NService commonI18NService;

	@InjectMocks
	private DefaultLocalizedPopulator localizedPopulator;
	@InjectMocks
	private BannerComponentDataPopulator populator;

	private BannerComponentData bannerDto;
	private BannerComponentModel bannerModel;

	@Before
	public void setUp()
	{
		bannerDto = new BannerComponentData();
		bannerModel = new BannerComponentModel();

		bannerDto.setExternal(Boolean.TRUE);
		bannerDto.setUrlLink(URL_LINK);
		final Map<String, String> contents = new HashMap<>();
		contents.put(EN, CONTENT_EN);
		contents.put(FR, CONTENT_FR);
		bannerDto.setContent(contents);
		final Map<String, String> headlines = new HashMap<>();
		headlines.put(EN, HEADLINE_EN);
		headlines.put(FR, HEADLINE_FR);
		bannerDto.setHeadline(headlines);
		final Map<String, String> media = new HashMap<>();
		media.put(EN, MEDIA_CODE_EN);
		media.put(FR, MEDIA_CODE_FR);
		bannerDto.setMedia(media);

		final LanguageData languageEN = new LanguageData();
		languageEN.setIsocode(EN);
		final LanguageData languageFR = new LanguageData();
		languageFR.setIsocode(FR);

		when(cmsAdminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersion);
		when(mediaService.getMedia(catalogVersion, MEDIA_CODE_EN)).thenReturn(mediaEN);
		when(mediaService.getMedia(catalogVersion, MEDIA_CODE_FR)).thenReturn(mediaFR);
		when(languageFacade.getLanguages()).thenReturn(Lists.newArrayList(languageEN, languageFR));
		when(commonI18NService.getLocaleForIsoCode(EN)).thenReturn(ENGLISH);
		when(commonI18NService.getLocaleForIsoCode(FR)).thenReturn(FRENCH);

		populator.setLocalizedPopulator(localizedPopulator);
	}

	@Test
	public void shouldPopulateNonLocalizedAttributes()
	{
		populator.populate(bannerDto, bannerModel);

		assertThat(bannerModel.isExternal(), is(Boolean.TRUE));
		assertThat(bannerModel.getUrlLink(), is(URL_LINK));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_AllLanguages()
	{
		populator.populate(bannerDto, bannerModel);

		assertThat(bannerModel.getContent(Locale.ENGLISH), is(CONTENT_EN));
		assertThat(bannerModel.getContent(Locale.FRENCH), is(CONTENT_FR));
		assertThat(bannerModel.getHeadline(Locale.ENGLISH), is(HEADLINE_EN));
		assertThat(bannerModel.getHeadline(Locale.FRENCH), is(HEADLINE_FR));
		assertThat(bannerModel.getMedia(Locale.ENGLISH), is(mediaEN));
		assertThat(bannerModel.getMedia(Locale.FRENCH), is(mediaFR));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_SingleLanguages()
	{
		bannerDto.getContent().remove(FR);
		bannerDto.getHeadline().remove(FR);
		bannerDto.getMedia().remove(FR);

		populator.populate(bannerDto, bannerModel);

		assertThat(bannerModel.getContent(Locale.ENGLISH), is(CONTENT_EN));
		assertThat(bannerModel.getContent(Locale.FRENCH), nullValue());
		assertThat(bannerModel.getHeadline(Locale.ENGLISH), is(HEADLINE_EN));
		assertThat(bannerModel.getHeadline(Locale.FRENCH), nullValue());
		assertThat(bannerModel.getMedia(Locale.ENGLISH), is(mediaEN));
		assertThat(bannerModel.getMedia(Locale.FRENCH), nullValue());
	}

	@Test
	public void shouldNotPopulateLocalizedAttributes_NullMaps()
	{
		bannerDto.setContent(null);
		bannerDto.setHeadline(null);
		bannerDto.setMedia(null);

		populator.populate(bannerDto, bannerModel);

		assertThat(bannerModel.getContent(Locale.ENGLISH), nullValue());
		assertThat(bannerModel.getContent(Locale.FRENCH), nullValue());
		assertThat(bannerModel.getHeadline(Locale.ENGLISH), nullValue());
		assertThat(bannerModel.getHeadline(Locale.FRENCH), nullValue());
		assertThat(bannerModel.getMedia(Locale.ENGLISH), nullValue());
		assertThat(bannerModel.getMedia(Locale.FRENCH), nullValue());
	}

	@Test
	public void shouldDefaultExternalValue()
	{
		bannerDto.setExternal(null);

		populator.populate(bannerDto, bannerModel);

		assertThat(bannerModel.isExternal(), is(Boolean.FALSE));
	}

	@Test(expected = ConversionException.class)
	public void shouldFailConversion_MediaNotFound()
	{
		when(cmsAdminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersion);
		when(mediaService.getMedia(catalogVersion, MEDIA_CODE_EN)).thenThrow(new ConversionException("invalid media code"));

		populator.populate(bannerDto, bannerModel);
	}
}
