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
package de.hybris.platform.cmsfacades.items.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.common.validator.LocalizedTypeValidator;
import de.hybris.platform.cmsfacades.common.validator.LocalizedValidator;
import de.hybris.platform.cmsfacades.data.BannerComponentData;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
public class BannerComponentValidatorTest
{
	private static final String ERROR_REQUIRED_FIELD = "missing required field";
	private static final String ERROR_INVALID_CODE = "invalid code value";
	private static final String MEDIA = "media";
	private static final String HEADLINE = "headline";
	private static final String CONTENT = "content";
	private static final String ENGLISH = Locale.ENGLISH.toString();
	private static final String FRENCH = Locale.FRENCH.toString();

	@InjectMocks
	private BannerComponentValidator validator;

	@Mock
	private LocalizedValidator localizedValidator;
	@Mock
	private LocalizedTypeValidator localizedStringValidator;
	@Mock
	private LocalizedTypeValidator localizedMediaValidator;

	@Test
	public void shouldValidateAllFields_Valid()
	{
		final BannerComponentData data = createBannerComponent();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		doNothing().when(localizedValidator).validateRequiredLanguages(any(), any(), any());
		doNothing().when(localizedValidator).validateAllLanguages(any(), any(), any());

		validator.validate(data, errors);

		verify(localizedValidator, times(3)).validateRequiredLanguages(any(), any(), any());
		verify(localizedValidator).validateAllLanguages(any(), any(), any());
		assertThat(errors.getFieldErrorCount(), is(0));
	}

	@Test
	public void shouldValidateAllFields_MissingRequiredFields()
	{
		final BannerComponentData data = createEmptyBannerComponent();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		doAnswer((Answer<Void>) invocationOnMock -> {
			reject(CONTENT, ERROR_REQUIRED_FIELD, ENGLISH, errors);
			return null;
		}).doAnswer((Answer<Void>) invocationOnMock -> {
			reject(HEADLINE, ERROR_REQUIRED_FIELD, ENGLISH, errors);
			return null;
		}).doAnswer((Answer<Void>) invocationOnMock -> {
			reject(MEDIA, ERROR_REQUIRED_FIELD, ENGLISH, errors);
			return null;
		}).when(localizedValidator).validateRequiredLanguages(any(), any(), any());
		doNothing().when(localizedValidator).validateAllLanguages(any(), any(), any());

		validator.validate(data, errors);

		// only english is required
		assertThat(errors.getFieldErrorCount(), is(3));
	}

	@Test
	public void shouldValidateAllFields_InvalidMediaCodes()
	{
		final BannerComponentData data = createBannerComponentWithInvalidMediaCodes();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		doNothing().when(localizedValidator).validateRequiredLanguages(any(), any(), any());
		// media codes are invalid
		doAnswer((Answer<Void>) invocationOnMock -> {
			reject(MEDIA, ERROR_INVALID_CODE, ENGLISH, errors);
			reject(MEDIA, ERROR_INVALID_CODE, FRENCH, errors);
			return null;
		}).when(localizedValidator).validateAllLanguages(any(), any(), any());

		validator.validate(data, errors);

		assertThat(errors.getFieldErrorCount(), is(2));
	}

	protected void reject(final String fieldName, final String errorCode, final String language, final Errors errors)
	{
		errors.rejectValue(fieldName, errorCode, new Object[]
				{ language }, null);
	}

	protected BannerComponentData createEmptyBannerComponent()
	{
		final BannerComponentData data = new BannerComponentData();
		data.setContent(new HashMap<>());
		data.setHeadline(new HashMap<>());
		final Map<String, String> localizedMediaCodes = new HashMap<>();
		localizedMediaCodes.put(ENGLISH, null);
		localizedMediaCodes.put(FRENCH, null);
		data.setMedia(localizedMediaCodes);
		return data;
	}

	protected BannerComponentData createBannerComponentWithInvalidMediaCodes()
	{
		final BannerComponentData data = new BannerComponentData();
		final Map<String, String> localizedContents = new HashMap<>();
		localizedContents.put(ENGLISH, "english content");
		data.setContent(localizedContents);

		final Map<String, String> localizedHeadlines = new HashMap<>();
		localizedHeadlines.put(ENGLISH, "english headline");
		data.setHeadline(localizedHeadlines);

		final Map<String, String> localizedMediaCodes = new HashMap<>();
		localizedMediaCodes.put(ENGLISH, "invalid");
		localizedMediaCodes.put(ENGLISH, "invalid");
		data.setMedia(localizedMediaCodes);
		return data;
	}

	protected BannerComponentData createBannerComponent()
	{
		final BannerComponentData data = new BannerComponentData();
		final Map<String, String> localizedContents = new HashMap<>();
		localizedContents.put(ENGLISH, "english content");
		data.setContent(localizedContents);

		final Map<String, String> localizedHeadlines = new HashMap<>();
		localizedHeadlines.put(ENGLISH, "english headline");
		data.setHeadline(localizedHeadlines);

		final Map<String, String> localizedMediaCodes = new HashMap<>();
		localizedMediaCodes.put(ENGLISH, "mediaCode1");
		localizedMediaCodes.put(ENGLISH, "mediaCode2");
		data.setMedia(localizedMediaCodes);
		return data;
	}

}
