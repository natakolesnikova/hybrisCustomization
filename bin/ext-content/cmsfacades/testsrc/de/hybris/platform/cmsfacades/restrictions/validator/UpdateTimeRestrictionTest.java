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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.TimeRestrictionData;
import de.hybris.platform.cmsfacades.dto.UpdateRestrictionValidationDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class UpdateTimeRestrictionTest
{

	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");

	@InjectMocks
	private UpdateTimeRestrictionValidator validator;

	private Errors errors;
	private UpdateRestrictionValidationDto validationDto;
	private TimeRestrictionData data;

	@Test
	public void shouldPassValidationForUpdateTimeRestriction() throws ParseException
	{
		final Date activeFrom = simpleDateFormat.parse("2016-01-01 00:00:00");
		final Date activeUntil = simpleDateFormat.parse("2016-12-31 00:00:00");

		data = new TimeRestrictionData();
		data.setActiveFrom(activeFrom);
		data.setActiveUntil(activeUntil);

		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validationDto = new UpdateRestrictionValidationDto();
		validationDto.setRestriction(data);

		validator.validate(validationDto, errors);

		assertThat(errors.getAllErrors(), empty());
	}

	@Test
	public void shouldFailActiveFrom_ActiveUntil_RequiredFields() throws ParseException
	{
		data = new TimeRestrictionData();

		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validationDto = new UpdateRestrictionValidationDto();
		validationDto.setRestriction(data);

		validator.validate(validationDto, errors);
		final List<ObjectError> errorList = errors.getAllErrors();

		assertThat(errors.getErrorCount(), equalTo(2));
		assertThat(((FieldError) errorList.get(0)).getField(), equalTo(CMSTimeRestrictionModel.ACTIVEFROM));
		assertThat((errorList.get(0)).getCode(), equalTo(CmsfacadesConstants.FIELD_REQUIRED));
		assertThat(((FieldError) errorList.get(1)).getField(), equalTo(CMSTimeRestrictionModel.ACTIVEUNTIL));
		assertThat((errorList.get(1)).getCode(), equalTo(CmsfacadesConstants.FIELD_REQUIRED));


	}

	@Test
	public void shouldFailInvalidDateRange_BeforeDateGreaterThanAfterDate() throws ParseException
	{
		final Date activeFrom = simpleDateFormat.parse("2016-01-01 24:00:00");
		final Date activeUntil = simpleDateFormat.parse("2016-01-01 00:00:00");

		data = new TimeRestrictionData();
		data.setActiveFrom(activeFrom);
		data.setActiveUntil(activeUntil);

		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validationDto = new UpdateRestrictionValidationDto();
		validationDto.setRestriction(data);

		validator.validate(validationDto, errors);
		final List<ObjectError> errorList = errors.getAllErrors();

		assertThat(errors.getErrorCount(), equalTo(1));
		assertThat(((FieldError) errorList.get(0)).getField(), equalTo(CMSTimeRestrictionModel.ACTIVEUNTIL));
		assertThat((errorList.get(0)).getCode(), equalTo(CmsfacadesConstants.INVALID_DATE_RANGE));
	}

	@Test
	public void shouldFailInvalidDateRange_SameDate() throws ParseException
	{
		final Date activeFrom = simpleDateFormat.parse("2016-01-01 24:00:00");
		final Date activeUntil = simpleDateFormat.parse("2016-01-01 20:00:00");

		data = new TimeRestrictionData();
		data.setActiveFrom(activeFrom);
		data.setActiveUntil(activeUntil);

		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validationDto = new UpdateRestrictionValidationDto();
		validationDto.setRestriction(data);

		validator.validate(validationDto, errors);
		final List<ObjectError> errorList = errors.getAllErrors();

		assertThat(errors.getErrorCount(), equalTo(1));
		assertThat(((FieldError) errorList.get(0)).getField(), equalTo(CMSTimeRestrictionModel.ACTIVEUNTIL));
		assertThat((errorList.get(0)).getCode(), equalTo(CmsfacadesConstants.INVALID_DATE_RANGE));
	}
}