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
import de.hybris.platform.cmsfacades.common.validator.ValidationDtoFactory;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.cmsfacades.dto.ComponentTypeAndContentSlotValidationDto;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.google.common.base.Strings;


/**
 * Validates DTO for creating a new component.
 *
 * <p>
 * Rules:</br>
 * <ul>
 * <li>slotId not null</li>
 * <li>typeCode not null</li>
 * <li>pageId not null</li>
 * <li>position not null</li>
 * <li>name not null</li>
 * <li>name length not exceeded</li>
 * <li>uid not null</li>
 * <li>uid length not exceeded</li>
 * <li>component does not exists: {@link ComponentExistsPredicate}</li>
 * <li>content slot exists: {@link ComponentAlreadyInContentSlotPredicate}</li>
 * <li>composed type exists: {@link TypeCodeExistsPredicate}</li>
 * <li>component type not valid for content slot: {@link ComponentTypeAllowedForContentSlotPredicate}</li>
 * </ul>
 * </p>
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class CreateComponentValidator implements Validator
{
	public static final String UID = "uid";
	public static final String TYPE_CODE = "typeCode";
	public static final String SLOT_ID = "slotId";
	public static final String NAME = "name";
	public static final String POSITION = "position";
	public static final String PAGE_ID = "pageId";

	private Predicate<String> contentSlotExistsPredicate;
	private Predicate<ComponentTypeAndContentSlotValidationDto> componentTypeAllowedForContentSlotPredicate;
	private Predicate<String> validStringLengthPredicate;
	private ValidationDtoFactory validationDtoFactory;
	private Predicate<String> pageExistsPredicate;
	private Predicate<String> componentExistsPredicate;
	private Map<Class<?>, CompositeValidator> cmsComponentValidatorFactory;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return AbstractCMSComponentData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final AbstractCMSComponentData target = (AbstractCMSComponentData) obj;

		if (!Strings.isNullOrEmpty(target.getSlotId()))
		{
			if (!getContentSlotExistsPredicate().test(target.getSlotId()))
			{
				errors.rejectValue(SLOT_ID, CmsfacadesConstants.FIELD_DOES_NOT_EXIST);
			}

			if (Objects.isNull(target.getPosition()))
			{
				errors.rejectValue(POSITION, CmsfacadesConstants.FIELD_REQUIRED);
			}
		}

		if (Objects.isNull(target.getPageId()))
		{
			errors.rejectValue(PAGE_ID, CmsfacadesConstants.FIELD_REQUIRED);
		}
		else if (!getPageExistsPredicate().test(target.getPageId()))
		{
			errors.rejectValue(PAGE_ID, CmsfacadesConstants.FIELD_DOES_NOT_EXIST);
		}

		if (!Objects.isNull(target.getUid()) && getComponentExistsPredicate().test(target.getUid()))
		{
			errors.rejectValue(UID, CmsfacadesConstants.FIELD_ALREADY_EXIST);
		}

		// Validate Component
		Optional
		.ofNullable(Optional.ofNullable(getCmsComponentValidatorFactory().get(target.getClass()))
				.orElseGet(() -> getCmsComponentValidatorFactory().get(AbstractCMSComponentData.class)))
		.orElseThrow(() -> new IllegalArgumentException("The validator is required and must not be null."))
		.validate(target, errors);

		// Only validate the type restrictions if slot is present and there were no errors with the slot or the type code.
		if (!Strings.isNullOrEmpty(target.getSlotId()) && errors.getFieldErrorCount(SLOT_ID) + errors.getFieldErrorCount(TYPE_CODE)
		+ errors.getFieldErrorCount(PAGE_ID) == 0)
		{
			final ComponentTypeAndContentSlotValidationDto validationDto = getValidationDtoFactory()
					.buildComponentTypeAndContentSlotValidationDto(target.getTypeCode(), target.getSlotId(), target.getPageId());

			if (!getComponentTypeAllowedForContentSlotPredicate().test(validationDto))
			{
				errors.rejectValue(UID, CmsfacadesConstants.FIELD_NOT_ALLOWED);
			}
		}

	}


	protected Predicate<String> getContentSlotExistsPredicate()
	{
		return contentSlotExistsPredicate;
	}

	@Required
	public void setContentSlotExistsPredicate(final Predicate<String> contentSlotExistsPredicate)
	{
		this.contentSlotExistsPredicate = contentSlotExistsPredicate;
	}

	protected Predicate<ComponentTypeAndContentSlotValidationDto> getComponentTypeAllowedForContentSlotPredicate()
	{
		return componentTypeAllowedForContentSlotPredicate;
	}

	@Required
	public void setComponentTypeAllowedForContentSlotPredicate(
			final Predicate<ComponentTypeAndContentSlotValidationDto> componentTypeAllowedForContentSlotPredicate)
	{
		this.componentTypeAllowedForContentSlotPredicate = componentTypeAllowedForContentSlotPredicate;
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

	protected ValidationDtoFactory getValidationDtoFactory()
	{
		return validationDtoFactory;
	}

	@Required
	public void setValidationDtoFactory(final ValidationDtoFactory validationDtoFactory)
	{
		this.validationDtoFactory = validationDtoFactory;
	}

	protected Predicate<String> getPageExistsPredicate()
	{
		return pageExistsPredicate;
	}

	@Required
	public void setPageExistsPredicate(final Predicate<String> pageExistsPredicate)
	{
		this.pageExistsPredicate = pageExistsPredicate;
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
