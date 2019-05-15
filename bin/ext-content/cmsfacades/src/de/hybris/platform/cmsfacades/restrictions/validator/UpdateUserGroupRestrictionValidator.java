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

import static de.hybris.platform.cms2.model.restrictions.CMSUserGroupRestrictionModel.USERGROUPS;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_DOES_NOT_EXIST;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_MIN_VIOLATED;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_REQUIRED;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.data.UserGroupRestrictionData;
import de.hybris.platform.cmsfacades.dto.UpdateRestrictionValidationDto;
import de.hybris.platform.core.model.user.UserGroupModel;

import java.util.Objects;
import java.util.function.BiPredicate;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Validate fields of {@link UserGroupRestrictionData} for update operation
 * 
 * @deprecated since 6.6
 */
@HybrisDeprecation(sinceVersion = "6.6")
@Deprecated
public class UpdateUserGroupRestrictionValidator implements Validator
{

	private BiPredicate<String, Class<?>> itemModelExistsPredicate;

	private static final String USERGROUP_ID = "userGroupUid";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return UpdateRestrictionValidationDto.class.isAssignableFrom(aClass);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final UpdateRestrictionValidationDto target = (UpdateRestrictionValidationDto) obj;
		final UserGroupRestrictionData restrictionData = (UserGroupRestrictionData) target.getRestriction();
		ValidationUtils.rejectIfEmpty(errors, USERGROUPS, FIELD_REQUIRED);

		if (Objects.nonNull(restrictionData.getUserGroups()))
		{
			if (restrictionData.getUserGroups().isEmpty())
			{
				errors.rejectValue(USERGROUPS, FIELD_MIN_VIOLATED);
			}

			restrictionData.getUserGroups().stream()
			.filter(userGroupKey -> !getItemModelExistsPredicate().test(userGroupKey, UserGroupModel.class))
			.forEach(userGroupKey -> errors.rejectValue(USERGROUPS, FIELD_DOES_NOT_EXIST, new Object[]
					{ USERGROUP_ID, userGroupKey }, null));
		}
	}

	protected BiPredicate<String, Class<?>> getItemModelExistsPredicate()
	{
		return itemModelExistsPredicate;
	}

	@Required
	public void setItemModelExistsPredicate(final BiPredicate<String, Class<?>> itemModelExistsPredicate)
	{
		this.itemModelExistsPredicate = itemModelExistsPredicate;
	}
}
