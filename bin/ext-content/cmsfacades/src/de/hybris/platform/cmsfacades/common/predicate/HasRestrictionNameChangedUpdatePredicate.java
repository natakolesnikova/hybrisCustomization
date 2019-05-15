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
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminRestrictionService;
import de.hybris.platform.cmsfacades.data.AbstractRestrictionData;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;


/**
 * Predicate to test if the name has been changed by comparing the name in {@code AbstractRestrictionData} to the name
 * in {@AbstractRestrictionModel}.
 * <p>
 * Returns <tt>TRUE</tt> if the restriction name has been modified; <tt>FALSE</tt> otherwise.
 * </p>
 * 
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class HasRestrictionNameChangedUpdatePredicate implements Predicate<AbstractRestrictionData>
{
	private CMSAdminRestrictionService adminRestrictionService;

	@Override
	public boolean test(final AbstractRestrictionData restrictionData)
	{
		boolean result = true;
		try
		{
			result = getAdminRestrictionService().getAllRestrictionsByType(restrictionData.getTypeCode()).stream() //
					.filter(restriction -> restriction.getUid().equals(restrictionData.getUid())) //
					.filter(restriction -> !restriction.getName().equals(restrictionData.getName())) //
					.findFirst().isPresent();
		}
		catch (final CMSItemNotFoundException e)
		{
			result = false;
		}
		return result;
	}

	protected CMSAdminRestrictionService getAdminRestrictionService()
	{
		return adminRestrictionService;
	}

	@Required
	public void setAdminRestrictionService(final CMSAdminRestrictionService adminRestrictionService)
	{
		this.adminRestrictionService = adminRestrictionService;
	}

}
