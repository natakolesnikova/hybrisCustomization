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

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImportParameters;


@RunWith(MockitoJUnitRunner.class)
public class ExcelMediaImportTranslatorTest extends AbstractTypeTranslatorTest
{
	public static final String GENERATED_CODE = "generatedCode";
	@InjectMocks
	@Spy
	private ExcelMediaImportTranslator translator;
	@Mock
	private TypeService typeService;
	@Mock
	private CatalogTypeService catalogTypeService;
	@Mock
	private KeyGenerator mediaCodeGenerator;

	@Before
	public void setUp()
	{
		when(catalogTypeService.getCatalogVersionContainerAttribute(MediaModel._TYPECODE)).thenReturn(MediaModel.CATALOGVERSION);
		when(typeService.isAssignableFrom(MediaModel._TYPECODE, MediaModel._TYPECODE)).thenReturn(true);
		when(translator.generateMediaRefId(any(), any())).thenReturn(GENERATED_CODE);
		when(mediaCodeGenerator.generate()).thenReturn(GENERATED_CODE);
	}

	@Test
	public void shouldImportMediaWithGeneratedCode()
	{
		final Map<String, String> params = new HashMap<>();
		params.put(ExcelMediaImportTranslator.PARAM_FILE_PATH, "path");
		params.put(CatalogVersion.CATALOG, "default");
		params.put(CatalogVersion.VERSION, "staged");
		final AttributeDescriptorModel attrDesc = mockAttributeDescriptor(MediaModel._TYPECODE);

		final ImportParameters importParameters = new ImportParameters(ProductModel._TYPECODE, "b", "c", "d",
				Lists.newArrayList(params));
		final Impex impex = translator.importData(attrDesc, importParameters);
		final ImpexForType mediaImpex = impex.findUpdates(MediaModel._TYPECODE);

		assertThat(mediaImpex).isNotNull();
		assertThat(mediaImpex.getImpexTable().row(0)).isNotNull();
		assertThat(mediaImpex.getImpexTable().row(0).keySet()).hasSize(4);
		assertThat(mediaImpex.getImpexTable().row(0).keySet()).containsOnly(
				translator.createMediaReferenceIdHeader(attrDesc, params), translator.createMediaCodeHeader(attrDesc, params),
				translator.createMediaCatalogVersionHeader(attrDesc, params), translator.createMediaContentHeader(attrDesc, params));
		assertThat(mediaImpex.getImpexTable().row(0).get(translator.createMediaCodeHeader(attrDesc, params)))
				.isEqualTo(GENERATED_CODE);

		final ImpexForType productImpex = impex.findUpdates(ProductModel._TYPECODE);
		assertThat(productImpex).isNotNull();
		assertThat(productImpex.getImpexTable().get(0, translator.createReferenceHeader(attrDesc))).isEqualTo(GENERATED_CODE);
	}

	@Test
	public void shouldNotImportContentIfFilePathIsEmpty()
	{
		final Map<String, String> params = new HashMap<>();
		params.put(ExcelMediaImportTranslator.PARAM_CODE, "theCode");
		params.put(CatalogVersion.CATALOG, "default");
		params.put(CatalogVersion.VERSION, "staged");

		final AttributeDescriptorModel attrDesc = mockAttributeDescriptor(MediaModel._TYPECODE);

		final ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList(params));
		final Impex impex = translator.importData(attrDesc, importParameters);
		final ImpexForType mediaImpex = impex.findUpdates(MediaModel._TYPECODE);

		assertThat(mediaImpex).isNotNull();
		assertThat(mediaImpex.getImpexTable().row(0)).isNotNull();
		assertThat(mediaImpex.getImpexTable().row(0).keySet()).hasSize(3);
		assertThat(mediaImpex.getImpexTable().row(0).keySet()).containsOnly(
				translator.createMediaReferenceIdHeader(attrDesc, params), translator.createMediaCodeHeader(attrDesc, params),
				translator.createMediaCatalogVersionHeader(attrDesc, params));
		assertThat(mediaImpex.getImpexTable().get(0, translator.createMediaCodeHeader(attrDesc, params))).isEqualTo("theCode");

	}

	@Test
	@Override
	public void shouldExportDataBeNullSafe()
	{
		assertThat(translator.exportData(null).isPresent()).isFalse();
	}

	@Test
	@Override
	public void shouldExportedDataBeInProperFormat()
	{
		final CatalogVersionModel cv = generateMock("default", "staged");
		final MediaModel media = mock(MediaModel.class);
		when(media.getCode()).thenReturn("theCode");
		when(media.getCatalogVersion()).thenReturn(cv);

		assertThat(translator.exportData(media).isPresent()).isTrue();
		assertThat(translator.exportData(media).get()).isEqualTo(":theCode:default:staged");
	}

	@Test
	@Override
	public void shouldGivenTypeBeHandled()
	{
		final AttributeDescriptorModel attributeDescriptor = mockAttributeDescriptor(MediaModel._TYPECODE);

		assertThat(translator.canHandle(attributeDescriptor)).isTrue();
	}

	@Test
	public void shouldNotHandlerOtherTypes()
	{
		final AttributeDescriptorModel attributeDescriptor = mockAttributeDescriptor(ProductModel._TYPECODE);

		assertThat(translator.canHandle(attributeDescriptor)).isFalse();
	}

	protected AttributeDescriptorModel mockAttributeDescriptor(final String typecode)
	{
		final AttributeDescriptorModel attributeDescriptor = mock(AttributeDescriptorModel.class);
		final TypeModel typeModel = mock(TypeModel.class);
		when(typeModel.getCode()).thenReturn(typecode);
		when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
		return attributeDescriptor;
	}
}
