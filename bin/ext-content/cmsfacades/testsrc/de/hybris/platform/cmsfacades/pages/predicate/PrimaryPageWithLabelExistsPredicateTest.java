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
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolver;

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
public class PrimaryPageWithLabelExistsPredicateTest
{
	private static final String UID = "test-uid";
	private static final String LABEL = "test-label";

	@Mock
	private CMSAdminPageService adminPageService;
	@Mock
	private PageVariationResolver<ContentPageModel> resolver;
	@InjectMocks
	private PrimaryPageWithLabelExistsPredicate predicate;

	@Mock
	private CatalogVersionModel catalogVersion;
	@Mock
	private ContentPageModel contentPage;
	private ContentPageData contentPageData;

	@Before
	public void setUp()
	{
		contentPageData = new ContentPageData();
		contentPageData.setUid(UID);
		contentPageData.setLabel(LABEL);

		when(adminPageService.getActiveCatalogVersion()).thenReturn(catalogVersion);
	}

	@Test
	public void shouldFindPageWithLabel()
	{
		when(contentPage.getUid()).thenReturn(UID);
		when(contentPage.getLabel()).thenReturn(LABEL);

		when(adminPageService.getContentPages(Arrays.asList(catalogVersion), LABEL)).thenReturn(Arrays.asList(contentPage));
		when(resolver.findPagesByType(ContentPageModel._TYPECODE, true)).thenReturn(Arrays.asList(contentPage));

		final boolean result = predicate.test(contentPageData);
		assertTrue(result);
	}

	@Test
	public void shouldNotFindPageWithLabel_Empty()
	{
		when(resolver.findPagesByType(ContentPageModel._TYPECODE, true)).thenReturn(Collections.emptyList());

		final boolean result = predicate.test(contentPageData);
		assertFalse(result);
	}

}
