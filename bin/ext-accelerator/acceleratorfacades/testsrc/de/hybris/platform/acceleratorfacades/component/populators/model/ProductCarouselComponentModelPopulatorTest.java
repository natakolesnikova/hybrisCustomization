/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.acceleratorfacades.component.populators.model;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.cmsfacades.common.populator.impl.DefaultLocalizedPopulator;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.data.ProductCarouselComponentData;
import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
public class ProductCarouselComponentModelPopulatorTest
{

	//localized title constants
	private static final String TITLE_EN = "title-EN";
	private static final String TITLE_FR = "title-FR";
	private static final String EN = Locale.ENGLISH.getLanguage();
	private static final String FR = Locale.FRENCH.getLanguage();

	//product carousel id
	private final String PRODUCT_CAROUSEL_ID = "product-carousel-uid";

	//unique identifier item constants
	private static final String PRODUCT_1_ITEM_ID = "Product1";
	private static final String PRODUCT_2_ITEM_ID = "Product2";
	private static final String CATEGORY_1_ITEM_ID = "Category1";
	private static final String CATEGORY_2_ITEM_ID = "Category2";

	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Mock
	private LanguageFacade languageFacade;
	@Mock
	private CommonI18NService commonI18NService;

	@Mock
	private ProductModel productModel1;
	@Mock
	private ProductModel productModel2;

	@Mock
	private CategoryModel categoryModel1;
	@Mock
	private CategoryModel categoryModel2;


	@InjectMocks
	private ProductCarouselComponentModelPopulator populator;

	@InjectMocks
	private final DefaultLocalizedPopulator localizedPopulator = new DefaultLocalizedPopulator();

	@Mock
	private ProductCarouselComponentModel productCarouselComponentModel;

	private ProductCarouselComponentData productCarouselComponentData;

	private SearchResult<ProductModel> productModel;
	private SearchResult<CategoryModel> categoryModel;

	private final ItemData product1data = mock(ItemData.class);
	private final ItemData product2data = mock(ItemData.class);
	private final ItemData category1data = mock(ItemData.class);
	private final ItemData category2data = mock(ItemData.class);

	private final Optional<ItemData> optionalProduct1Data = Optional.of(product1data);
	private final Optional<ItemData> optionalProduct2Data = Optional.of(product2data);
	private final Optional<ItemData> optionalCategory1Data = Optional.of(category1data);
	private final Optional<ItemData> optionalCategory2Data = Optional.of(category2data);

	List<String> products = Arrays.asList(PRODUCT_1_ITEM_ID, PRODUCT_2_ITEM_ID);
	List<String> categories = Arrays.asList(CATEGORY_1_ITEM_ID, CATEGORY_2_ITEM_ID);

	@Before
	public void setUp()
	{

		productCarouselComponentData = new ProductCarouselComponentData();

		when(productCarouselComponentModel.getTitle(Locale.ENGLISH)).thenReturn(TITLE_EN);
		when(productCarouselComponentModel.getTitle(Locale.FRENCH)).thenReturn(TITLE_FR);
		when(productCarouselComponentModel.getUid()).thenReturn(PRODUCT_CAROUSEL_ID);

		// mock localizedPopulator for language mocks
		mockLocalization();

		// mock search results from ProductCarouselSearchService
		mockProductCarouselSearchResults();

		//mock getItemData of UniqueItemIdentifierService
		mockGenerateUniqueIdentifier();

	}

	@Test
	public void shouldPopulateLocalizedTitle()
	{

		populator.populate(productCarouselComponentModel, productCarouselComponentData);

		assertThat(productCarouselComponentData.getTitle().get(EN), equalTo(TITLE_EN));
		assertThat(productCarouselComponentData.getTitle().get(FR), equalTo(TITLE_FR));
	}

	@Test
	public void shouldPopulateProducts()
	{

		populator.populate(productCarouselComponentModel, productCarouselComponentData);

		assertTrue(productCarouselComponentData.getProducts().containsAll(products));
	}

	@Test
	public void shouldPopulateCategories()
	{

		populator.populate(productCarouselComponentModel, productCarouselComponentData);

		assertTrue(productCarouselComponentData.getCategories().containsAll(categories));
	}

	private void mockLocalization()
	{

		populator.setLocalizedPopulator(localizedPopulator);

		final LanguageData languageEN = new LanguageData();
		languageEN.setIsocode(EN);
		final LanguageData languageFR = new LanguageData();
		languageFR.setIsocode(FR);
		when(languageFacade.getLanguages()).thenReturn(Lists.newArrayList(languageEN, languageFR));
		when(commonI18NService.getLocaleForIsoCode(EN)).thenReturn(ENGLISH);
		when(commonI18NService.getLocaleForIsoCode(FR)).thenReturn(FRENCH);
	}

	private void mockProductCarouselSearchResults()
	{

		final List<ProductModel> productList = Arrays.asList(productModel1, productModel2);
		productModel = new SearchResultImpl<>(productList, 10, 2, 0);

		final List<CategoryModel> categoryList = Arrays.asList(categoryModel1, categoryModel2);
		categoryModel = new SearchResultImpl<>(categoryList, 10, 2, 0);

		final CatalogVersionModel catalogVersion = mock(CatalogVersionModel.class);
		when(productCarouselComponentModel.getCatalogVersion()).thenReturn(catalogVersion);
		when(productCarouselComponentModel.getProducts()).thenReturn(productList);
		when(productCarouselComponentModel.getCategories()).thenReturn(categoryList);
	}

	private void mockGenerateUniqueIdentifier()
	{

		// itemData mocks
		when(product1data.getItemId()).thenReturn(PRODUCT_1_ITEM_ID);
		when(product2data.getItemId()).thenReturn(PRODUCT_2_ITEM_ID);
		when(category1data.getItemId()).thenReturn(CATEGORY_1_ITEM_ID);
		when(category2data.getItemId()).thenReturn(CATEGORY_2_ITEM_ID);

		when(uniqueItemIdentifierService.getItemData(productModel1)).thenReturn(optionalProduct1Data);
		when(uniqueItemIdentifierService.getItemData(productModel2)).thenReturn(optionalProduct2Data);
		when(uniqueItemIdentifierService.getItemData(categoryModel1)).thenReturn(optionalCategory1Data);
		when(uniqueItemIdentifierService.getItemData(categoryModel2)).thenReturn(optionalCategory2Data);

	}

}
