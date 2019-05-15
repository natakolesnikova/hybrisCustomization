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
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.ContentPageData;
import de.hybris.platform.cmsfacades.dto.UpdatePageValidationDto;

import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class UpdateContentPageValidatorTest
{
	private static final String TEST_LABEL = "test-label";

	@InjectMocks
	private UpdateContentPageValidator validator;

	@Mock
	private Predicate<ContentPageData> primaryPageWithLabelExistsPredicate;
	@Mock
	private Predicate<ContentPageData> hasPageLabelChangedPredicate;

	@Mock
	private ContentPageData page;

	private Errors errors;
	private UpdatePageValidationDto validationDto;

	@Before
	public void setUp()
	{
		validationDto = new UpdatePageValidationDto();
		validationDto.setPage(page);

		errors = new BeanPropertyBindingResult(page, ContentPageData.class.getSimpleName());

		when(page.getDefaultPage()).thenReturn(Boolean.TRUE);
	}

	@Test
	public void shouldPassValidationForVariationPage()
	{
		when(page.getLabel()).thenReturn(TEST_LABEL);
		when(page.getDefaultPage()).thenReturn(Boolean.FALSE);
		when(primaryPageWithLabelExistsPredicate.test(page)).thenReturn(Boolean.TRUE);

		validator.validate(validationDto, errors);

		assertThat(errors.getAllErrors(), empty());
	}

	@Test
	public void shouldPassValidationForPrimaryPage()
	{
		when(page.getLabel()).thenReturn(TEST_LABEL);
		when(page.getDefaultPage()).thenReturn(Boolean.TRUE);
		when(primaryPageWithLabelExistsPredicate.test(page)).thenReturn(Boolean.FALSE);
		when(hasPageLabelChangedPredicate.test(page)).thenReturn(Boolean.TRUE);

		validator.validate(validationDto, errors);

		assertThat(errors.getAllErrors(), empty());
	}

	@Test
	public void shouldFailValidation_MissingLabel()
	{
		when(page.getLabel()).thenReturn(null);

		validator.validate(validationDto, errors);

		assertThat(errors.getFieldErrorCount(), is(1));
		assertThat(errors.getFieldError().getField(), is(ContentPageModel.LABEL));
		assertThat(errors.getFieldError().getCode(), is(CmsfacadesConstants.FIELD_REQUIRED));

		verifyZeroInteractions(primaryPageWithLabelExistsPredicate, hasPageLabelChangedPredicate);
	}

	@Test
	public void shouldFailValidationForVariationPage_LabelNotExists()
	{
		when(page.getLabel()).thenReturn(TEST_LABEL);
		when(page.getDefaultPage()).thenReturn(Boolean.FALSE);
		when(primaryPageWithLabelExistsPredicate.test(page)).thenReturn(Boolean.FALSE);

		validator.validate(validationDto, errors);

		assertThat(errors.getFieldErrorCount(), is(1));
		assertThat(errors.getFieldError().getField(), is(ContentPageModel.LABEL));
		assertThat(errors.getFieldError().getCode(), is(CmsfacadesConstants.DEFAULT_PAGE_DOES_NOT_EXIST));
	}

	@Test
	public void shouldFailValidationForPrimaryPage_LabelAlreadyExists()
	{
		when(page.getLabel()).thenReturn(TEST_LABEL);
		when(page.getDefaultPage()).thenReturn(Boolean.TRUE);
		when(primaryPageWithLabelExistsPredicate.test(page)).thenReturn(Boolean.TRUE);
		when(hasPageLabelChangedPredicate.test(page)).thenReturn(Boolean.TRUE);

		validator.validate(validationDto, errors);

		assertThat(errors.getFieldErrorCount(), is(1));
		assertThat(errors.getFieldError().getField(), is(ContentPageModel.LABEL));
		assertThat(errors.getFieldError().getCode(), is(CmsfacadesConstants.DEFAULT_PAGE_ALREADY_EXIST));
	}

}
