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
package com.hybris.backoffice.cockpitng.search.builder.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericValueCondition;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.impl.DefaultTypeService;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;


@RunWith(MockitoJUnitRunner.class)
public class GenericConditionQueryBuilderTest
{

	private static final String TYPE_CODE = "Product";
	public static final String DATE_TYPE_ATTRIBUTE = "dateAttribute";
	private final Set<Character> queryBuilderSeparators = Sets.newHashSet(ArrayUtils.toObject(new char[]
		{ ' ', ',', ';', '\t', '\n', '\r' }));

	@Mock
	private DefaultTypeService typeService;
	@Mock
	private ModelService modelService;

	@InjectMocks
	private GenericConditionQueryBuilder queryBuilder;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		queryBuilder.setSeparators(queryBuilderSeparators);
	}

	@Test
	public void testSearchForDateWithEquals()
	{
		SearchAttributeDescriptor attributeDescriptor = mock(SearchAttributeDescriptor.class);
		when(attributeDescriptor.getAttributeName()).thenReturn(DATE_TYPE_ATTRIBUTE);

		SearchQueryData searchQueryData = mock(SearchQueryData.class);
		when(searchQueryData.getSearchType()).thenReturn(TYPE_CODE);
		when(typeService.getAttributeDescriptor(TYPE_CODE, DATE_TYPE_ATTRIBUTE)).thenReturn(mock(AttributeDescriptorModel.class));

		final Date now = new Date();
		final Date midnightOfToday = DateUtils.truncate(now, Calendar.DAY_OF_MONTH);
		final Date midnightOfTomorrow = DateUtils.addDays(midnightOfToday, 1);

		final GenericCondition condition = queryBuilder.createSingleTokenCondition(searchQueryData, attributeDescriptor, now,
				ValueComparisonOperator.EQUALS);

		assertThat(condition instanceof GenericConditionList).isTrue();
		GenericConditionList conditionList = (GenericConditionList)condition;

		assertThat(conditionList.getConditionList()).hasSize(2);
		assertThat(conditionList.getOperator()).isEqualTo(Operator.AND);

		assertThat(conditionList.getConditionList().get(0).getOperator()).isEqualTo(Operator.GREATER_OR_EQUAL);
		assertThat(((GenericValueCondition)conditionList.getConditionList().get(0)).getValue()).isEqualTo(midnightOfToday);

		assertThat(conditionList.getConditionList().get(1).getOperator()).isEqualTo(Operator.LESS);
		assertThat(((GenericValueCondition)conditionList.getConditionList().get(1)).getValue()).isEqualTo(midnightOfTomorrow);
	}

	@Test
	public void testSearchForExactDateWithEquals()
	{
		SearchAttributeDescriptor attributeDescriptor = mock(SearchAttributeDescriptor.class);
		when(attributeDescriptor.getAttributeName()).thenReturn(DATE_TYPE_ATTRIBUTE);
		final HashMap<String, String> editorParameters = new HashMap<>();
		editorParameters.put(GenericConditionQueryBuilder.EDITOR_PARAM_EQUALS_COMPARES_EXACT_DATE, "true");
		when(attributeDescriptor.getEditorParameters()).thenReturn(editorParameters);

		SearchQueryData searchQueryData = mock(SearchQueryData.class);
		when(searchQueryData.getSearchType()).thenReturn(TYPE_CODE);
		when(typeService.getAttributeDescriptor(TYPE_CODE, DATE_TYPE_ATTRIBUTE)).thenReturn(mock(AttributeDescriptorModel.class));

		final Date now = new Date();

		final GenericCondition condition = queryBuilder.createSingleTokenCondition(searchQueryData, attributeDescriptor, now,
				ValueComparisonOperator.EQUALS);

		assertThat(condition instanceof GenericValueCondition).isTrue();
		GenericValueCondition valueCondition = (GenericValueCondition) condition;

		assertThat(valueCondition.getOperator()).isEqualTo(Operator.EQUAL);
		assertThat(valueCondition.getValue()).isEqualTo(now);
	}
}
