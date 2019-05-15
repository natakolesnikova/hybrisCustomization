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
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmsfacades.data.ContentPageData;

import java.util.Arrays;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;


/**
 * Predicate to test if the label has been changed by comparing the label in {@code ContentPageData} to the label in
 * {@ContentPageModel}.
 * <p>
 * Returns <tt>TRUE</tt> if the page label has been modified; <tt>FALSE</tt> otherwise.
 * </p>
 * 
 * @deprecated since 6.6. Please {@link de.hybris.platform.cmsfacades.cmsitems.predicates.HasPageLabelChangedPredicate}
 *             instead
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class HasPageLabelChangedPredicate implements Predicate<ContentPageData>
{
	private CMSAdminPageService adminPageService;

	@Override
	public boolean test(final ContentPageData pageData)
	{
		return getAdminPageService().getAllContentPages(Arrays.asList(getAdminPageService().getActiveCatalogVersion())).stream() //
				.filter(page -> page.getUid().equals(pageData.getUid())) //
				.filter(page -> !page.getLabel().equals(pageData.getLabel())) //
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

}
