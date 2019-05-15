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

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolver;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolverTypeRegistry;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates fields of {@link AbstractPageData} for create operation
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class CreatePageValidator implements Validator
{
	protected static final String TYPE_CODE = "typeCode";

	private PageVariationResolverTypeRegistry pageVariationResolverTypeRegistry;

	@Override
	public boolean supports(final Class<?> arg0)
	{
		return AbstractPageData.class.isAssignableFrom(arg0);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final AbstractPageData pageData = (AbstractPageData) target;
		final String typeCode = pageData.getTypeCode();
		final List<AbstractPageModel> defaultPages = getPageVariationResolver(typeCode).findPagesByType(typeCode, true);

		if (pageData.getDefaultPage())
		{
			if (!defaultPages.isEmpty())
			{
				errors.rejectValue(TYPE_CODE, CmsfacadesConstants.DEFAULT_PAGE_ALREADY_EXIST, new Object[]
						{ TYPE_CODE, typeCode }, null);
			}
		}
		else if (defaultPages.isEmpty())
		{
			errors.rejectValue(TYPE_CODE, CmsfacadesConstants.DEFAULT_PAGE_DOES_NOT_EXIST, new Object[]
					{ TYPE_CODE, typeCode }, null);
		}
	}

	protected PageVariationResolver<AbstractPageModel> getPageVariationResolver(final String typeCode)
	{
		return getPageVariationResolverTypeRegistry().getPageVariationResolverType(typeCode).get().getResolver();
	}

	protected PageVariationResolverTypeRegistry getPageVariationResolverTypeRegistry()
	{
		return pageVariationResolverTypeRegistry;
	}

	@Required
	public void setPageVariationResolverTypeRegistry(final PageVariationResolverTypeRegistry pageVariationResolverTypeRegistry)
	{
		this.pageVariationResolverTypeRegistry = pageVariationResolverTypeRegistry;
	}
}
