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
package de.hybris.platform.cms2.cmsitems.service.impl;

import static de.hybris.platform.cms2.cmsitems.service.impl.DefaultFlexibleCMSItemSearchService.ITEM_SEARCH_PARAM_CHECK;
import static de.hybris.platform.cms2.cmsitems.service.impl.DefaultFlexibleCMSItemSearchService.MASK_CHECK;
import static de.hybris.platform.cms2.cmsitems.service.impl.DefaultFlexibleCMSItemSearchService.MASK_QUERY_PARAM;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.cmsitems.service.SortStatementFormatter;
import de.hybris.platform.cms2.data.CMSItemSearchData;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultFlexibleCMSItemSearchServiceTest
{
	private static final String CATALOG_ID = "CATALOG_ID";
	private static final String CATALOG_VERSION = "CATALOG_VERSION";
	private static final String ORDER_BY_NAME = " ORDER BY LOWER({c.name}) ASC";
	private static final String ORDER_BY_NAME_AND_DESCRIPTION = " ORDER BY LOWER({c.name}) ASC, LOWER({c.description}) DESC";
	private static final String TYPECODE = "testTypeCode";

	@InjectMocks
	private DefaultFlexibleCMSItemSearchService flexibleCMSItemsSearchService;

	@Mock
	private CatalogVersionService catalogVersionService;
	@Mock
	private CatalogVersionModel catalogVersionModel;
	@Mock
	private FlexibleSearchService flexibleSearchService;
	@Mock
	private DefaultFlexibleSearchAttributeValueConverter defaultFlexibleSearchAttributeValueConverter;
	@Mock
	private TypeService typeService;
	@Mock
	private SortStatementFormatter itemtypeSortStatementFormatter;
	@Mock
	private SortStatementFormatter stringSortStatementFormatter;
	@Mock
	private DefaultSortStatementFormatter defaultSortStatementFormatter;
	@Mock
	private AttributeDescriptorModel attributeDescriptor;
	@Mock
	private Map<String, List<String>> cmsItemSearchTypeBlacklistMap;

	private List<SortStatementFormatter> sortStatementFormatters;

	@Before
	public void setup()
	{
		when(catalogVersionService.getCatalogVersion(CATALOG_ID, CATALOG_VERSION)).thenReturn(catalogVersionModel);
		when(typeService.getAttributeDescriptor(anyString(), anyString())).thenReturn(attributeDescriptor);

		sortStatementFormatters = Arrays.asList(stringSortStatementFormatter, itemtypeSortStatementFormatter);
		flexibleCMSItemsSearchService.setSortStatementFormatters(sortStatementFormatters);
		flexibleCMSItemsSearchService.setDefaultSortStatementFormatter(defaultSortStatementFormatter);
		flexibleCMSItemsSearchService.setFlexibleSearchAttributeValueConverter(defaultFlexibleSearchAttributeValueConverter);
		flexibleCMSItemsSearchService.setCmsItemSearchTypeBlacklistMap(cmsItemSearchTypeBlacklistMap);
	}

	@Test
	public void testSearchQueryContainsNoMaskAndNoType()
	{
		final String mask = null;
		final String type = null;
		final FlexibleSearchQuery capturedQuery = triggerQuery(mask, type);

		assertNoMaskQuery(capturedQuery, mask);
		assertExpectedValueInQuery(capturedQuery, CMSItemModel._TYPECODE);
	}

	@Test
	public void testSearchQueryContainsMaskAndNoType()
	{
		final String mask = "someMask";
		final String type = null;
		final FlexibleSearchQuery capturedQuery = triggerQuery(mask, type);

		assertMaskQuery(capturedQuery, mask);
		assertExpectedValueInQuery(capturedQuery, CMSItemModel._TYPECODE);
	}

	@Test
	public void testSearchQueryContainsNoMaskAndType()
	{
		final String mask = null;
		final String type = "someType";
		final FlexibleSearchQuery capturedQuery = triggerQuery(mask, type);

		assertNoMaskQuery(capturedQuery, mask);
		assertExpectedValueInQuery(capturedQuery, type);
	}

	@Test
	public void testSearchQueryContainsMaskAndType()
	{
		final String mask = "someMask";
		final String type = "someType";
		final FlexibleSearchQuery capturedQuery = triggerQuery(mask, type);

		assertMaskQuery(capturedQuery, mask);
		assertExpectedValueInQuery(capturedQuery, type);
	}

	@Test
	public void testSearchQueryContainsAdditionalParams()
	{
		final String param1 = "label";
		final String param2 = "title";

		final String mask = null;
		final String type = "someType";
		final Map<String, String> itemSearchParams = new HashMap<>();
		itemSearchParams.put(param1, "123");
		itemSearchParams.put(param2, "456");

		final AttributeDescriptorModel model1 = new AttributeDescriptorModel();
		when(typeService.getAttributeDescriptor(type, param1)).thenReturn(model1);
		when(defaultFlexibleSearchAttributeValueConverter.convert(model1, "123")).thenReturn("123");

		final AttributeDescriptorModel model2 = new AttributeDescriptorModel();
		when(typeService.getAttributeDescriptor(type, param2)).thenReturn(model2);
		when(defaultFlexibleSearchAttributeValueConverter.convert(model2, "456")).thenReturn("456");

		final FlexibleSearchQuery capturedQuery = triggerQuery(mask, type, itemSearchParams);

		assertExpectedValueInQuery(capturedQuery, type);

		itemSearchParams.keySet().forEach(field -> {
			assertThat(capturedQuery.getQueryParameters().keySet(), hasItem(field));
			assertThat(capturedQuery.getQueryParameters().get(field), equalTo(itemSearchParams.get(field)));
			assertThat(capturedQuery.getQuery(), containsString(String.format(ITEM_SEARCH_PARAM_CHECK, field, field)));
		});
	}

	@Test
	public void testSearchQueryContainsSort()
	{
		when(stringSortStatementFormatter.isApplicable(attributeDescriptor)).thenReturn(true);
		when(stringSortStatementFormatter.formatSortStatement(any())).thenReturn("LOWER({c.name})");

		final String type = "someType";

		final FlexibleSearchQuery capturedQuery = triggerQuery(null, type, new HashMap<>());

		assertExpectedValueInQuery(capturedQuery, type);
		assertExpectedValueInQuery(capturedQuery, ORDER_BY_NAME);
	}

	@Test
	public void testSearchQueryNoTypeCodeContainsSort()
	{
		when(stringSortStatementFormatter.isApplicable(attributeDescriptor)).thenReturn(true);
		when(stringSortStatementFormatter.formatSortStatement(any())).thenReturn("LOWER({c.name})");

		final FlexibleSearchQuery capturedQuery = triggerQuery(null, null, new HashMap<>());

		assertExpectedValueInQuery(capturedQuery, CMSItemModel._TYPECODE);
		assertExpectedValueInQuery(capturedQuery, ORDER_BY_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void exceptionIsThrownForMissingSearchParamData()
	{
		flexibleCMSItemsSearchService.findCMSItems(null, new PageableData());
	}

	@Test(expected = IllegalArgumentException.class)
	public void exceptionIsThrownForMissingPagingData()
	{
		flexibleCMSItemsSearchService.findCMSItems(new CMSItemSearchData(), null);
	}

	@Test
	public void testAppendSortNameAndLowerCaseDirection()
	{
		when(stringSortStatementFormatter.isApplicable(attributeDescriptor)).thenReturn(true);
		when(stringSortStatementFormatter.formatSortStatement(any())).thenReturn("LOWER({c.name})");

		final StringBuilder queryBuilder = new StringBuilder();
		flexibleCMSItemsSearchService.appendSort("name:asc", queryBuilder, TYPECODE);

		assertThat(queryBuilder.toString(), equalTo(ORDER_BY_NAME));
	}

	@Test
	public void testAppendSortNameAndUpperCaseDirection()
	{
		when(stringSortStatementFormatter.isApplicable(attributeDescriptor)).thenReturn(true);
		when(stringSortStatementFormatter.formatSortStatement(any())).thenReturn("LOWER({c.name})");

		final StringBuilder queryBuilder = new StringBuilder();
		flexibleCMSItemsSearchService.appendSort("name:ASC", queryBuilder, TYPECODE);

		assertThat(queryBuilder.toString(), equalTo(ORDER_BY_NAME));
	}

	@Test
	public void testAppendSortNameAndInvalidDirection()
	{
		when(stringSortStatementFormatter.isApplicable(attributeDescriptor)).thenReturn(true);
		when(stringSortStatementFormatter.formatSortStatement(any())).thenReturn("LOWER({c.name})");

		final StringBuilder queryBuilder = new StringBuilder();
		flexibleCMSItemsSearchService.appendSort("name:Invalid", queryBuilder, TYPECODE);

		assertThat(queryBuilder.toString(), equalTo(ORDER_BY_NAME));
	}

	@Test
	public void testAppendSortNameWithDefaultSortDirection()
	{
		when(stringSortStatementFormatter.isApplicable(attributeDescriptor)).thenReturn(true);
		when(stringSortStatementFormatter.formatSortStatement(any())).thenReturn("LOWER({c.name})");

		final StringBuilder queryBuilder = new StringBuilder();
		flexibleCMSItemsSearchService.appendSort("name", queryBuilder, TYPECODE);

		assertThat(queryBuilder.toString(), equalTo(ORDER_BY_NAME));
	}

	@Test
	public void testAppendSortNameAndDescription()
	{
		when(stringSortStatementFormatter.isApplicable(attributeDescriptor)).thenReturn(true).thenReturn(true);
		when(stringSortStatementFormatter.formatSortStatement(any())).thenReturn("LOWER({c.name})")
				.thenReturn("LOWER({c.description})");

		final StringBuilder queryBuilder = new StringBuilder();
		flexibleCMSItemsSearchService.appendSort("name:ASC,description:DESC", queryBuilder, TYPECODE);

		assertThat(queryBuilder.toString(), equalTo(ORDER_BY_NAME_AND_DESCRIPTION));
	}

	@Test
	public void testNotAppendTypeExclusions()
	{
		when(cmsItemSearchTypeBlacklistMap.containsKey(anyString())).thenReturn(Boolean.FALSE);

		final StringBuilder queryBuilder = new StringBuilder();
		final HashMap<String, Object> queryParameters = new HashMap<>();
		flexibleCMSItemsSearchService.appendTypeExclusions(TYPECODE, queryBuilder, queryParameters);

		verify(cmsItemSearchTypeBlacklistMap, times(0)).get(AbstractPageModel._TYPECODE);
		assertThat(queryBuilder.toString(), isEmptyString());
		assertThat(queryParameters.values(), empty());
	}

	protected FlexibleSearchQuery triggerQuery(final String mask, final String typeCode,
			final Map<String, String> itemSearchParams)
	{
		final CMSItemSearchData cmsItemSearchData = new CMSItemSearchData();
		cmsItemSearchData.setCatalogId(CATALOG_ID);
		cmsItemSearchData.setCatalogVersion(CATALOG_VERSION);
		cmsItemSearchData.setMask(mask);
		cmsItemSearchData.setTypeCode(typeCode);
		cmsItemSearchData.setItemSearchParams(itemSearchParams);

		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(0);
		pageableData.setPageSize(5);
		pageableData.setSort("name:ASC");

		flexibleCMSItemsSearchService.findCMSItems(cmsItemSearchData, pageableData);

		// capture query
		final ArgumentCaptor<FlexibleSearchQuery> args = ArgumentCaptor.forClass(FlexibleSearchQuery.class);
		verify(flexibleSearchService).search(args.capture());

		return args.getValue();
	}

	protected FlexibleSearchQuery triggerQuery(final String mask, final String typeCode)
	{
		return triggerQuery(mask, typeCode, null);
	}

	protected void assertMaskQuery(final FlexibleSearchQuery flexibleSearchQuery, final String expectedMask)
	{
		assertThat(flexibleSearchQuery.getQueryParameters().keySet(), hasItem(MASK_QUERY_PARAM));
		assertThat(flexibleSearchQuery.getQueryParameters().get(MASK_QUERY_PARAM), equalTo("%" + expectedMask + "%"));
		assertThat(flexibleSearchQuery.getQuery(), containsString(MASK_CHECK));
	}

	protected void assertNoMaskQuery(final FlexibleSearchQuery flexibleSearchQuery, final String expectedMask)
	{
		assertThat(flexibleSearchQuery.getQueryParameters().keySet(), not(hasItem(MASK_QUERY_PARAM)));
		assertThat(flexibleSearchQuery.getQueryParameters().get(MASK_QUERY_PARAM), not(equalTo("%" + expectedMask + "%")));
		assertThat(flexibleSearchQuery.getQuery(), not(containsString(MASK_CHECK)));
	}

	protected void assertExpectedValueInQuery(final FlexibleSearchQuery flexibleSearchQuery, final String expectedValue)
	{
		assertThat(expectedValue, notNullValue());
		assertThat(flexibleSearchQuery.getQuery(), containsString(expectedValue));
	}
}
