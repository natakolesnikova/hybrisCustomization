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
package de.hybris.platform.cmsfacades.util.models;

import static de.hybris.platform.cmsfacades.util.models.MediaModelMother.MediaTemplate.THUMBNAIL;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.model.pages.ProductPageModel;
import de.hybris.platform.cmsfacades.util.builder.ProductPageModelBuilder;
import de.hybris.platform.cmsfacades.util.dao.impl.ProductPageDao;

import org.springframework.beans.factory.annotation.Required;


public class ProductPageModelMother extends AbstractModelMother<ProductPageModel>
{

	public static final String UID_PRODUCT_PAGE = "uid-product-detail-page";
	public static final String UID_DEFAULT_PRODUCT_PAGE = "uid-default-product-detail-page";
	public static final String NAME_PRODUCT_PAGE = "Product Detail";

	private ProductPageDao productPageDao;
	private PageTemplateModelMother pageTemplateModelMother;
	private MediaModelMother mediaModelMother;

	public ProductPageModel ProductPage(final CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> getProductPageDao().getByUidAndCatalogVersion(UID_PRODUCT_PAGE, catalogVersion), //
				() -> ProductPageModelBuilder.aModel() //
						.withUid(UID_PRODUCT_PAGE) //
						.withCatalogVersion(catalogVersion) //
						.withMasterTemplate(pageTemplateModelMother.SearchPage_Template(catalogVersion)) //
						.withDefaultPage(Boolean.FALSE) //
						.withName(NAME_PRODUCT_PAGE) //
						.withApprovalStatus(CmsApprovalStatus.APPROVED) //
						.withThumbnail(mediaModelMother.createMediaModel(catalogVersion, THUMBNAIL)).build());
	}

	public ProductPageModel DefaultProductPage(final CatalogVersionModel catalogVersion, final CmsPageStatus pageStatus)
	{
		return getOrSaveAndReturn( //
				() -> getProductPageDao().getByUidAndCatalogVersion(UID_DEFAULT_PRODUCT_PAGE, catalogVersion), //
				() -> ProductPageModelBuilder.aModel() //
						.withUid(UID_DEFAULT_PRODUCT_PAGE) //
						.withCatalogVersion(catalogVersion) //
						.withMasterTemplate(pageTemplateModelMother.SearchPage_Template(catalogVersion)) //
						.withDefaultPage(Boolean.TRUE) //
						.withName(NAME_PRODUCT_PAGE) //
						.withApprovalStatus(CmsApprovalStatus.APPROVED) //
						.withPageStatus(pageStatus)
						.withThumbnail(mediaModelMother.createMediaModel(catalogVersion, THUMBNAIL)).build());
	}

	public ProductPageModel DefaultProductPage(final CatalogVersionModel catalogVersion)
	{
		return this.DefaultProductPage(catalogVersion, CmsPageStatus.ACTIVE);
	}

	public ProductPageDao getProductPageDao()
	{
		return productPageDao;
	}

	@Required
	public void setProductPageDao(final ProductPageDao productPageDao)
	{
		this.productPageDao = productPageDao;
	}

	public PageTemplateModelMother getPageTemplateModelMother()
	{
		return pageTemplateModelMother;
	}

	@Required
	public void setPageTemplateModelMother(final PageTemplateModelMother pageTemplateModelMother)
	{
		this.pageTemplateModelMother = pageTemplateModelMother;
	}

	public MediaModelMother getMediaModelMother()
	{
		return mediaModelMother;
	}

	@Required
	public void setMediaModelMother(final MediaModelMother mediaModelMother)
	{
		this.mediaModelMother = mediaModelMother;
	}
}
