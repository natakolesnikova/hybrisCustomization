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
import de.hybris.platform.cmsfacades.dto.UpdatePageValidationDto;

import java.util.function.Predicate;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validate label field for content page update.
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class UpdateContentPageValidator implements Validator
{
	private Predicate<ContentPageData> primaryPageWithLabelExistsPredicate;
	private Predicate<ContentPageData> hasPageLabelChangedPredicate;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return UpdatePageValidationDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final UpdatePageValidationDto target = (UpdatePageValidationDto) obj;
		final ContentPageData pageData = (ContentPageData) target.getPage();
		if (Strings.isBlank(pageData.getLabel()))
		{
			errors.rejectValue(LABEL, CmsfacadesConstants.FIELD_REQUIRED);
		}
		else
		{
			final boolean primaryPageWithLabelExists = getPrimaryPageWithLabelExistsPredicate().test(pageData);

			// Validate that a variation page label matches to a primary page label
			if (!pageData.getDefaultPage() && !primaryPageWithLabelExists)
			{
				errors.rejectValue(LABEL, CmsfacadesConstants.DEFAULT_PAGE_DOES_NOT_EXIST, new Object[]
						{ LABEL, pageData.getLabel() }, null);
			}
			// Validate that the label of a primary page is unique
			else if (pageData.getDefaultPage() && getHasPageLabelChangedPredicate().test(pageData) && primaryPageWithLabelExists)
			{
				errors.rejectValue(LABEL, CmsfacadesConstants.DEFAULT_PAGE_ALREADY_EXIST, new Object[]
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

	protected Predicate<ContentPageData> getHasPageLabelChangedPredicate()
	{
		return hasPageLabelChangedPredicate;
	}

	@Required
	public void setHasPageLabelChangedPredicate(final Predicate<ContentPageData> hasPageLabelChangedPredicate)
	{
		this.hasPageLabelChangedPredicate = hasPageLabelChangedPredicate;
	}

}
