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
package com.hybris.backoffice;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.AbstractItemModel;

import java.lang.reflect.Field;

public class BackofficeTestUtil
{
	/**
	 * Only for unit tests! Sets the pk of an ItemModel. It relies on internal platform implementation and should not be used
	 * outside unit tests.
	 */
	public static void setPk(final AbstractItemModel itemModel, final long pk)
	{
		try
		{
			final Field field = itemModel.getItemModelContext().getClass().getDeclaredField("pk");
			field.setAccessible(true);
			field.set(itemModel.getItemModelContext(), PK.fromLong(pk));
		}
		catch (NoSuchFieldException | IllegalAccessException e)
		{
			throw new RuntimeException("Cannot set pk on itemModel: " + itemModel, e);
		}
	}
}
