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

import de.hybris.platform.cmsfacades.common.function.ValidationConsumer;
import de.hybris.platform.cmsfacades.common.validator.LocalizedValidator;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.SimpleResponsiveBannerComponentData;
import de.hybris.platform.cmsfacades.items.validator.consumer.LocalizedValidationData;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates simple responsive banner fields.
 */
public class SimpleResponsiveBannerComponentValidator implements Validator
{
	private static final String URL_LINK = "urlLink";
	private static final String MEDIA_CODE = "media";

	private LocalizedValidator localizedValidator;
	private ValidationConsumer<LocalizedValidationData> localizedFormatMediaAttributeValidationConsumer;
	private ValidationConsumer<LocalizedValidationData> localizedFormatStringAttributeValidationConsumer;
	private List<String> cmsRequiredMediaFormatQualifiers;
	private Predicate<String> validStringLengthPredicate;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return SimpleResponsiveBannerComponentData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final SimpleResponsiveBannerComponentData target = (SimpleResponsiveBannerComponentData) obj;

		final BiConsumer<String, Map<String, String>> validateMedia = //
				(language, mediaFormatAndCodeMap) -> Optional.ofNullable(mediaFormatAndCodeMap) //
						.ifPresent(
								stringStringMap -> validateMediaCodeIsPresent(stringStringMap, errors, Locale.forLanguageTag(language)));

		final BiConsumer<String, Map<String, String>> validateMediaCode = //
				(language, mediaFormatAndCodeMap) -> Optional.ofNullable(mediaFormatAndCodeMap) //
						.ifPresent(stringStringMap -> validateMediaCode(stringStringMap, errors, Locale.forLanguageTag(language)));

		final Function<String, Map<String, String>> mediaFormatGetter = language -> Optional.ofNullable(target.getMedia())
				.orElse(Collections.emptyMap()).get(language);

		getLocalizedValidator().validateRequiredLanguages(validateMedia, mediaFormatGetter, errors);
		getLocalizedValidator().validateAllLanguages(validateMediaCode, mediaFormatGetter, errors);

		if (!Objects.isNull(target.getUrlLink()) && !getValidStringLengthPredicate().test(target.getUrlLink()))
		{
			errors.rejectValue(URL_LINK, CmsfacadesConstants.FIELD_LENGTH_EXCEEDED);
		}
	}

	protected void validateMediaCodeIsPresent(final Map<String, String> mediaFormatAndCodeMap, final Errors errors,
			final Locale locale)
	{
		getCmsRequiredMediaFormatQualifiers().forEach(mediaFormatQualifier -> {
			final String mediaCode = mediaFormatAndCodeMap.get(mediaFormatQualifier);
			validateRequiredMediaFormat(mediaFormatQualifier, mediaCode, errors, locale);
		});
	}

	/**
	 * Verify that a media code is set for all required media formats for the required language
	 *
	 * @param mediaFormatQualifier
	 *           - the media format qualifier
	 * @param mediaCode
	 *           - the media code
	 * @param errors
	 *           - the validation errors
	 * @param locale
	 *           - the language locale
	 */
	protected void validateRequiredMediaFormat(final String mediaFormatQualifier, final String mediaCode, final Errors errors,
			final Locale locale)
	{
		final Map<String, String> optionals = new HashMap<>();
		optionals.put(CmsfacadesConstants.MEDIA_FORMAT, mediaFormatQualifier);

		getLocalizedFormatStringAttributeValidationConsumer().accept(new LocalizedValidationData.Builder() //
				.setFieldName(MEDIA_CODE) //
				.setLocale(locale) //
				.setValue(mediaCode) //
				.setRequiredLanguage(getCmsRequiredMediaFormatQualifiers().contains(mediaFormatQualifier) ? true : false) //
				.setOptionals(optionals).build(), errors);
	}

	protected void validateMediaCode(final Map<String, String> mediaFormatAndCodeMap, final Errors errors, final Locale locale)
	{
		mediaFormatAndCodeMap
				.forEach((mediaFormatQualifier, mediaCode) -> getLocalizedFormatMediaAttributeValidationConsumer().accept(
						new LocalizedValidationData.Builder().setFieldName(MEDIA_CODE).setLocale(locale).setValue(mediaCode)
								.setRequiredLanguage(true)
								.setOptionals(Stream.of(mediaFormatQualifier)
										.collect(Collectors.toMap(entry -> CmsfacadesConstants.MEDIA_FORMAT, entry -> entry)))
								.build(),
						errors));
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

	protected ValidationConsumer<LocalizedValidationData> getLocalizedFormatMediaAttributeValidationConsumer()
	{
		return localizedFormatMediaAttributeValidationConsumer;
	}

	@Required
	public void setLocalizedFormatMediaAttributeValidationConsumer(
			final ValidationConsumer<LocalizedValidationData> localizedFormatMediaAttributeValidationConsumer)
	{
		this.localizedFormatMediaAttributeValidationConsumer = localizedFormatMediaAttributeValidationConsumer;
	}

	protected ValidationConsumer<LocalizedValidationData> getLocalizedFormatStringAttributeValidationConsumer()
	{
		return localizedFormatStringAttributeValidationConsumer;
	}

	@Required
	public void setLocalizedFormatStringAttributeValidationConsumer(
			final ValidationConsumer<LocalizedValidationData> localizedFormatStringAttributeValidationConsumer)
	{
		this.localizedFormatStringAttributeValidationConsumer = localizedFormatStringAttributeValidationConsumer;
	}

	protected List<String> getCmsRequiredMediaFormatQualifiers()
	{
		return cmsRequiredMediaFormatQualifiers;
	}

	@Required
	public void setCmsRequiredMediaFormatQualifiers(final List<String> cmsRequiredMediaFormatQualifiers)
	{
		this.cmsRequiredMediaFormatQualifiers = cmsRequiredMediaFormatQualifiers;
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
