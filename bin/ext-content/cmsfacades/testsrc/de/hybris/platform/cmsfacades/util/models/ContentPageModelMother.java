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
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmsfacades.util.builder.ContentPageModelBuilder;
import de.hybris.platform.cmsfacades.util.dao.impl.ContentPageDao;

import org.springframework.beans.factory.annotation.Required;


public class ContentPageModelMother extends AbstractModelMother<ContentPageModel>
{

	public static final String UID_HOMEPAGE = "uid-homepage";
	public static final String UID_HOMEPAGE_EU = "uid-homepage-eu";
	public static final String UID_SEARCHPAGE = "uid-searchpage";
	public static final String UID_DEFAULT_HOMEPAGE = "uid-default-homepage";
	public static final String UID_DEFAULT_SEARCHPAGE = "uid-default-searchpage";
	public static final String NAME_SUFFIX = "_page";
	public static final String TITLE_SUFFIX = "_pagetitle";
	public static final String LABEL_HOMEPAGE = "/homepage";
	public static final String LABEL_SEARCHPAGE = "/searchpage";
	public static final String NAME_HOMEPAGE = "Home";
	public static final String NAME_HOMEPAGE_EU = "Home-Europe";
	public static final String NAME_SEARCHPAGE = "Search";

	private ContentPageDao contentPageDao;
	private PageTemplateModelMother pageTemplateModelMother;
	private MediaModelMother mediaModelMother;

	public ContentPageModel HomePage(final CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> getContentPageDao().getByUidAndCatalogVersion(UID_HOMEPAGE, catalogVersion), //
				() -> ContentPageModelBuilder.aModel() //
						.withUid(UID_HOMEPAGE) //
						.withCatalogVersion(catalogVersion) //
						.asHomepage().withMasterTemplate(pageTemplateModelMother.HomePage_Template(catalogVersion)) //
						.withDefaultPage(Boolean.FALSE) //
						.withLabel(LABEL_HOMEPAGE) //
						.withName(NAME_HOMEPAGE) //
						.withEnglishTitle(NAME_HOMEPAGE + TITLE_SUFFIX)
						.withPageStatus(CmsPageStatus.ACTIVE) //
						.withApprovalStatus(CmsApprovalStatus.APPROVED) //
						.withThumbnail(mediaModelMother.createMediaModel(catalogVersion, THUMBNAIL)).build());
	}

	public ContentPageModel HomePageEurope(final CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> getContentPageDao().getByUidAndCatalogVersion(UID_HOMEPAGE_EU, catalogVersion), //
				() -> ContentPageModelBuilder.aModel() //
						.withUid(UID_HOMEPAGE_EU) //
						.withCatalogVersion(catalogVersion) //
						.asHomepage().withMasterTemplate(pageTemplateModelMother.HomePage_Template(catalogVersion)) //
						.withDefaultPage(Boolean.FALSE) //
						.withLabel(LABEL_HOMEPAGE) //
						.withName(NAME_HOMEPAGE_EU) //
						.withApprovalStatus(CmsApprovalStatus.APPROVED) //
						.withThumbnail(mediaModelMother.createMediaModel(catalogVersion, THUMBNAIL)).build());
	}

	public ContentPageModel DefaultHomePage(final CatalogVersionModel catalogVersion, final CmsPageStatus pageStatus)
	{
		return getOrSaveAndReturn( //
				() -> getContentPageDao().getByUidAndCatalogVersion(UID_DEFAULT_HOMEPAGE, catalogVersion), //
				() -> ContentPageModelBuilder.aModel() //
						.withUid(UID_DEFAULT_HOMEPAGE) //
						.withCatalogVersion(catalogVersion) //
						.asHomepage().withMasterTemplate(pageTemplateModelMother.HomePage_Template(catalogVersion)) //
						.withDefaultPage(Boolean.TRUE) //
						.withLabel(LABEL_HOMEPAGE) //
						.withName(NAME_HOMEPAGE) //
						.withApprovalStatus(CmsApprovalStatus.APPROVED) //
						.withPageStatus(pageStatus) //
						.withThumbnail(mediaModelMother.createMediaModel(catalogVersion, THUMBNAIL)).build());
	}

	public ContentPageModel DefaultHomePage(final CatalogVersionModel catalogVersion)
	{
		return this.DefaultHomePage(catalogVersion, CmsPageStatus.ACTIVE);
	}

	public ContentPageModel SearchPageFromHomePageTemplate(final CatalogVersionModel catalogVersion,
			final CmsPageStatus pageStatus)
	{
		return getOrSaveAndReturn( //
				() -> getContentPageDao().getByUidAndCatalogVersion(UID_SEARCHPAGE, catalogVersion), //
				() -> ContentPageModelBuilder.aModel() //
						.withUid(UID_SEARCHPAGE) //
						.withCatalogVersion(catalogVersion) //
						.withMasterTemplate(pageTemplateModelMother.HomePage_Template(catalogVersion)) //
						.withThumbnail(mediaModelMother.createMediaModel(catalogVersion, THUMBNAIL)) //
						.withDefaultPage(Boolean.FALSE) //
						.withLabel(LABEL_SEARCHPAGE) //
						.withPageStatus(pageStatus) //
						.withName(NAME_SEARCHPAGE).build());
	}

	public ContentPageModel SearchPageFromHomePageTemplate(final CatalogVersionModel catalogVersion)
	{
		return this.SearchPageFromHomePageTemplate(catalogVersion, CmsPageStatus.ACTIVE);
	}


	public ContentPageModel DefaultSearchPageFromHomePageTemplate(final CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> getContentPageDao().getByUidAndCatalogVersion(UID_DEFAULT_SEARCHPAGE, catalogVersion), //
				() -> ContentPageModelBuilder.aModel() //
						.withUid(UID_DEFAULT_SEARCHPAGE) //
						.withCatalogVersion(catalogVersion) //
						.withMasterTemplate(pageTemplateModelMother.HomePage_Template(catalogVersion)) //
						.withThumbnail(mediaModelMother.createMediaModel(catalogVersion, THUMBNAIL)) //
						.withDefaultPage(Boolean.TRUE) //
						.withLabel(LABEL_SEARCHPAGE) //
						.withName(NAME_SEARCHPAGE).build());
	}

	public ContentPageModel SearchPage(final CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> getContentPageDao().getByUidAndCatalogVersion(UID_SEARCHPAGE, catalogVersion), //
				() -> ContentPageModelBuilder.aModel() //
						.withUid(UID_SEARCHPAGE) //
						.withCatalogVersion(catalogVersion) //
						.asHomepage().withMasterTemplate(pageTemplateModelMother.SearchPage_Template(catalogVersion)) //
						.withDefaultPage(Boolean.FALSE) //
						.withLabel(LABEL_SEARCHPAGE) //
						.withName(NAME_SEARCHPAGE) //
						.withApprovalStatus(CmsApprovalStatus.APPROVED) //
						.withPageStatus(CmsPageStatus.ACTIVE)
						.withThumbnail(mediaModelMother.createMediaModel(catalogVersion, THUMBNAIL)).build());
	}

	public ContentPageModel somePage(final CatalogVersionModel catalogVersion, final String uid, final String nameTitle,
			final CmsPageStatus pageStatus)
	{
		return getOrSaveAndReturn( //
				() -> getContentPageDao().getByUidAndCatalogVersion(uid, catalogVersion), //
				() -> ContentPageModelBuilder.aModel() //
						.withUid(uid) //
						.withCatalogVersion(catalogVersion) //
						.withMasterTemplate(pageTemplateModelMother.HomePage_Template(catalogVersion)) //
						.withThumbnail(mediaModelMother.createMediaModel(catalogVersion, THUMBNAIL))
						.withName(nameTitle + NAME_SUFFIX) //
						.withEnglishTitle(nameTitle + TITLE_SUFFIX) //
						.withPageStatus(pageStatus) //
						.withLabel("/" + nameTitle) //
						.withOnlyOneRestrictionMustApply(Boolean.TRUE).build());
	}

	public ContentPageModel somePage(final CatalogVersionModel catalogVersion, final String uid, final String nameTitle)
	{
		return this.somePage(catalogVersion, uid, nameTitle, CmsPageStatus.ACTIVE);
	}

	public ContentPageDao getContentPageDao()
	{
		return contentPageDao;
	}

	@Required
	public void setContentPageDao(final ContentPageDao contentPageDao)
	{
		this.contentPageDao = contentPageDao;
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
