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
package com.project.fulfilmentprocess.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import com.project.fulfilmentprocess.constants.MerchandiseFulfilmentProcessConstants;

public class MerchandiseFulfilmentProcessManager extends GeneratedMerchandiseFulfilmentProcessManager
{
	public static final MerchandiseFulfilmentProcessManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MerchandiseFulfilmentProcessManager) em.getExtension(MerchandiseFulfilmentProcessConstants.EXTENSIONNAME);
	}
	
}
