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
package com.hybris.backoffice.widgets.actions.locking;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.locking.ItemLockingService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.testing.AbstractCockpitngUnitTest;
import com.hybris.cockpitng.testing.annotation.ExtensibleWidget;

import jersey.repackaged.com.google.common.collect.Lists;


@RunWith(MockitoJUnitRunner.class)
@ExtensibleWidget(level = ExtensibleWidget.ALL)
public class ToggleItemLockActionRendererTest extends AbstractCockpitngUnitTest<ToggleItemLockActionRenderer>
{

	@Spy
	@InjectMocks
	private ToggleItemLockActionRenderer renderer;

	@Mock
	private ItemLockingService itemLockingService;

	@Mock
	private ItemModel lockedModel;

	@Mock
	private ItemModel unlockedModel;

	@Before
	public void setUp()
	{
		when(itemLockingService.isLocked(lockedModel)).thenReturn(true);
		when(itemLockingService.isLocked(unlockedModel)).thenReturn(false);
	}

	@Test
	public void isLocked()
	{
		assertThat(renderer.isLocked(null)).isFalse();
		assertThat(renderer.isLocked(lockedModel)).isTrue();
		assertThat(renderer.isLocked(unlockedModel)).isFalse();
		assertThat(renderer.isLocked(Lists.newArrayList(lockedModel, unlockedModel))).isFalse();
		assertThat(renderer.isLocked(Lists.newArrayList(unlockedModel, unlockedModel))).isFalse();
		assertThat(renderer.isLocked(Lists.newArrayList(lockedModel, lockedModel))).isTrue();
		assertThat(renderer.isLocked(new Object())).isFalse();
	}

}
