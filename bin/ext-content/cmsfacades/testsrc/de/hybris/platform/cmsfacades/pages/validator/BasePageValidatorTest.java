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
package de.hybris.platform.cmsfacades.pages.validator;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmsfacades.common.validator.LocalizedTypeValidator;
import de.hybris.platform.cmsfacades.common.validator.LocalizedValidator;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.AbstractPageData;

import java.util.Locale;
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
public class BasePageValidatorTest
{
	private static final String TYPE_CODE = "typeCode";
	private static final String NAME = "name";
	private static final String UID = "uid";
	private static final String TITLE = "title";

	private static final String TEST_UID = "test-uid";
	private static final String TEST_NAME = "test-name";
	private static final String INVALID = "invalid";

	@InjectMocks
	private BasePageValidator validator;

	@Mock
	private Predicate<String> pageExistsPredicate;
	@Mock
	private Predicate<String> onlyHasSupportedCharactersPredicate;
	@Mock
	private LocalizedValidator localizedValidator;
	@Mock
	private LocalizedTypeValidator localizedStringValidator;

	private Errors errors;
	private AbstractPageData pageData;

	@Before
	public void setUp()
	{
		pageData = new AbstractPageData();
		pageData.setUid(TEST_UID);
		pageData.setName(TEST_NAME);
		pageData.setTypeCode(ContentPageModel._TYPECODE);

		errors = new BeanPropertyBindingResult(pageData, pageData.getClass().getSimpleName());

		when(onlyHasSupportedCharactersPredicate.test(TEST_UID)).thenReturn(true);
		when(pageExistsPredicate.test(TEST_UID)).thenReturn(false);
		doNothing().when(localizedValidator).validateRequiredLanguages(any(), any(), any());
		doNothing().when(localizedStringValidator).validate(any(), any(), any(), any());
	}

	@Test
	public void shouldValidatePage_AllFieldsValid()
	{
		validator.validate(pageData, errors);
		assertThat(errors.getAllErrors(), empty());
	}

	@Test
	public void shouldValidatePage_NullName()
	{
		pageData.setName(null);

		validator.validate(pageData, errors);

		assertThat(errors.getFieldErrors(), iterableWithSize(1));
		assertEquals(NAME, errors.getFieldError().getField());
	}

	@Test
	public void shouldValidatePage_NullTypeCode()
	{
		pageData.setTypeCode(null);

		validator.validate(pageData, errors);

		assertThat(errors.getFieldErrors(), iterableWithSize(1));
		assertEquals(TYPE_CODE, errors.getFieldError().getField());
	}

	@Test
	public void shouldValidatePage_NullUid()
	{
		pageData.setUid(null);

		validator.validate(pageData, errors);

		assertThat(errors.getFieldErrors(), iterableWithSize(1));
		assertEquals(UID, errors.getFieldError().getField());
	}

	@Test
	public void shouldValidatePage_InvalidCharUid()
	{
		pageData.setUid(INVALID);
		when(onlyHasSupportedCharactersPredicate.test(TEST_UID)).thenReturn(false);

		validator.validate(pageData, errors);

		assertThat(errors.getFieldErrors(), iterableWithSize(1));
		assertThat(errors.getFieldError().getField(), is(UID));
		assertThat(errors.getFieldError().getCode(), is(CmsfacadesConstants.FIELD_CONTAINS_INVALID_CHARS));
	}

	@Test
	public void shouldValidatePage_UidAlreadyExists()
	{
		pageData.setUid(TEST_UID);
		when(pageExistsPredicate.test(TEST_UID)).thenReturn(true);

		validator.validate(pageData, errors);

		assertThat(errors.getFieldErrors(), iterableWithSize(1));
		assertThat(errors.getFieldError().getField(), is(UID));
		assertThat(errors.getFieldError().getCode(), is(CmsfacadesConstants.FIELD_ALREADY_EXIST));
	}

	@Test
	public void shouldValidatePage_MissingRequiredTitle()
	{
		doAnswer((Answer<Void>) invocationOnMock -> {
			reject(TITLE, CmsfacadesConstants.FIELD_REQUIRED_L10N, Locale.ENGLISH.toString(), errors);
			return null;
		}).when(localizedValidator).validateRequiredLanguages(any(), any(), any());

		validator.validate(pageData, errors);

		assertThat(errors.getFieldErrors(), iterableWithSize(1));
		assertThat(errors.getFieldError().getField(), is(TITLE));
	}

	protected void reject(final String fieldName, final String errorCode, final String language, final Errors errors)
	{
		errors.rejectValue(fieldName, errorCode, new Object[]
				{ language }, null);
	}
}
