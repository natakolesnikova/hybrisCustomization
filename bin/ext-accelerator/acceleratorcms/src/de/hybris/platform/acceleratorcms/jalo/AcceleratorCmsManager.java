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
package de.hybris.platform.acceleratorcms.jalo;

import de.hybris.platform.acceleratorcms.constants.AcceleratorCmsConstants;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;


/**
 * Do not use. Please use {@link SystemSetup}.
 */
public class AcceleratorCmsManager extends GeneratedAcceleratorCmsManager
{
	public static final AcceleratorCmsManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (AcceleratorCmsManager) em.getExtension(AcceleratorCmsConstants.EXTENSIONNAME);
	}

}
