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

import de.hybris.platform.cmsfacades.common.validator.LocalizedValidator;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.CMSParagraphComponentData;

import java.util.Collections;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates fields of {@link CMSParagraphComponentData}
 */
public class ParagraphComponentValidator implements Validator
{

	private static final String CONTENT = "content";

	private LocalizedValidator localizedValidator;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return CMSParagraphComponentData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final CMSParagraphComponentData target = (CMSParagraphComponentData) obj;

		final BiConsumer<String, String> consumer = (language, content) -> validateContent(language, content, errors);
		final Function<String, String> function = (language) -> Optional.ofNullable(target.getContent())
				.orElse(Collections.emptyMap()).get(language);
		getLocalizedValidator().validateRequiredLanguages(consumer, function, errors);
	}

	/**
	 * Validate the content attribute. Content in required languages cannot be <code>null</code> or empty.
	 *
	 * @param locale
	 *           - the Locale under validation
	 * @param content
	 *           - the content in the given locale
	 * @param errors
	 *           - the current errors context
	 */
	protected void validateContent(final String language, final String content, final Errors errors)
	{
		if (StringUtils.isEmpty(content))
		{
			errors.rejectValue(CONTENT, CmsfacadesConstants.FIELD_REQUIRED_L10N, new Object[]
					{ language }, null);
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

}
