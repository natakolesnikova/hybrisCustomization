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
package com.hybris.backoffice.excel.exporting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.template.ExcelTemplateService;
import com.hybris.backoffice.excel.translators.ExcelTranslatorRegistry;


@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelExportServiceTest
{

	@Mock
	private DefaultExcelExportDivider defaultExcelExportDivider;
	@Mock
	private ExcelTranslatorRegistry excelTranslatorRegistry;
	@Mock
	private ExcelTemplateService excelTemplateService;
	@Mock
	private TypeService typeService;
	@Mock
	private ModelService modelService;
	@InjectMocks
	@Spy
	private DefaultExcelExportService excelExportService;

	@Test
	public void shouldExportTemplateForTypeWithSubtypes()
	{
		// given
		final String product = "Product";
		final String shoes = "Shoes";
		final String jeans = "Jeans";

		final ComposedTypeModel productType = mock(ComposedTypeModel.class);
		when(typeService.getComposedTypeForCode(product)).thenReturn(productType);

		final ComposedTypeModel shoesSubtype = mock(ComposedTypeModel.class);
		when(shoesSubtype.getAbstract()).thenReturn(false);
		when(shoesSubtype.getCode()).thenReturn(shoes);
		final ComposedTypeModel jeansSubtype = mock(ComposedTypeModel.class);
		when(jeansSubtype.getAbstract()).thenReturn(null);
		when(jeansSubtype.getCode()).thenReturn(jeans);

		final Collection<ComposedTypeModel> subTypes = Lists.newArrayList(shoesSubtype, jeansSubtype);
		when(productType.getAllSubTypes()).thenReturn(subTypes);

		when(defaultExcelExportDivider.groupAttributesByType(any(), any())).thenReturn(Collections.emptyMap());

		final Workbook excel = mock(Workbook.class);
		doReturn(excel).when(excelExportService).exportData(any(Map.class), any(List.class));

		// when
		final Workbook ret = excelExportService.exportTemplate(product);

		// then
		assertThat(ret).isSameAs(excel);

		final ArgumentCaptor<Map<String, Set<ItemModel>>> mapCaptor = (ArgumentCaptor) ArgumentCaptor.forClass(Map.class);
		final ArgumentCaptor<List<SelectedAttribute>> listCaptor = (ArgumentCaptor) ArgumentCaptor.forClass(List.class);
		verify(excelExportService).exportData(mapCaptor.capture(), listCaptor.capture());

		assertThat(mapCaptor.getValue()).hasSize(3);
		assertThat(mapCaptor.getValue()).containsEntry(product, Collections.emptySet());
		assertThat(mapCaptor.getValue()).containsEntry(shoes, Collections.emptySet());
		assertThat(mapCaptor.getValue()).containsEntry(jeans, Collections.emptySet());

		assertThat(listCaptor.getValue()).isEmpty();
	}

	@Test
	public void shouldFilterOutAbstractTypesWhenExportingTemplate()
	{
		// given
		final String product = "Product";
		final String variantProduct = "VariantProduct";

		final ComposedTypeModel productType = mock(ComposedTypeModel.class);
		when(typeService.getComposedTypeForCode(product)).thenReturn(productType);

		final ComposedTypeModel variantProductSubtype = mock(ComposedTypeModel.class);
		when(variantProductSubtype.getAbstract()).thenReturn(true);
		when(variantProductSubtype.getCode()).thenReturn(variantProduct);

		final Collection<ComposedTypeModel> subTypes = Lists.newArrayList(variantProductSubtype);
		when(productType.getAllSubTypes()).thenReturn(subTypes);

		when(defaultExcelExportDivider.groupAttributesByType(any(), any())).thenReturn(Collections.emptyMap());

		final Workbook excel = mock(Workbook.class);
		doReturn(excel).when(excelExportService).exportData(any(Map.class), any(List.class));

		// when
		final Workbook ret = excelExportService.exportTemplate(product);

		// then
		assertThat(ret).isSameAs(excel);

		final ArgumentCaptor<Map<String, Set<ItemModel>>> mapCaptor = (ArgumentCaptor) ArgumentCaptor.forClass(Map.class);
		final ArgumentCaptor<List<SelectedAttribute>> listCaptor = (ArgumentCaptor) ArgumentCaptor.forClass(List.class);
		verify(excelExportService).exportData(mapCaptor.capture(), listCaptor.capture());

		assertThat(mapCaptor.getValue()).hasSize(1);
		assertThat(mapCaptor.getValue()).containsEntry(product, Collections.emptySet());

		assertThat(listCaptor.getValue()).isEmpty();
	}

}
