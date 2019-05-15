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
import de.hybris.platform.cmsfacades.dto.UpdateRestrictionValidationDto;

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
public class UpdateRestrictionValidatorTest
{
	private static final String UID = "test-restriction-id";
	private static final String NEW_UID = "test-new-restriction-id";
	private static final String NAME = "My Test Restriction";
	private static final String LONG_NAME = "This test restriction name should be veeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeery "
			+ "loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo"
			+ "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong";

	@InjectMocks
	private UpdateRestrictionValidator validator;
	@Mock
	private Predicate<String> validStringLengthPredicate;
	@Mock
	private Predicate<AbstractRestrictionData> restrictionNameExistsPredicate;
	@Mock
	private Predicate<AbstractRestrictionData> hasRestrictionNameChangedPredicate;
	@Mock
	private Predicate<String> restrictionExistsPredicate;


	private Errors errors;
	private UpdateRestrictionValidationDto validationDto;
	private AbstractRestrictionData data;

	@Before
	public void setUp()
	{
		data = new AbstractRestrictionData();
		data.setTypeCode(AbstractRestrictionModel._TYPECODE);
		data.setUid(UID);
		data.setName(NAME);

		validationDto = new UpdateRestrictionValidationDto();
		validationDto.setOriginalUid(UID);
		validationDto.setRestriction(data);

		when(validStringLengthPredicate.test(NAME)).thenReturn(Boolean.TRUE);
		when(restrictionNameExistsPredicate.test(data)).thenReturn(Boolean.FALSE);
		when(hasRestrictionNameChangedPredicate.test(data)).thenReturn(Boolean.FALSE);
		when(restrictionExistsPredicate.test(UID)).thenReturn(Boolean.TRUE);
	}

	@Test
	public void shouldPassValidationForUpdateRestriction()
	{
		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(validationDto, errors);

		assertThat(errors.getAllErrors(), empty());
	}

	@Test
	public void shouldPassValidationWithUnchangedName()
	{
		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		when(restrictionNameExistsPredicate.test(data)).thenReturn(Boolean.FALSE);
		when(hasRestrictionNameChangedPredicate.test(data)).thenReturn(Boolean.FALSE);

		validator.validate(validationDto, errors);

		assertThat(errors.getAllErrors(), empty());
	}

	@Test
	public void shouldFailMissingRequiredFields()
	{
		data.setTypeCode(null);
		data.setUid(null);
		data.setName(null);
		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(validationDto, errors);

		assertThat(errors.getErrorCount(), equalTo(3));
	}

	@Test
	public void shouldFailSuperLongName()
	{
		data.setName(LONG_NAME);
		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		when(validStringLengthPredicate.test(NAME)).thenReturn(Boolean.FALSE);

		validator.validate(validationDto, errors);

		assertThat(errors.getErrorCount(), equalTo(1));
	}

	@Test
	public void shouldFailDuplicateName()
	{

		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		when(restrictionNameExistsPredicate.test(data)).thenReturn(Boolean.TRUE);
		when(hasRestrictionNameChangedPredicate.test(data)).thenReturn(Boolean.TRUE);

		validator.validate(validationDto, errors);

		assertThat(errors.getErrorCount(), equalTo(1));
	}

	@Test
	public void shouldFailChangedId()
	{
		data.setUid(NEW_UID);

		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(validationDto, errors);

		assertThat(errors.getErrorCount(), equalTo(1));
	}

}
