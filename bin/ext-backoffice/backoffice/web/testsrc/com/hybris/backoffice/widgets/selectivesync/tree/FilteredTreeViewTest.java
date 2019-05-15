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
package com.hybris.backoffice.widgets.selectivesync.tree;

import static com.google.common.collect.Lists.newArrayList;
import static com.hybris.cockpitng.testing.util.CockpitTestUtil.findAllChildren;
import static com.hybris.cockpitng.testing.util.CockpitTestUtil.findChild;
import static com.hybris.cockpitng.testing.util.CockpitTestUtil.simulateEvent;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.catalog.model.SyncAttributeDescriptorConfigModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;

import com.google.common.collect.Maps;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;

@RunWith(MockitoJUnitRunner.class)
public class FilteredTreeViewTest
{
	@Mock
	private ComposedTypeModel mockedRootType;
	@Mock
	private SyncAttributeTreeModel mockedModel;
	@Mock
	private WidgetInstanceManager widgetInstanceManager;
	@Mock
	private WidgetModel widgetModel;


	@Before
	public void before()
	{
		CockpitTestUtil.mockZkEnvironment();
		when(widgetInstanceManager.getModel()).thenReturn(widgetModel);
		when(widgetModel.getValue(anyString(), any())).thenReturn(null);
	}

	@Test
	public void shouldCreateTreeViewContainingFilterWithTree()
	{
		// given
		final SyncAttributeDescriptorConfigModel rootNode = prepareNode(mockedRootType);
		final SyncAttributeTreeModel treeModel = SyncAttributeTreeModelFactory.create(newArrayList(rootNode), mockedRootType);

		// when
		final FilteredTreeView section = new FilteredTreeView(treeModel, widgetInstanceManager);

		// then
		assertThat(section).isNotNull();
		assertThat(section).isNotNull();
		assertThat(findChild(section, Tree.class).isPresent()).isTrue();
		assertThat(findChild(section, Button.class).isPresent()).isTrue();
		assertThat(findChild(section, Textbox.class).isPresent()).isTrue();
	}

	@Test
	public void shouldCreateTreeViewBasedOnModel()
	{
		// given
		final SyncAttributeDescriptorConfigModel rootNode = prepareNode(mockedRootType);
		final SyncAttributeDescriptorConfigModel firstElement = prepareNode();
		final SyncAttributeDescriptorConfigModel secondElement = prepareNode();
		final SyncAttributeTreeModel treeModel = SyncAttributeTreeModelFactory.create(newArrayList(rootNode, firstElement, secondElement),
				mockedRootType);

		// when
		final FilteredTreeView section = new FilteredTreeView(treeModel, widgetInstanceManager);

		// then
		assertThat(section).isNotNull();
		assertThat(findChild(section, Tree.class).isPresent()).isTrue();
		assertThat(findChild(section, Tree.class).get().getModel()).isInstanceOf(FilteredTreeModel.class);
	}

	@Test
	public void shouldShowPopupOnFilterButtonClick() throws InterruptedException
	{
		// given
		final SyncAttributeDescriptorConfigModel rootNode = prepareNode(mockedRootType);
		final SyncAttributeDescriptorConfigModel firstElement = prepareNode();
		final SyncAttributeDescriptorConfigModel secondElement = prepareNode();
		final SyncAttributeTreeModel treeModel = SyncAttributeTreeModelFactory.create(newArrayList(rootNode, firstElement, secondElement),
				mockedRootType);
		final FilteredTreeView section = new FilteredTreeView(treeModel, widgetInstanceManager);

		// when
		tryToOpenPopUp(section);

		// then
		assertThat(findChild(section, Popup.class).isPresent()).isTrue();
	}

	@Test
	public void shouldFilterIncludedAttributes()
	{
		// given
		final FilteredTreeView section = new FilteredTreeView(mockedModel, widgetInstanceManager);
		final Tree tree = findChild(section, Tree.class).orElseThrow(AssertionError::new);
		tryToOpenPopUp(section);

		final Checkbox includedCheckbox = findAllChildren(section, Checkbox.class)
				.filter(checkbox -> "filter_included_checkbox".equals(checkbox.getAttribute("ytestid"))).findFirst()
				.orElseThrow(AssertionError::new);

		final boolean expectedShowIncluded = false;
		// when
		simulateEvent(includedCheckbox, new CheckEvent(Events.ON_CHECK, includedCheckbox, expectedShowIncluded));
		simulateEvent(tree, "onToggleIncludedAttributesEvent", null);

		// then
		final ArgumentCaptor<FilterContext> filterContextCaptor = ArgumentCaptor.forClass(FilterContext.class);
		verify(mockedModel, times(2)).filter(filterContextCaptor.capture());
		assertThat(filterContextCaptor.getValue().getShowIncluded()).isEqualTo(expectedShowIncluded);
	}

	@Test
	public void shouldFilterNotIncludedAttributes()
	{
		// given
		final FilteredTreeView section = new FilteredTreeView(mockedModel, widgetInstanceManager);
		final Tree tree = findChild(section, Tree.class).orElseThrow(AssertionError::new);
		tryToOpenPopUp(section);

		final Checkbox includedCheckbox = findAllChildren(section, Checkbox.class)
				.filter(checkbox -> "filter_not_included_checkbox".equals(checkbox.getAttribute("ytestid"))).findFirst()
				.orElseThrow(AssertionError::new);

		final boolean expectedShowNotIncluded = false;
		// when
		simulateEvent(includedCheckbox, new CheckEvent(Events.ON_CHECK, includedCheckbox, expectedShowNotIncluded));
		simulateEvent(tree, "onToggleNotIncludedAttributesEvent", null);

		// then
		final ArgumentCaptor<FilterContext> filterContextCaptor = ArgumentCaptor.forClass(FilterContext.class);
		verify(mockedModel, times(2)).filter(filterContextCaptor.capture());
		assertThat(filterContextCaptor.getValue().getShowNotIncluded()).isEqualTo(expectedShowNotIncluded);
	}

	@Test
	public void shouldFilterBySearchInput()
	{
		// given
		final FilteredTreeView section = new FilteredTreeView(mockedModel, widgetInstanceManager);
		final Tree tree = findChild(section, Tree.class).orElseThrow(AssertionError::new);
		tryToOpenPopUp(section);

		final Textbox filterTextBox = findChild(section, Textbox.class).orElseThrow(AssertionError::new);

		// when
		simulateEvent(filterTextBox, new InputEvent(Events.ON_CHANGING, filterTextBox, "newValue", "oldValue"));
		simulateEvent(tree, "onSearchFilterChangeEvent", null);

		// then
		final ArgumentCaptor<FilterContext> filterContextCaptor = ArgumentCaptor.forClass(FilterContext.class);
		verify(mockedModel, times(2)).filter(filterContextCaptor.capture());
		assertThat(filterContextCaptor.getValue().getFilterQuery()).isEqualTo("newValue");
	}

	private void tryToOpenPopUp(final FilteredTreeView section)
	{
		final Button filterButton = findChild(section, Button.class).orElseThrow(AssertionError::new);
		simulateEvent(filterButton, Events.ON_CLICK, null);
	}

	private SyncAttributeDescriptorConfigModel prepareNode()
	{
		return prepareNode(mock(ComposedTypeModel.class));
	}

	private SyncAttributeDescriptorConfigModel prepareNode(final ComposedTypeModel rootType)
	{
		final SyncAttributeDescriptorConfigModel mockedSyncAttributeDescriptorConfigModel = mock(
				SyncAttributeDescriptorConfigModel.class);
		final AttributeDescriptorModel mockedAttributeDescriptorModel = mock(AttributeDescriptorModel.class);
		given(mockedSyncAttributeDescriptorConfigModel.getAttributeDescriptor()).willReturn(mockedAttributeDescriptorModel);
		given(mockedAttributeDescriptorModel.getEnclosingType()).willReturn(rootType);
		return mockedSyncAttributeDescriptorConfigModel;
	}
}
