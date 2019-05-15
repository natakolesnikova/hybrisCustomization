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

import static de.hybris.platform.cms2.model.contents.CMSItemModel.NAME;
import static de.hybris.platform.cms2.model.contents.CMSItemModel.UID;
import static de.hybris.platform.cms2.model.pages.AbstractPageModel.DEFAULTPAGE;
import static de.hybris.platform.cms2.model.pages.AbstractPageModel.TITLE;
import static de.hybris.platform.cms2.model.pages.AbstractPageModel.TYPECODE;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmsfacades.common.validator.LocalizedTypeValidator;
import de.hybris.platform.cmsfacades.common.validator.LocalizedValidator;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsfacades.dto.UpdatePageValidationDto;

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
public class UpdatePageValidatorTest
{
	private static final String TEST_UID = "test-uid";
	private static final String TEST_NEW_UID = "test-new-uid";
	private static final String TEST_NAME = "test-name";
	private static final String INVALID = "invalid";

	@InjectMocks
	private UpdatePageValidator validator;

	@Mock
	private Predicate<String> pageExistsPredicate;
	@Mock
	private Predicate<String> onlyHasSupportedCharactersPredicate;
	@Mock
	private LocalizedValidator localizedValidator;
	@Mock
	private LocalizedTypeValidator localizedStringValidator;
	@Mock
	private CMSAdminPageService cmsAdminPageService;
	@Mock
	private AbstractPageModel pageModel;

	private AbstractPageData page;
	private Errors errors;
	private UpdatePageValidationDto validationDto;

	@Before
	public void setUp()
	{
		page = new AbstractPageData();
		page.setName(TEST_NAME);
		page.setDefaultPage(Boolean.TRUE);
		page.setUid(TEST_UID);
		page.setTypeCode(AbstractPageModel._TYPECODE);

		errors = new BeanPropertyBindingResult(page, AbstractPageData.class.getSimpleName());

		validationDto = new UpdatePageValidationDto();
		validationDto.setOriginalUid(TEST_UID);
		validationDto.setPage(page);

		when(onlyHasSupportedCharactersPredicate.test(TEST_UID)).thenReturn(Boolean.TRUE);
		when(pageExistsPredicate.test(TEST_UID)).thenReturn(Boolean.TRUE);
		doNothing().when(localizedValidator).validateRequiredLanguages(any(), any(), any());

		when(pageModel.getDefaultPage()).thenReturn(Boolean.TRUE);
		when(cmsAdminPageService.getPageForIdFromActiveCatalogVersion(TEST_UID)).thenReturn(pageModel);
	}

	@Test
	public void shouldPassValidation()
	{
		validator.validate(validationDto, errors);

		assertThat(errors.getAllErrors(), empty());
	}

	@Test
	public void shouldFailValidation_MissingUid()
	{
		page.setUid(null);
		when(pageExistsPredicate.test(null)).thenReturn(Boolean.FALSE);

		validator.validate(validationDto, errors);

		assertThat(errors.getFieldErrorCount(), is(1));
		assertThat(errors.getFieldError().getField(), is(UID));
		assertThat(errors.getFieldError().getCode(), is(CmsfacadesConstants.FIELD_REQUIRED));
	}

	@Test
	public void shouldFailValidation_MissingName()
	{
		page.setName(null);

		validator.validate(validationDto, errors);

		assertThat(errors.getFieldErrorCount(), is(1));
		assertThat(errors.getFieldError().getField(), is(NAME));
		assertThat(errors.getFieldError().getCode(), is(CmsfacadesConstants.FIELD_REQUIRED));
	}

	@Test
	public void shouldFailValidation_MissingTypeCode()
	{
		page.setTypeCode(null);

		validator.validate(validationDto, errors);

		assertThat(errors.getFieldErrorCount(), is(1));
		assertThat(errors.getFieldError().getField(), is(TYPECODE));
		assertThat(errors.getFieldError().getCode(), is(CmsfacadesConstants.FIELD_REQUIRED));
	}

	@Test
	public void shouldFailValidation_UidHasUnsupportedChar()
	{
		page.setUid(INVALID);
		when(pageExistsPredicate.test(INVALID)).thenReturn(Boolean.FALSE);
		when(onlyHasSupportedCharactersPredicate.test(INVALID)).thenReturn(Boolean.FALSE);

		validator.validate(validationDto, errors);

		assertThat(errors.getFieldErrorCount(), is(1));
		assertThat(errors.getFieldError().getField(), is(UID));
		assertThat(errors.getFieldError().getCode(), is(CmsfacadesConstants.FIELD_CONTAINS_INVALID_CHARS));
	}

	@Test
	public void shouldFailValidation_UidAlreadyExists()
	{
		page.setUid(TEST_NEW_UID);
		when(onlyHasSupportedCharactersPredicate.test(TEST_NEW_UID)).thenReturn(Boolean.TRUE);
		when(pageExistsPredicate.test(TEST_NEW_UID)).thenReturn(Boolean.TRUE);
		when(cmsAdminPageService.getPageForIdFromActiveCatalogVersion(TEST_NEW_UID)).thenReturn(pageModel);

		validator.validate(validationDto, errors);

		assertThat(errors.getFieldErrorCount(), is(1));
		assertThat(errors.getFieldError().getField(), is(UID));
		assertThat(errors.getFieldError().getCode(), is(CmsfacadesConstants.FIELD_ALREADY_EXIST));
	}

	@Test
	public void shouldValidatePage_MissingRequiredTitle()
	{
		doAnswer((Answer<Void>) invocationOnMock -> {
			//create validation error
			errors.rejectValue(TITLE, CmsfacadesConstants.FIELD_REQUIRED_L10N, new Object[]
					{ Locale.ENGLISH.toString() }, null);
			return null;
		}).when(localizedValidator).validateRequiredLanguages(any(), any(), any());

		validator.validate(validationDto, errors);

		assertThat(errors.getFieldErrors(), iterableWithSize(1));
		assertThat(errors.getFieldError().getField(), is(TITLE));
		assertThat(errors.getFieldError().getCode(), is(CmsfacadesConstants.FIELD_REQUIRED_L10N));
	}

	@Test
	public void shouldFailValidation_UpdatedDefaultPage()
	{
		page.setDefaultPage(Boolean.FALSE);

		validator.validate(validationDto, errors);

		assertThat(errors.getFieldErrorCount(), is(1));
		assertThat(errors.getFieldError().getField(), is(DEFAULTPAGE));
		assertThat(errors.getFieldError().getCode(), is(CmsfacadesConstants.FIELD_NOT_ALLOWED));
	}

}
