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
package de.hybris.platform.acceleratorfacades.component.populators.data;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.cmsfacades.common.populator.impl.DefaultLocalizedPopulator;
import de.hybris.platform.cmsfacades.common.service.ProductCatalogItemModelFinder;
import de.hybris.platform.cmsfacades.data.ProductCarouselComponentData;
import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ProductCarouselComponentReversePopulatorTest
{

	//localized title constants
	private static final String TITLE_EN = "title-EN";
	private static final String TITLE_FR = "title-FR";
	private static final String EN = Locale.ENGLISH.getLanguage();
	private static final String FR = Locale.FRENCH.getLanguage();

	//unique identifier item constants
	private static final String PRODUCT_1_ITEM_ID = "Product1";
	private static final String PRODUCT_2_ITEM_ID = "Product2";
	private static final String CATEGORY_1_ITEM_ID = "Category1";
	private static final String CATEGORY_2_ITEM_ID = "Category2";

	//product model constants
	private static final String PRODUCT_1_CODE = "Product1Code";
	private static final String PRODUCT_2_CODE = "Product2Code";

	//category model constants
	private static final String CATEGORY_1_CODE = "Category1Code";
	private static final String CATEGORY_2_CODE = "Category2Code";

	@Mock
	private ProductCatalogItemModelFinder productCatalogItemModelFinder;
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
	private ProductCarouselComponentReversePopulator populator;

	@InjectMocks
	private final DefaultLocalizedPopulator localizedPopulator = new DefaultLocalizedPopulator();

	private ProductCarouselComponentModel carouselModel;
	private ProductCarouselComponentData carouselDto;

	private List<ProductModel> productList;
	private List<CategoryModel> categoryList;

	List<String> products = Arrays.asList(PRODUCT_1_ITEM_ID, PRODUCT_2_ITEM_ID);
	List<String> categories = Arrays.asList(CATEGORY_1_ITEM_ID, CATEGORY_2_ITEM_ID);

	@Before
	public void setUp()
	{

		carouselModel = new ProductCarouselComponentModel();
		carouselDto = new ProductCarouselComponentData();

		mockTitleLocalization();

		populator.setLocalizedPopulator(localizedPopulator);

	}

	@Test
	public void shouldPopulateLocalizedTitle_AllLanguages()
	{

		populator.populate(carouselDto, carouselModel);

		assertThat(carouselModel.getTitle(Locale.ENGLISH), equalTo(TITLE_EN));
		assertThat(carouselModel.getTitle(Locale.FRENCH), equalTo(TITLE_FR));

	}

	@Test
	public void shouldPopulateLocalizedTitle_SingleLanguage()
	{

		carouselDto.getTitle().remove(FR);

		populator.populate(carouselDto, carouselModel);

		assertThat(carouselModel.getTitle(Locale.ENGLISH), equalTo(TITLE_EN));
		assertThat(carouselModel.getTitle(Locale.FRENCH), equalTo(null));

	}

	@Test
	public void shouldPopulateLocalizedTitle_NoLanguage()
	{

		carouselDto.setTitle(null);

		populator.populate(carouselDto, carouselModel);

		assertThat(carouselModel.getTitle(Locale.ENGLISH), equalTo(null));
		assertThat(carouselModel.getTitle(Locale.FRENCH), equalTo(null));

	}

	@Test
	public void shouldPopulateProducts_NoProducts()
	{

		populator.populate(carouselDto, carouselModel);

		assertTrue(carouselModel.getProducts().isEmpty());
	}

	@Test
	public void shouldPopulateProducts_WithProducts()
	{

		mockItemModelResults();
		carouselDto.setProducts(products);
		when(productCatalogItemModelFinder.getProductsForCompositeKeys(any())).thenReturn(productList);
		when(productCatalogItemModelFinder.getCategoriesForCompositeKeys(any())).thenReturn(categoryList);

		populator.populate(carouselDto, carouselModel);

		assertThat(carouselModel.getProducts(), containsInAnyOrder(productModel1, productModel2));
	}

	@Test
	public void shouldPopulateProducts_NoCategories()
	{

		populator.populate(carouselDto, carouselModel);

		assertTrue(carouselModel.getCategories().isEmpty());
	}

	@Test
	public void shouldPopulateProducts_WithCategories()
	{

		mockItemModelResults();
		carouselDto.setCategories(categories);
		when(productCatalogItemModelFinder.getProductsForCompositeKeys(any())).thenReturn(productList);
		when(productCatalogItemModelFinder.getCategoriesForCompositeKeys(any())).thenReturn(categoryList);

		populator.populate(carouselDto, carouselModel);

		assertThat(carouselModel.getCategories(), containsInAnyOrder(categoryModel1, categoryModel2));
	}

	private void mockTitleLocalization()
	{

		final Map<String, String> title = new HashMap<>();
		title.put(EN, TITLE_EN);
		title.put(FR, TITLE_FR);
		carouselDto.setTitle(title);

		final LanguageData languageEN = new LanguageData();
		languageEN.setIsocode(EN);
		final LanguageData languageFR = new LanguageData();
		languageFR.setIsocode(FR);
		when(languageFacade.getLanguages()).thenReturn(Lists.newArrayList(languageEN, languageFR));
		when(commonI18NService.getLocaleForIsoCode(EN)).thenReturn(ENGLISH);
		when(commonI18NService.getLocaleForIsoCode(FR)).thenReturn(FRENCH);

	}

	private void mockItemModelResults()
	{

		productModel1.setCode(PRODUCT_1_CODE);
		productModel2.setCode(PRODUCT_2_CODE);
		productList = Arrays.asList(productModel1, productModel2);

		categoryModel1.setCode(CATEGORY_1_CODE);
		categoryModel2.setCode(CATEGORY_2_CODE);
		categoryList = Arrays.asList(categoryModel1, categoryModel2);

	}

}
