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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cmsfacades.util.BaseIntegrationTest;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

@IntegrationTest
public class DefaultLanguageFacadeIntegrationTest extends BaseIntegrationTest
{
	@Mock
	private StoreSessionFacade storeSessionFacade;
	@Resource
	private CommonI18NService commonI18NService;

	private DefaultLanguageFacade cmsLanguageFacade;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		cmsLanguageFacade = new DefaultLanguageFacade();
		cmsLanguageFacade.setCommonI18NService(commonI18NService);
		cmsLanguageFacade.setStoreSessionFacade(storeSessionFacade);
	}

	@Test
	public void isocodes_with_underscores_are_converted_to_hybris_hyphen_format() {

		// Given
		LanguageData en = new LanguageData();
		en.setIsocode("en");

		LanguageData en_us = new LanguageData();
		en_us.setIsocode("en_us");

		LanguageData en_US = new LanguageData();
		en_US.setIsocode("en_US");

		Mockito.when(storeSessionFacade.getAllLanguages()).thenReturn(Arrays.asList(en, en_us, en_US));
		Mockito.when(storeSessionFacade.getDefaultLanguage()).thenReturn(en);

		// When
		List<LanguageData> languages = cmsLanguageFacade.getLanguages();

		// Then
		assertThat(languages.stream().map(languageDate -> languageDate.getIsocode()).collect(Collectors.toList()), contains("en", "en-US", "en-US"));
	}
}
