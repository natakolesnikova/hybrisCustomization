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


import de.hybris.platform.cmsfacades.common.validator.LocalizedTypeValidator;
import de.hybris.platform.cmsfacades.common.validator.LocalizedValidator;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.SimpleBannerComponentData;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates simple banner fields.
 */
public class SimpleBannerComponentValidator implements Validator
{
	private static final String URL_LINK = "urlLink";
	private static final String MEDIA_CODE = "media";

	private LocalizedValidator localizedValidator;
	private LocalizedTypeValidator localizedStringValidator;
	private LocalizedTypeValidator localizedMediaValidator;
	private Predicate<String> validStringLengthPredicate;


	@Override
	public boolean supports(final Class<?> clazz)
	{
		return SimpleBannerComponentData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final SimpleBannerComponentData target = (SimpleBannerComponentData) obj;

		final Function<String, String> mediaGetter = language -> Optional.ofNullable(target.getMedia())
				.orElse(Collections.emptyMap()).get(language);

		getLocalizedValidator().validateRequiredLanguages(
				(language, value) -> getLocalizedStringValidator().validate(language, MEDIA_CODE, value, errors), mediaGetter,
				errors);
		getLocalizedValidator().validateAllLanguages(
				(language, value) -> getLocalizedMediaValidator().validate(language, MEDIA_CODE, value, errors), mediaGetter, errors);

		if (!Objects.isNull(target.getUrlLink()) && !getValidStringLengthPredicate().test(target.getUrlLink()))
		{
			errors.rejectValue(URL_LINK, CmsfacadesConstants.FIELD_LENGTH_EXCEEDED);
		}
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

	protected LocalizedTypeValidator getLocalizedMediaValidator()
	{
		return localizedMediaValidator;
	}

	@Required
	public void setLocalizedMediaValidator(final LocalizedTypeValidator localizedMediaValidator)
	{
		this.localizedMediaValidator = localizedMediaValidator;
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

}
