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
package de.hybris.platform.commercefacades.user.exceptions;

import de.hybris.platform.servicelayer.exceptions.SystemException;


/**
 * Exception is thrown when there is attempt to change customer password but given old password does not match the one
 * stored in the system.
 * 
 */
public class PasswordMismatchException extends SystemException
{
	public PasswordMismatchException(final String message)
	{
		super(message);
	}

	public PasswordMismatchException(final Throwable cause)
	{
		super(cause);
	}

	public PasswordMismatchException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
