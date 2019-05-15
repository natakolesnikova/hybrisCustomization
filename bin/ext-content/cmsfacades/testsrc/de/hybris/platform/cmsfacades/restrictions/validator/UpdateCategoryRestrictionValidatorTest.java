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
package de.hybris.platform.cmsfacades.restrictions.validator;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.CategoryRestrictionData;
import de.hybris.platform.cmsfacades.dto.UpdateRestrictionValidationDto;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class UpdateCategoryRestrictionValidatorTest
{

	private static final String validKey = "valid-key";
	private static final String invalidKey = "valid-key";

	@InjectMocks
	private UpdateCategoryRestrictionValidator validator;
	@Mock
	private Predicate<String> categoryExistsPredicate;

	private Errors errors;
	private UpdateRestrictionValidationDto validationDto;
	private CategoryRestrictionData data;

	@Before
	public void setUp()
	{
		final List<String> categories = new ArrayList<>(Arrays.asList(validKey));
		data = new CategoryRestrictionData();
		data.setRecursive(false);
		data.setCategories(categories);

		validationDto = new UpdateRestrictionValidationDto();
		validationDto.setRestriction(data);

		when(categoryExistsPredicate.test(validKey)).thenReturn(Boolean.TRUE);
	}

	@Test
	public void shouldPassValidationForUpdateCategoryRestriction() throws ParseException
	{
		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(validationDto, errors);

		assertThat(errors.getAllErrors(), empty());
	}

	@Test
	public void shouldFailMissingCategoriesFields() throws ParseException
	{
		data.setCategories(null);

		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(validationDto, errors);
		final List<ObjectError> errorList = errors.getAllErrors();

		assertThat(errors.getErrorCount(), equalTo(1));
		assertThat(((FieldError) errorList.get(0)).getField(), equalTo(CMSCategoryRestrictionModel.CATEGORIES));
		assertThat((errorList.get(0)).getCode(), equalTo(CmsfacadesConstants.FIELD_REQUIRED));

	}

	@Test
	public void shouldFailEmptyCategories() throws ParseException
	{
		data.setCategories(new ArrayList<String>());

		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(validationDto, errors);
		final List<ObjectError> errorList = errors.getAllErrors();

		assertThat(errors.getErrorCount(), equalTo(1));
		assertThat(((FieldError) errorList.get(0)).getField(), equalTo(CMSCategoryRestrictionModel.CATEGORIES));
		assertThat((errorList.get(0)).getCode(), equalTo(CmsfacadesConstants.FIELD_MIN_VIOLATED));
	}

	@Test
	public void shouldFailNonExistantCategoryCodes() throws ParseException
	{
		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		data.setCategories(new ArrayList<>(Arrays.asList(invalidKey)));

		when(categoryExistsPredicate.test(invalidKey)).thenReturn(Boolean.FALSE);

		validator.validate(validationDto, errors);
		final List<ObjectError> errorList = errors.getAllErrors();

		assertThat(errors.getErrorCount(), equalTo(1));
		assertThat(((FieldError) errorList.get(0)).getField(), equalTo(CMSCategoryRestrictionModel.CATEGORIES));
		assertThat((errorList.get(0)).getCode(), equalTo(CmsfacadesConstants.FIELD_DOES_NOT_EXIST));
	}

}