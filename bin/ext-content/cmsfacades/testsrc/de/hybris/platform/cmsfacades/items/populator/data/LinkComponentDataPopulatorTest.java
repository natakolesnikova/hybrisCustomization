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
package de.hybris.platform.cmsfacades.items.populator.data;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.enums.LinkTargets;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.common.populator.impl.DefaultLocalizedPopulator;
import de.hybris.platform.cmsfacades.data.CMSLinkComponentData;
import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LinkComponentDataPopulatorTest
{
	private static final String EN = Locale.ENGLISH.getLanguage();
	private static final String FR = Locale.FRENCH.getLanguage();

	private static final String LINKNAME_EN = "linkName-EN";
	private static final String LINKNAME_FR = "linkName-FR";
	private static final String URL_LINK = "http://help.hybris.com";
	private static final String UUID = "composite-key-hashed-uuid";

	@Mock
	private CMSAdminSiteService cmsAdminSiteService;
	@Mock
	private CatalogVersionModel catalogVersion;
	@Mock
	private LanguageFacade languageFacade;
	@Mock
	private CommonI18NService commonI18NService;
	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@InjectMocks
	private DefaultLocalizedPopulator localizedPopulator;
	@InjectMocks
	private LinkComponentDataPopulator populator;

	@Mock
	private ProductModel product;
	@Mock
	private CategoryModel category;
	@Mock
	private ContentPageModel contentPage;

	private CMSLinkComponentData linkComponentData;
	private CMSLinkComponentModel linkComponentModel;

	@Before
	public void setUp()
	{
		linkComponentModel = new CMSLinkComponentModel();
		linkComponentData = new CMSLinkComponentData();
		linkComponentData.setTarget(true);

		final Map<String, String> linkNames = new HashMap<>();
		linkNames.put(EN, LINKNAME_EN);
		linkNames.put(FR, LINKNAME_FR);
		linkComponentData.setLinkName(linkNames);

		final LanguageData languageEN = new LanguageData();
		languageEN.setIsocode(EN);
		final LanguageData languageFR = new LanguageData();
		languageFR.setIsocode(FR);

		when(cmsAdminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersion);
		when(languageFacade.getLanguages()).thenReturn(Lists.newArrayList(languageEN, languageFR));
		when(commonI18NService.getLocaleForIsoCode(EN)).thenReturn(ENGLISH);
		when(commonI18NService.getLocaleForIsoCode(FR)).thenReturn(FRENCH);

		populator.setLocalizedPopulator(localizedPopulator);
	}

	@Test
	public void shouldPopulateExternalLinkAttributes()
	{
		linkComponentData.setUrl(URL_LINK);

		populator.populate(linkComponentData, linkComponentModel);

		assertThat(linkComponentModel.isExternal(), is(Boolean.TRUE));
		assertThat(linkComponentModel.getUrl(), is(URL_LINK));
		assertThat(linkComponentModel.getTarget(), is(LinkTargets.NEWWINDOW));
	}

	@Test
	public void shouldPopulateCategoryLinkAttributes()
	{
		linkComponentData.setCategory(UUID);
		when(uniqueItemIdentifierService.getItemModel(UUID, CategoryModel.class)).thenReturn(Optional.of(category));

		populator.populate(linkComponentData, linkComponentModel);

		verify(uniqueItemIdentifierService).getItemModel(UUID, CategoryModel.class);
		assertThat(linkComponentModel.getCategory(), is(category));
		assertThat(linkComponentModel.getContentPage(), nullValue());
		assertThat(linkComponentModel.getProduct(), nullValue());
		assertThat(linkComponentModel.isExternal(), is(Boolean.FALSE));
		assertThat(linkComponentModel.getUrl(), nullValue());
		assertThat(linkComponentModel.getTarget(), is(LinkTargets.NEWWINDOW));
	}

	@Test
	public void shouldPopulateContentPageLinkAttributes()
	{
		linkComponentData.setContentPage(UUID);
		when(uniqueItemIdentifierService.getItemModel(UUID, ContentPageModel.class)).thenReturn(Optional.of(contentPage));

		populator.populate(linkComponentData, linkComponentModel);

		verify(uniqueItemIdentifierService).getItemModel(UUID, ContentPageModel.class);
		assertThat(linkComponentModel.getCategory(), nullValue());
		assertThat(linkComponentModel.getContentPage(), is(contentPage));
		assertThat(linkComponentModel.getProduct(), nullValue());
		assertThat(linkComponentModel.isExternal(), is(Boolean.FALSE));
		assertThat(linkComponentModel.getUrl(), nullValue());
		assertThat(linkComponentModel.getTarget(), is(LinkTargets.NEWWINDOW));
	}

	@Test
	public void shouldPopulateProductLinkAttributes()
	{
		linkComponentData.setProduct(UUID);
		when(uniqueItemIdentifierService.getItemModel(UUID, ProductModel.class)).thenReturn(Optional.of(product));

		populator.populate(linkComponentData, linkComponentModel);

		verify(uniqueItemIdentifierService).getItemModel(UUID, ProductModel.class);
		assertThat(linkComponentModel.getCategory(), nullValue());
		assertThat(linkComponentModel.getContentPage(), nullValue());
		assertThat(linkComponentModel.getProduct(), is(product));
		assertThat(linkComponentModel.isExternal(), is(Boolean.FALSE));
		assertThat(linkComponentModel.getUrl(), nullValue());
		assertThat(linkComponentModel.getTarget(), is(LinkTargets.NEWWINDOW));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_AllLanguages()
	{
		populator.populate(linkComponentData, linkComponentModel);

		assertThat(linkComponentModel.getLinkName(Locale.ENGLISH), is(LINKNAME_EN));
		assertThat(linkComponentModel.getLinkName(Locale.FRENCH), is(LINKNAME_FR));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_SingleLanguages()
	{
		linkComponentData.getLinkName().remove(FR);

		populator.populate(linkComponentData, linkComponentModel);

		assertThat(linkComponentModel.getLinkName(Locale.ENGLISH), is(LINKNAME_EN));
		assertThat(linkComponentModel.getLinkName(Locale.FRENCH), nullValue());
	}

	@Test
	public void shouldNotPopulateLocalizedAttributes_NullMaps()
	{
		linkComponentData.setLinkName(null);

		populator.populate(linkComponentData, linkComponentModel);

		assertThat(linkComponentModel.getLinkName(Locale.ENGLISH), nullValue());
		assertThat(linkComponentModel.getLinkName(Locale.FRENCH), nullValue());
	}

	@Test
	public void shouldDefaultExternalValue()
	{
		linkComponentData.setExternal(null);

		populator.populate(linkComponentData, linkComponentModel);

		assertThat(linkComponentModel.isExternal(), is(Boolean.FALSE));
	}

}
