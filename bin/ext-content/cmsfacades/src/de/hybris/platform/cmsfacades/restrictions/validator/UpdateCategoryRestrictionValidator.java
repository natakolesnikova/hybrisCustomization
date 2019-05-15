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

import static de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel.CATEGORIES;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_DOES_NOT_EXIST;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_MIN_VIOLATED;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_REQUIRED;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.data.CategoryRestrictionData;
import de.hybris.platform.cmsfacades.dto.UpdateRestrictionValidationDto;

import java.util.Objects;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Validate recursive, categoryCodes, product version and product catalog fields for category restriction update.
 * 
 * @deprecated since 6.6
 */
@HybrisDeprecation(sinceVersion = "6.6")
@Deprecated
public class UpdateCategoryRestrictionValidator implements Validator
{
	private static final String CATEGORY_ID = "categoryUid";

	private Predicate<String> categoryExistsPredicate;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return UpdateRestrictionValidationDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final UpdateRestrictionValidationDto target = (UpdateRestrictionValidationDto) obj;
		final CategoryRestrictionData restrictionData = (CategoryRestrictionData) target.getRestriction();

		ValidationUtils.rejectIfEmpty(errors, CATEGORIES, FIELD_REQUIRED);

		if (Objects.nonNull(restrictionData.getCategories()))
		{
			if (restrictionData.getCategories().isEmpty())
			{
				errors.rejectValue(CATEGORIES, FIELD_MIN_VIOLATED);
			}
			restrictionData.getCategories().stream().filter(categoryKey -> !getCategoryExistsPredicate().test(categoryKey))
			.forEach(categoryKey -> errors.rejectValue(CATEGORIES, FIELD_DOES_NOT_EXIST, new Object[]
					{ CATEGORY_ID, categoryKey }, null));
		}
	}

	@Required
	public void setCategoryExistsPredicate(
			final Predicate<String> categoryExistsPredicate)
	{
		this.categoryExistsPredicate = categoryExistsPredicate;
	}

	protected Predicate<String> getCategoryExistsPredicate()
	{
		return categoryExistsPredicate;
	}

}
