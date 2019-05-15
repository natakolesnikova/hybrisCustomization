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
package de.hybris.platform.cmsfacades.languages.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultLanguageFacadeTest
{
	private static final String ENGLISH = "EN";
	private static final String GERMAN = "DE";
	private static final String GERMAN_SWISS = "de_CH";

	@InjectMocks
	private DefaultLanguageFacade languageFacade;

	@Mock
	private StoreSessionFacade storeSessionFacade;

	@Mock
	private CommonI18NService commonI18NService;

	private LanguageData languageEN;
	private LanguageData languageDE;
	private LanguageData languageDE_CH;
	private List<LanguageData> languages;

	@Before
	public void setUp()
	{
		languageEN = new LanguageData();
		languageEN.setIsocode(ENGLISH);
		languageDE = new LanguageData();
		languageDE.setIsocode(GERMAN);
		languageDE_CH = new LanguageData();
		languageDE_CH.setIsocode(GERMAN_SWISS);
		languages = Arrays.asList(languageEN, languageDE, languageDE_CH);

		when(storeSessionFacade.getAllLanguages()).thenReturn(languages);
		when(storeSessionFacade.getDefaultLanguage()).thenReturn(languageEN);
		when(commonI18NService.getLocaleForIsoCode(ENGLISH)).thenReturn(new Locale("en"));
		when(commonI18NService.getLocaleForIsoCode(GERMAN)).thenReturn(new Locale("de"));
		when(commonI18NService.getLocaleForIsoCode(GERMAN_SWISS)).thenReturn(new Locale("de", "CH"));

	}

	@Test
	public void getLanguages_default_english()
	{
		when(storeSessionFacade.getDefaultLanguage()).thenReturn(languageEN);
		final List<LanguageData> languagesFound = languageFacade.getLanguages();

		assertEquals("en", languagesFound.get(0).getIsocode());
		assertTrue(languagesFound.get(0).isRequired());
		assertEquals("de", languagesFound.get(1).getIsocode());
		assertFalse(languagesFound.get(1).isRequired());
	}

	@Test
	public void getLanguages_default_german()
	{
		when(storeSessionFacade.getDefaultLanguage()).thenReturn(languageDE);
		final List<LanguageData> languagesFound = languageFacade.getLanguages();

		assertEquals("de", languagesFound.get(0).getIsocode());
		assertTrue(languagesFound.get(0).isRequired());
		assertEquals("en", languagesFound.get(1).getIsocode());
		assertFalse(languagesFound.get(1).isRequired());
	}

	@Test
	public void getLanguages_returns_isoCode_given_underScore_delimiter()
	{
		// Given
		when(storeSessionFacade.getDefaultLanguage()).thenReturn(languageDE_CH);

		// When
		final List<LanguageData> languagesFound = languageFacade.getLanguages();

		// Then
		assertThat("de-CH", is(languagesFound.get(0).getIsocode()));
	}
}
