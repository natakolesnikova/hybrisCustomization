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
package de.hybris.platform.acceleratorfacades.component.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.common.function.ValidationConsumer;
import de.hybris.platform.cmsfacades.common.validator.impl.DefaultLocalizedValidator;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.SimpleResponsiveBannerComponentData;
import de.hybris.platform.cmsfacades.items.validator.consumer.LocalizedFormatMediaAttributeValidationConsumer;
import de.hybris.platform.cmsfacades.items.validator.consumer.LocalizedFormatStringAttributeValidationConsumer;
import de.hybris.platform.cmsfacades.items.validator.consumer.LocalizedValidationData;
import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SimpleResponsiveBannerComponentValidatorTest
{
	private static final String SALES_BANNER_UID = "test-sales-banner";
	private static final String URL_LINK = "urlLink";

	private static final String FORMAT_WIDESCREEN = "1-widescreen";
	private static final String FORMAT_MOBILE = "2-mobile";
	private static final String FORMAT_TABLET = "3-tablet";
	private static final String MEDIA_WIDESCREEN = "image_widescreen";
	private static final String MEDIA_MOBILE = "image_mobile";
	private static final String MEDIA_TABLET = "image_tablet";

	private static final String ENGLISH = Locale.ENGLISH.getLanguage();
	private static final String FRENCH = Locale.FRENCH.getLanguage();
	private static final String EN = Locale.ENGLISH.getLanguage();

	private final List<String> cmsRequiredMediaFormatQualifiers = Arrays.asList(FORMAT_WIDESCREEN);

	@Mock
	private MediaService mediaService;

	@Mock
	private CMSAdminSiteService cmsAdminSiteService;

	@Mock
	private CatalogVersionModel catalogVersion;

	@Mock
	private Predicate<String> validStringLengthPredicate;

	@InjectMocks
	private final ValidationConsumer<LocalizedValidationData> localizedFormatMediaAttributePredicate = new LocalizedFormatMediaAttributeValidationConsumer();

	@InjectMocks
	private final ValidationConsumer<LocalizedValidationData> localizedFormatStringAttributePredicate = new LocalizedFormatStringAttributeValidationConsumer();

	@InjectMocks
	private final SimpleResponsiveBannerComponentValidator validator = new SimpleResponsiveBannerComponentValidator();

	private Map<String, String> mediaCodePerFormatMap;

	@Mock
	private LanguageFacade languageFacade;
	@InjectMocks
	private DefaultLocalizedValidator localizedValidator;


	@Before
	public void setup()
	{
		mediaCodePerFormatMap = new LinkedHashMap<>();
		mediaCodePerFormatMap.put(FORMAT_WIDESCREEN, MEDIA_WIDESCREEN);
		mediaCodePerFormatMap.put(FORMAT_MOBILE, MEDIA_MOBILE);
		mediaCodePerFormatMap.put(FORMAT_TABLET, MEDIA_TABLET);

		validator.setLocalizedFormatMediaAttributeValidationConsumer(localizedFormatMediaAttributePredicate);
		validator.setLocalizedFormatStringAttributeValidationConsumer(localizedFormatStringAttributePredicate);
		validator.setCmsRequiredMediaFormatQualifiers(cmsRequiredMediaFormatQualifiers);
		validator.setValidStringLengthPredicate(validStringLengthPredicate);

		when(cmsAdminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersion);
		when(mediaService.getMedia(catalogVersion, MEDIA_WIDESCREEN)).thenReturn(new MediaModel());
		when(mediaService.getMedia(catalogVersion, MEDIA_MOBILE)).thenReturn(new MediaModel());
		when(mediaService.getMedia(catalogVersion, MEDIA_TABLET)).thenReturn(new MediaModel());
		when(Boolean.valueOf(validStringLengthPredicate.test(URL_LINK))).thenReturn(Boolean.TRUE);

		final LanguageData languageEN = new LanguageData();
		languageEN.setIsocode(EN);
		languageEN.setRequired(true);

		when(languageFacade.getLanguages()).thenReturn(Lists.newArrayList(languageEN));
		validator.setLocalizedValidator(localizedValidator);
	}

	@Test
	public void shouldValidateAllMediaFormatsAndCodesMultipleLanguages()
	{
		final SimpleResponsiveBannerComponentData data = createComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertFalse(errors.hasErrors());
	}

	@Test
	public void shouldValidateAllMediaFormatsAndCodesSingleLanguage()
	{
		final SimpleResponsiveBannerComponentData data = createComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertFalse(errors.hasErrors());
	}

	@Test
	public void shouldFailAllMediaCodesNull()
	{
		mediaCodePerFormatMap.put(FORMAT_WIDESCREEN, null);
		mediaCodePerFormatMap.put(FORMAT_MOBILE, null);
		mediaCodePerFormatMap.put(FORMAT_TABLET, null);

		final SimpleResponsiveBannerComponentData data = createComponentDataNoMediaCodes();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertEquals(1, errors.getFieldErrorCount());
		assertEquals(CmsfacadesConstants.FIELD_MEDIA_FORMAT_REQUIRED_L10N, errors.getFieldError().getCode());
	}

	@Test
	public void shouldValidateMediaCodesEmptyForNonRequiredFormats()
	{
		final Map<String, Boolean> locales = new HashMap<>();
		locales.put(ENGLISH, Boolean.TRUE);

		mediaCodePerFormatMap.put(FORMAT_WIDESCREEN, MEDIA_WIDESCREEN);
		mediaCodePerFormatMap.put(FORMAT_MOBILE, "");
		mediaCodePerFormatMap.put(FORMAT_TABLET, "");

		final SimpleResponsiveBannerComponentData data = createComponentDataNoMediaCodes();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertFalse(errors.hasErrors());
	}

	@Test
	public void shouldFailMediaCodeNullForRequiredLanguageAndRequiredFormat()
	{
		mediaCodePerFormatMap = new TreeMap<>();
		mediaCodePerFormatMap.put(FORMAT_WIDESCREEN, null);
		mediaCodePerFormatMap.put(FORMAT_MOBILE, MEDIA_MOBILE);
		mediaCodePerFormatMap.put(FORMAT_TABLET, MEDIA_TABLET);

		final SimpleResponsiveBannerComponentData data = createComponentDataNoMediaCodes();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertEquals(1, errors.getFieldErrorCount());
		assertEquals(CmsfacadesConstants.FIELD_MEDIA_FORMAT_REQUIRED_L10N, errors.getFieldError().getCode());
	}

	@Test
	public void shouldFailMediaCodeEmptyForRequiredLanguageAndRequiredFormat()
	{
		mediaCodePerFormatMap = new TreeMap<>();
		mediaCodePerFormatMap.put(FORMAT_WIDESCREEN, "");
		mediaCodePerFormatMap.put(FORMAT_MOBILE, MEDIA_MOBILE);
		mediaCodePerFormatMap.put(FORMAT_TABLET, MEDIA_TABLET);

		final SimpleResponsiveBannerComponentData data = createComponentDataNoMediaCodes();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertEquals(1, errors.getFieldErrorCount());
		assertEquals(CmsfacadesConstants.FIELD_MEDIA_FORMAT_REQUIRED_L10N, errors.getFieldError().getCode());
	}

	@Test
	public void shouldFailMediaCodeAndFormatEmptyForRequiredLanguage()
	{
		mediaCodePerFormatMap = new TreeMap<>();

		final SimpleResponsiveBannerComponentData data = createComponentDataNoMediaCodes();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertEquals(1, errors.getFieldErrorCount());
		assertEquals(CmsfacadesConstants.FIELD_MEDIA_FORMAT_REQUIRED_L10N, errors.getFieldError().getCode());
	}

	@Test
	public void shouldValidateUrlLink_ValidLength()
	{
		final SimpleResponsiveBannerComponentData data = createComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().isEmpty());
	}

	@Test
	public void shouldValidateUrlLink_InvalidLength()
	{
		when(Boolean.valueOf(validStringLengthPredicate.test(URL_LINK))).thenReturn(Boolean.FALSE);
		final SimpleResponsiveBannerComponentData data = createComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().size() == 1);
		assertEquals(URL_LINK, errors.getFieldError().getField());
	}

	protected SimpleResponsiveBannerComponentData createComponentData()
	{
		final Map<String, Map<String, String>> localizedMediaValues = new HashMap<>();
		localizedMediaValues.put(ENGLISH, mediaCodePerFormatMap);
		localizedMediaValues.put(FRENCH, Collections.emptyMap());

		final SimpleResponsiveBannerComponentData data = new SimpleResponsiveBannerComponentData();
		data.setUid(SALES_BANNER_UID);
		data.setUrlLink(URL_LINK);
		data.setMedia(localizedMediaValues);
		return data;
	}

	protected SimpleResponsiveBannerComponentData createComponentDataNoMediaCodes()
	{
		final Map<String, Map<String, String>> localizedMediaValues = new HashMap<>();
		localizedMediaValues.put(ENGLISH, mediaCodePerFormatMap);

		final SimpleResponsiveBannerComponentData data = new SimpleResponsiveBannerComponentData();
		data.setUid(SALES_BANNER_UID);
		data.setUrlLink(URL_LINK);
		data.setMedia(localizedMediaValues);
		return data;
	}
}
