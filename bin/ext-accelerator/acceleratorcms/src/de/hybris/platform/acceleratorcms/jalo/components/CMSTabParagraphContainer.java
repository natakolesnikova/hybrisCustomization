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
package de.hybris.platform.acceleratorcms.jalo.components;

import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.jalo.SessionContext;

import java.util.List;


/**
 * Container for tab paragraph components.
 */
public class CMSTabParagraphContainer extends GeneratedCMSTabParagraphContainer
{
	@Override
	public List<SimpleCMSComponent> getCurrentCMSComponents(final SessionContext ctx)
	{
		return getSimpleCMSComponents(ctx);
	}
}
