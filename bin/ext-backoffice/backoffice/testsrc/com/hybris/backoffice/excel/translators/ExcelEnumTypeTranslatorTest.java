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

import com.hybris.backoffice.excel.data.ImpexValue;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.excel.data.ImportParameters;


@RunWith(MockitoJUnitRunner.class)
public class ExcelEnumTypeTranslatorTest extends AbstractTypeTranslatorTest
{

	private final AbstractExcelValueTranslator<HybrisEnumValue> translator = new ExcelEnumTypeTranslator();

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
		final String code = "some";
		final HybrisEnumValue hybrisEnumValue = mock(HybrisEnumValue.class);
		given(hybrisEnumValue.getCode()).willReturn(code);

		// when
		final String output = translator.exportData(hybrisEnumValue).map(String.class::cast).get();

		// then
		assertThat(output).isEqualTo(code);
	}

	@Override
	@Test
	public void shouldGivenTypeBeHandled()
	{
		// given
		final AttributeDescriptorModel attributeDescriptor = mock(AttributeDescriptorModel.class);
		final EnumerationMetaTypeModel enumerationMetaType = mock(EnumerationMetaTypeModel.class);
		given(attributeDescriptor.getAttributeType()).willReturn(enumerationMetaType);

		// when
		final boolean canHandle = translator.canHandle(attributeDescriptor);

		// then
		assertThat(canHandle).isTrue();
	}

	@Test
	public void shouldImportEnumValue()
	{
		// given
		final AttributeDescriptorModel attributeDescriptor = mock(AttributeDescriptorModel.class);
		given(attributeDescriptor.getQualifier()).willReturn(ProductModel.APPROVALSTATUS);
		final String cellValue = "approved";
		final ImportParameters importParameters = new ImportParameters(ProductModel._TYPECODE, null, cellValue, UUID.randomUUID()
				.toString(), null);

		// when
		final ImpexValue impexValue = translator.importValue(attributeDescriptor, importParameters);

		// then
		assertThat(impexValue.getValue()).isEqualTo(cellValue);
		assertThat(impexValue.getHeaderValue().getName()).isEqualTo(String.format("%s(code)", ProductModel.APPROVALSTATUS));
	}
}
