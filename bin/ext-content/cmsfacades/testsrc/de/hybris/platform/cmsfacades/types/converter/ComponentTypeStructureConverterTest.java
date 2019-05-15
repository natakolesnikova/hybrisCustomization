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
package de.hybris.platform.cmsfacades.types.converter;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.common.service.StringDecapitalizer;
import de.hybris.platform.cmsfacades.data.CMSParagraphComponentData;
import de.hybris.platform.cmsfacades.data.ComponentTypeAttributeData;
import de.hybris.platform.cmsfacades.data.ComponentTypeData;
import de.hybris.platform.cmsfacades.data.StructureTypeCategory;
import de.hybris.platform.cmsfacades.types.service.ComponentTypeAttributeStructure;
import de.hybris.platform.cmsfacades.types.service.ComponentTypeStructure;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.ObjectFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ComponentTypeStructureConverterTest
{
	private static final String QUALIFIER1 = "qualifier1";
	private static final String QUALIFIER2 = "qualifier2";
	private static final String QUALIFIER3 = "qualifier3";
	private static final String QUALIFIER4 = "qualifier4";

	private static final String CODE = "code";
	private static final String ATTRIBUTE4_TYPECODE = "attribute4_code";

	@InjectMocks
	private ComponentTypeStructureConverter converter;

	@Mock
	private TypeService typeService;

	@Mock
	private StringDecapitalizer stringDecapitalizer;

	@Mock
	private ComposedTypeModel composedType, attribute4ComposedType;
	@Mock
	private AttributeDescriptorModel attribute1, attribute2, attribute3;
	@Mock
	private ComponentTypeStructure structureType;
	@Mock
	private ComponentTypeAttributeStructure structureAttribute1, structureAttribute2, structureAttribute4;
	@Mock
	private Populator<ComposedTypeModel, ComponentTypeData> popType1, popType2, defaultPopType;
	@Mock
	private Populator<AttributeDescriptorModel, ComponentTypeAttributeData> popAttribute1, popAttribute2, popAttribute4;
	@Mock
	private ObjectFactory<ComponentTypeAttributeData> componentTypeAttributeDataFactory;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp()
	{
		when(typeService.getComposedTypeForCode(CODE)).thenReturn(composedType);
		when(typeService.getComposedTypeForCode(ATTRIBUTE4_TYPECODE)).thenReturn(attribute4ComposedType);

		when(composedType.getCode()).thenReturn(CODE);
		when(composedType.getDeclaredattributedescriptors()).thenReturn(Sets.newHashSet(attribute1));
		when(composedType.getInheritedattributedescriptors()).thenReturn(Sets.newHashSet(attribute2, attribute3));

		when(attribute1.getQualifier()).thenReturn(QUALIFIER1);
		when(attribute2.getQualifier()).thenReturn(QUALIFIER2);
		when(attribute3.getQualifier()).thenReturn(QUALIFIER3);

		when(structureType.getTypecode()).thenReturn(CODE);
		when(structureType.getPopulators()).thenReturn(Lists.newArrayList(popType1, popType2));
		when(structureType.getAttributes()).thenReturn(Sets.newHashSet(structureAttribute1, structureAttribute2, structureAttribute4));
		when(structureType.getCategory()).thenReturn(StructureTypeCategory.COMPONENT);
		when(structureType.getTypeDataClass()).thenReturn(CMSParagraphComponentData.class);
		when(structureAttribute1.getQualifier()).thenReturn(QUALIFIER1);
		when(structureAttribute1.getPopulators()).thenReturn(Lists.newArrayList(popAttribute1, popAttribute2));
		when(structureAttribute2.getQualifier()).thenReturn(QUALIFIER2);
		when(structureAttribute2.getPopulators()).thenReturn(Lists.newArrayList(popAttribute1, popAttribute2));
		when(structureAttribute4.getQualifier()).thenReturn(QUALIFIER4);
		when(structureAttribute4.getTypecode()).thenReturn(ATTRIBUTE4_TYPECODE);
		when(structureAttribute4.getPopulators()).thenReturn(Lists.newArrayList(popAttribute4));
		when(componentTypeAttributeDataFactory.getObject()).thenReturn(new ComponentTypeAttributeData());

		when(stringDecapitalizer.decapitalize(any())).thenReturn(Optional.of("cmsParagraphComponentData"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldConvertWithDefault_NoStructureTypeFoundForCode()
	{
		when(structureType.getPopulators()).thenReturn(Lists.newArrayList(defaultPopType));
		final ComponentTypeData target = new ComponentTypeData();

		converter.convert(structureType, target);

		verify(defaultPopType).populate(composedType, target);
	}

	@Test
	public void shouldPopulateComponentTypeProperties()
	{
		final ComponentTypeData target = new ComponentTypeData();
		converter.convert(structureType, target);

		verify(popType1).populate(composedType, target);
		verify(popType2).populate(composedType, target);
	}

	@Test
	public void shouldConvertComponentTypeAttributes()
	{
		final ComponentTypeData target = new ComponentTypeData();
		converter.convert(structureType, target);

		verify(popAttribute1).populate(Mockito.eq(attribute1), Mockito.any(ComponentTypeAttributeData.class));
		verify(popAttribute2).populate(Mockito.eq(attribute1), Mockito.any(ComponentTypeAttributeData.class));
		verify(popAttribute1).populate(Mockito.eq(attribute2), Mockito.any(ComponentTypeAttributeData.class));
		verify(popAttribute2).populate(Mockito.eq(attribute2), Mockito.any(ComponentTypeAttributeData.class));
	}

	@Test
	public void shouldConvertComponentTypeAttributesWithNoDescriptor()
	{
		final ArgumentCaptor<AttributeDescriptorModel> populatorArg = ArgumentCaptor.forClass(AttributeDescriptorModel.class);

		final ComponentTypeData target = new ComponentTypeData();
		converter.convert(structureType, target);

		verify(popAttribute4).populate(populatorArg.capture(), Mockito.any(ComponentTypeAttributeData.class));
		assertThat(populatorArg.getValue().getQualifier(), equalTo(QUALIFIER4));
		assertThat(populatorArg.getValue().getAttributeType(), equalTo(attribute4ComposedType));
		assertThat(populatorArg.getValue().getEnclosingType(), equalTo(composedType));
	}

	@Test
	public void shouldNotConvertComponentTypeAttributesWithNoStructureAttributes()
	{
		final ComponentTypeData target = new ComponentTypeData();
		converter.convert(structureType, target);

		verify(popAttribute1, times(0)).populate(Mockito.eq(attribute3), Mockito.any(ComponentTypeAttributeData.class));
		verify(popAttribute2, times(0)).populate(Mockito.eq(attribute3), Mockito.any(ComponentTypeAttributeData.class));
	}


	@Test
	public void shouldPopulateTypAttributeWithCorrectCMSParagraphComponentName()
	{
		final ComponentTypeData target = new ComponentTypeData();
		converter.convert(structureType, target);
		verify(stringDecapitalizer).decapitalize(CMSParagraphComponentData.class);
	}

}
