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

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.common.validator.CompositeValidator;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.cmsfacades.dto.UpdateComponentValidationDto;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validate a component to be updated.
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class UpdateComponentValidator implements Validator
{
	private static final String UID = "uid";

	private Map<Class<?>, CompositeValidator> cmsComponentValidatorFactory;
	private Predicate<String> componentExistsPredicate;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return UpdateComponentValidationDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final UpdateComponentValidationDto target = (UpdateComponentValidationDto) obj;

		// Validate original uid different from new uid and new uid does not exist already
		if (!target.getOriginalUid().equals(target.getComponent().getUid())
				&& getComponentExistsPredicate().test(target.getComponent().getUid()))
		{
			errors.rejectValue(UID, CmsfacadesConstants.FIELD_ALREADY_EXIST);
		}

		// Validate Component
		Optional
		.ofNullable(Optional.ofNullable(getCmsComponentValidatorFactory().get(target.getComponent().getClass()))
				.orElseGet(() -> getCmsComponentValidatorFactory().get(AbstractCMSComponentData.class)))
		.orElseThrow(() -> new IllegalArgumentException("The validator is required and must not be null."))
		.validate(target.getComponent(), errors);
	}

	protected Map<Class<?>, CompositeValidator> getCmsComponentValidatorFactory()
	{
		return cmsComponentValidatorFactory;
	}

	@Required
	public void setCmsComponentValidatorFactory(final Map<Class<?>, CompositeValidator> cmsComponentValidatorFactory)
	{
		this.cmsComponentValidatorFactory = cmsComponentValidatorFactory;
	}

	protected Predicate<String> getComponentExistsPredicate()
	{
		return componentExistsPredicate;
	}

	@Required
	public void setComponentExistsPredicate(final Predicate<String> componentExistsPredicate)
	{
		this.componentExistsPredicate = componentExistsPredicate;
	}
}
