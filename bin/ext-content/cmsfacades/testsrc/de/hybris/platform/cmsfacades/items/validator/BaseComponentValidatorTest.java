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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.cmsfacades.data.CMSParagraphComponentData;

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
public class BaseComponentValidatorTest
{
	private static final String COMPONENT_ID = "component-id";
	private static final String INVALID_COMPONENT_ID = "invalid-component-id";
	private static final String TYPE_CODE = "typeCode";
	private static final String NAME = "name";
	private static final String UID_FIELD = "uid";

	@InjectMocks
	private final BaseComponentValidator validator = new BaseComponentValidator();

	@Mock
	private Predicate<String> validStringLengthPredicate;
	@Mock
	private Predicate<String> componentExistsPredicate;
	@Mock
	private Predicate<String> typeCodeExistsPredicate;

	@Before
	public void setUp()
	{
		when(componentExistsPredicate.test(COMPONENT_ID)).thenReturn(Boolean.FALSE);
		when(componentExistsPredicate.test(INVALID_COMPONENT_ID)).thenReturn(Boolean.TRUE);
		when(typeCodeExistsPredicate.test(TYPE_CODE)).thenReturn(Boolean.TRUE);
		when(validStringLengthPredicate.test(COMPONENT_ID)).thenReturn(Boolean.TRUE);
		when(validStringLengthPredicate.test(NAME)).thenReturn(Boolean.TRUE);
	}

	@Test
	public void shouldFail_ComponentUidNull()
	{
		final AbstractCMSComponentData data = createComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		when(validStringLengthPredicate.test(COMPONENT_ID)).thenReturn(Boolean.FALSE);

		validator.validate(data, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}

	@Test
	public void shouldFail_TypeCodeNotFound()
	{
		final AbstractCMSComponentData data = createComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		when(typeCodeExistsPredicate.test(TYPE_CODE)).thenReturn(Boolean.FALSE);

		validator.validate(data, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}


	@Test
	public void shouldFail_TypeCodeNull()
	{
		final AbstractCMSComponentData data = createComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		data.setTypeCode(null);

		validator.validate(data, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}




	@Test
	public void shouldFail_NameNull()
	{
		final AbstractCMSComponentData data = createComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		data.setName(null);

		validator.validate(data, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}

	@Test
	public void shouldFail_NameTooLong()
	{
		final AbstractCMSComponentData data = createComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		when(validStringLengthPredicate.test(NAME)).thenReturn(Boolean.FALSE);

		validator.validate(data, errors);
		assertThat(errors.hasErrors(), is(true));
		assertEquals(1, errors.getFieldErrorCount());
	}

	@Test
	public void shouldValidateComponent_All_Fields_are_valid()
	{
		final AbstractCMSComponentData data = createComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().isEmpty());
	}

	@Test
	public void shouldValidateComponent_Invalid_Uid()
	{
		final AbstractCMSComponentData data = createComponentData();
		data.setUid(null);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().size() == 1);
		assertEquals(UID_FIELD, errors.getFieldError().getField());
	}

	@Test
	public void shouldValidateComponent_Invalid_Name()
	{
		final AbstractCMSComponentData data = createComponentData();
		data.setName(null);

		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().size() == 1);
		assertEquals(NAME, errors.getFieldError().getField());
	}

	@Test
	public void shouldValidateComponent_Invalid_Name_Length()
	{
		when(validStringLengthPredicate.test(NAME)).thenReturn(Boolean.FALSE);
		final AbstractCMSComponentData data = createComponentData();

		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertEquals(1, errors.getAllErrors().size());
		assertEquals(NAME, errors.getFieldError().getField());
	}

	@Test
	public void shouldValidateComponent_Invalid_TypeCode()
	{
		final AbstractCMSComponentData data = createComponentData();
		data.setTypeCode(null);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().size() == 1);
		assertEquals(TYPE_CODE, errors.getFieldError().getField());
	}

	protected AbstractCMSComponentData createComponentData()
	{
		final CMSParagraphComponentData data = new CMSParagraphComponentData();
		data.setTypeCode(TYPE_CODE);
		data.setUid(COMPONENT_ID);
		data.setName(NAME);
		return data;
	}
}
