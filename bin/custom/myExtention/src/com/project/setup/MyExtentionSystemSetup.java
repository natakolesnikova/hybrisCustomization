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
package com.project.setup;

import static com.project.constants.MyExtentionConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import com.project.constants.MyExtentionConstants;
import com.project.service.MyExtentionService;


@SystemSetup(extension = MyExtentionConstants.EXTENSIONNAME)
public class MyExtentionSystemSetup
{
	private final MyExtentionService myExtentionService;

	public MyExtentionSystemSetup(final MyExtentionService myExtentionService)
	{
		this.myExtentionService = myExtentionService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		myExtentionService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return MyExtentionSystemSetup.class.getResourceAsStream("/myExtention/sap-hybris-platform.png");
	}
}
