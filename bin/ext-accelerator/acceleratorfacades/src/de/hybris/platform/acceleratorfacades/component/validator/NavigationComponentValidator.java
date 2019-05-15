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

import de.hybris.platform.acceleratorcms.model.components.CategoryNavigationComponentModel;
import de.hybris.platform.acceleratorcms.model.components.FooterNavigationComponentModel;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.NavigationComponentData;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Validates navigation node fields.
 */
public class NavigationComponentValidator implements Validator
{
	private static final String NAVIGATION_NODE = "navigationNode";
	private static final String WRAP_AFTER = "wrapAfter";

	private Predicate<String> validPositiveIntegerPredicate;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return NavigationComponentData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final NavigationComponentData target = (NavigationComponentData) obj;

		ValidationUtils.rejectIfEmpty(errors, NAVIGATION_NODE, CmsfacadesConstants.FIELD_REQUIRED);

		if ((target.getTypeCode().equals(CategoryNavigationComponentModel._TYPECODE)
				|| target.getTypeCode().equals(FooterNavigationComponentModel._TYPECODE))
				&& !getValidPositiveIntegerPredicate().test(target.getWrapAfter()))
		{
			errors.rejectValue(WRAP_AFTER, CmsfacadesConstants.FIELD_NOT_POSITIVE_INTEGER);
		}

	}

	protected Predicate<String> getValidPositiveIntegerPredicate()
	{
		return validPositiveIntegerPredicate;
	}

	@Required
	public void setValidPositiveIntegerPredicate(final Predicate<String> validPositiveIntegerPredicate)
	{
		this.validPositiveIntegerPredicate = validPositiveIntegerPredicate;
	}
}
