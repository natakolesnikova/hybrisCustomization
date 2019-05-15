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
package de.hybris.platform.cmsfacades.items.populator.model;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.enums.LinkTargets;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmsfacades.common.populator.impl.DefaultLocalizedPopulator;
import de.hybris.platform.cmsfacades.data.CMSLinkComponentData;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CmsLinkComponentModelPopulatorTest
{
	private static final String EN = Locale.ENGLISH.getLanguage();
	private static final String FR = Locale.FRENCH.getLanguage();

	private static final String LINKNAME_EN = "linkName-EN";
	private static final String LINKNAME_FR = "linkName-FR";
	private static final String URL_LINK = "http://help.hybris.com";
	private static final String UUID = "composite-key-hashed-uuid";

	@Mock
	private LanguageFacade languageFacade;
	@Mock
	private CommonI18NService commonI18NService;
	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Mock
	private CMSLinkComponentModel linkComponentModel;
	@Mock
	private ProductModel product;
	@Mock
	private CategoryModel category;
	@Mock
	private ContentPageModel contentPage;

	@InjectMocks
	private DefaultLocalizedPopulator localizedPopulator;
	@InjectMocks
	private CmsLinkComponentModelPopulator populator;

	private CMSLinkComponentData linkComponentData;

	@Before
	public void setUp()
	{
		linkComponentData = new CMSLinkComponentData();
		linkComponentData.setLinkName(new HashMap<>());

		when(linkComponentModel.getLinkName(Locale.ENGLISH)).thenReturn(LINKNAME_EN);
		when(linkComponentModel.getLinkName(Locale.FRENCH)).thenReturn(LINKNAME_FR);
		when(linkComponentModel.getTarget()).thenReturn(LinkTargets.NEWWINDOW);

		final LanguageData languageEN = new LanguageData();
		languageEN.setIsocode(EN);
		final LanguageData languageFR = new LanguageData();
		languageFR.setIsocode(FR);
		when(languageFacade.getLanguages()).thenReturn(Arrays.asList(languageEN, languageFR));
		when(commonI18NService.getLocaleForIsoCode(EN)).thenReturn(ENGLISH);
		when(commonI18NService.getLocaleForIsoCode(FR)).thenReturn(FRENCH);

		populator.setLocalizedPopulator(localizedPopulator);
	}

	protected void setUpExternalLinkComponentMock()
	{
		when(linkComponentModel.isExternal()).thenReturn(Boolean.TRUE);
		when(linkComponentModel.getUrl()).thenReturn(URL_LINK);
	}

	protected void setUpProductLinkComponentMock()
	{
		when(linkComponentModel.isExternal()).thenReturn(Boolean.FALSE);
		when(linkComponentModel.getProduct()).thenReturn(product);

		final ItemData itemData = new ItemData();
		itemData.setItemId(UUID);
		itemData.setItemType(ProductModel._TYPECODE);
		when(uniqueItemIdentifierService.getItemData(product)).thenReturn(Optional.of(itemData));
	}

	protected void setUpCategoryLinkComponentMock()
	{
		when(linkComponentModel.isExternal()).thenReturn(Boolean.FALSE);
		when(linkComponentModel.getCategory()).thenReturn(category);

		final ItemData itemData = new ItemData();
		itemData.setItemId(UUID);
		itemData.setItemType(CategoryModel._TYPECODE);
		when(uniqueItemIdentifierService.getItemData(category)).thenReturn(Optional.of(itemData));
	}

	protected void setUpContentPageLinkComponentMock()
	{
		when(linkComponentModel.isExternal()).thenReturn(Boolean.FALSE);
		when(linkComponentModel.getContentPage()).thenReturn(contentPage);

		when(contentPage.getUid()).thenReturn(UUID);
	}

	@Test
	public void shouldPopulateExternalLinkAttributes()
	{
		setUpExternalLinkComponentMock();

		populator.populate(linkComponentModel, linkComponentData);

		assertThat(linkComponentData.getCategory(), nullValue());
		assertThat(linkComponentData.getContentPage(), nullValue());
		assertThat(linkComponentData.getProduct(), nullValue());
		assertThat(linkComponentData.getUrl(), is(URL_LINK));
		assertThat(linkComponentData.getExternal(), is(Boolean.TRUE));
		assertThat(linkComponentData.isTarget(), is(Boolean.TRUE));
	}

	@Test
	public void shouldPopulateCategoryLinkAttributes()
	{
		setUpCategoryLinkComponentMock();

		populator.populate(linkComponentModel, linkComponentData);

		verify(uniqueItemIdentifierService).getItemData(category);
		assertThat(linkComponentData.getCategory(), equalTo(UUID));
		assertThat(linkComponentData.getContentPage(), nullValue());
		assertThat(linkComponentData.getProduct(), nullValue());
		assertThat(linkComponentData.getUrl(), nullValue());
		assertThat(linkComponentData.getExternal(), is(Boolean.FALSE));
		assertThat(linkComponentData.isTarget(), is(Boolean.TRUE));
	}

	@Test
	public void shouldPopulateContentPageLinkAttributes()
	{
		setUpContentPageLinkComponentMock();

		populator.populate(linkComponentModel, linkComponentData);

		assertThat(linkComponentData.getCategory(), nullValue());
		assertThat(linkComponentData.getContentPage(), equalTo(UUID));
		assertThat(linkComponentData.getProduct(), nullValue());
		assertThat(linkComponentData.getUrl(), nullValue());
		assertThat(linkComponentData.getExternal(), is(Boolean.FALSE));
		assertThat(linkComponentData.isTarget(), is(Boolean.TRUE));
	}

	@Test
	public void shouldPopulateProductLinkAttributes()
	{
		setUpProductLinkComponentMock();

		populator.populate(linkComponentModel, linkComponentData);

		verify(uniqueItemIdentifierService).getItemData(product);
		assertThat(linkComponentData.getCategory(), nullValue());
		assertThat(linkComponentData.getContentPage(), nullValue());
		assertThat(linkComponentData.getProduct(), equalTo(UUID));
		assertThat(linkComponentData.getUrl(), nullValue());
		assertThat(linkComponentData.getExternal(), is(Boolean.FALSE));
		assertThat(linkComponentData.isTarget(), is(Boolean.TRUE));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_NullMaps()
	{
		linkComponentData.setLinkName(null);

		populator.populate(linkComponentModel, linkComponentData);

		assertThat(linkComponentData.getLinkName(), //
				allOf(hasEntry(EN, LINKNAME_EN), //
						hasEntry(FR, LINKNAME_FR)));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_AllLanguages()
	{
		populator.populate(linkComponentModel, linkComponentData);

		assertThat(linkComponentData.getLinkName(), //
				allOf(hasEntry(EN, LINKNAME_EN), //
						hasEntry(FR, LINKNAME_FR)));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_SingleLanguages()
	{
		when(linkComponentModel.getLinkName(Locale.FRENCH)).thenReturn(null);

		populator.populate(linkComponentModel, linkComponentData);

		assertThat(linkComponentData.getLinkName(), //
				allOf(hasEntry(EN, LINKNAME_EN), //
						hasEntry(FR, null)));
	}

}