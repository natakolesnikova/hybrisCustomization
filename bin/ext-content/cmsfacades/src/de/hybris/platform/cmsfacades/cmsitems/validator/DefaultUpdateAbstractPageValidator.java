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
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_NOT_ALLOWED;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmsfacades.common.function.Validator;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrorsProvider;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of the validator for {@link AbstractPageModel}
 */
public class DefaultUpdateAbstractPageValidator implements Validator<AbstractPageModel>
{
	private CMSAdminPageService cmsAdminPageService;
	private Predicate<String> pageExistsPredicate;
	private ValidationErrorsProvider validationErrorsProvider;

	@Override
	public void validate(final AbstractPageModel validatee)
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
		}
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
