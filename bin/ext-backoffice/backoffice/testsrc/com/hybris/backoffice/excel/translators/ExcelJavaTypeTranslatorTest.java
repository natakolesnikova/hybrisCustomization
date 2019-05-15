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
package com.hybris.backoffice.excel.translators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;

import java.util.Date;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.util.DefaultExcelDateUtils;


@RunWith(MockitoJUnitRunner.class)
public class ExcelJavaTypeTranslatorTest extends AbstractTypeTranslatorTest
{

	private final ExcelJavaTypeTranslator translator = new ExcelJavaTypeTranslator();
	private final DefaultExcelDateUtils excelDateUtils = new DefaultExcelDateUtils();

	@Before
	public void setUp()
	{
		translator.setExcelDateUtils(excelDateUtils);
	}

	@Override
	@Test
	public void shouldExportDataBeNullSafe()
	{
		// expect
		assertThat(translator.exportData(null).isPresent()).isFalse();
	}

	@Override
	@Test
	public void shouldExportedDataBeInProperFormat()
	{
		// given
		final String input = "input";

		// when
		final String output = translator.exportData(input).map(String.class::cast).get();

		// then
		assertThat(output).isEqualTo(input);
	}

	@Override
	@Test
	public void shouldGivenTypeBeHandled()
	{
		// given
		final AtomicTypeModel atomicType = mock(AtomicTypeModel.class);
		final AttributeDescriptorModel attributeDescriptor = mock(AttributeDescriptorModel.class);
		given(attributeDescriptor.getAttributeType()).willReturn(atomicType);

		// when
		final boolean canHandle = translator.canHandle(attributeDescriptor);

		// then
		assertThat(canHandle).isTrue();
	}

	@Test
	public void shouldDateBeParsedInLocalizedWay()
	{
		// given
		final Date input = new Date();

		// when
		final String output = translator.exportData(input).map(String.class::cast).get();

		// then
		assertThat(output).isEqualTo(excelDateUtils.exportDate(input));
	}

	@Test
	public void shouldImportStringData()
	{
		// given
		final String value = "value";
		final AttributeDescriptorModel attributeDescriptor = mock(AttributeDescriptorModel.class);
		given(attributeDescriptor.getQualifier()).willReturn(ProductModel.CODE);
		given(attributeDescriptor.getAttributeType()).willReturn(mock(TypeModel.class));
		final ImportParameters importParameters = new ImportParameters(ProductModel._TYPECODE, null, value,
				UUID.randomUUID().toString(), null);

		// when
		final ImpexValue impexValue = translator.importValue(attributeDescriptor, importParameters);

		// then
		assertThat(impexValue.getValue()).isEqualTo(value);
		assertThat(impexValue.getHeaderValue().getName()).isEqualTo(ProductModel.CODE);
	}

	@Test
	public void shouldImportNumberData()
	{
		// given
		final Double value = 3.14;
		final AttributeDescriptorModel attributeDescriptor = mock(AttributeDescriptorModel.class);
		given(attributeDescriptor.getQualifier()).willReturn(ProductModel.EUROPE1DISCOUNTS);
		given(attributeDescriptor.getAttributeType()).willReturn(mock(TypeModel.class));
		final ImportParameters importParameters = new ImportParameters(ProductModel._TYPECODE, null, value,
				UUID.randomUUID().toString(), null);

		// when
		final ImpexValue impexValue = translator.importValue(attributeDescriptor, importParameters);

		// then
		assertThat(impexValue.getValue()).isEqualTo(value);
		assertThat(impexValue.getHeaderValue().getName()).isEqualTo(ProductModel.EUROPE1DISCOUNTS);
	}

	@Test
	public void shouldImportDateData()
	{
		// given
		final String value = "10.12.2016 18:23:44";
		final AttributeDescriptorModel attributeDescriptor = mock(AttributeDescriptorModel.class);
		final TypeModel typeModel = mock(TypeModel.class);
		given(attributeDescriptor.getQualifier()).willReturn(ProductModel.ONLINEDATE);
		given(attributeDescriptor.getAttributeType()).willReturn(typeModel);
		given(typeModel.getCode()).willReturn(Date.class.getCanonicalName());
		final ImportParameters importParameters = new ImportParameters(ProductModel._TYPECODE, null, value,
				UUID.randomUUID().toString(), null);

		// when
		final ImpexValue impexValue = translator.importValue(attributeDescriptor, importParameters);

		// then
		assertThat(impexValue.getValue()).isEqualTo(excelDateUtils.importDate(value));
		assertThat(impexValue.getHeaderValue().getName()).isEqualTo(ProductModel.ONLINEDATE);
		assertThat(impexValue.getHeaderValue().getDateFormat()).isEqualTo(excelDateUtils.getDateTimeFormat());
	}

}
