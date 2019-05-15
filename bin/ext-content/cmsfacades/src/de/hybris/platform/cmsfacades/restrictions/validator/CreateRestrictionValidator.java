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

import static de.hybris.platform.cms2.model.contents.CMSItemModel.NAME;
import static de.hybris.platform.cms2.model.contents.CMSItemModel.UID;
import static de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel.TYPECODE;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_ALREADY_EXIST;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_LENGTH_EXCEEDED;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_REQUIRED;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.data.AbstractRestrictionData;

import java.util.Objects;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Validate fields of {@link AbstractRestrictionData} for create operation
 * 
 * @deprecated since 6.6
 */
@HybrisDeprecation(sinceVersion = "6.6")
@Deprecated
public class CreateRestrictionValidator implements Validator
{
	private Predicate<String> validStringLengthPredicate;
	private Predicate<AbstractRestrictionData> restrictionNameExistsPredicate;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return AbstractRestrictionData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final AbstractRestrictionData target = (AbstractRestrictionData) obj;

		ValidationUtils.rejectIfEmpty(errors, UID, FIELD_REQUIRED);
		ValidationUtils.rejectIfEmpty(errors, NAME, FIELD_REQUIRED);
		ValidationUtils.rejectIfEmpty(errors, TYPECODE, FIELD_REQUIRED);

		if (Objects.nonNull(target.getName()))
		{
			if(!getValidStringLengthPredicate().test(target.getName()))
			{
				errors.rejectValue(NAME, FIELD_LENGTH_EXCEEDED);
			}
			else if (getRestrictionNameExistsPredicate().test(target))
			{
				errors.rejectValue(NAME, FIELD_ALREADY_EXIST);
			}
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

	protected Predicate<AbstractRestrictionData> getRestrictionNameExistsPredicate()
	{
		return restrictionNameExistsPredicate;
	}

	@Required
	public void setRestrictionNameExistsPredicate(final Predicate<AbstractRestrictionData> restrictionNameExistsPredicate)
	{
		this.restrictionNameExistsPredicate = restrictionNameExistsPredicate;
	}
}
