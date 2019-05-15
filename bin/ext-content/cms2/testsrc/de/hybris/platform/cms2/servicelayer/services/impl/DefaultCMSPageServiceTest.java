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
package de.hybris.platform.cms2.servicelayer.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.*;
import de.hybris.platform.cms2.servicelayer.daos.CMSContentSlotDao;
import de.hybris.platform.cms2.servicelayer.daos.CMSPageDao;
import de.hybris.platform.cms2.servicelayer.data.CMSDataFactory;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.CMSRestrictionService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static de.hybris.platform.cms2.servicelayer.services.impl.AbstractCMSService.CURRENTCATALOGVERSION;
import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@UnitTest
public class DefaultCMSPageServiceTest
{
	@InjectMocks
	private DefaultCMSPageService cmsPageService;
	@Mock
	private CMSContentSlotDao cmsContentSlotDaoMock;
	@Mock
	private PageTemplateModel pageTemplateModelMock;
	@Mock
	private AbstractPageModel pageModelMock;
	@Mock
	private CMSPageDao cmsPageDao;
	@Mock
	private CatalogVersionService catalogVersionService;
	@Mock
	private CMSRestrictionService cmsRestrictionService;
	@Mock
	private TypeService typeService;
	@Mock
	private CMSDataFactory cmsDataFactory;
	@Mock
	private ProductService productService;
	@Mock
	private CategoryService categoryService;
	@Mock
	private SessionService sessionService;


	private String PAGE_LABEL = "fakeLabel";
	private String PAGE_ID = "fakeId";
	private String PRODUCT_CODE = "fakeProductCode";
	private String CATEGORY_NAME = "fakeCategoryName";
	private String CATEGORY_CODE = "fakeCategoryCode";

	@Mock
	private CatalogVersionModel catalogVersionModel1;
	@Mock
	private CatalogVersionModel catalogVersionModel2;
	@Mock
	private ContentPageModel contentPageModel1;
	@Mock
	private ContentPageModel contentPageModel2;
	@Mock
	private ProductPageModel productPageModel1;
	@Mock
	private ProductPageModel productPageModel2;
	@Mock
	private CategoryPageModel categoryPageModel1;
	@Mock
	private CategoryPageModel categoryPageModel2;
	@Mock
	private ProductModel productModel;
	@Mock
	private CategoryModel categoryModel;
	@Mock
	private RestrictionData restrictionData;
	@Mock
	private CatalogVersionModel catalogVersionModel;

	@Mock
	private ComposedTypeModel composedTypeModel;

	private Collection<CatalogVersionModel> sessionCatalogVersions = asList(catalogVersionModel1, catalogVersionModel2);

	@Before
	public void setUp() throws Exception
	{
		cmsPageService = new DefaultCMSPageService();
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSPageService#getContentSlotsForPageTemplate(de.hybris.platform.cms2.model.pages.PageTemplateModel)}
	 * .
	 */
	@Test
	public void testShouldCallCmsContentSlotDaoAndFindAllContentPagesByCatalogVersion()
	{
		// given
		given(cmsContentSlotDaoMock.findAllContentSlotRelationsForPageTemplate(pageTemplateModelMock)).willReturn(
				Collections.EMPTY_LIST);

		// when
		cmsPageService.getContentSlotsForPageTemplate(pageTemplateModelMock);

		verify(cmsContentSlotDaoMock, times(1)).findAllContentSlotRelationsForPageTemplate(pageTemplateModelMock);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSPageService#getContentSlotsForPageTemplate(de.hybris.platform.cms2.model.pages.PageTemplateModel)}
	 * .
	 */
	@Test
	public void testShouldCallCmsContentSlotDaoAndGetOwnContentSlotsForPage()
	{
		// given
		given(cmsContentSlotDaoMock.findAllContentSlotRelationsByPage(pageModelMock)).willReturn(Collections.EMPTY_LIST);

		// when
		cmsPageService.getOwnContentSlotsForPage(pageModelMock);

		verify(cmsContentSlotDaoMock, times(1)).findAllContentSlotRelationsByPage(pageModelMock);
	}

	@Test
	public void testShouldReturnFrontendTemplateName()
	{
		// given
		given(pageTemplateModelMock.getFrontendTemplateName()).willReturn("FooBar");

		// when
		final String frontendTemplateName = cmsPageService.getFrontendTemplateName(pageTemplateModelMock);

		// then
		assertThat(frontendTemplateName).isEqualTo("FooBar");
		verify(pageTemplateModelMock, times(2)).getFrontendTemplateName();
	}

	@Test
	public void testShouldReturnThePageByLabelWhenOnlyOnePageWithoutRestrictionsIsFound() throws Exception
	{
		// given
		when(catalogVersionService.getSessionCatalogVersions()).thenReturn(sessionCatalogVersions);
		when(cmsPageDao.findPagesByLabelAndPageStatuses(PAGE_LABEL, sessionCatalogVersions, asList(CmsPageStatus.ACTIVE)))
				.thenReturn(asList(contentPageModel1));
		when(contentPageModel1.getRestrictions()).thenReturn(Collections.EMPTY_LIST);

		// when
		ContentPageModel resultContentPageModel = cmsPageService.getPageForLabel(PAGE_LABEL);

		// then
		assertThat(contentPageModel1).isEqualTo(resultContentPageModel);
	}

	@Test
	public void testShouldReturnLastPageByLabelWhenThereAreMoreThenOnePage() throws Exception
	{
		// given
		Collection<AbstractPageModel> pages = asList(contentPageModel1, contentPageModel2);

		when(catalogVersionService.getSessionCatalogVersions()).thenReturn(sessionCatalogVersions);
		when(cmsPageDao.findPagesByLabelAndPageStatuses(PAGE_LABEL, sessionCatalogVersions, asList(CmsPageStatus.ACTIVE)))
				.thenReturn(pages);
		when(cmsRestrictionService.evaluatePages(pages, null)).thenReturn(pages);

		// when
		ContentPageModel contentPageModel = cmsPageService.getPageForLabel(PAGE_LABEL);

		// then
		assertThat(contentPageModel).isEqualTo(contentPageModel2);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void testShouldThrowExceptionWhenNoPageWithLabelFound() throws Exception
	{
		// given
		Collection<AbstractPageModel> pages = asList(contentPageModel1, contentPageModel2);

		when(catalogVersionService.getSessionCatalogVersions()).thenReturn(sessionCatalogVersions);
		when(cmsPageDao.findPagesByLabelAndPageStatuses(PAGE_LABEL, sessionCatalogVersions, asList(CmsPageStatus.ACTIVE)))
				.thenReturn(pages);
		when(cmsRestrictionService.evaluatePages(pages, null)).thenReturn(Collections.EMPTY_LIST);

		// when
		cmsPageService.getPageForLabel(PAGE_LABEL);
	}

	@Test
	public void testShouldReturnFirstPageByIdWhenPageByLabelHasNotBeenFound() throws Exception
	{
		// given
		List<AbstractPageModel> pages = asList(contentPageModel1, contentPageModel2);
		when(catalogVersionService.getSessionCatalogVersions()).thenReturn(sessionCatalogVersions);
		when(cmsPageDao.findPagesByLabelAndPageStatuses(PAGE_LABEL, sessionCatalogVersions, asList(CmsPageStatus.ACTIVE)))
				.thenReturn(pages);
		when(cmsRestrictionService.evaluatePages(pages, null)).thenReturn(Collections.EMPTY_LIST);
		when(cmsPageDao.findPagesByIdAndPageStatuses(PAGE_ID, sessionCatalogVersions, asList(CmsPageStatus.ACTIVE)))
				.thenReturn(pages);

		// when
		ContentPageModel contentPageModel = cmsPageService.getPageForLabelOrId(PAGE_ID);

		// then
		assertThat(contentPageModel).isEqualTo(contentPageModel1);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void testShouldThrowExceptionWhenNoPageWithIdFound() throws Exception
	{
		// given
		List<AbstractPageModel> pages = asList(contentPageModel1, contentPageModel2);
		when(catalogVersionService.getSessionCatalogVersions()).thenReturn(sessionCatalogVersions);
		when(cmsPageDao.findPagesByLabelAndPageStatuses(PAGE_LABEL, sessionCatalogVersions, asList(CmsPageStatus.ACTIVE)))
				.thenReturn(pages);
		when(cmsRestrictionService.evaluatePages(pages, null)).thenReturn(Collections.EMPTY_LIST);
		when(cmsPageDao.findPagesByIdAndPageStatuses(PAGE_ID, sessionCatalogVersions, asList(CmsPageStatus.ACTIVE)))
				.thenReturn(Collections.EMPTY_LIST);

		// when
		cmsPageService.getPageForLabelOrId(PAGE_ID);
	}

	@Test
	public void testShouldReturnPageForProductWhenOnlyOnePageWithoutRestrictionsFound() throws Exception
	{
		// given
		when(typeService.getComposedTypeForCode(ProductPageModel._TYPECODE)).thenReturn(composedTypeModel);
		when(catalogVersionService.getSessionCatalogVersions()).thenReturn(sessionCatalogVersions);
		when(cmsPageDao.findAllPagesByTypeAndCatalogVersionsAndPageStatuses(composedTypeModel, sessionCatalogVersions,
				asList(CmsPageStatus.ACTIVE))).thenReturn(asList(productPageModel1));
		when(productModel.getCode()).thenReturn(PRODUCT_CODE);

		// when
		ProductPageModel productPageModel = cmsPageService.getPageForProduct(productModel);

		// then
		assertThat(productPageModel).isEqualTo(productPageModel1);
	}

	@Test
	public void testShouldReturnLastProductPageByProductWhenThereAreMoreThenOnePage() throws Exception
	{
		// given
		List<AbstractPageModel> pages = asList(productPageModel1, productPageModel2);
		when(typeService.getComposedTypeForCode(ProductPageModel._TYPECODE)).thenReturn(composedTypeModel);
		when(catalogVersionService.getSessionCatalogVersions()).thenReturn(sessionCatalogVersions);
		when(cmsPageDao.findAllPagesByTypeAndCatalogVersionsAndPageStatuses(composedTypeModel, sessionCatalogVersions,
				asList(CmsPageStatus.ACTIVE))).thenReturn(asList(productPageModel1, productPageModel2));
		when(productModel.getCode()).thenReturn(PRODUCT_CODE);
		when(cmsDataFactory.createRestrictionData(productModel)).thenReturn(restrictionData);
		when(cmsPageDao.findAllPagesByTypeAndCatalogVersionsAndPageStatuses(composedTypeModel, sessionCatalogVersions,
				asList(CmsPageStatus.ACTIVE))).thenReturn(pages);
		when(cmsRestrictionService.evaluatePages(pages, restrictionData)).thenReturn(pages);

		// when
		ProductPageModel productPageModel = cmsPageService.getPageForProduct(productModel);

		// then
		assertThat(productPageModel).isEqualTo(productPageModel2);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void testShouldThrowExceptionWhenNoProductPageFound() throws Exception
	{
		// given
		List<AbstractPageModel> pages = asList(productPageModel1, productPageModel2);
		when(typeService.getComposedTypeForCode(ProductPageModel._TYPECODE)).thenReturn(composedTypeModel);
		when(catalogVersionService.getSessionCatalogVersions()).thenReturn(sessionCatalogVersions);
		when(cmsPageDao.findAllPagesByTypeAndCatalogVersionsAndPageStatuses(composedTypeModel, sessionCatalogVersions,
				asList(CmsPageStatus.ACTIVE))).thenReturn(asList(productPageModel1, productPageModel2));
		when(productModel.getCode()).thenReturn(PRODUCT_CODE);
		when(cmsDataFactory.createRestrictionData(productModel)).thenReturn(restrictionData);
		when(cmsPageDao.findAllPagesByTypeAndCatalogVersionsAndPageStatuses(composedTypeModel, sessionCatalogVersions,
				asList(CmsPageStatus.ACTIVE))).thenReturn(pages);
		when(cmsRestrictionService.evaluatePages(pages, restrictionData)).thenReturn(Collections.EMPTY_LIST);

		// when
		cmsPageService.getPageForProduct(productModel);
	}

	@Test
	public void shouldReturnProductPageByProductCode() throws Exception
	{
		// given
		when(productService.getProductForCode(PRODUCT_CODE)).thenReturn(productModel);

		List<AbstractPageModel> pages = asList(productPageModel1, productPageModel2);
		when(typeService.getComposedTypeForCode(ProductPageModel._TYPECODE)).thenReturn(composedTypeModel);
		when(catalogVersionService.getSessionCatalogVersions()).thenReturn(sessionCatalogVersions);
		when(cmsPageDao.findAllPagesByTypeAndCatalogVersionsAndPageStatuses(composedTypeModel, sessionCatalogVersions,
				asList(CmsPageStatus.ACTIVE))).thenReturn(asList(productPageModel1, productPageModel2));
		when(productModel.getCode()).thenReturn(PRODUCT_CODE);
		when(cmsDataFactory.createRestrictionData(productModel)).thenReturn(restrictionData);
		when(cmsPageDao.findAllPagesByTypeAndCatalogVersionsAndPageStatuses(composedTypeModel, sessionCatalogVersions,
				asList(CmsPageStatus.ACTIVE))).thenReturn(pages);
		when(cmsRestrictionService.evaluatePages(pages, restrictionData)).thenReturn(pages);

		// when
		ProductPageModel productPageModel = cmsPageService.getPageForProductCode(PRODUCT_CODE);

		// then
		assertThat(productPageModel).isEqualTo(productPageModel2);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldThrowExceptionWhenProductCodeDoesNotExists() throws Exception
	{
		// given
		when(productService.getProductForCode(PRODUCT_CODE)).thenThrow(new UnknownIdentifierException("fakeMessage"));

		// when
		cmsPageService.getPageForProductCode(PRODUCT_CODE);
	}

	@Test
	public void shouldReturnPageForCategoryWhenOnlyOnePageWithoutRestrictionsFound() throws Exception
	{
		// given
		when(typeService.getComposedTypeForCode(CategoryPageModel._TYPECODE)).thenReturn(composedTypeModel);
		when(catalogVersionService.getSessionCatalogVersions()).thenReturn(sessionCatalogVersions);
		when(cmsPageDao.findAllPagesByTypeAndCatalogVersionsAndPageStatuses(composedTypeModel, sessionCatalogVersions,
				asList(CmsPageStatus.ACTIVE))).thenReturn(asList(categoryPageModel1));
		when(categoryModel.getName()).thenReturn(CATEGORY_NAME);

		// when
		CategoryPageModel categoryPageModel = cmsPageService.getPageForCategory(categoryModel);

		// then
		assertThat(categoryPageModel).isEqualTo(categoryPageModel1);
	}

	@Test
	public void shouldReturnLastCategoryPageByCategoryWhenThereAreMoreThenOnePage() throws Exception
	{
		// given
		List<AbstractPageModel> pages = asList(categoryPageModel1, categoryPageModel2);
		when(typeService.getComposedTypeForCode(CategoryPageModel._TYPECODE)).thenReturn(composedTypeModel);
		when(catalogVersionService.getSessionCatalogVersions()).thenReturn(sessionCatalogVersions);
		when(cmsPageDao.findAllPagesByTypeAndCatalogVersionsAndPageStatuses(composedTypeModel, sessionCatalogVersions,
				asList(CmsPageStatus.ACTIVE))).thenReturn(pages);
		when(categoryModel.getName()).thenReturn(CATEGORY_NAME);
		when(cmsDataFactory.createRestrictionData(categoryModel)).thenReturn(restrictionData);
		when(cmsPageDao.findAllPagesByTypeAndCatalogVersionsAndPageStatuses(composedTypeModel, sessionCatalogVersions,
				asList(CmsPageStatus.ACTIVE))).thenReturn(pages);
		when(cmsRestrictionService.evaluatePages(pages, restrictionData)).thenReturn(pages);

		// when
		CategoryPageModel categoryPageModel = cmsPageService.getPageForCategory(categoryModel);

		// then
		assertThat(categoryPageModel).isEqualTo(categoryPageModel2);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldThrowExceptionWhenNoCategoryPageFound() throws Exception
	{
		// given
		List<AbstractPageModel> pages = asList(categoryPageModel1, categoryPageModel2);
		when(typeService.getComposedTypeForCode(CategoryPageModel._TYPECODE)).thenReturn(composedTypeModel);
		when(catalogVersionService.getSessionCatalogVersions()).thenReturn(sessionCatalogVersions);
		when(cmsPageDao.findAllPagesByTypeAndCatalogVersionsAndPageStatuses(composedTypeModel, sessionCatalogVersions,
				asList(CmsPageStatus.ACTIVE))).thenReturn(pages);
		when(categoryModel.getName()).thenReturn(CATEGORY_NAME);
		when(cmsDataFactory.createRestrictionData(categoryModel)).thenReturn(restrictionData);
		when(cmsPageDao.findAllPagesByTypeAndCatalogVersionsAndPageStatuses(composedTypeModel, sessionCatalogVersions,
				asList(CmsPageStatus.ACTIVE))).thenReturn(pages);
		when(cmsRestrictionService.evaluatePages(pages, restrictionData)).thenReturn(Collections.EMPTY_LIST);

		// when
		cmsPageService.getPageForCategory(categoryModel);
	}

	@Test
	public void shouldReturnCategoryPageByCategoryCode() throws Exception
	{
		// given
		when(sessionService.getAttribute(CURRENTCATALOGVERSION)).thenReturn(catalogVersionModel);
		when(categoryService.getCategoryForCode(catalogVersionModel, CATEGORY_CODE)).thenReturn(categoryModel);

		List<AbstractPageModel> pages = asList(categoryPageModel1, categoryPageModel2);
		when(typeService.getComposedTypeForCode(CategoryPageModel._TYPECODE)).thenReturn(composedTypeModel);
		when(catalogVersionService.getSessionCatalogVersions()).thenReturn(sessionCatalogVersions);
		when(cmsPageDao.findAllPagesByTypeAndCatalogVersionsAndPageStatuses(composedTypeModel, sessionCatalogVersions,
				asList(CmsPageStatus.ACTIVE))).thenReturn(pages);
		when(categoryModel.getName()).thenReturn(CATEGORY_NAME);
		when(cmsDataFactory.createRestrictionData(categoryModel)).thenReturn(restrictionData);
		when(cmsPageDao.findAllPagesByTypeAndCatalogVersionsAndPageStatuses(composedTypeModel, sessionCatalogVersions,
				asList(CmsPageStatus.ACTIVE))).thenReturn(pages);
		when(cmsRestrictionService.evaluatePages(pages, restrictionData)).thenReturn(pages);

		// when
		CategoryPageModel categoryPageModel = cmsPageService.getPageForCategoryCode(CATEGORY_CODE);

		// then
		assertThat(categoryPageModel).isEqualTo(categoryPageModel2);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldThrowExceptionWhenCategoryPageIsNotFoundByCode() throws Exception
	{
		// given
		when(sessionService.getAttribute(CURRENTCATALOGVERSION)).thenReturn(catalogVersionModel);
		when(categoryService.getCategoryForCode(catalogVersionModel, CATEGORY_CODE))
				.thenThrow(new UnknownIdentifierException("fakeMessage"));

		// when
		cmsPageService.getPageForCategoryCode(CATEGORY_CODE);
	}
}
