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
package de.hybris.platform.cmsfacades.pages.predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmsfacades.data.ContentPageData;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class HasPageLabelChangedPredicateTest
{
	private static final String LABEL = "test-label";
	private static final String NEW_LABEL = "test-new-label";
	private static final String UID = "test-page-uid";

	@InjectMocks
	private HasPageLabelChangedPredicate predicate;

	@Mock
	private CMSAdminPageService adminPageService;
	@Mock
	private CatalogVersionModel catalogVersion;
	@Mock
	private ContentPageModel contentPageModel;
	private ContentPageData contentPageData;

	@Before
	public void setUp()
	{
		contentPageData = new ContentPageData();
		contentPageData.setLabel(LABEL);
		contentPageData.setUid(UID);

		when(adminPageService.getActiveCatalogVersion()).thenReturn(catalogVersion);
	}

	@Test
	public void testLabelHasChanged()
	{
		when(adminPageService.getAllContentPages(Arrays.asList(catalogVersion))).thenReturn(Arrays.asList(contentPageModel));
		when(contentPageModel.getUid()).thenReturn(UID);
		when(contentPageModel.getLabel()).thenReturn(NEW_LABEL);

		final boolean result = predicate.test(contentPageData);

		assertTrue(result);
	}

	@Test
	public void testLabelNotChanged()
	{
		when(adminPageService.getAllContentPages(Arrays.asList(catalogVersion))).thenReturn(Arrays.asList(contentPageModel));
		when(contentPageModel.getUid()).thenReturn(UID);
		when(contentPageModel.getLabel()).thenReturn(LABEL);

		final boolean result = predicate.test(contentPageData);

		assertFalse(result);
	}

	@Test
	public void testLabelNotChanged_NoPagesFoundForLabel()
	{
		when(adminPageService.getAllContentPages(Arrays.asList(catalogVersion))).thenReturn(Collections.emptyList());

		final boolean result = predicate.test(contentPageData);

		assertFalse(result);
	}

}
