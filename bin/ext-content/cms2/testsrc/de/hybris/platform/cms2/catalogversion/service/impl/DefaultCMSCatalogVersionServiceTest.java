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
package de.hybris.platform.cms2.catalogversion.service.impl;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSCatalogVersionServiceTest
{
	@InjectMocks
	private DefaultCMSCatalogVersionService cmsCatalogVersionService;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private CatalogVersionService catalogVersionService;
	@Mock
	private UserService userService;
	@Mock
	private UserModel user;
	@Mock
	private CatalogVersionModel catalogVersionOnline;
	@Mock
	private CatalogVersionModel catalogVersionStaged;
	@Mock
	private CatalogVersionModel catalogVersionUAT;
	@Mock
	private ContentCatalogModel contentCatalog;
	@Mock
	private CatalogModel catalog;
	@Mock
	private BaseSiteModel baseSiteModel;
	@Mock
	private CMSSiteModel cmsSiteModel;

	@Before
	public void setUp()
	{
		when(userService.getCurrentUser()).thenReturn(user);

		when(catalogVersionService.getAllCatalogVersions())
				.thenReturn(Arrays.asList(catalogVersionOnline, catalogVersionStaged, catalogVersionUAT));
		when(catalogVersionService.getAllReadableCatalogVersions(user)).thenReturn(Arrays.asList(catalogVersionStaged));
		when(catalogVersionService.getAllWritableCatalogVersions(user))
				.thenReturn(Arrays.asList(catalogVersionStaged, catalogVersionUAT));
	}

	protected void setUpContentCatalog()
	{
		when(cmsSiteModel.getContentCatalogs()).thenReturn(Arrays.asList(contentCatalog));

		when(catalogVersionOnline.getCatalog()).thenReturn(contentCatalog);
		when(catalogVersionStaged.getCatalog()).thenReturn(contentCatalog);
		when(catalogVersionUAT.getCatalog()).thenReturn(contentCatalog);

		when(catalogVersionOnline.getActive()).thenReturn(true);
	}

	protected void setUpProductCatalog()
	{
		when(baseSiteService.getProductCatalogs(baseSiteModel)).thenReturn(Arrays.asList(catalog));

		when(catalogVersionOnline.getCatalog()).thenReturn(catalog);
		when(catalogVersionStaged.getCatalog()).thenReturn(catalog);
		when(catalogVersionUAT.getCatalog()).thenReturn(catalog);
	}

	@Test
	public void shouldGetReadAndWriteContentCatalogsAndVersions()
	{
		setUpContentCatalog();

		final Map<CatalogModel, Set<CatalogVersionModel>> permittedCatalogsAndVersions = cmsCatalogVersionService
				.getContentCatalogsAndVersions(true, true, cmsSiteModel);

		verify(catalogVersionService).getAllReadableCatalogVersions(user);
		verify(catalogVersionService).getAllWritableCatalogVersions(user);
		assertThat(permittedCatalogsAndVersions.containsKey(contentCatalog), is(true));
		assertThat(permittedCatalogsAndVersions.get(contentCatalog),
				containsInAnyOrder(catalogVersionStaged, catalogVersionOnline, catalogVersionUAT));
	}

	@Test
	public void shouldGetReadContentCatalogsAndVersions()
	{
		setUpContentCatalog();

		final Map<CatalogModel, Set<CatalogVersionModel>> permittedCatalogsAndVersions = cmsCatalogVersionService
				.getContentCatalogsAndVersions(true, false, cmsSiteModel);

		verify(catalogVersionService).getAllReadableCatalogVersions(user);
		verify(catalogVersionService, times(0)).getAllWritableCatalogVersions(user);
		assertThat(permittedCatalogsAndVersions.containsKey(contentCatalog), is(true));
		assertThat(permittedCatalogsAndVersions.get(contentCatalog),
				containsInAnyOrder(catalogVersionOnline, catalogVersionStaged));
	}

	@Test
	public void shouldGetWriteContentCatalogsAndVersions()
	{
		setUpContentCatalog();

		final Map<CatalogModel, Set<CatalogVersionModel>> permittedCatalogsAndVersions = cmsCatalogVersionService
				.getContentCatalogsAndVersions(false, true, cmsSiteModel);

		verify(catalogVersionService, times(0)).getAllReadableCatalogVersions(user);
		verify(catalogVersionService).getAllWritableCatalogVersions(user);
		assertThat(permittedCatalogsAndVersions.containsKey(contentCatalog), is(true));
		assertThat(permittedCatalogsAndVersions.get(contentCatalog), containsInAnyOrder(catalogVersionStaged, catalogVersionUAT));
	}

	@Test
	public void shouldGetNoPermittedContentCatalogsAndVersions()
	{
		setUpContentCatalog();
		when(catalogVersionService.getAllReadableCatalogVersions(user)).thenReturn(Collections.emptyList());
		when(catalogVersionService.getAllWritableCatalogVersions(user)).thenReturn(Collections.emptyList());

		final Map<CatalogModel, Set<CatalogVersionModel>> permittedCatalogsAndVersions = cmsCatalogVersionService
				.getContentCatalogsAndVersions(true, true, cmsSiteModel);

		verify(catalogVersionService).getAllReadableCatalogVersions(user);
		verify(catalogVersionService).getAllWritableCatalogVersions(user);
		assertThat(permittedCatalogsAndVersions.get(contentCatalog), contains(catalogVersionOnline));
	}

	@Test
	public void shouldGetReadAndWriteProductCatalogsAndVersions()
	{
		setUpProductCatalog();

		final Map<CatalogModel, Set<CatalogVersionModel>> permittedCatalogsAndVersions = cmsCatalogVersionService
				.getProductCatalogsAndVersions(true, true, baseSiteModel);

		verify(catalogVersionService).getAllReadableCatalogVersions(user);
		verify(catalogVersionService).getAllWritableCatalogVersions(user);
		assertThat(permittedCatalogsAndVersions.containsKey(catalog), is(true));
		assertThat(permittedCatalogsAndVersions.get(catalog), containsInAnyOrder(catalogVersionStaged, catalogVersionUAT));
	}
}
