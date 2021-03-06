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

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.util.function.Predicate;


/**
 * Predicate to verify that the CMSItemModel is of type AbstractCMSComponentModel
 */
public class CMSComponentPredicate implements Predicate<CMSItemModel>
{
	@Override
	public boolean test(final CMSItemModel cmsItemModel)
	{
		return AbstractCMSComponentModel.class.isAssignableFrom(cmsItemModel.getClass());
	}
}
