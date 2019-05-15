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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.common.validator.LocalizedTypeValidator;
import de.hybris.platform.cmsfacades.common.validator.LocalizedValidator;
import de.hybris.platform.cmsfacades.data.SimpleBannerComponentData;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SimpleBannerComponentValidatorTest
{
	private static final String ERROR_REQUIRED_FIELD = "missing required field";
	private static final String ERROR_INVALID_CODE = "invalid code value";
	private static final String MEDIA = "media";
	private static final String ENGLISH = Locale.ENGLISH.toString();
	private static final String FRENCH = Locale.FRENCH.toString();
	private static final String URL_LINK = "urlLink";

	@InjectMocks
	private SimpleBannerComponentValidator validator;

	@Mock
	private LocalizedValidator localizedValidator;
	@Mock
	private LocalizedTypeValidator localizedStringValidator;
	@Mock
	private LocalizedTypeValidator localizedMediaValidator;
	@Mock
	private Predicate<String> validStringLengthPredicate;

	@Before
	public void setup()
	{
		when(Boolean.valueOf(validStringLengthPredicate.test(URL_LINK))).thenReturn(Boolean.TRUE);
	}

	@Test
	public void shouldValidateAllFields_Valid()
	{
		final SimpleBannerComponentData data = createSimpleBannerComponent();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		doNothing().when(localizedValidator).validateRequiredLanguages(any(), any(), any());
		doNothing().when(localizedValidator).validateAllLanguages(any(), any(), any());

		validator.validate(data, errors);

		verify(localizedValidator).validateRequiredLanguages(any(), any(), any());
		verify(localizedValidator).validateAllLanguages(any(), any(), any());
		assertThat(Integer.valueOf(errors.getFieldErrorCount()), is(Integer.valueOf(0)));
	}

	@Test
	public void shouldValidateAllFields_MissingRequiredFields()
	{
		final SimpleBannerComponentData data = createEmptySimpleBannerComponent();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		doAnswer((Answer<Void>) invocationOnMock -> {
			reject(MEDIA, ERROR_REQUIRED_FIELD, ENGLISH, errors);
			return null;
		}).when(localizedValidator).validateRequiredLanguages(any(), any(), any());
		doNothing().when(localizedValidator).validateAllLanguages(any(), any(), any());

		validator.validate(data, errors);

		// only english is required
		assertThat(Integer.valueOf(errors.getFieldErrorCount()), is(Integer.valueOf(1)));
	}

	@Test
	public void shouldValidateAllFields_InvalidMediaCodes()
	{
		final SimpleBannerComponentData data = createSimpleBannerComponentWithInvalidMediaCodes();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		doNothing().when(localizedValidator).validateRequiredLanguages(any(), any(), any());
		// media codes are invalid
		doAnswer((Answer<Void>) invocationOnMock -> {
			reject(MEDIA, ERROR_INVALID_CODE, ENGLISH, errors);
			reject(MEDIA, ERROR_INVALID_CODE, FRENCH, errors);
			return null;
		}).when(localizedValidator).validateAllLanguages(any(), any(), any());

		validator.validate(data, errors);

		assertThat(Integer.valueOf(errors.getFieldErrorCount()), is(Integer.valueOf(2)));
	}

	@Test
	public void shouldValidateUrlLink_ValidLength()
	{
		final SimpleBannerComponentData data = createSimpleBannerComponent();

		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertThat(Integer.valueOf(errors.getFieldErrorCount()), is(Integer.valueOf(0)));
	}

	@Test
	public void shouldValidateUrlLink_InvalidLength()
	{
		final SimpleBannerComponentData data = createSimpleBannerComponent();
		when(Boolean.valueOf(validStringLengthPredicate.test(URL_LINK))).thenReturn(Boolean.FALSE);

		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertThat(Integer.valueOf(errors.getAllErrors().size()), is(Integer.valueOf(1)));
		assertThat(URL_LINK, is(errors.getFieldError().getField()));
	}

	protected void reject(final String fieldName, final String errorCode, final String language, final Errors errors)
	{
		errors.rejectValue(fieldName, errorCode, new Object[]
		{ language }, null);
	}

	protected SimpleBannerComponentData createEmptySimpleBannerComponent()
	{
		final SimpleBannerComponentData data = new SimpleBannerComponentData();
		final Map<String, String> localizedMediaCodes = new HashMap<>();
		localizedMediaCodes.put(ENGLISH, null);
		localizedMediaCodes.put(FRENCH, null);
		data.setMedia(localizedMediaCodes);
		return data;
	}

	protected SimpleBannerComponentData createSimpleBannerComponentWithInvalidMediaCodes()
	{
		final SimpleBannerComponentData data = new SimpleBannerComponentData();
		final Map<String, String> localizedMediaCodes = new HashMap<>();
		localizedMediaCodes.put(ENGLISH, "invalid");
		localizedMediaCodes.put(ENGLISH, "invalid");
		data.setMedia(localizedMediaCodes);
		return data;
	}

	protected SimpleBannerComponentData createSimpleBannerComponent()
	{
		final SimpleBannerComponentData data = new SimpleBannerComponentData();
		data.setExternal(Boolean.TRUE);
		data.setUrlLink(URL_LINK);

		final Map<String, String> localizedMediaCodes = new HashMap<>();
		localizedMediaCodes.put(ENGLISH, "mediaCode1");
		localizedMediaCodes.put(ENGLISH, "mediaCode2");
		data.setMedia(localizedMediaCodes);
		return data;
	}

}
