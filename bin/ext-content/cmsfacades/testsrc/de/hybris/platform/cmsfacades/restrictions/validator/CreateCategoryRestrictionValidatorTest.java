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
public class CreateCategoryRestrictionValidatorTest
{
	@InjectMocks
	private CreateCategoryRestrictionValidator validator;
	@Mock
	private Predicate<String> categoryExistsPredicate;

	private Errors errors;
	private CategoryRestrictionData data;
	private static final String CATEGORY_CODE = "PantsUK-staged-pants007";

	@Before
	public void setUp()
	{
		final List<String> categoryCodes = Arrays.asList(CATEGORY_CODE);
		data = new CategoryRestrictionData();
		data.setRecursive(false);
		data.setCategories(categoryCodes);

		when(categoryExistsPredicate.test(CATEGORY_CODE)).thenReturn(Boolean.TRUE);
	}

	@Test
	public void shouldPassValidation() throws ParseException
	{
		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertThat(errors.getAllErrors(), empty());
	}

	@Test
	public void shouldFailMissingRequiredField() throws ParseException
	{
		data.setCategories(null);
		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);
		final List<ObjectError> errorList = errors.getAllErrors();

		assertThat(errors.getErrorCount(), equalTo(1));
		assertThat(((FieldError) errorList.get(0)).getField(), equalTo(CMSCategoryRestrictionModel.CATEGORIES));
		assertThat((errorList.get(0)).getCode(), equalTo(CmsfacadesConstants.FIELD_REQUIRED));

	}

	@Test
	public void shouldFailEmptyCategoryCodes() throws ParseException
	{
		data.setCategories(new ArrayList<String>());

		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);
		final List<ObjectError> errorList = errors.getAllErrors();

		assertThat(errors.getErrorCount(), equalTo(1));
		assertThat(((FieldError) errorList.get(0)).getField(), equalTo(CMSCategoryRestrictionModel.CATEGORIES));
		assertThat((errorList.get(0)).getCode(), equalTo(CmsfacadesConstants.FIELD_MIN_VIOLATED));
	}

	@Test
	public void shouldFailNonExistantCategoryCode() throws ParseException
	{
		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		when(categoryExistsPredicate.test(CATEGORY_CODE)).thenReturn(Boolean.FALSE);

		validator.validate(data, errors);
		final List<ObjectError> errorList = errors.getAllErrors();

		assertThat(errors.getErrorCount(), equalTo(1));
		assertThat(((FieldError) errorList.get(0)).getField(), equalTo(CMSCategoryRestrictionModel.CATEGORIES));
		assertThat((errorList.get(0)).getCode(), equalTo(CmsfacadesConstants.FIELD_DOES_NOT_EXIST));
	}

}
