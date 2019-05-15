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

import static de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel.ACTIVEFROM;
import static de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel.ACTIVEUNTIL;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_REQUIRED;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.INVALID_DATE_RANGE;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.data.TimeRestrictionData;
import de.hybris.platform.cmsfacades.dto.UpdateRestrictionValidationDto;

import java.util.Objects;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Validate activefrom and activeuntil field for time restriction update.
 *
 * @deprecated since 6.6
 */
@HybrisDeprecation(sinceVersion = "6.6")
@Deprecated
public class UpdateTimeRestrictionValidator implements Validator
{

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return UpdateRestrictionValidationDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final UpdateRestrictionValidationDto target = (UpdateRestrictionValidationDto) obj;
		final TimeRestrictionData restrictionData = (TimeRestrictionData) target.getRestriction();

		ValidationUtils.rejectIfEmpty(errors, ACTIVEFROM, FIELD_REQUIRED);
		ValidationUtils.rejectIfEmpty(errors, ACTIVEUNTIL, FIELD_REQUIRED);

		if (Objects.nonNull(restrictionData.getActiveFrom()) && Objects.nonNull(restrictionData.getActiveUntil())
				&& (restrictionData.getActiveUntil().before(restrictionData.getActiveFrom()) //
						|| restrictionData.getActiveUntil().equals(restrictionData.getActiveFrom())))
		{
			errors.rejectValue(ACTIVEUNTIL, INVALID_DATE_RANGE);
		}
	}

}