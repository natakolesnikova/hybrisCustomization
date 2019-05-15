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
package de.hybris.platform.cmsfacades.items.comparator;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;

import java.util.Comparator;


/**
 * Compare {@link AbstractCMSComponentData} by modified time and sort by descending order.
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class ItemModifiedTimeComparator implements Comparator<AbstractCMSComponentData>
{

	@Override
	public int compare(final AbstractCMSComponentData component1, final AbstractCMSComponentData component2)
	{
		int value = component1.getModifiedtime().compareTo(component2.getModifiedtime());

		if (value > 0)
		{
			value = -1;
		}
		else if (value < 0)
		{
			value = 1;
		}
		return value;
	}

}