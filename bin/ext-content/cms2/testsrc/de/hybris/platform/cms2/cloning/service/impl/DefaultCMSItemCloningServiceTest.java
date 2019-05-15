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
package de.hybris.platform.cms2.cloning.service.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.cloning.service.CMSItemDeepCloningService;
import de.hybris.platform.cms2.cloning.service.CMSModelCloningContextFactory;
import de.hybris.platform.cms2.cloning.service.predicate.CMSItemCloneablePredicate;
import de.hybris.platform.cms2.constants.Cms2Constants;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSItemCloningServiceTest
{
	@InjectMocks
	private DefaultCMSItemCloningService cmsItemCloningService;

	@Mock
	private CMSModelCloningContextFactory cmsModelCloningContextFactory;
	@Mock
	private CMSItemCloneablePredicate cmsItemCloneablePredicate;
	@Mock
	private CMSItemDeepCloningService cmsItemDeepCloningService;

	@Mock
	private CatalogVersionModel catalogVersionModel;
	@Mock
	private CMSModelCloningContext cmsModelCloningContext;

	@Mock
	private AbstractCMSComponentModel cloneableCMSComponentModel;
	@Mock
	private AbstractCMSComponentModel nonCloneableCMSComponentModel;
	@Mock
	private AbstractCMSComponentModel clonedCMSComponentModel;
	@Mock
	private ContentSlotModel contentSlotModel;

	@Before
	public void setUp()
	{
		when(cmsModelCloningContextFactory.createCloningContextWithCatalogVersionPredicates(catalogVersionModel)).thenReturn(cmsModelCloningContext);
		when(cmsItemCloneablePredicate.test(cloneableCMSComponentModel)).thenReturn(true);
		when(cmsItemCloneablePredicate.test(nonCloneableCMSComponentModel)).thenReturn(false);
		when(cmsItemDeepCloningService.deepCloneComponent(cloneableCMSComponentModel, cmsModelCloningContext)).thenReturn(clonedCMSComponentModel);
		when(contentSlotModel.getCmsComponents()).thenReturn(Arrays.asList(cloneableCMSComponentModel, nonCloneableCMSComponentModel));
	}

	@Test
	public void shouldCloneContentSlotComponents()
	{
		final ContentSlotModel clonedContentSlotModel = new ContentSlotModel();
		cmsItemCloningService.cloneContentSlotComponents(contentSlotModel, clonedContentSlotModel, catalogVersionModel);

		verify(cmsItemDeepCloningService, times(1)).deepCloneComponent(cloneableCMSComponentModel, cmsModelCloningContext);
		verify(cmsItemDeepCloningService, never()).deepCloneComponent(nonCloneableCMSComponentModel, cmsModelCloningContext);

		assertThat(clonedContentSlotModel.getCmsComponents().size(), equalTo(1));
		assertThat(clonedContentSlotModel.getCmsComponents().get(0), equalTo(clonedCMSComponentModel));
	}

	@Test
	public void shouldReturnFalseForNullOrEmptyContext()
	{
		assertThat(cmsItemCloningService.shouldCloneComponents(null), is(false));
		assertThat(cmsItemCloningService.shouldCloneComponents(new HashMap<>()), is(false));
	}

	@Test
	public void shouldReturnFalseForInvalidContext()
	{
		assertThat(cmsItemCloningService.shouldCloneComponents(Maps.newHashMap(Cms2Constants.SHOULD_CLONE_COMPONENTS_CONTEXT_KEY, "blah")), is(false));
		assertThat(cmsItemCloningService.shouldCloneComponents(Maps.newHashMap(Cms2Constants.SHOULD_CLONE_COMPONENTS_CONTEXT_KEY, "")), is(false));
		assertThat(cmsItemCloningService.shouldCloneComponents(Maps.newHashMap(Cms2Constants.SHOULD_CLONE_COMPONENTS_CONTEXT_KEY, null)), is(false));
	}

	@Test
	public void shouldReturnValueFromContext()
	{
		assertThat(cmsItemCloningService.shouldCloneComponents(Maps.newHashMap(Cms2Constants.SHOULD_CLONE_COMPONENTS_CONTEXT_KEY, true)), is(true));
		assertThat(cmsItemCloningService.shouldCloneComponents(Maps.newHashMap(Cms2Constants.SHOULD_CLONE_COMPONENTS_CONTEXT_KEY, "true")), is(true));
		assertThat(cmsItemCloningService.shouldCloneComponents(Maps.newHashMap(Cms2Constants.SHOULD_CLONE_COMPONENTS_CONTEXT_KEY, "TRUE")), is(true));
	}
}
