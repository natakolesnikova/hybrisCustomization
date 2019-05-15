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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.common.validator.CompositeValidator;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.cmsfacades.dto.UpdateComponentValidationDto;

import java.util.Map;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class UpdateComponentValidatorTest
{
	private static final String ORIGINAL_UID = "original-uid";
	private static final String UID = "uid";

	@InjectMocks
	private UpdateComponentValidator validator;

	@Mock
	private Map<Class<?>, Validator> cmsComponentValidatorFactory;
	@Mock
	private Predicate<String> componentExistsPredicate;
	@Mock
	private CompositeValidator compositeValidator;

	private AbstractCMSComponentData component;
	private UpdateComponentValidationDto target;
	private Errors errors;

	@Before
	public void setUp()
	{
		component = new AbstractCMSComponentData();
		component.setUid(UID);

		target = new UpdateComponentValidationDto();
		target.setOriginalUid(ORIGINAL_UID);
		target.setComponent(component);

		errors = new BeanPropertyBindingResult(target.getComponent(), target.getComponent().getClass().getSimpleName());

		when(componentExistsPredicate.test(UID)).thenReturn(Boolean.FALSE);
		when(cmsComponentValidatorFactory.get(target.getComponent().getClass())).thenReturn(compositeValidator);
	}

	@Test
	public void shouldValidate_OriginalUidEqualsNewUid()
	{
		target.setOriginalUid(UID);

		validator.validate(target, errors);
		assertFalse(errors.hasErrors());
	}

	@Test
	public void shouldValidate_NewUidDoesNotExist()
	{
		validator.validate(target, errors);
		Assert.assertFalse(errors.hasErrors());
	}

	@Test
	public void shouldFailValidation_NewUidAlreadyExists()
	{
		when(componentExistsPredicate.test(UID)).thenReturn(Boolean.TRUE);

		validator.validate(target, errors);
		assertEquals(1, errors.getFieldErrorCount());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailValidation_CompositeValidatorNotFound()
	{
		when(cmsComponentValidatorFactory.get(target.getComponent().getClass())).thenReturn(null);

		validator.validate(target, errors);
	}
}
