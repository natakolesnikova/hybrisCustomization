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
package de.hybris.platform.cmsfacades.navigations.validator.predicate;


import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;

import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;


/**
 * Validates if the Navigation Node UID is valid
 */
public class ValidUidPredicate implements Predicate<String>
{
	@Override
	public boolean test(final String target)
	{
		if (StringUtils.endsWithIgnoreCase(CmsfacadesConstants.ROOT_NODE_UID, target))
		{
			return false;
		}
		return true;
	}
}
