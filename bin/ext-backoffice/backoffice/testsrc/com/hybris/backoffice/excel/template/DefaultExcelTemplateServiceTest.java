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
package com.hybris.backoffice.excel.template;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.translators.ExcelTranslatorRegistry;
import com.hybris.backoffice.excel.translators.ExcelValueTranslator;


@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelTemplateServiceTest
{

	private static final String TYPE_TEMPLATE = "TypeTemplate";
	private static final String TYPE_SYSTEM = "TypeSystem";
	private static final String PRODUCT = "Product";
	private static final String CATEGORY = "Category";
	public static final String CATALOG_VERSION_REFERENCE_FORMAT = "catalog:version";

	@Mock
	private Workbook workbook;

	@Mock
	private Sheet productSheet;

	@Mock
	private Sheet categorySheet;

	@Mock
	private Sheet typeTemplateSheet;

	@Mock
	private Sheet typeSystemSheet;

	@Mock
	private ExcelTranslatorRegistry excelTranslatorRegistry;

	@Mock
	private ExcelValueTranslator codeTranslator;

	@Mock
	private ExcelValueTranslator catalogVersionTranslator;

	@Mock
	private CommonI18NService commonI18NService;

	@Spy
	@InjectMocks
	private DefaultExcelTemplateService excelTemplateService;

	public static final String CATALOG_VERSION_DEFAULT_VALUE = "Clothing:Online";

	@Before
	public void setup()
	{
		given(workbook.getNumberOfSheets()).willReturn(4);
		given(workbook.getSheetName(0)).willReturn(TYPE_TEMPLATE);
		given(workbook.getSheetAt(0)).willReturn(typeTemplateSheet);
		given(workbook.getSheetName(1)).willReturn(TYPE_SYSTEM);
		given(workbook.getSheetAt(1)).willReturn(typeSystemSheet);
		given(workbook.getSheetName(2)).willReturn(PRODUCT);
		given(workbook.getSheetAt(2)).willReturn(productSheet);
		given(workbook.getSheetName(3)).willReturn(CATEGORY);
		given(workbook.getSheetAt(3)).willReturn(categorySheet);
		given(workbook.getSheet(TYPE_TEMPLATE)).willReturn(typeTemplateSheet);
		given(workbook.getSheet(TYPE_SYSTEM)).willReturn(typeSystemSheet);
		given(workbook.getSheet(PRODUCT)).willReturn(productSheet);
		given(workbook.getSheet(CATEGORY)).willReturn(categorySheet);
		given(productSheet.getSheetName()).willReturn(PRODUCT);
		doReturn(Workbook.SHEET_STATE_HIDDEN).when(excelTemplateService).getUtilitySheetHiddenLevel();
	}

	@Test
	public void shouldReturnSheetsNames()
	{
		// given

		// when
		final List<String> sheetsNames = excelTemplateService.getSheetsNames(workbook);

		// then
		assertThat(sheetsNames).hasSize(2);
		assertThat(sheetsNames).contains(PRODUCT, CATEGORY);
	}


	@Test
	public void shouldReturnSheets()
	{
		// given

		// when
		final List<Sheet> availableSheets = excelTemplateService.getSheets(workbook);

		// then
		assertThat(availableSheets).hasSize(2);
		assertThat(availableSheets).contains(productSheet, categorySheet);
	}

	@Test
	public void shouldReturnTypeSystemSheet()
	{
		// given

		// when
		final Sheet foundTypeSystemSheet = excelTemplateService.getTypeSystemSheet(workbook);

		// then
		assertThat(foundTypeSystemSheet).isSameAs(typeSystemSheet);
	}

	@Test
	public void shouldReturnListOfHeaders()
	{
		// given
		final Row headerRow = mock(Row.class);
		final Row referenceFormatRow = mock(Row.class);
		final Row defaultValuesRow = mock(Row.class);
		final Cell codeHeader = mock(Cell.class);
		final Cell codeReferenceFormat = mock(Cell.class);
		final Cell codeDefaultValue = mock(Cell.class);
		final Cell catalogVersionHeader = mock(Cell.class);
		final Cell catalogVersionReferenceFormat = mock(Cell.class);
		final Cell catalogVersionDefaultValue = mock(Cell.class);
		mockCell(codeHeader, ProductModel.CODE);
		mockCell(codeReferenceFormat, StringUtils.EMPTY);
		mockCell(codeDefaultValue, StringUtils.EMPTY);
		mockCell(catalogVersionHeader, ProductModel.CATALOGVERSION);
		mockCell(catalogVersionReferenceFormat, CATALOG_VERSION_REFERENCE_FORMAT);
		mockCell(catalogVersionDefaultValue, CATALOG_VERSION_DEFAULT_VALUE);

		given(referenceFormatRow.getCell(0)).willReturn(codeReferenceFormat);
		given(referenceFormatRow.getCell(1)).willReturn(catalogVersionReferenceFormat);
		given(defaultValuesRow.getCell(0)).willReturn(codeDefaultValue);
		given(defaultValuesRow.getCell(1)).willReturn(catalogVersionDefaultValue);

		given(productSheet.getLastRowNum()).willReturn(3);
		given(productSheet.getRow(0)).willReturn(headerRow);
		given(productSheet.getRow(1)).willReturn(referenceFormatRow);
		given(productSheet.getRow(2)).willReturn(defaultValuesRow);

		given(headerRow.getLastCellNum()).willReturn((short) 2);
		given(headerRow.getCell(0)).willReturn(codeHeader);
		given(headerRow.getCell(1)).willReturn(catalogVersionHeader);

		final Row codeTypeSystemRow = mock(Row.class);
		final Row catalogVersionTypeSystemRow = mock(Row.class);
		final AttributeDescriptorModel codeAttributeDescriptor = mock(AttributeDescriptorModel.class);
		final AttributeDescriptorModel catalogVersionAttributeDescriptor = mock(AttributeDescriptorModel.class);

		given(excelTranslatorRegistry.getTranslator(codeAttributeDescriptor)).willReturn(Optional.of(codeTranslator));
		given(excelTranslatorRegistry.getTranslator(catalogVersionAttributeDescriptor))
				.willReturn(Optional.of(catalogVersionTranslator));
		given(codeTranslator.referenceFormat(codeAttributeDescriptor)).willReturn(StringUtils.EMPTY);
		given(catalogVersionTranslator.referenceFormat(catalogVersionAttributeDescriptor)).willReturn("catalog:version");

		doReturn(Optional.of(codeTypeSystemRow)).when(excelTemplateService).findTypeSystemRowForGivenHeader(typeSystemSheet,
				PRODUCT, ProductModel.CODE);
		doReturn(Optional.of(catalogVersionTypeSystemRow)).when(excelTemplateService)
				.findTypeSystemRowForGivenHeader(typeSystemSheet, PRODUCT, ProductModel.CATALOGVERSION);
		doReturn(codeAttributeDescriptor).when(excelTemplateService).loadAttributeDescriptor(codeTypeSystemRow);
		doReturn(catalogVersionAttributeDescriptor).when(excelTemplateService).loadAttributeDescriptor(catalogVersionTypeSystemRow);

		// when
		final List<SelectedAttribute> headers = excelTemplateService.getHeaders(typeSystemSheet, productSheet);

		// then
		assertThat(headers).hasSize(2);
		assertThat(headers.get(0).getAttributeDescriptor()).isEqualTo(codeAttributeDescriptor);
		assertThat(headers.get(0).getDefaultValues()).isEqualTo(StringUtils.EMPTY);
		assertThat(headers.get(0).getReferenceFormat()).isEqualTo(StringUtils.EMPTY);

		assertThat(headers.get(1).getAttributeDescriptor()).isEqualTo(catalogVersionAttributeDescriptor);
		assertThat(headers.get(1).getReferenceFormat()).isEqualTo(CATALOG_VERSION_REFERENCE_FORMAT);
		assertThat(headers.get(1).getDefaultValues()).isEqualTo(CATALOG_VERSION_DEFAULT_VALUE);
	}

	@Test
	public void shouldIndicateAttributeAsMandatoryWhenIsNotOptionalAndNotLocalized()
	{
		// given
		final AttributeDescriptorModel attributeDescriptor = mock(AttributeDescriptorModel.class);
		given(attributeDescriptor.getOptional()).willReturn(false);
		given(attributeDescriptor.getLocalized()).willReturn(false);

		// when
		final boolean mandatory = excelTemplateService.isMandatory(attributeDescriptor, "en");

		// then
		assertThat(mandatory).isTrue();
	}

	@Test
	public void shouldIndicateAttributeAsMandatoryWhenIsNotOptionalAndIsLocalizedForCurrentLanguage()
	{
		// given
		final AttributeDescriptorModel attributeDescriptor = mock(AttributeDescriptorModel.class);
		given(attributeDescriptor.getOptional()).willReturn(false);
		given(attributeDescriptor.getLocalized()).willReturn(true);
		final LanguageModel languageModel = mock(LanguageModel.class);
		given(languageModel.getIsocode()).willReturn("en");
		given(commonI18NService.getCurrentLanguage()).willReturn(languageModel);

		// when
		final boolean mandatory = excelTemplateService.isMandatory(attributeDescriptor, "en");

		// then
		assertThat(mandatory).isTrue();
	}

	@Test
	public void shouldIndicateAttributeAsNotMandatoryWhenIsOptional()
	{
		// given
		final AttributeDescriptorModel attributeDescriptor = mock(AttributeDescriptorModel.class);
		given(attributeDescriptor.getOptional()).willReturn(true);

		// when
		final boolean mandatory = excelTemplateService.isMandatory(attributeDescriptor, "en");

		// then
		assertThat(mandatory).isFalse();
	}

	@Test
	public void shouldIndicateAttributeAsNotMandatoryWhenIsNotOptionalAndIsNotLocalizedForCurrentLanguage()
	{
		// given
		final AttributeDescriptorModel attributeDescriptor = mock(AttributeDescriptorModel.class);
		given(attributeDescriptor.getOptional()).willReturn(false);
		given(attributeDescriptor.getLocalized()).willReturn(true);
		final LanguageModel languageModel = mock(LanguageModel.class);
		given(languageModel.getIsocode()).willReturn("fr");
		given(commonI18NService.getCurrentLanguage()).willReturn(languageModel);

		// when
		final boolean mandatory = excelTemplateService.isMandatory(attributeDescriptor, "en");

		// then
		assertThat(mandatory).isFalse();
	}

	protected void mockCell(final Cell cell, final String cellValue)
	{
		given(cell.getCellTypeEnum()).willReturn(CellType.STRING);
		given(cell.getStringCellValue()).willReturn(cellValue);
	}

}
