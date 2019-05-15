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

import de.hybris.platform.cmsfacades.common.validator.LocalizedTypeValidator;
import de.hybris.platform.cmsfacades.common.validator.LocalizedValidator;
import de.hybris.platform.cmsfacades.data.BannerComponentData;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates banner component fields
 */
public class BannerComponentValidator implements Validator
{
	private static final String MEDIA_CODE = "media";
	private static final String HEADLINE = "headline";
	private static final String CONTENT = "content";

	private LocalizedValidator localizedValidator;
	private LocalizedTypeValidator localizedStringValidator;
	private LocalizedTypeValidator localizedMediaValidator;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return BannerComponentData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final BannerComponentData target = (BannerComponentData) obj;

		final Function<String, String> contentGetter = (language) -> Optional.ofNullable(target.getContent())
				.orElse(Collections.emptyMap()).get(language);
		getLocalizedValidator().validateRequiredLanguages(
				(language, value) -> getLocalizedStringValidator().validate(language, CONTENT, value, errors), contentGetter, errors);

		final Function<String, String> headlineGetter = (language) -> Optional.ofNullable(target.getHeadline())
				.orElse(Collections.emptyMap()).get(language);
		getLocalizedValidator().validateRequiredLanguages(
				(language, value) -> getLocalizedStringValidator().validate(language, HEADLINE, value, errors), headlineGetter,
				errors);

		final Function<String, String> mediaGetter = (language) -> Optional.ofNullable(target.getMedia())
				.orElse(Collections.emptyMap()).get(language);
		// verify that media code is present for the required language
		getLocalizedValidator().validateRequiredLanguages(
				(language, value) -> getLocalizedStringValidator().validate(language, MEDIA_CODE, value, errors), mediaGetter,
				errors);
		// verify that provided media codes are valid
		getLocalizedValidator().validateAllLanguages(
				(language, value) -> getLocalizedMediaValidator().validate(language, MEDIA_CODE, value, errors), mediaGetter, errors);

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

}
