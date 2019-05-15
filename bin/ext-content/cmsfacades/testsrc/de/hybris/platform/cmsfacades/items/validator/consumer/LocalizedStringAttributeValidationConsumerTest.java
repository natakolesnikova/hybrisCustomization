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
package de.hybris.platform.cmsfacades.items.validator.consumer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.data.BannerComponentData;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LocalizedStringAttributeValidationConsumerTest
{

	private static final String FIELD_NAME = "urlLink";

	private final LocalizedStringAttributeValidationConsumer mediaAttributeValidationConsumer = new LocalizedStringAttributeValidationConsumer();

	@Spy
	private final Errors errors = new BeanPropertyBindingResult(new BannerComponentData(),
			BannerComponentData.class.getSimpleName());

	private final String fieldValue = "my-value";

	@Test
	public void testValidNonLocalizedString()
	{
		final LocalizedValidationData errorContainer = new LocalizedValidationData.Builder().setFieldName(FIELD_NAME)
				.setValue(fieldValue).setRequiredLanguage(false).setLocale(null).build();

		mediaAttributeValidationConsumer.accept(errorContainer, errors);
		verifyZeroInteractions(errors);
	}

	@Test
	public void testInvalidValue()
	{
		final LocalizedValidationData errorContainer = new LocalizedValidationData.Builder().setFieldName(FIELD_NAME).setValue(null)
				.setRequiredLanguage(false).setLocale(null).build();

		mediaAttributeValidationConsumer.accept(errorContainer, errors);
		verify(errors).rejectValue(anyString(), anyString());

	}

	@Test
	public void testValidLocalizedValueRequiredLanguage()
	{
		final LocalizedValidationData errorContainer = new LocalizedValidationData.Builder().setFieldName(FIELD_NAME)
				.setValue(fieldValue).setRequiredLanguage(true).setLocale(Locale.ENGLISH).build();

		mediaAttributeValidationConsumer.accept(errorContainer, errors);
		verifyZeroInteractions(errors);

	}


	@Test
	public void testInvalidLocalizedValueRequiredLanguage()
	{
		final LocalizedValidationData errorContainer = new LocalizedValidationData.Builder().setFieldName(FIELD_NAME).setValue(null)
				.setRequiredLanguage(true).setLocale(Locale.ENGLISH).build();

		mediaAttributeValidationConsumer.accept(errorContainer, errors);
		verify(errors).rejectValue(anyString(), anyString(), any(), any());
	}

}
