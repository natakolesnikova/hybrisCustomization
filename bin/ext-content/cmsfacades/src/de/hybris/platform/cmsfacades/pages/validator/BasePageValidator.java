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
package de.hybris.platform.cmsfacades.pages.validator;

import static de.hybris.platform.cms2.model.contents.CMSItemModel.NAME;
import static de.hybris.platform.cms2.model.contents.CMSItemModel.UID;
import static de.hybris.platform.cms2.model.pages.AbstractPageModel.TITLE;
import static de.hybris.platform.cms2.model.pages.AbstractPageModel.TYPECODE;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.common.validator.LocalizedTypeValidator;
import de.hybris.platform.cmsfacades.common.validator.LocalizedValidator;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.AbstractPageData;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Validates fields of {@link AbstractPageData}
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class BasePageValidator implements Validator
{
	private Predicate<String> pageExistsPredicate;
	private Predicate<String> onlyHasSupportedCharactersPredicate;
	private LocalizedValidator localizedValidator;
	private LocalizedTypeValidator localizedStringValidator;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return AbstractPageData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final AbstractPageData pageData = (AbstractPageData) target;
		ValidationUtils.rejectIfEmpty(errors, UID, CmsfacadesConstants.FIELD_REQUIRED);
		ValidationUtils.rejectIfEmpty(errors, NAME, CmsfacadesConstants.FIELD_REQUIRED);
		ValidationUtils.rejectIfEmpty(errors, TYPECODE, CmsfacadesConstants.FIELD_REQUIRED);

		if (Strings.isNotBlank(pageData.getUid()))
		{
			if (!getOnlyHasSupportedCharactersPredicate().test(pageData.getUid()))
			{
				errors.rejectValue(UID, CmsfacadesConstants.FIELD_CONTAINS_INVALID_CHARS);
			}
			else if (getPageExistsPredicate().test(pageData.getUid()))
			{
				errors.rejectValue(UID, CmsfacadesConstants.FIELD_ALREADY_EXIST);
			}
		}

		final Function<String, String> getTitle = (language) -> Optional.ofNullable(pageData.getTitle())
				.orElse(Collections.emptyMap()).get(language);
		getLocalizedValidator().validateRequiredLanguages(
				(language, value) -> getLocalizedStringValidator().validate(language, TITLE, value, errors), getTitle, errors);
	}

	protected LocalizedValidator getLocalizedValidator()
	{
		return localizedValidator;
	}

	@Required
	public void setLocalizedValidator(final LocalizedValidator localizedValidator)
	{
		this.localizedValidator = localizedValidator;
	}

	protected LocalizedTypeValidator getLocalizedStringValidator()
	{
		return localizedStringValidator;
	}

	@Required
	public void setLocalizedStringValidator(final LocalizedTypeValidator localizedStringValidator)
	{
		this.localizedStringValidator = localizedStringValidator;
	}

	protected Predicate<String> getOnlyHasSupportedCharactersPredicate()
	{
		return onlyHasSupportedCharactersPredicate;
	}

	@Required
	public void setOnlyHasSupportedCharactersPredicate(final Predicate<String> onlyHasSupportedCharactersPredicate)
	{
		this.onlyHasSupportedCharactersPredicate = onlyHasSupportedCharactersPredicate;
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

}
