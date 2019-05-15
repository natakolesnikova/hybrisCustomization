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
package de.hybris.platform.acceleratorcms.jalo.actions;

import de.hybris.platform.jalo.SessionContext;

import org.apache.log4j.Logger;


public class SimpleCMSAction extends GeneratedSimpleCMSAction
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(SimpleCMSAction.class.getName());

	/**
	 * @deprecated Since 5.2. Use
	 *             {@link de.hybris.platform.cms2.servicelayer.services.CMSComponentService#isContainer(String)} instead.
	 */
	@Deprecated
	@Override
	public Boolean isContainer(final SessionContext sessionContext)
	{
		return Boolean.FALSE;
	}
}
