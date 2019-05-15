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
package de.hybris.platform.acceleratorfacades.component.populators.model;

import de.hybris.platform.acceleratorcms.model.components.NavigationComponentModel;
import de.hybris.platform.cmsfacades.data.NavigationComponentData;
import de.hybris.platform.converters.Populator;


/**
 * Default navigation component implementation of {@link Populator}.
 */
public class NavigationComponentModelPopulator implements Populator<NavigationComponentModel, NavigationComponentData>
{

	@Override
	public void populate(final NavigationComponentModel source, final NavigationComponentData target)
	{
		if (source.getNavigationNode() != null)
		{
			target.setNavigationNode(source.getNavigationNode().getUid());
		}

		if (source.getWrapAfter() != null)
		{
			target.setWrapAfter(Integer.toString(source.getWrapAfter().intValue()));
		}
	}

}
