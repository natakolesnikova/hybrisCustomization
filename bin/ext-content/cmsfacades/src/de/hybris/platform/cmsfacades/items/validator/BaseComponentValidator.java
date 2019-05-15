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

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_LENGTH_EXCEEDED;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_REQUIRED;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;

import java.util.Objects;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Validates fields of {@link AbstractCMSComponentData}
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class BaseComponentValidator implements Validator
{
	private static final String TYPE_CODE = "typeCode";
	private static final String NAME = "name";
	private static final String UID = "uid";

	private Predicate<String> validStringLengthPredicate;

	private Predicate<String> typeCodeExistsPredicate;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return AbstractCMSComponentData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final AbstractCMSComponentData target = (AbstractCMSComponentData) obj;

		ValidationUtils.rejectIfEmpty(errors, UID, FIELD_REQUIRED);
		ValidationUtils.rejectIfEmpty(errors, NAME, FIELD_REQUIRED);
		ValidationUtils.rejectIfEmpty(errors, TYPE_CODE, FIELD_REQUIRED);

		if (!Objects.isNull(target.getName()) && !getValidStringLengthPredicate().test(target.getName()))
		{
			errors.rejectValue(NAME, FIELD_LENGTH_EXCEEDED);
		}

		if (!Objects.isNull(target.getTypeCode()) && !getTypeCodeExistsPredicate().test(target.getTypeCode()))
		{
			errors.rejectValue(TYPE_CODE, CmsfacadesConstants.FIELD_DOES_NOT_EXIST);
		}

		if (!Objects.isNull(target.getUid()) && !getValidStringLengthPredicate().test(target.getUid()))
		{
			errors.rejectValue(UID, CmsfacadesConstants.FIELD_LENGTH_EXCEEDED);
		}
	}

	protected Predicate<String> getValidStringLengthPredicate()
	{
		return validStringLengthPredicate;
	}

	@Required
	public void setValidStringLengthPredicate(final Predicate<String> validStringLengthPredicate)
	{
		this.validStringLengthPredicate = validStringLengthPredicate;
	}

	protected Predicate<String> getTypeCodeExistsPredicate()
	{
		return typeCodeExistsPredicate;
	}

	@Required
	public void setTypeCodeExistsPredicate(final Predicate<String> typeCodeExistsPredicate)
	{
		this.typeCodeExistsPredicate = typeCodeExistsPredicate;
	}
}
