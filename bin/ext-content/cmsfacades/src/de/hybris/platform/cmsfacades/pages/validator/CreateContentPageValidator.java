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

import static de.hybris.platform.cms2.model.pages.ContentPageModel.LABEL;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.ContentPageData;

import java.util.function.Predicate;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Validates fields of {@link ContentPageData} for create operation
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class CreateContentPageValidator implements Validator
{
	private Predicate<ContentPageData> primaryPageWithLabelExistsPredicate;

	@Override
	public boolean supports(final Class<?> arg0)
	{
		return ContentPageData.class.isAssignableFrom(arg0);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final ContentPageData pageData = (ContentPageData) target;
		ValidationUtils.rejectIfEmpty(errors, LABEL, CmsfacadesConstants.FIELD_REQUIRED);

		if (Strings.isNotBlank(pageData.getLabel()))
		{
			final boolean existsPageWithLabel = getPrimaryPageWithLabelExistsPredicate().test(pageData);
			if (pageData.getDefaultPage())
			{
				if (existsPageWithLabel)
				{
					errors.rejectValue(LABEL, CmsfacadesConstants.DEFAULT_PAGE_ALREADY_EXIST, new Object[]
							{ LABEL, pageData.getLabel() }, null);
				}
			}
			else if (!existsPageWithLabel)
			{
				errors.rejectValue(LABEL, CmsfacadesConstants.DEFAULT_PAGE_DOES_NOT_EXIST, new Object[]
						{ LABEL, pageData.getLabel() }, null);
			}
		}
	}

	protected Predicate<ContentPageData> getPrimaryPageWithLabelExistsPredicate()
	{
		return primaryPageWithLabelExistsPredicate;
	}

	@Required
	public void setPrimaryPageWithLabelExistsPredicate(final Predicate<ContentPageData> primaryPageWithLabelExistsPredicate)
	{
		this.primaryPageWithLabelExistsPredicate = primaryPageWithLabelExistsPredicate;
	}

}
