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
package de.hybris.platform.cmsfacades.common.populator.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultLocalizedPopulatorTest
{

	private static final String ENGLISH_ISOCODE = Locale.ENGLISH.toString();
	private static final String FRENCH_CA_ISOCODE = Locale.CANADA_FRENCH.toString();

	@Mock
	private LanguageFacade languageFacade;

	@Mock
	private CommonI18NService commonI18NService;

	@InjectMocks
	private DefaultLocalizedPopulator defaultLocalizedPopulator;

	@Mock
	private BiConsumer<Locale, String> setter;

	@Mock
	private Function<Locale, String> getter;

	@Test
	public void testPopulator()
	{
		final LanguageData english = new LanguageData();
		english.setIsocode(ENGLISH_ISOCODE);
		when(languageFacade.getLanguages()).thenReturn(Arrays.asList(english));
		when(commonI18NService.getLocaleForIsoCode(english.getIsocode())).thenReturn(Locale.ENGLISH);

		final String expectedValue = "value";
		when(getter.apply(Locale.ENGLISH)).thenReturn(expectedValue);

		defaultLocalizedPopulator.populate(setter, getter);

		verify(languageFacade).getLanguages();
		verify(commonI18NService).getLocaleForIsoCode(ENGLISH_ISOCODE);
		verify(getter).apply(Locale.ENGLISH);
		verify(setter).accept(Locale.ENGLISH, expectedValue);
	}

	@Test
	public void testPopulatorWithLocalContainingUnderscore()
	{
		final LanguageData french = new LanguageData();
		french.setIsocode(FRENCH_CA_ISOCODE);
		when(languageFacade.getLanguages()).thenReturn(Arrays.asList(french));
		when(commonI18NService.getLocaleForIsoCode(french.getIsocode())).thenReturn(Locale.CANADA_FRENCH);

		final String expectedValue = "value";
		when(getter.apply(Locale.CANADA_FRENCH)).thenReturn(expectedValue);

		defaultLocalizedPopulator.populate(setter, getter);

		verify(languageFacade).getLanguages();
		verify(commonI18NService).getLocaleForIsoCode(FRENCH_CA_ISOCODE);
		verify(getter).apply(Locale.CANADA_FRENCH);
		verify(setter).accept(Locale.CANADA_FRENCH, expectedValue);
	}
}
