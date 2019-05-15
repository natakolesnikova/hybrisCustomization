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
package de.hybris.platform.acceleratorcms.jalo.restrictions;

import de.hybris.platform.jalo.SessionContext;


public class CMSActionRestriction extends GeneratedCMSActionRestriction
{
	/**
	 * @deprecated Since 5.2. use
	 *             {@link de.hybris.platform.acceleratorcms.model.restrictions.CMSUiExperienceRestrictionModel#getDescription()}
	 *             instead.
	 */
	@Override
	@Deprecated
	public String getDescription(final SessionContext sessionContext)
	{
		return "CMSCatalogRestriction";
	}
}
