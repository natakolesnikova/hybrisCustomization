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


import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.common.function.ValidationConsumer;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;

import java.util.Locale;

import org.springframework.validation.Errors;

import com.google.common.base.Strings;


/**
 * Validates localized string attributes
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class LocalizedStringAttributeValidationConsumer implements ValidationConsumer<LocalizedValidationData>
{

	/**
	 * Tests if the String value is valid.
	 *
	 * @param target
	 *           {@link LocalizedValidationData} validatee
	 */
	@Override
	public void accept(final LocalizedValidationData target, final Errors errors)
	{
		if (target.getLocale() == null)
		{
			validate(target.getFieldName(), target.getValue(), errors);
		}
		else
		{
			validate(target.getFieldName(), target.getValue(), errors, target.getLocale(), target.isRequiredLanguage());
		}
	}


	/**
	 * Default implementation to validate String values when localization is not relevant. <br>
	 * A "required field" error is created if the given value is invalid.
	 *
	 * @param field
	 *           the attribute field name that is current being validated
	 * @param value
	 *           the value being validated
	 * @param errors
	 *           the error object that holds all validation errors
	 */
	protected void validate(final String field, final String value, final Errors errors)
	{
		if (Strings.isNullOrEmpty(value))
		{
			errors.rejectValue(field, CmsfacadesConstants.FIELD_REQUIRED);
		}
	}


	/**
	 * Default implementation to validate String values when localization is relevant.
	 *
	 * @param field
	 *           the attribute field name that is current being validated
	 * @param value
	 *           the value being validated
	 * @param errors
	 *           the error object that holds all validation errors
	 * @param locale
	 *           the locale in which the current value has significance
	 * @param requiredLanguage
	 *           true if the locale is a required language.
	 * @return true if the value was not rejected and false otherwise
	 */
	protected boolean validate(final String field, final String value, final Errors errors, final Locale locale,
			final Boolean requiredLanguage)
	{
		if (requiredLanguage && Strings.isNullOrEmpty(value))
		{
			errors.rejectValue(field, CmsfacadesConstants.FIELD_REQUIRED_L10N, new Object[]
					{ locale }, null);
			return false;
		}
		return true;
	}
}
