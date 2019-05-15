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
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.ContentPageData;

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
public class CreateContentPageValidatorTest
{
	private static final String LABEL = "label";
	private static final String TEST_LABEL = "test-label";

	@InjectMocks
	private CreateContentPageValidator validator;
	@Mock
	private Predicate<ContentPageData> primaryPageWithLabelExistsPredicate;

	private Errors errors;
	private ContentPageData pageData;

	@Before
	public void setUp()
	{
		pageData = new ContentPageData();
		pageData.setLabel(TEST_LABEL);
		pageData.setDefaultPage(Boolean.FALSE);

		errors = new BeanPropertyBindingResult(pageData, pageData.getClass().getSimpleName());
	}

	@Test
	public void shouldFail_MissingRequiredLabel()
	{
		pageData.setLabel(null);

		validator.validate(pageData, errors);

		assertThat(errors.getFieldErrorCount(), is(1));
		assertThat(errors.getFieldError().getField(), is(LABEL));
	}

	@Test
	public void shouldPassCreatePrimaryPage()
	{
		pageData.setDefaultPage(Boolean.TRUE);
		when(primaryPageWithLabelExistsPredicate.test(pageData)).thenReturn(Boolean.FALSE);

		validator.validate(pageData, errors);

		assertThat(errors.getAllErrors(), empty());
	}

	@Test
	public void shouldPassCreateVariationPage()
	{
		pageData.setDefaultPage(Boolean.FALSE);
		when(primaryPageWithLabelExistsPredicate.test(pageData)).thenReturn(Boolean.TRUE);

		validator.validate(pageData, errors);

		assertThat(errors.getAllErrors(), empty());
	}

	@Test
	public void shouldFailCreatePrimaryPage_PrimaryAlreadyExists()
	{
		pageData.setDefaultPage(Boolean.TRUE);
		when(primaryPageWithLabelExistsPredicate.test(pageData)).thenReturn(Boolean.TRUE);

		validator.validate(pageData, errors);

		assertThat(errors.getFieldErrorCount(), is(1));
		assertThat(errors.getFieldError().getField(), is(LABEL));
		assertThat(errors.getFieldError().getCode(), is(CmsfacadesConstants.DEFAULT_PAGE_ALREADY_EXIST));
	}

	@Test
	public void shouldFailCreateVariationPage_PrimaryNotExist()
	{
		pageData.setDefaultPage(Boolean.FALSE);
		when(primaryPageWithLabelExistsPredicate.test(pageData)).thenReturn(Boolean.FALSE);

		validator.validate(pageData, errors);

		assertThat(errors.getFieldErrorCount(), is(1));
		assertThat(errors.getFieldError().getField(), is(LABEL));
		assertThat(errors.getFieldError().getCode(), is(CmsfacadesConstants.DEFAULT_PAGE_DOES_NOT_EXIST));
	}

}
