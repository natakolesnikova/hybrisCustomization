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
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
public class ExcelMediaCollectionImportTranslatorTest extends AbstractTypeTranslatorTest
{
	public static final String GENERATED_CODE = "generatedCode";
	@InjectMocks
	@Spy
	private ExcelMediaCollectionImportTranslator translator;
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
		doAnswer(inv -> {
			Map params = (Map) inv.getArguments()[1];
			return generateRefId(params);
		}).when(translator).generateMediaRefId(any(), any());
		when(mediaCodeGenerator.generate()).thenReturn(GENERATED_CODE);
	}

	protected String generateRefId(final Map params)
	{
		final String path = (String) params.getOrDefault(ExcelMediaCollectionImportTranslator.PARAM_FILE_PATH, StringUtils.EMPTY);
		final String code = (String) params.getOrDefault(ExcelMediaCollectionImportTranslator.PARAM_CODE, StringUtils.EMPTY);
		return String.format("generatedFrom(%s,%s)", path, code);
	}

	@Test
	public void shouldImportMediaWithGeneratedCode()
	{
		final Map<String, String> params = new HashMap<>();
		params.put(ExcelMediaImportTranslator.PARAM_FILE_PATH, "path");
		params.put(ExcelMediaImportTranslator.PARAM_CODE, "theCode");
		params.put(CatalogVersion.CATALOG, "default");
		params.put(CatalogVersion.VERSION, "staged");

		final Map<String, String> params2 = new HashMap<>();
		params2.put(ExcelMediaImportTranslator.PARAM_FILE_PATH, "path2");
		params2.put(CatalogVersion.CATALOG, "default");
		params2.put(CatalogVersion.VERSION, "staged");

		final AttributeDescriptorModel attrDesc = mockAttributeDescriptor(MediaModel._TYPECODE);

		final ImportParameters importParameters = new ImportParameters(ProductModel._TYPECODE, "b", "c", "d",
				Lists.newArrayList(params, params2));
		final Impex impex = translator.importData(attrDesc, importParameters);
		final ImpexForType mediaImpex = impex.findUpdates(MediaModel._TYPECODE);

		assertThat(mediaImpex).isNotNull();
		assertThat(mediaImpex.getImpexTable().row(0)).isNotNull();
		assertThat(mediaImpex.getImpexTable().row(0).keySet()).hasSize(4);
		assertThat(mediaImpex.getImpexTable().row(0).keySet()).containsOnly(
				translator.createMediaReferenceIdHeader(attrDesc, params), translator.createMediaCodeHeader(attrDesc, params),
				translator.createMediaCatalogVersionHeader(attrDesc, params), translator.createMediaContentHeader(attrDesc, params));
		assertThat(mediaImpex.getImpexTable().row(0).get(translator.createMediaCodeHeader(attrDesc, params))).isEqualTo("theCode");

		assertThat(mediaImpex).isNotNull();
		assertThat(mediaImpex.getImpexTable().row(1)).isNotNull();
		assertThat(mediaImpex.getImpexTable().row(1).keySet()).hasSize(4);
		assertThat(mediaImpex.getImpexTable().row(1).keySet()).containsOnly(
				translator.createMediaReferenceIdHeader(attrDesc, params2), translator.createMediaCodeHeader(attrDesc, params2),
				translator.createMediaCatalogVersionHeader(attrDesc, params2),
				translator.createMediaContentHeader(attrDesc, params2));
		assertThat(mediaImpex.getImpexTable().row(1).get(translator.createMediaCodeHeader(attrDesc, params2)))
				.isEqualTo(GENERATED_CODE);

		final ImpexForType productImpex = impex.findUpdates(ProductModel._TYPECODE);
		assertThat(productImpex).isNotNull();
		assertThat(productImpex.getImpexTable().get(0, translator.createReferenceHeader(attrDesc)))
				.isEqualTo(generateRefId(params) + "," + generateRefId(params2));
	}

	@Test
	public void shouldNotImportContentIfFilePathIsEmpty()
	{
		final Map<String, String> params1 = new HashMap<>();
		params1.put(ExcelMediaImportTranslator.PARAM_CODE, "theCode1");
		params1.put(CatalogVersion.CATALOG, "default");
		params1.put(CatalogVersion.VERSION, "staged");

		final Map<String, String> params2 = new HashMap<>();
		params2.put(ExcelMediaImportTranslator.PARAM_CODE, "theCode2");
		params2.put(CatalogVersion.CATALOG, "default");
		params2.put(CatalogVersion.VERSION, "staged");

		final AttributeDescriptorModel attrDesc = mockAttributeDescriptor(MediaModel._TYPECODE);

		final ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList(params1, params2));
		final Impex impex = translator.importData(attrDesc, importParameters);
		final ImpexForType mediaImpex = impex.findUpdates(MediaModel._TYPECODE);

		assertThat(mediaImpex).isNotNull();
		assertThat(mediaImpex.getImpexTable().row(0)).isNotNull();
		assertThat(mediaImpex.getImpexTable().row(0).keySet()).hasSize(3);
		assertThat(mediaImpex.getImpexTable().row(0).keySet()).containsOnly(
				translator.createMediaReferenceIdHeader(attrDesc, params1), translator.createMediaCodeHeader(attrDesc, params1),
				translator.createMediaCatalogVersionHeader(attrDesc, params1));
		assertThat(mediaImpex.getImpexTable().get(0, translator.createMediaCodeHeader(attrDesc, params1))).isEqualTo("theCode1");

		assertThat(mediaImpex.getImpexTable().row(1)).isNotNull();
		assertThat(mediaImpex.getImpexTable().row(1).keySet()).hasSize(3);
		assertThat(mediaImpex.getImpexTable().row(1).keySet()).containsOnly(
				translator.createMediaReferenceIdHeader(attrDesc, params2), translator.createMediaCodeHeader(attrDesc, params2),
				translator.createMediaCatalogVersionHeader(attrDesc, params2));
		assertThat(mediaImpex.getImpexTable().get(1, translator.createMediaCodeHeader(attrDesc, params2))).isEqualTo("theCode2");
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
		final MediaModel media1 = mock(MediaModel.class);
		when(media1.getCode()).thenReturn("theCode1");
		when(media1.getCatalogVersion()).thenReturn(cv);

		final MediaModel media2 = mock(MediaModel.class);
		when(media2.getCode()).thenReturn("theCode2");
		when(media2.getCatalogVersion()).thenReturn(cv);

		assertThat(translator.exportData(Lists.newArrayList(media1, media2)).isPresent()).isTrue();
		assertThat(translator.exportData(Lists.newArrayList(media1, media2)).get())
				.isEqualTo(":theCode1:default:staged,:theCode2:default:staged");
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
		final CollectionTypeModel collectionType = mock(CollectionTypeModel.class);
		when(collectionType.getCode()).thenReturn("CollectionType");

		when(attributeDescriptor.getAttributeType()).thenReturn(collectionType);

		final TypeModel typeModel = mock(TypeModel.class);
		when(typeModel.getCode()).thenReturn(typecode);
		when(collectionType.getElementType()).thenReturn(typeModel);
		return attributeDescriptor;
	}

	@Test
	public void shouldGenerateTheSameRefId()
	{
		doCallRealMethod().when(translator).generateMediaRefId(any(), any());

		final Map<String, String> params1 = new HashMap<>();
		params1.put(ExcelMediaImportTranslator.PARAM_CODE, "theCode1");
		params1.put(CatalogVersion.CATALOG, "default");
		params1.put(CatalogVersion.VERSION, "staged");

		final Map<String, String> params2 = new HashMap<>();
		params2.put(ExcelMediaImportTranslator.PARAM_CODE, "theCode1");
		params2.put(CatalogVersion.CATALOG, "default");
		params2.put(CatalogVersion.VERSION, "staged");

		assertThat(translator.generateMediaRefId(null, params1)).isEqualTo(translator.generateMediaRefId(null, params2));
	}

	@Test
	public void shouldGenerateDifferentRefId()
	{
		doCallRealMethod().when(translator).generateMediaRefId(any(), any());

		final Map<String, String> params1 = new HashMap<>();
		params1.put(ExcelMediaImportTranslator.PARAM_CODE, "theCode1");
		params1.put(CatalogVersion.CATALOG, "Default");
		params1.put(CatalogVersion.VERSION, "Staged");

		final Map<String, String> params2 = new HashMap<>(params1);
		params2.put(ExcelMediaImportTranslator.PARAM_CODE, "differentCode");
		assertThat(translator.generateMediaRefId(null, params1)).isNotEqualTo(translator.generateMediaRefId(null, params2));

		final Map<String, String> params3 = new HashMap<>(params1);
		params3.put(CatalogVersion.CATALOG, "notDefault");
		assertThat(translator.generateMediaRefId(null, params1)).isNotEqualTo(translator.generateMediaRefId(null, params3));

		final Map<String, String> params4 = new HashMap<>(params1);
		params4.put(CatalogVersion.VERSION, "notStaged");
		assertThat(translator.generateMediaRefId(null, params1)).isNotEqualTo(translator.generateMediaRefId(null, params4));

	}
}
