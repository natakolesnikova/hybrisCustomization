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
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cmsfacades.data.AbstractRestrictionData;

import java.util.function.Predicate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CreateRestrictionValidatorTest
{
	private static final String UID = "test-restriction-id";
	private static final String NAME = "My Test Restriction";
	private static final String LONG_NAME = "This test restriction name should be veeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeery "
			+ "loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo"
			+ "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong";

	@Mock
	private Predicate<String> validStringLengthPredicate;
	@Mock
	private Predicate<String> restrictionNameExistsPredicate;
	@InjectMocks
	private CreateRestrictionValidator validator;

	private Errors errors;
	private AbstractRestrictionData data;

	@Test
	public void shouldPassValidation()
	{
		data = new AbstractRestrictionData();
		data.setTypeCode(AbstractRestrictionModel._TYPECODE);
		data.setUid(UID);
		data.setName(NAME);
		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		when(validStringLengthPredicate.test(NAME)).thenReturn(Boolean.TRUE);
		when(restrictionNameExistsPredicate.test(NAME)).thenReturn(Boolean.FALSE);

		validator.validate(data, errors);

		assertThat(errors.getAllErrors(), empty());
	}

	@Test
	public void shouldFailMissingRequiredFields()
	{
		data = new AbstractRestrictionData();
		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertThat(errors.getErrorCount(), equalTo(3));
	}

	@Test
	public void shouldFailSuperLongName()
	{
		data = new AbstractRestrictionData();
		data.setTypeCode(AbstractRestrictionModel._TYPECODE);
		data.setUid(UID);
		data.setName(LONG_NAME);
		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertThat(errors.getErrorCount(), equalTo(1));
	}

	@Test
	public void shouldFailDuplicateName()
	{
		data = new AbstractRestrictionData();
		data.setTypeCode(AbstractRestrictionModel._TYPECODE);
		data.setUid(UID);
		data.setName(NAME);
		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		when(restrictionNameExistsPredicate.test(NAME)).thenReturn(Boolean.TRUE);

		validator.validate(data, errors);

		assertThat(errors.getErrorCount(), equalTo(1));
	}

}
