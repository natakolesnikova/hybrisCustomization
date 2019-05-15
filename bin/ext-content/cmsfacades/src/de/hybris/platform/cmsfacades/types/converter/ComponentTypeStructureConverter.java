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

import de.hybris.platform.cmsfacades.common.service.StringDecapitalizer;
import de.hybris.platform.cmsfacades.data.ComponentTypeAttributeData;
import de.hybris.platform.cmsfacades.data.ComponentTypeData;
import de.hybris.platform.cmsfacades.types.service.ComponentTypeAttributeStructure;
import de.hybris.platform.cmsfacades.types.service.ComponentTypeStructure;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Required;

import static org.apache.commons.lang3.StringUtils.isNotBlank;


/**
 * Converter use to convert a <code>ComponentTypeStructure</code> to a <code>ComponentTypeData</code>.
 */
public class ComponentTypeStructureConverter implements Converter<ComponentTypeStructure, ComponentTypeData>
{

	private StringDecapitalizer stringDecapitalizer;
	private ObjectFactory<ComponentTypeAttributeData> componentTypeAttributeDataFactory;
	private TypeService typeService;

	@Override
	public ComponentTypeData convert(final ComponentTypeStructure source) throws ConversionException
	{
		return convert(source, new ComponentTypeData());
	}

	@Override
	public ComponentTypeData convert(final ComponentTypeStructure source, final ComponentTypeData target) throws ConversionException
	{
		// Get structure type
		final ComposedTypeModel composedType = getTypeService().getComposedTypeForCode(source.getTypecode());

		// populates the category, but if there is a populator that populates it again, it will be overridden by the populator.
		target.setCategory(source.getCategory().name());

		// Populate component type properties
		source.getPopulators().forEach(populator -> populator.populate(composedType, target));

		// Convert attributes
		target.setAttributes(source.getAttributes().stream() //
				.map(attribute -> convertAttribute(attribute, getAttributeDescriptor(composedType, attribute))) //
				.filter(componentTypeAttributeData -> isNotBlank(componentTypeAttributeData.getCmsStructureType()))
				.collect(Collectors.toList()));

		getStringDecapitalizer() //
		.decapitalize(source.getTypeDataClass()) //
		.ifPresent(typeData -> target.setType(typeData));
		return target;
	}

	/**
	 * Creates a new  <code>AttributeDescriptor</code> with basic data copied from the provided attribute.
	 * @param composedTypeModel
	 *           - the composed type model containing the provided attribute
	 * @param attribute
	 *           - the attribute from where to copy the new descriptor data
	 * @return the newly created attribute descriptor
	 */
	protected AttributeDescriptorModel buildAttributeDescriptorModel( final ComposedTypeModel composedTypeModel, final ComponentTypeAttributeStructure attribute ){
		final ComposedTypeModel attributeType = getTypeService().getComposedTypeForCode(attribute.getTypecode());

		final AttributeDescriptorModel attributeDescriptorModel = new AttributeDescriptorModel();
		attributeDescriptorModel.setQualifier(attribute.getQualifier());
		attributeDescriptorModel.setEnclosingType(composedTypeModel);
		attributeDescriptorModel.setAttributeType(attributeType);

		return attributeDescriptorModel;
	}

	/**
	 * Get <code>AttributeDescriptor</code> matching with the qualifier of the attribute for the type provided.
	 * @param type
	 *           - the composed type model in which to search for the descriptor
	 * @param originalAttribute
	 *           - the attribute whose descriptor to search for.
	 * @return the attribute descriptor matching the given criteria
	 */
	protected AttributeDescriptorModel getAttributeDescriptor(final ComposedTypeModel type, final ComponentTypeAttributeStructure originalAttribute )
	{
		return Stream.of(type.getDeclaredattributedescriptors(), type.getInheritedattributedescriptors())
				.flatMap(Collection::stream).filter(attributeDescriptor -> attributeDescriptor.getQualifier().equals(originalAttribute.getQualifier()))
				.findAny()
				.orElseGet(() -> buildAttributeDescriptorModel(type, originalAttribute));
	}

	/**
	 * Convert the attribute descriptor to a POJO using the structure attribute's populators.
	 * @param attribute
	 *           - the structure type attribute
	 * @param attributeDescriptor
	 *           - the attribute descriptor
	 * @return the component type attribute POJO
	 */
	protected ComponentTypeAttributeData convertAttribute(final ComponentTypeAttributeStructure attribute,
			final AttributeDescriptorModel attributeDescriptor)
	{
		final ComponentTypeAttributeData target = getComponentTypeAttributeDataFactory().getObject();
		target.setQualifier(attribute.getQualifier());
		attribute.getPopulators().forEach(populator -> populator.populate(attributeDescriptor, target));
		return target;
	}

	protected StringDecapitalizer getStringDecapitalizer()
	{
		return stringDecapitalizer;
	}

	@Required
	public void setStringDecapitalizer(final StringDecapitalizer stringDecapitalizer)
	{
		this.stringDecapitalizer = stringDecapitalizer;
	}

	protected ObjectFactory<ComponentTypeAttributeData> getComponentTypeAttributeDataFactory()
	{
		return componentTypeAttributeDataFactory;
	}

	@Required
	public void setComponentTypeAttributeDataFactory(
			final ObjectFactory<ComponentTypeAttributeData> componentTypeAttributeDataFactory)
	{
		this.componentTypeAttributeDataFactory = componentTypeAttributeDataFactory;
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}
}
