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
package com.hybris.backoffice.excel.importing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.template.ExcelTemplateService;
import com.hybris.backoffice.excel.translators.ExcelTranslatorRegistry;
import com.hybris.backoffice.excel.translators.ExcelValueTranslator;


@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelImportServiceTest
{

	private static final String PRODUCT_TYPE_CODE = ProductModel._TYPECODE;
	private static final String CATEGORY_TYPE_CODE = CategoryModel._TYPECODE;

	private static final String CATALOG_KEY = "catalog";
	private static final String VERSION_KEY = "version";
	private static final String CATALOG_CLOTHING_VALUE = "Clothing";
	private static final String CATALOG_DEFAULT_VALUE = "Default";
	private static final String VERSION_ONLINE_VALUE = "Online";
	private static final String VERSION_STAGED_VALUE = "Staged";
	private static final String APPROVAL_STATUS_APPROVED = "Approved";
	private static final String APPROVAL_STATUS_CHECK = "Check";
	private static final String FIRST_PRODUCT_NAME = "Jeans";
	private static final String SECOND_PRODUCT_NAME = "Blue Jeans";
	private static final String CATEGORY_NAME = "Blue";
	private static final String CATEGORY_VERSION = "version(code, catalog(id))";
	private static final String REFERENCE_VALUE = "productRef";
	private static final Integer FIRST_ROW_INDEX = 0;
	private static final Integer SECOND_ROW_INDEX = 1;

	private static final String CATALOG_VERSION_PATTERN = "%s:%s";
	private static final String CATALOG_VERSION_FORMAT = String.format(CATALOG_VERSION_PATTERN, CATALOG_KEY, VERSION_KEY);

	private static final ImpexHeaderValue PRODUCT_HEADER_NAME = new ImpexHeaderValue("name", false);
	private static final ImpexHeaderValue PRODUCT_HEADER_APPROVAL_STATUS = new ImpexHeaderValue("approvalStatus", false);
	private static final ImpexHeaderValue CATEGORY_HEADER_CODE = new ImpexHeaderValue("code", false);
	private static final ImpexHeaderValue CATEGORY_HEADER_VERSION = new ImpexHeaderValue("version(code, catalog(id))", false);

	private static final String EMPTY_CELL_VALUE = "";
	private static final String CATEGORY_KEY = "category";

	@Mock
	private ExcelTemplateService excelTemplateService;

	@Mock
	private ExcelTranslatorRegistry excelTranslatorRegistry;

	@Spy
	@InjectMocks
	private DefaultExcelImportService excelImportService;

	@Test
	public void shouldFindDefaultValuesWhenCellIsNull()
	{
		// given
		final SelectedAttribute selectedAttribute = new SelectedAttribute();

		// when
		final Map<String, String> defaultValues = selectedAttribute.findDefaultValues();

		// then
		assertThat(defaultValues).isNotNull();
		assertThat(defaultValues.keySet()).isEmpty();
	}

	@Test
	public void shouldFindDefaultValuesWhenCellValueIsEmpty()
	{
		// given
		final SelectedAttribute selectedAttribute = new SelectedAttribute();
		selectedAttribute.setReferenceFormat("");

		// when
		final Map<String, String> defaultValues = selectedAttribute.findDefaultValues();

		// then
		assertThat(defaultValues).isNotNull();
		assertThat(defaultValues.keySet()).isEmpty();
	}

	@Test
	public void shouldFindDefaultValuesWhenOnlyPatternIsProvided()
	{
		// given
		final SelectedAttribute selectedAttribute = new SelectedAttribute();
		selectedAttribute.setReferenceFormat(CATALOG_VERSION_FORMAT);

		// when
		final Map<String, String> defaultValues = selectedAttribute.findDefaultValues();

		// then
		assertThat(defaultValues).isNotNull();
		assertThat(defaultValues.keySet()).contains(CATALOG_KEY, VERSION_KEY);
	}

	@Test
	public void shouldFindDefaultValuesWhenFirstPatternHasDefault()
	{
		// given
		final SelectedAttribute selectedAttribute = new SelectedAttribute();
		selectedAttribute.setReferenceFormat(String.format(CATALOG_VERSION_PATTERN, CATALOG_KEY, VERSION_KEY));
		selectedAttribute.setDefaultValues(CATALOG_CLOTHING_VALUE);

		// when
		final Map<String, String> defaultValues = selectedAttribute.findDefaultValues();

		// then
		assertThat(defaultValues).isNotNull();
		assertThat(defaultValues.keySet()).contains(CATALOG_KEY, VERSION_KEY);
		assertThat(defaultValues.values()).contains(CATALOG_CLOTHING_VALUE, null);
	}

	@Test
	public void shouldFindDefaultValuesWhenSecondPatternHasDefault()
	{
		// given
		final SelectedAttribute selectedAttribute = new SelectedAttribute();
		selectedAttribute.setReferenceFormat(String.format(CATALOG_VERSION_PATTERN, CATALOG_KEY, VERSION_KEY));
		selectedAttribute.setDefaultValues(String.format(":%s", VERSION_ONLINE_VALUE));

		// when
		final Map<String, String> defaultValues = selectedAttribute.findDefaultValues();

		// then
		assertThat(defaultValues).isNotNull();
		assertThat(defaultValues.keySet()).contains(CATALOG_KEY, VERSION_KEY);
		assertThat(defaultValues.values()).contains(null, VERSION_ONLINE_VALUE);
	}


	@Test
	public void shouldFindDefaultValuesWhenBothPatternsHaveDefaults()
	{
		// given
		final SelectedAttribute selectedAttribute = new SelectedAttribute();
		selectedAttribute.setReferenceFormat(CATALOG_VERSION_FORMAT);
		selectedAttribute.setDefaultValues(String.format(CATALOG_VERSION_PATTERN, CATALOG_CLOTHING_VALUE, VERSION_ONLINE_VALUE));

		// when
		final Map<String, String> defaultValues = selectedAttribute.findDefaultValues();

		// then
		assertThat(defaultValues).isNotNull();
		assertThat(defaultValues.keySet()).contains(CATALOG_KEY, VERSION_KEY);
		assertThat(defaultValues.values()).contains(CATALOG_CLOTHING_VALUE, VERSION_ONLINE_VALUE);
	}


	@Test
	public void shouldPrepareImportParametersForEmptyCellWithoutDefaults()
	{
		// given
		final SelectedAttribute selectedAttribute = new SelectedAttribute();

		// when
		final ImportParameters importParameters = excelImportService.findImportParameters(selectedAttribute, EMPTY_CELL_VALUE,
				PRODUCT_TYPE_CODE, REFERENCE_VALUE);

		// then
		assertThat(importParameters).isNotNull();
		assertThat(importParameters.getCellValue()).isEqualTo(EMPTY_CELL_VALUE);
		assertThat(importParameters.getMultiValueParameters()).isEmpty();
	}

	@Test
	public void shouldPrepareImportParametersForEmptyCellWithDefaults()
	{
		// given
		final SelectedAttribute selectedAttribute = new SelectedAttribute();
		selectedAttribute.setReferenceFormat(CATALOG_VERSION_FORMAT);
		selectedAttribute.setDefaultValues(String.format(CATALOG_VERSION_PATTERN, CATALOG_CLOTHING_VALUE, VERSION_ONLINE_VALUE));

		// when
		final ImportParameters importParameters = excelImportService.findImportParameters(selectedAttribute, EMPTY_CELL_VALUE,
				PRODUCT_TYPE_CODE, REFERENCE_VALUE);

		// then
		assertThat(importParameters).isNotNull();
		assertThat(importParameters.getCellValue()).isEqualTo(EMPTY_CELL_VALUE);
		assertThat(importParameters.getMultiValueParameters()).hasSize(1);
		assertThat(importParameters.getSingleValueParameters().keySet()).containsSequence(CATALOG_KEY, VERSION_KEY);
		assertThat(importParameters.getSingleValueParameters().values()).containsSequence(CATALOG_CLOTHING_VALUE,
				VERSION_ONLINE_VALUE);
	}

	@Test
	public void shouldPrepareImportParametersForNotReferenceCellWithoutDefaults()
	{
		// given
		final SelectedAttribute selectedAttribute = new SelectedAttribute();

		// when
		final ImportParameters importParameters = excelImportService.findImportParameters(selectedAttribute,
				APPROVAL_STATUS_APPROVED, PRODUCT_TYPE_CODE, REFERENCE_VALUE);

		// then
		assertThat(importParameters).isNotNull();
		assertThat(importParameters.getCellValue()).isEqualTo(APPROVAL_STATUS_APPROVED);
		assertThat(importParameters.getMultiValueParameters()).isEmpty();
	}

	@Test
	public void shouldPrepareImportParametersForReferenceCellWithoutDefaults()
	{
		// given
		final SelectedAttribute selectedAttribute = new SelectedAttribute();
		final String cellValue = String.format(CATALOG_VERSION_PATTERN, CATALOG_CLOTHING_VALUE, VERSION_ONLINE_VALUE);
		selectedAttribute.setReferenceFormat(CATALOG_VERSION_FORMAT);

		// when
		final ImportParameters importParameters = excelImportService.findImportParameters(selectedAttribute, cellValue,
				PRODUCT_TYPE_CODE, REFERENCE_VALUE);

		// then
		assertThat(importParameters).isNotNull();
		assertThat(importParameters.getCellValue()).isEqualTo(cellValue);
		assertThat(importParameters.getMultiValueParameters()).hasSize(1);
		assertThat(importParameters.getSingleValueParameters().keySet()).containsSequence(CATALOG_KEY, VERSION_KEY);
		assertThat(importParameters.getSingleValueParameters().values()).containsSequence(CATALOG_CLOTHING_VALUE,
				VERSION_ONLINE_VALUE);
	}

	@Test
	public void shouldPrepareImportParametersForReferenceCellWithDefaultFirstValue()
	{
		// given
		final SelectedAttribute selectedAttribute = new SelectedAttribute();
		final String cellValue = String.format(":%s", VERSION_ONLINE_VALUE);
		selectedAttribute.setReferenceFormat(CATALOG_VERSION_FORMAT);
		selectedAttribute.setDefaultValues(String.format(CATALOG_VERSION_PATTERN, CATALOG_CLOTHING_VALUE, VERSION_ONLINE_VALUE));

		// when
		final ImportParameters importParameters = excelImportService.findImportParameters(selectedAttribute, cellValue,
				PRODUCT_TYPE_CODE, REFERENCE_VALUE);

		// then
		assertThat(importParameters).isNotNull();
		assertThat(importParameters.getCellValue()).isEqualTo(cellValue);
		assertThat(importParameters.getMultiValueParameters()).hasSize(1);
		assertThat(importParameters.getSingleValueParameters().keySet()).containsSequence(CATALOG_KEY, VERSION_KEY);
		assertThat(importParameters.getSingleValueParameters().values()).containsSequence(CATALOG_CLOTHING_VALUE,
				VERSION_ONLINE_VALUE);
	}

	@Test
	public void shouldPrepareImportParametersForReferenceCellWithDefaultSecondValue()
	{
		// given
		final SelectedAttribute selectedAttribute = new SelectedAttribute();
		final String cellValue = String.format("%s", CATALOG_CLOTHING_VALUE);
		selectedAttribute.setReferenceFormat(CATALOG_VERSION_FORMAT);
		selectedAttribute.setDefaultValues(String.format(CATALOG_VERSION_PATTERN, CATALOG_CLOTHING_VALUE, VERSION_ONLINE_VALUE));

		// when
		final ImportParameters importParameters = excelImportService.findImportParameters(selectedAttribute, cellValue,
				PRODUCT_TYPE_CODE, REFERENCE_VALUE);

		// then
		assertThat(importParameters).isNotNull();
		assertThat(importParameters.getCellValue()).isEqualTo(cellValue);
		assertThat(importParameters.getMultiValueParameters()).hasSize(1);
		assertThat(importParameters.getSingleValueParameters().keySet()).containsSequence(CATALOG_KEY, VERSION_KEY);
		assertThat(importParameters.getSingleValueParameters().values()).containsSequence(CATALOG_CLOTHING_VALUE,
				VERSION_ONLINE_VALUE);
	}

	@Test
	public void shouldPrepareImportParametersForReferenceCellWithDefaultBothValues()
	{
		// given
		final SelectedAttribute selectedAttribute = new SelectedAttribute();
		final String cellValue = ":";
		selectedAttribute.setReferenceFormat(CATALOG_VERSION_FORMAT);
		selectedAttribute.setDefaultValues(String.format(CATALOG_VERSION_PATTERN, CATALOG_CLOTHING_VALUE, VERSION_ONLINE_VALUE));

		// when
		final ImportParameters importParameters = excelImportService.findImportParameters(selectedAttribute, cellValue,
				PRODUCT_TYPE_CODE, REFERENCE_VALUE);

		// then
		assertThat(importParameters).isNotNull();
		assertThat(importParameters.getCellValue()).isEqualTo(cellValue);
		assertThat(importParameters.getMultiValueParameters()).hasSize(1);
		assertThat(importParameters.getSingleValueParameters().keySet()).containsSequence(CATALOG_KEY, VERSION_KEY);
		assertThat(importParameters.getSingleValueParameters().values()).containsSequence(CATALOG_CLOTHING_VALUE,
				VERSION_ONLINE_VALUE);
	}

	@Test
	public void shouldPrepareImportParametersForReferenceCellWith()
	{
		// given
		final SelectedAttribute selectedAttribute = new SelectedAttribute();
		final String cellValue = "15:EUR";
		selectedAttribute.setReferenceFormat("price:currency:scale:unit:unitFactor:pricing");
		selectedAttribute.setDefaultValues(":USD:1:piece:3:Gross");

		// when
		final ImportParameters importParameters = excelImportService.findImportParameters(selectedAttribute, cellValue,
				PRODUCT_TYPE_CODE, REFERENCE_VALUE);

		// then
		assertThat(importParameters).isNotNull();
		assertThat(importParameters.getCellValue()).isEqualTo(cellValue);
		assertThat(importParameters.getMultiValueParameters()).hasSize(1);
		assertThat(importParameters.getSingleValueParameters().keySet()).containsSequence("price", "currency", "scale", "unit",
				"unitFactor", "pricing");
		assertThat(importParameters.getSingleValueParameters().values()).containsSequence("15", "EUR", "1", "piece", "3", "Gross");
	}

	@Test
	public void shouldPrepareImportParametersForMultiValue()
	{
		// given
		final SelectedAttribute selectedAttribute = new SelectedAttribute();
		final String cellValue = "Shoes:Online:Clothing,Hats,Jeans::Default,Shirts:Online:Default";
		selectedAttribute.setReferenceFormat("category:version:catalog");
		selectedAttribute.setDefaultValues(":Staged:Clothing");

		// when
		final ImportParameters importParameters = excelImportService.findImportParameters(selectedAttribute, cellValue,
				PRODUCT_TYPE_CODE, REFERENCE_VALUE);

		// then
		assertThat(importParameters).isNotNull();
		assertThat(importParameters.getCellValue()).isEqualTo(cellValue);
		assertThat(importParameters.getMultiValueParameters()).hasSize(4);
		assertThat(importParameters.getMultiValueParameters().get(0).keySet()).containsSequence(CATEGORY_KEY, VERSION_KEY,
				CATALOG_KEY);
		assertThat(importParameters.getMultiValueParameters().get(0).values()).containsSequence("Shoes", VERSION_ONLINE_VALUE,
				CATALOG_CLOTHING_VALUE);

		assertThat(importParameters.getMultiValueParameters().get(1).keySet()).containsSequence(CATEGORY_KEY, VERSION_KEY,
				CATALOG_KEY);
		assertThat(importParameters.getMultiValueParameters().get(1).values()).containsSequence("Hats", VERSION_STAGED_VALUE,
				CATALOG_CLOTHING_VALUE);

		assertThat(importParameters.getMultiValueParameters().get(2).keySet()).containsSequence(CATEGORY_KEY, VERSION_KEY,
				CATALOG_KEY);
		assertThat(importParameters.getMultiValueParameters().get(2).values()).containsSequence("Jeans", VERSION_STAGED_VALUE,
				CATALOG_DEFAULT_VALUE);

		assertThat(importParameters.getMultiValueParameters().get(3).keySet()).containsSequence(CATEGORY_KEY, VERSION_KEY,
				CATALOG_KEY);
		assertThat(importParameters.getMultiValueParameters().get(3).values()).containsSequence("Shirts", VERSION_ONLINE_VALUE,
				CATALOG_DEFAULT_VALUE);
	}

	@Test
	public void shouldMergeImpexWhenMainImpexIsEmpty()
	{
		// given
		final Impex mainImpex = new Impex();
		final Impex subImpex = new Impex();
		final ImpexForType impexForType = subImpex.findUpdates(PRODUCT_TYPE_CODE);
		impexForType.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME, FIRST_PRODUCT_NAME);

		// when
		mainImpex.mergeImpex(subImpex, PRODUCT_TYPE_CODE, FIRST_ROW_INDEX);

		// then
		assertThat(mainImpex.getImpexes()).hasSize(1);
		assertThat(mainImpex.getImpexes().get(0).getTypeCode()).isEqualTo(PRODUCT_TYPE_CODE);
		assertThat(mainImpex.getImpexes().get(0).getImpexTable().rowKeySet().size()).isEqualTo(1);
		assertThat(mainImpex.getImpexes().get(0).getImpexTable().columnKeySet().size()).isEqualTo(1);
		assertThat(mainImpex.getImpexes().get(0).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME)).isEqualTo(
				FIRST_PRODUCT_NAME);
	}

	@Test
	public void shouldMergeImpexForTheSameTypeCodeForExistingRow()
	{
		// given
		final Impex mainImpex = new Impex();
		final ImpexForType mainImpexForType = mainImpex.findUpdates(PRODUCT_TYPE_CODE);
		mainImpexForType.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME, FIRST_PRODUCT_NAME);

		final Impex subImpex = new Impex();
		final ImpexForType impexForType = subImpex.findUpdates(PRODUCT_TYPE_CODE);
		impexForType.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS, APPROVAL_STATUS_APPROVED);

		// when
		mainImpex.mergeImpex(subImpex, PRODUCT_TYPE_CODE, FIRST_ROW_INDEX);

		// then
		assertThat(mainImpex.getImpexes()).hasSize(1);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getTypeCode()).isEqualTo(PRODUCT_TYPE_CODE);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getImpexTable().rowKeySet().size()).isEqualTo(1);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getImpexTable().columnKeySet().size()).isEqualTo(2);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME))
				.isEqualTo(FIRST_PRODUCT_NAME);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS))
				.isEqualTo(APPROVAL_STATUS_APPROVED);
	}

	@Test
	public void shouldMergeImpexForTheSameTypeCodeForNextRow()
	{
		// given
		final Impex mainImpex = new Impex();
		final ImpexForType mainImpexForType = mainImpex.findUpdates(PRODUCT_TYPE_CODE);
		mainImpexForType.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME, FIRST_PRODUCT_NAME);
		mainImpexForType.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS, APPROVAL_STATUS_APPROVED);

		final Impex subImpex = new Impex();
		final ImpexForType impexForType = subImpex.findUpdates(PRODUCT_TYPE_CODE);
		impexForType.putValue(SECOND_ROW_INDEX, PRODUCT_HEADER_NAME, SECOND_PRODUCT_NAME);

		// when
		mainImpex.mergeImpex(subImpex, PRODUCT_TYPE_CODE, SECOND_ROW_INDEX);

		// then
		assertThat(mainImpex.getImpexes()).hasSize(1);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getTypeCode()).isEqualTo(PRODUCT_TYPE_CODE);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getImpexTable().rowKeySet().size()).isEqualTo(2);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getImpexTable().columnKeySet().size()).isEqualTo(2);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME))
				.isEqualTo(FIRST_PRODUCT_NAME);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS))
				.isEqualTo(APPROVAL_STATUS_APPROVED);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getImpexTable().get(SECOND_ROW_INDEX, PRODUCT_HEADER_NAME))
				.isEqualTo(SECOND_PRODUCT_NAME);
	}

	@Test
	public void shouldMergeImpexForDifferentTypeCode()
	{
		// given
		final Impex mainImpex = new Impex();
		final ImpexForType mainProductImpex = mainImpex.findUpdates(PRODUCT_TYPE_CODE);
		mainProductImpex.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME, FIRST_PRODUCT_NAME);
		mainProductImpex.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS, APPROVAL_STATUS_APPROVED);

		final Impex subImpex = new Impex();
		final ImpexForType productImpex = subImpex.findUpdates(PRODUCT_TYPE_CODE);
		productImpex.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME, SECOND_PRODUCT_NAME);
		final ImpexForType categorySubImpex = subImpex.findUpdates(CATEGORY_TYPE_CODE);
		categorySubImpex.putValue(FIRST_ROW_INDEX, CATEGORY_HEADER_CODE, CATEGORY_NAME);
		categorySubImpex.putValue(FIRST_ROW_INDEX, CATEGORY_HEADER_VERSION, CATEGORY_VERSION);

		// when
		mainImpex.mergeImpex(subImpex, PRODUCT_TYPE_CODE, SECOND_ROW_INDEX);

		// then
		assertThat(mainImpex.getImpexes()).hasSize(2);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getTypeCode()).isEqualTo(CATEGORY_TYPE_CODE);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getImpexTable().rowKeySet().size()).isEqualTo(1);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getImpexTable().columnKeySet().size()).isEqualTo(2);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getImpexTable().get(FIRST_ROW_INDEX, CATEGORY_HEADER_CODE)).isEqualTo(
				CATEGORY_NAME);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getImpexTable().get(FIRST_ROW_INDEX, CATEGORY_HEADER_VERSION)).isEqualTo(
				CATEGORY_VERSION);

		assertThat(mainImpex.getImpexes().get(SECOND_ROW_INDEX).getTypeCode()).isEqualTo(PRODUCT_TYPE_CODE);
		assertThat(mainImpex.getImpexes().get(SECOND_ROW_INDEX).getImpexTable().rowKeySet().size()).isEqualTo(2);
		assertThat(mainImpex.getImpexes().get(SECOND_ROW_INDEX).getImpexTable().columnKeySet().size()).isEqualTo(2);
		assertThat(mainImpex.getImpexes().get(SECOND_ROW_INDEX).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME))
				.isEqualTo(FIRST_PRODUCT_NAME);
		assertThat(mainImpex.getImpexes().get(SECOND_ROW_INDEX).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS))
				.isEqualTo(APPROVAL_STATUS_APPROVED);
		assertThat(mainImpex.getImpexes().get(SECOND_ROW_INDEX).getImpexTable().get(SECOND_ROW_INDEX, PRODUCT_HEADER_NAME))
				.isEqualTo(SECOND_PRODUCT_NAME);
	}

	@Test
	public void shouldMergeTwoImpexes()
	{
		// given
		final Impex mainImpex = new Impex();
		final ImpexForType mainProductImpex = mainImpex.findUpdates(PRODUCT_TYPE_CODE);
		mainProductImpex.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME, FIRST_PRODUCT_NAME);
		mainProductImpex.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS, APPROVAL_STATUS_APPROVED);

		final Impex subImpex = new Impex();
		final ImpexForType categoryImpex = subImpex.findUpdates(CATEGORY_TYPE_CODE);
		categoryImpex.putValue(FIRST_ROW_INDEX, CATEGORY_HEADER_CODE, CATEGORY_NAME);
		categoryImpex.putValue(FIRST_ROW_INDEX, CATEGORY_HEADER_VERSION, CATEGORY_VERSION);

		final ImpexForType productSubImpex = subImpex.findUpdates(PRODUCT_TYPE_CODE);
		productSubImpex.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME, SECOND_PRODUCT_NAME);
		productSubImpex.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS, APPROVAL_STATUS_CHECK);

		// when
		mainImpex.mergeImpex(subImpex);

		// then
		assertThat(mainImpex.getImpexes()).hasSize(2);

		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getTypeCode()).isEqualTo(CATEGORY_TYPE_CODE);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getImpexTable().rowKeySet().size()).isEqualTo(1);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getImpexTable().columnKeySet().size()).isEqualTo(2);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getImpexTable().get(FIRST_ROW_INDEX, CATEGORY_HEADER_CODE))
				.isEqualTo(CATEGORY_NAME);
		assertThat(mainImpex.getImpexes().get(FIRST_ROW_INDEX).getImpexTable().get(FIRST_ROW_INDEX, CATEGORY_HEADER_VERSION))
				.isEqualTo(CATEGORY_VERSION);

		assertThat(mainImpex.getImpexes().get(SECOND_ROW_INDEX).getTypeCode()).isEqualTo(PRODUCT_TYPE_CODE);
		assertThat(mainImpex.getImpexes().get(SECOND_ROW_INDEX).getImpexTable().rowKeySet().size()).isEqualTo(2);
		assertThat(mainImpex.getImpexes().get(SECOND_ROW_INDEX).getImpexTable().columnKeySet().size()).isEqualTo(2);
		assertThat(mainImpex.getImpexes().get(SECOND_ROW_INDEX).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME))
				.isEqualTo(FIRST_PRODUCT_NAME);
		assertThat(mainImpex.getImpexes().get(SECOND_ROW_INDEX).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS))
				.isEqualTo(APPROVAL_STATUS_APPROVED);
		assertThat(mainImpex.getImpexes().get(SECOND_ROW_INDEX).getImpexTable().get(SECOND_ROW_INDEX, PRODUCT_HEADER_NAME))
				.isEqualTo(SECOND_PRODUCT_NAME);
		assertThat(
				mainImpex.getImpexes().get(SECOND_ROW_INDEX).getImpexTable().get(SECOND_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS))
				.isEqualTo(APPROVAL_STATUS_CHECK);
	}

	@Test
	public void shouldInvokeTranslatorTwiceForTwoSelectedAttributes()
	{
		// given
		final int lastRowNumber = 3;
		final Sheet typeSystemSheet = mock(Sheet.class);
		final Sheet productSheet = mock(Sheet.class);
		final ExcelValueTranslator translator = mock(ExcelValueTranslator.class);
		final Optional<ExcelValueTranslator> translatorOpt = Optional.of(translator);
		final Row firstRow = mock(Row.class);
		final Cell codeFirstCell = mock(Cell.class);
		final Cell catalogVersionFirstCell = mock(Cell.class);
		final List<SelectedAttribute> selectedAttributes = new ArrayList<>();
		final List<String> documentRefs = Arrays.asList("documentRef1");

		final ImportParameters codeImportParameters = new ImportParameters(ProductModel._TYPECODE, "", "Abc",
				documentRefs.get(lastRowNumber - DefaultExcelImportService.FIRST_DATA_ROW_INDEX), new ArrayList<>());
		final ImportParameters catalogVersionImportParameters = new ImportParameters(ProductModel._TYPECODE, "", ":Online",
				documentRefs.get(lastRowNumber - DefaultExcelImportService.FIRST_DATA_ROW_INDEX), new ArrayList<>());
		selectedAttributes.add(prepareSelectedAttribute("code", "", ""));
		selectedAttributes.add(prepareSelectedAttribute("catalogVersion", "catalog:version", "Clothing:"));

		given(productSheet.getSheetName()).willReturn(ProductModel._TYPECODE);
		given(excelTemplateService.findTypeCodeForSheetName(Mockito.eq(ProductModel._TYPECODE), Mockito.any())).willReturn(ProductModel._TYPECODE);
		given(productSheet.getLastRowNum()).willReturn(lastRowNumber);
		given(excelTranslatorRegistry.getTranslator(any())).willReturn(translatorOpt);
		given(productSheet.getRow(lastRowNumber)).willReturn(firstRow);
		given(firstRow.getCell(0)).willReturn(codeFirstCell);
		given(firstRow.getCell(1)).willReturn(catalogVersionFirstCell);
		final short num = 1;
		given(firstRow.getLastCellNum()).willReturn(num);
		given(excelTemplateService.getCellValue(codeFirstCell)).willReturn(ProductModel.CODE);
		given(excelTemplateService.getCellValue(catalogVersionFirstCell)).willReturn(ProductModel.CATALOGVERSION);
		given(excelTemplateService.getHeaders(typeSystemSheet, productSheet)).willReturn(selectedAttributes);

		doReturn(documentRefs).when(excelImportService).generateDocumentRefs(
				lastRowNumber - DefaultExcelImportService.FIRST_DATA_ROW_INDEX);
		doReturn(codeImportParameters).when(excelImportService).findImportParameters(selectedAttributes.get(0), "Abc",
				ProductModel._TYPECODE, documentRefs.get(lastRowNumber - DefaultExcelImportService.FIRST_DATA_ROW_INDEX));
		doReturn(catalogVersionImportParameters).when(excelImportService).findImportParameters(selectedAttributes.get(1),
				":Online", ProductModel._TYPECODE, documentRefs.get(lastRowNumber - DefaultExcelImportService.FIRST_DATA_ROW_INDEX));

		// when
		final Impex impex = excelImportService.generateImpexForSheet(typeSystemSheet, productSheet);

		// then
		assertThat(impex).isNotNull();
		verify(translator, times(2)).importData(any(), any());
	}

	private SelectedAttribute prepareSelectedAttribute(final String qualifier, final String referenceFormat,
			final String defaultValue)
	{
		final AttributeDescriptorModel attributeDescriptor = mock(AttributeDescriptorModel.class);
		given(attributeDescriptor.getQualifier()).willReturn(qualifier);
		return new SelectedAttribute(null, referenceFormat, defaultValue, attributeDescriptor);
	}

}
