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
package de.hybris.platform.orderhistory.jalo.mockups.actions;

import de.hybris.platform.util.Utilities;

import java.text.DateFormat;
import java.util.Calendar;




public class TestAction// implements Action
{
	private String result;
	private final DateFormat dateFormat = Utilities.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM); //NOPMD

	public TestAction()
	{
		super();
	}

	public TestAction(final String result)
	{
		this.result = result;
	}

	private String formatResult(final String result)
	{
		return dateFormat.format(Calendar.getInstance().getTime()) + ": " + getClass().getName() + ", result : " + result;
	}

	public String execute()
	{
		//execution...
		return formatResult(result);
	}


}