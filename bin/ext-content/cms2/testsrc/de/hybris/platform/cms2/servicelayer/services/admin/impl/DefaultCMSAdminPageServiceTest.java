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
package de.hybris.platform.cms2.servicelayer.services.admin.impl;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSPageDao;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class DefaultCMSAdminPageServiceTest
{
	private static final String INVALID = "invalid";
	private static final String TEST_PAGE_TYPE = "testPageType";

	@Spy
	@InjectMocks
	private DefaultCMSAdminPageService pageService;

	@Mock
	private CMSPageDao cmsPageDao;
	@Mock
	private TypeService typeService;
	@Mock
	private ComposedTypeModel composedTypeModel;
	@Mock
	private CMSPageTypeModel pageType1;
	@Mock
	private CMSPageTypeModel pageType2;

	@Before
	public void setUp()
	{
		when(typeService.getComposedTypeForClass(AbstractPageModel.class)).thenReturn(composedTypeModel);
		when(composedTypeModel.getAllSubTypes()).thenReturn(Arrays.asList(pageType1, pageType2));
		when(pageType1.getCode()).thenReturn(TEST_PAGE_TYPE);
		when(pageType2.getCode()).thenReturn(TEST_PAGE_TYPE);
	}

	@Test
	public void shouldGetAllPageTypes()
	{
		final Collection<CMSPageTypeModel> pageTypes = pageService.getAllPageTypes();
		assertThat(pageTypes, containsInAnyOrder(pageType1, pageType2));
	}

	@Test
	public void shouldGetPageTypeByCode()
	{
		final Optional<CMSPageTypeModel> pageType = pageService.getPageTypeByCode(TEST_PAGE_TYPE);
		assertThat(pageType.isPresent(), is(true));
		assertThat(pageType.get(), is(pageType1));
	}

	@Test
	public void shouldNotGetPageTypeByInvalidCode()
	{
		final Optional<CMSPageTypeModel> pageType = pageService.getPageTypeByCode(INVALID);
		assertThat(pageType.isPresent(), is(false));
	}

	@Test
	public void shouldGetHomepageForSite()
	{
		final CatalogVersionModel catalogVersionOnline = mock(CatalogVersionModel.class);

		final ContentPageModel faqPage = mock(ContentPageModel.class);
		final ContentPageModel termsPage = mock(ContentPageModel.class);
		final ContentPageModel homepage = mock(ContentPageModel.class);
		when(homepage.isHomepage()).thenReturn(true);
		when(cmsPageDao.findAllContentPagesByCatalogVersionsAndPageStatuses(Arrays.asList(catalogVersionOnline),
				Arrays.asList(CmsPageStatus.ACTIVE))).thenReturn(Arrays.asList(faqPage, termsPage, homepage));

		final CMSSiteModel cmsSite = mock(CMSSiteModel.class);
		final ContentCatalogModel contentCatalog = mock(ContentCatalogModel.class);
		when(contentCatalog.getActiveCatalogVersion()).thenReturn(catalogVersionOnline);
		when(cmsSite.getContentCatalogs()).thenReturn(Arrays.asList(contentCatalog));

		final ContentPageModel result = pageService.getHomepage(cmsSite);

		assertThat(result, equalTo(homepage));
	}

	@Test
	public void shouldGetHomepageForCatalogVersions()
	{
		final CatalogVersionModel catalogVersionStaged = mock(CatalogVersionModel.class);
		final CatalogVersionModel catalogVersionOnline = mock(CatalogVersionModel.class);
		final List<CatalogVersionModel> catalogVersions = Arrays.asList(catalogVersionStaged, catalogVersionOnline);

		final ContentPageModel faqPage = mock(ContentPageModel.class);
		final ContentPageModel termsPage = mock(ContentPageModel.class);
		final ContentPageModel homepage = mock(ContentPageModel.class);
		when(homepage.isHomepage()).thenReturn(true);
		when(cmsPageDao.findAllContentPagesByCatalogVersionsAndPageStatuses(catalogVersions, Arrays.asList(CmsPageStatus.ACTIVE)))
				.thenReturn(Arrays.asList(faqPage, termsPage, homepage));

		final ContentPageModel result = pageService.getHomepage(catalogVersions);

		assertThat(result, equalTo(homepage));
	}

	@Test
	public void shouldGetNullHomepageForCatalogVersions()
	{
		final CatalogVersionModel catalogVersionStaged = mock(CatalogVersionModel.class);
		final CatalogVersionModel catalogVersionOnline = mock(CatalogVersionModel.class);
		final List<CatalogVersionModel> catalogVersions = Arrays.asList(catalogVersionStaged, catalogVersionOnline);

		final ContentPageModel faqPage = mock(ContentPageModel.class);
		final ContentPageModel termsPage = mock(ContentPageModel.class);
		when(cmsPageDao.findAllContentPagesByCatalogVersions(catalogVersions)).thenReturn(Arrays.asList(faqPage, termsPage));

		final ContentPageModel result = pageService.getHomepage(catalogVersions);

		assertThat(result, is(nullValue()));
	}

	@Test
	public void shouldGetAllActiveContentPages()
	{
		final CatalogVersionModel catalogVersionStaged = mock(CatalogVersionModel.class);
		final CatalogVersionModel catalogVersionOnline = mock(CatalogVersionModel.class);
		final List<CatalogVersionModel> catalogVersions = Arrays.asList(catalogVersionStaged, catalogVersionOnline);

		final ContentPageModel faqPage = mock(ContentPageModel.class);
		final ContentPageModel termsPage = mock(ContentPageModel.class);

		when(cmsPageDao.findAllContentPagesByCatalogVersionsAndPageStatuses(catalogVersions, Arrays.asList(CmsPageStatus.ACTIVE)))
				.thenReturn(Arrays.asList(faqPage, termsPage));

		pageService.getAllContentPagesForPageStatuses(catalogVersions, Arrays.asList(CmsPageStatus.ACTIVE));
		verify(cmsPageDao)
				.findAllContentPagesByCatalogVersionsAndPageStatuses(catalogVersions, Arrays.asList(CmsPageStatus.ACTIVE));
	}

	@Test
	public void shouldGetAllActivePagesForCatalogVersionAndPageStatuses()
	{
		final CatalogVersionModel catalogVersionStaged = mock(CatalogVersionModel.class);

		final ContentPageModel faqPage = mock(ContentPageModel.class);
		final ContentPageModel termsPage = mock(ContentPageModel.class);

		when(cmsPageDao.findAllPagesByCatalogVersionAndPageStatuses(catalogVersionStaged, Arrays.asList(CmsPageStatus.ACTIVE)))
				.thenReturn(Arrays.asList(faqPage, termsPage));

		pageService.getAllPagesForCatalogVersionAndPageStatuses(catalogVersionStaged, Arrays.asList(CmsPageStatus.ACTIVE));
		verify(cmsPageDao).findAllPagesByCatalogVersionAndPageStatuses(catalogVersionStaged, Arrays.asList(CmsPageStatus.ACTIVE));
	}

	@Test
	public void shouldGetActivePageForIdFromActiveCatalogVersionByPageStatuses()
	{
		final CatalogVersionModel catalogVersionStaged = mock(CatalogVersionModel.class);

		final String faqPageUId = "cool-uild";
		final AbstractPageModel faqPage = mock(AbstractPageModel.class);
		faqPage.setUid(faqPageUId);
		doReturn(catalogVersionStaged).when(pageService).getActiveCatalogVersion();

		when(cmsPageDao
				.findPagesByIdAndCatalogVersionAndPageStatuses(faqPageUId, catalogVersionStaged, Arrays.asList(CmsPageStatus.ACTIVE)))
				.thenReturn(Arrays.asList(faqPage));

		AbstractPageModel result = pageService
				.getPageForIdFromActiveCatalogVersionByPageStatuses(faqPageUId, Arrays.asList(CmsPageStatus.ACTIVE));
		assertThat(result, equalTo(faqPage));
	}

	@Test(expected = UnknownIdentifierException.class)
	public void expectGetPageForIdFromActiveCatalogVersionByPageStatusesToThrowUnknownIdentifierException()
	{
		final CatalogVersionModel catalogVersionStaged = mock(CatalogVersionModel.class);

		final String faqPageUId = "cool-uild";
		final AbstractPageModel faqPage = mock(AbstractPageModel.class);
		faqPage.setUid(faqPageUId);
		doReturn(catalogVersionStaged).when(pageService).getActiveCatalogVersion();

		when(cmsPageDao
				.findPagesByIdAndCatalogVersionAndPageStatuses(faqPageUId, catalogVersionStaged, Arrays.asList(CmsPageStatus.ACTIVE)))
				.thenReturn(Arrays.asList());

		pageService.getPageForIdFromActiveCatalogVersionByPageStatuses(faqPageUId, Arrays.asList(CmsPageStatus.ACTIVE));
	}

	@Test(expected = AmbiguousIdentifierException.class)
	public void expectGetPageForIdFromActiveCatalogVersionByPageStatusesToThrowAmbiguousIdentifierException()
	{
		final CatalogVersionModel catalogVersionStaged = mock(CatalogVersionModel.class);

		final String faqPageUId = "cool-uild";
		final AbstractPageModel faqPage = mock(AbstractPageModel.class);
		faqPage.setUid(faqPageUId);
		final ContentPageModel termsPage = mock(ContentPageModel.class);

		doReturn(catalogVersionStaged).when(pageService).getActiveCatalogVersion();

		when(cmsPageDao
				.findPagesByIdAndCatalogVersionAndPageStatuses(faqPageUId, catalogVersionStaged, Arrays.asList(CmsPageStatus.ACTIVE)))
				.thenReturn(Arrays.asList(faqPage, termsPage));

		pageService.getPageForIdFromActiveCatalogVersionByPageStatuses(faqPageUId, Arrays.asList(CmsPageStatus.ACTIVE));
	}

	@Test
	public void shouldFindActivePagesByType()
	{
		final CatalogVersionModel catalogVersionStaged = mock(CatalogVersionModel.class);
		doReturn(catalogVersionStaged).when(pageService).getActiveCatalogVersion();

		pageService.findPagesByTypeAndPageStatuses(composedTypeModel, Boolean.TRUE, Arrays.asList(CmsPageStatus.ACTIVE));

		verify(cmsPageDao).findPagesByTypeAndPageStatuses(composedTypeModel, Arrays.asList(catalogVersionStaged), Boolean.TRUE,
				Arrays.asList(CmsPageStatus.ACTIVE));
	}

}
