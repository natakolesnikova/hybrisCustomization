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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.data.ProductCarouselComponentData;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ProductCarouselComponentValidatorTest
{

	//product and categories id's constants
	private static final String PRODUCT_1_ID = "Product1";
	private static final String PRODUCT_2_ID = "Product2";
	private static final String CATEGORY_1_ID = "Category1";
	private static final String CATEGORY_2_ID = "Category2";

	@InjectMocks
	private ProductCarouselComponentValidator validator;

	final ProductCarouselComponentData data = new ProductCarouselComponentData();
	final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

	@Test
	public void shouldValidateAllFields_Valid()
	{
		data.setProducts(Arrays.asList(PRODUCT_1_ID, PRODUCT_2_ID));
		data.setCategories(Arrays.asList(CATEGORY_1_ID, CATEGORY_2_ID));

		validator.validate(data, errors);
		assertThat(Integer.valueOf(errors.getFieldErrorCount()), is(Integer.valueOf(0)));

	}

	@Test
	public void shouldValidateAllFields_ValidOnlyProductsIsAvailable()
	{

		data.setProducts(Arrays.asList(PRODUCT_1_ID, PRODUCT_2_ID));

		validator.validate(data, errors);
		assertThat(Integer.valueOf(errors.getFieldErrorCount()), is(Integer.valueOf(0)));

	}

	@Test
	public void shouldValidateAllFields_ValidOnlyCategoriesIsAvailable()
	{

		data.setCategories(Arrays.asList(CATEGORY_1_ID, CATEGORY_2_ID));

		validator.validate(data, errors);
		assertThat(Integer.valueOf(errors.getFieldErrorCount()), is(Integer.valueOf(0)));

	}

	@Test
	public void shouldValidateAllFields_NeitherProductsNorCategoriesIsProvided()
	{

		validator.validate(data, errors);
		assertThat(Integer.valueOf(errors.getFieldErrorCount()), is(Integer.valueOf(1)));

	}

}
