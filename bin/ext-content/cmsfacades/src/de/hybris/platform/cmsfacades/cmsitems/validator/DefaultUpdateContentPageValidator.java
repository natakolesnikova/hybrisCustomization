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
package de.hybris.platform.cmsfacades.cmsitems.validator;

import static de.hybris.platform.cmsfacades.common.validator.ValidationErrorBuilder.newValidationErrorBuilder;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.DEFAULT_PAGE_DOES_NOT_EXIST;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.DEFAULT_PAGE_LABEL_ALREADY_EXIST;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_NOT_ALLOWED;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmsfacades.common.function.Validator;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrorsProvider;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_REQUIRED;
import java.util.function.Predicate;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of the validator for {@link AbstractPageModel}
 */
public class DefaultUpdateContentPageValidator implements Validator<ContentPageModel>
{
	private CMSAdminPageService cmsAdminPageService;
	private Predicate<String> primaryPageWithLabelExistsPredicate;
	private Predicate<String> hasPageLabelChangedPredicate;
	private Predicate<String> pageExistsPredicate;
	private ValidationErrorsProvider validationErrorsProvider;

	@Override
	public void validate(final ContentPageModel validatee)
	{
		if (getPageExistsPredicate().test(validatee.getUid()))
		{
			//	Validate that defaultPage value has not been modified.
			final AbstractPageModel pageModel = getCmsAdminPageService().getPageForIdFromActiveCatalogVersion(validatee.getUid());
			if (!validatee.getDefaultPage().equals(pageModel.getDefaultPage()))
			{
				getValidationErrorsProvider().getCurrentValidationErrors().add(
						newValidationErrorBuilder() //
						.field(AbstractPageModel.DEFAULTPAGE) //
						.errorCode(FIELD_NOT_ALLOWED) //
						.build()
						);
			}

			final boolean primaryPageWithLabelExists = getPrimaryPageWithLabelExistsPredicate().test(validatee.getLabel());

            if (Strings.isBlank(validatee.getLabel())) {
                getValidationErrorsProvider().getCurrentValidationErrors().add(
                        newValidationErrorBuilder() //
                                .field(ContentPageModel.LABEL) //
                                .errorCode(FIELD_REQUIRED) //
                                .build());
            }
			// Validate that a variation page label matches to a primary page label
			else if (!validatee.getDefaultPage() && !primaryPageWithLabelExists)
			{
				getValidationErrorsProvider().getCurrentValidationErrors()
						.add(newValidationErrorBuilder() //
								.field(ContentPageModel.LABEL) //
								.errorCode(DEFAULT_PAGE_DOES_NOT_EXIST) //
								.errorArgs(new Object[] { ContentPageModel.LABEL, validatee.getLabel() }) //
								.build());
			}
			// Validate that the label of a primary page is unique
			else if (validatee.getDefaultPage() && getHasPageLabelChangedPredicate().test(validatee.getLabel())
					&& primaryPageWithLabelExists)
			{
				getValidationErrorsProvider().getCurrentValidationErrors()
						.add(newValidationErrorBuilder() //
								.field(ContentPageModel.LABEL) //
								.errorCode(DEFAULT_PAGE_LABEL_ALREADY_EXIST) //
								.build());
			}
		}
	}

	protected Predicate<String> getHasPageLabelChangedPredicate()
	{
		return hasPageLabelChangedPredicate;
	}

	@Required
	public void setHasPageLabelChangedPredicate(final Predicate<String> hasPageLabelChangedPredicate)
	{
		this.hasPageLabelChangedPredicate = hasPageLabelChangedPredicate;
	}

	protected CMSAdminPageService getCmsAdminPageService()
	{
		return cmsAdminPageService;
	}

	@Required
	public void setCmsAdminPageService(final CMSAdminPageService cmsAdminPageService)
	{
		this.cmsAdminPageService = cmsAdminPageService;
	}

	protected Predicate<String> getPrimaryPageWithLabelExistsPredicate()
	{
		return primaryPageWithLabelExistsPredicate;
	}

	@Required
	public void setPrimaryPageWithLabelExistsPredicate(final Predicate<String> primaryPageWithLabelExistsPredicate)
	{
		this.primaryPageWithLabelExistsPredicate = primaryPageWithLabelExistsPredicate;
	}

	protected final Predicate<String> getPageExistsPredicate()
	{
		return pageExistsPredicate;
	}

	@Required
	public final void setPageExistsPredicate(final Predicate<String> pageExistsPredicate)
	{
		this.pageExistsPredicate = pageExistsPredicate;
	}

	protected ValidationErrorsProvider getValidationErrorsProvider()
	{
		return validationErrorsProvider;
	}

	@Required
	public void setValidationErrorsProvider(final ValidationErrorsProvider validationErrorsProvider)
	{
		this.validationErrorsProvider = validationErrorsProvider;
	}
}
