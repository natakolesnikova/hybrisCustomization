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
package de.hybris.platform.cms2.multicountry.comparator;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.multicountry.service.CatalogLevelService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class ContentSlotCatalogLevelComparatorTest
{
	public static final int ROOT_LEVEL = 0;
	public static final int LEAF_LEVEL = 5;

	@InjectMocks
	private ContentSlotCatalogLevelComparator comparator;
	@Mock
	private CatalogLevelService cmsCatalogLevelService;

	@Mock
	private CatalogVersionModel catalogVersion1;
	@Mock
	private CatalogVersionModel catalogVersion2;
	@Mock
	private ContentCatalogModel contentCatalog;
	@Mock
	private ContentSlotModel slot1;
	@Mock
	private ContentSlotModel slot2;

	@Before
	public void setUp()
	{
		when(catalogVersion1.getCatalog()).thenReturn(contentCatalog);
		when(catalogVersion2.getCatalog()).thenReturn(contentCatalog);
		when(slot1.getCatalogVersion()).thenReturn(catalogVersion1);
		when(slot2.getCatalogVersion()).thenReturn(catalogVersion2);
	}

	@Test
	public void shouldBeGreater_Slot1Null()
	{
		slot1 = null;

		final int value = comparator.compare(slot1, slot2);

		assertThat(value, greaterThan(0));
	}

	@Test
	public void shouldBeSmaller_Slot2Null()
	{
		slot2 = null;

		final int value = comparator.compare(slot1, slot2);

		assertThat(value, lessThan(0));
	}

	@Test
	public void shouldBeSmaller_DifferentCatalogLevels()
	{
		when(cmsCatalogLevelService.getCatalogLevel(contentCatalog)).thenReturn(ROOT_LEVEL).thenReturn(LEAF_LEVEL);

		final int value = comparator.compare(slot1, slot2);

		assertThat(value, lessThan(0));
	}

	@Test
	public void shouldBeGreater_DifferentCatalogLevels()
	{
		when(cmsCatalogLevelService.getCatalogLevel(contentCatalog)).thenReturn(LEAF_LEVEL).thenReturn(ROOT_LEVEL);

		final int value = comparator.compare(slot1, slot2);

		assertThat(value, greaterThan(0));
	}

	@Test
	public void shouldBeGreater_SameCatalogLevels_Slot2FromActiveCatalogVersion()
	{
		when(cmsCatalogLevelService.getCatalogLevel(contentCatalog)).thenReturn(ROOT_LEVEL);
		when(catalogVersion1.getActive()).thenReturn(Boolean.FALSE);
		when(catalogVersion2.getActive()).thenReturn(Boolean.TRUE);

		final int value = comparator.compare(slot1, slot2);

		assertThat(value, greaterThan(0));
	}

	@Test
	public void shouldBeSmaller_SameCatalogLevels_Slot1FromActiveCatalogVersion()
	{
		when(cmsCatalogLevelService.getCatalogLevel(contentCatalog)).thenReturn(ROOT_LEVEL);
		when(catalogVersion1.getActive()).thenReturn(Boolean.TRUE);
		when(catalogVersion2.getActive()).thenReturn(Boolean.FALSE);

		final int value = comparator.compare(slot1, slot2);

		assertThat(value, lessThan(0));
	}

	@Test
	public void shouldBeEqual()
	{
		when(cmsCatalogLevelService.getCatalogLevel(contentCatalog)).thenReturn(ROOT_LEVEL);
		when(catalogVersion1.getActive()).thenReturn(Boolean.TRUE);
		when(catalogVersion2.getActive()).thenReturn(Boolean.TRUE);

		final int value = comparator.compare(slot1, slot2);

		assertThat(value, equalTo(0));
	}

}
