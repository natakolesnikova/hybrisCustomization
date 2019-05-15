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

import static org.fest.assertions.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSRestrictionService;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultCMSRestrictionServiceIntegrationTest extends ServicelayerTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private CMSRestrictionService cmsRestrictionService;

	private CMSCategoryRestrictionModel restriction;
	private ContentPageModel homepageGlobal;
	private ContentPageModel homepageRegion;
	private ContentPageModel homepageLocal;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		final Collection<CategoryModel> categories = new ArrayList<CategoryModel>();
		categories.add(getCategoryByCode("testCategory0"));
		categories.add(getCategoryByCode("testCategory1"));
		categories.add(getCategoryByCode("testCategory2"));

		restriction = modelService.create(CMSCategoryRestrictionModel.class);
		restriction.setCategories(categories);
		restriction.setName("FooBar");
		restriction.setUid("FooBar");
		modelService.save(restriction);
	}

	public void multiCountrySetUp() throws ImpExException
	{
		importCsv("/test/cmsMultiCountryTestData.csv", "UTF-8");

		homepageGlobal = flexibleSearchService.<ContentPageModel> search("SELECT {pk} FROM {ContentPage} WHERE {uid} = ?uid",
				Collections.singletonMap("uid", "TestHomePageGlobal")).getResult().iterator().next();

		homepageRegion = flexibleSearchService.<ContentPageModel> search("SELECT {pk} FROM {ContentPage} WHERE {uid} = ?uid",
				Collections.singletonMap("uid", "TestHomePageRegionEU")).getResult().iterator().next();

		homepageLocal = flexibleSearchService.<ContentPageModel> search("SELECT {pk} FROM {ContentPage} WHERE {uid} = ?uid",
				Collections.singletonMap("uid", "TestHomePageLocalIT")).getResult().iterator().next();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSRestrictionService#getCategoryCodesForRestriction(de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel)}
	 * .
	 */
	@Test
	public void testGetCategoryCodesForRestriction()
	{
		final Collection<String> categoryCodes = cmsRestrictionService.getCategoryCodesForRestriction(restriction);
		assertThat(categoryCodes).hasSize(3);
		assertThat(categoryCodes).contains("testCategory0", "testCategory1", "testCategory2");
	}

	protected CategoryModel getCategoryByCode(final String code)
	{
		final CategoryModel example = new CategoryModel();
		example.setCode(code);

		return flexibleSearchService.getModelByExample(example);
	}

	@Test
	public void shouldFindGlobalPage() throws ImpExException
	{
		multiCountrySetUp();

		final AbstractPageModel[] data = new AbstractPageModel[]
		{ homepageGlobal };
		final Collection<AbstractPageModel> pages = Arrays.asList(data);

		final Collection<AbstractPageModel> evaluatePages = cmsRestrictionService.evaluatePages(pages, null);

		assertThat(evaluatePages, hasSize(1));
	}

	@Test
	public void shouldFindRegionPage() throws ImpExException
	{
		multiCountrySetUp();

		final AbstractPageModel[] data = new AbstractPageModel[]
		{ homepageRegion };
		final Collection<AbstractPageModel> pages = Arrays.asList(data);

		final Collection<AbstractPageModel> evaluatePages = cmsRestrictionService.evaluatePages(pages, null);

		assertThat(evaluatePages, hasSize(1));
	}

	@Test
	public void shouldFindLocalPage() throws ImpExException
	{
		multiCountrySetUp();

		final AbstractPageModel[] data = new AbstractPageModel[]
		{ homepageLocal };
		final Collection<AbstractPageModel> pages = Arrays.asList(data);

		final Collection<AbstractPageModel> evaluatePages = cmsRestrictionService.evaluatePages(pages, null);

		assertThat(evaluatePages, hasSize(1));
	}
}
