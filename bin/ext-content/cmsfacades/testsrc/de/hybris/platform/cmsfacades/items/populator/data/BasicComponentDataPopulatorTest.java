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
package de.hybris.platform.cmsfacades.items.populator.data;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class BasicComponentDataPopulatorTest
{
	private static final String COMPONENT_UID = "test-id";
	private static final String COMPONENT_NAME = "Test Component";

	BasicComponentDataPopulator populator = new BasicComponentDataPopulator();

	AbstractCMSComponentData dto;
	AbstractCMSComponentModel model;

	@Before
	public void setUp()
	{
		model = new AbstractCMSComponentModel();

		dto = new AbstractCMSComponentData();
		dto.setUid(COMPONENT_UID);
		dto.setName(COMPONENT_NAME);
		dto.setVisible(Boolean.FALSE);
	}

	@Test
	public void shouldPopulateBasicData()
	{
		populator.populate(dto, model);

		assertEquals(COMPONENT_UID, model.getUid());
		assertEquals(COMPONENT_NAME, model.getName());
		assertEquals(Boolean.FALSE, model.getVisible());
	}

	@Test
	public void shouldPopulateWithDefaultBooleanValue()
	{
		dto.setVisible(null);

		populator.populate(dto, model);

		assertEquals(COMPONENT_UID, model.getUid());
		assertEquals(COMPONENT_NAME, model.getName());
		assertEquals(Boolean.TRUE, model.getVisible());
	}

}
