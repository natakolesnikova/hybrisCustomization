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
package de.hybris.platform.cmscockpit.session.impl;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmscockpit.events.impl.CmsNavigationEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LiveEditBrowserAreaTest
{

	@Spy
	LiveEditBrowserArea browserArea;

	@Mock
	CmsNavigationEvent cmsNavigationEvent;

	@Mock
	LiveEditBrowserModel browserModel;

	@Test
	public void onCockpitEvent_calledWithCmsNavigationEvent_delegatesTo_setNavigationEventAttributes() throws Exception
	{
		// Arrange
		doReturn(browserModel).when(browserArea).getFocusedBrowser();

		// Act
		browserArea.onCockpitEvent(cmsNavigationEvent);

		// Assert
		verify(browserModel, times(1)).setNavigationEventAttributes(cmsNavigationEvent);
	}

}
