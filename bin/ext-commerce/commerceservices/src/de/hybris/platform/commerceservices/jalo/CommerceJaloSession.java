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
package de.hybris.platform.commerceservices.jalo;

import de.hybris.platform.jalo.JaloSession;


/**
 * JaloSession that does not delete the cart so we have a chance to restore it later.
 */
public class CommerceJaloSession extends JaloSession
{
	@Override
	public void removeCart()
	{
		setAttachedCart(null);
	}
}
