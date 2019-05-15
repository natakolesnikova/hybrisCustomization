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

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class ItemModifiedTimeComparatorTest
{
	private final ItemModifiedTimeComparator comparator = new ItemModifiedTimeComparator();

	private final LocalDateTime time1 = LocalDateTime.of(2015, 1, 1, 10, 30);
	private final LocalDateTime time2 = LocalDateTime.of(2015, 1, 2, 12, 45);

	private AbstractCMSComponentData component1;
	private AbstractCMSComponentData component2;

	@Before
	public void setUp()
	{
		component1 = new AbstractCMSComponentData();
		component2 = new AbstractCMSComponentData();
		component1.setModifiedtime(Date.from(time1.atZone(ZoneId.systemDefault()).toInstant()));
		component2.setModifiedtime(Date.from(time2.atZone(ZoneId.systemDefault()).toInstant()));
	}

	@Test
	public void shouldBeGreaterTest()
	{
		final int value = comparator.compare(component1, component2);
		assertEquals(1, value);
	}

	@Test
	public void shouldBeSmallerTest()
	{
		final int value = comparator.compare(component2, component1);
		assertEquals(-1, value);
	}

	@Test
	public void shouldBeEqualTest()
	{
		component1.setModifiedtime(Date.from(time1.atZone(ZoneId.systemDefault()).toInstant()));
		component2.setModifiedtime(Date.from(time1.atZone(ZoneId.systemDefault()).toInstant()));

		final int value = comparator.compare(component1, component2);
		assertEquals(0, value);
	}

}
