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
import de.hybris.platform.cmsfacades.data.TimeRestrictionData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CreateTimeRestrictionValidatorTest
{
	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");

	private final CreateTimeRestrictionValidator validator = new CreateTimeRestrictionValidator();

	private Errors errors;
	private TimeRestrictionData data;

	@Test
	public void shouldPassValidation() throws ParseException
	{
		final Date activeFrom = simpleDateFormat.parse("2016-01-01 00:00:00");
		final Date activeUntil = simpleDateFormat.parse("2016-12-31 00:00:00");

		data = new TimeRestrictionData();
		data.setActiveFrom(activeFrom);
		data.setActiveUntil(activeUntil);
		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertThat(errors.getAllErrors(), empty());
	}

	@Test
	public void shouldFailMissingRequiredField() throws ParseException
	{
		data = new TimeRestrictionData();
		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertThat(errors.getErrorCount(), equalTo(2));

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

		validator.validate(data, errors);

		assertThat(errors.getErrorCount(), equalTo(1));
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

		validator.validate(data, errors);

		assertThat(errors.getErrorCount(), equalTo(1));
	}

}
