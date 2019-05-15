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
package de.hybris.platform.cmsfacades.pages.predicate;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmsfacades.data.ContentPageData;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolver;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;


/**
 * Predicate to test if a given page label maps to an existing primary page.
 * <p>
 * Returns <tt>TRUE</tt> if the page exists; <tt>FALSE</tt> otherwise.
 * </p>
 * 
 * @deprecated since 6.6. Please use
 *             {@link de.hybris.platform.cmsfacades.cmsitems.predicates.PrimaryPageWithLabelExistsPredicate} instead
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class PrimaryPageWithLabelExistsPredicate implements Predicate<ContentPageData>
{
	private PageVariationResolver<ContentPageModel> pageVariationResolver;
	private CMSAdminPageService adminPageService;

	@Override
	public boolean test(final ContentPageData pageData)
	{
		return getPageVariationResolver().findPagesByType(ContentPageModel._TYPECODE, Boolean.TRUE).stream() //
				.filter(defaultPage -> defaultPage.getLabel().equals(pageData.getLabel())) //
				.findFirst().isPresent();
	}

	protected CMSAdminPageService getAdminPageService()
	{
		return adminPageService;
	}

	@Required
	public void setAdminPageService(final CMSAdminPageService adminPageService)
	{
		this.adminPageService = adminPageService;
	}

	protected PageVariationResolver<ContentPageModel> getPageVariationResolver()
	{
		return pageVariationResolver;
	}

	@Required
	public void setPageVariationResolver(final PageVariationResolver<ContentPageModel> pageVariationResolver)
	{
		this.pageVariationResolver = pageVariationResolver;
	}

}
