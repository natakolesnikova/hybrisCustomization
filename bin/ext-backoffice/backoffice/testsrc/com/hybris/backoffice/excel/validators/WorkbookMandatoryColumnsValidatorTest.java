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
package com.hybris.backoffice.excel.validators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.template.ExcelTemplateService;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;


@RunWith(MockitoJUnitRunner.class)
public class WorkbookMandatoryColumnsValidatorTest
{

	@Mock
	private ExcelTemplateService excelTemplateService;

	@Mock
	private TypeService typeService;

	@Mock
	private CommonI18NService commonI18NService;

	@Spy
	@InjectMocks
	private WorkbookMandatoryColumnsValidator workbookMandatoryColumnsValidator;

	@Mock
	private Workbook workbook;

	@Mock
	private Sheet productSheet;

	@Mock
	private Sheet typeSystemSheet;

	@Before
	public void setup()
	{
		when(excelTemplateService.getSheets(workbook)).thenReturn(Arrays.asList(productSheet));
		when(excelTemplateService.getTypeSystemSheet(workbook)).thenReturn(typeSystemSheet);
		when(productSheet.getSheetName()).thenReturn(ProductModel._TYPECODE);
		when(excelTemplateService.findTypeCodeForSheetName(eq(ProductModel._TYPECODE), any())).thenReturn(ProductModel._TYPECODE);
		final LanguageModel languageModel = mock(LanguageModel.class);
		when(commonI18NService.getCurrentLanguage()).thenReturn(languageModel);
		when(languageModel.getIsocode()).thenReturn("en");
	}

	@Test
	public void shouldNotReturnValidationErrorsWhenAllMandatoryColumnsAreSelected()
	{
		// given
		final AttributeDescriptorModel code = mock(AttributeDescriptorModel.class);
		final SelectedAttribute codeSelectedAttribute = new SelectedAttribute(code);

		doReturn(codeSelectedAttribute).when(workbookMandatoryColumnsValidator).prepareSelectedAttribute(code);
		when(typeService.getAttributesForModifiers(eq(ProductModel._TYPECODE), any())).thenReturn(Sets.newHashSet(code));

		when(excelTemplateService.findColumnIndex(typeSystemSheet, productSheet, codeSelectedAttribute)).thenReturn(0);

		// when
		final List<ExcelValidationResult> validationResult = workbookMandatoryColumnsValidator.validate(workbook);

		// then
		assertThat(validationResult).isEmpty();
	}

	@Test
	public void shouldNotReturnValidationErrorsWhenLocalizedMandatoryColumnIsSelectedForCurrentLanguage()
	{
		// given
		final AttributeDescriptorModel code = mock(AttributeDescriptorModel.class);
		final SelectedAttribute codeSelectedAttribute = new SelectedAttribute("en", code);

		doReturn(codeSelectedAttribute).when(workbookMandatoryColumnsValidator).prepareSelectedAttribute(code);
		when(typeService.getAttributesForModifiers(eq(ProductModel._TYPECODE), any())).thenReturn(Sets.newHashSet(code));

		when(excelTemplateService.findColumnIndex(typeSystemSheet, productSheet, codeSelectedAttribute)).thenReturn(0);

		// when
		final List<ExcelValidationResult> validationResult = workbookMandatoryColumnsValidator.validate(workbook);

		// then
		assertThat(validationResult).isEmpty();
	}

	@Test
	public void shouldReturnValidationErrorsWhenMandatoryColumnIsNotSelected()
	{
		// given
		final AttributeDescriptorModel code = mock(AttributeDescriptorModel.class);
		final SelectedAttribute codeSelectedAttribute = new SelectedAttribute(code);

		doReturn(codeSelectedAttribute).when(workbookMandatoryColumnsValidator).prepareSelectedAttribute(code);
		when(typeService.getAttributesForModifiers(eq(ProductModel._TYPECODE), any())).thenReturn(Sets.newHashSet(code));

		when(excelTemplateService.findColumnIndex(typeSystemSheet, productSheet, codeSelectedAttribute)).thenReturn(-1);

		// when
		final List<ExcelValidationResult> validationResult = workbookMandatoryColumnsValidator.validate(workbook);

		// then
		assertThat(validationResult).hasSize(1);
		assertThat(validationResult.get(0).getValidationErrors()).hasSize(1);
		assertThat(validationResult.get(0).getValidationErrors().get(0).getMessageKey()).isEqualTo(
				WorkbookMandatoryColumnsValidator.VALIDATION_MESSAGE_DESCRIPTION);
		assertThat(validationResult.get(0).getHeader().getMessageKey()).isEqualTo(
				WorkbookMandatoryColumnsValidator.VALIDATION_MESSAGE_HEADER);
	}

}
