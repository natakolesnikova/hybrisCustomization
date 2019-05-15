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
package de.hybris.platform.cmsfacades.types.service.populator;


import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.types.service.ComponentTypeAttributeStructure;
import de.hybris.platform.cmsfacades.types.service.ComponentTypeStructureRegistry;
import de.hybris.platform.cmsfacades.types.service.impl.DefaultComponentTypeAttributeStructure;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Sets;


/**
 * Populator that lookup for populators defined in the registry for a given attribute and its enclosing type. 
 * @deprecated since 6.5
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.5")
public class ComponentTypeStructureAttributeRegistryPopulator implements Populator<AttributeDescriptorModel, DefaultComponentTypeAttributeStructure>
{

	private ComponentTypeStructureRegistry componentTypeStructureRegistry;
	
	@Override
	public void populate(final AttributeDescriptorModel attributeDescriptor,
			final DefaultComponentTypeAttributeStructure componentTypeAttributeStructure) throws ConversionException
	{
		final ComposedTypeModel composedType = attributeDescriptor.getEnclosingType();
		
		final Set<ComponentTypeAttributeStructure> attributesOnRegistry = Optional //
				.ofNullable(getComponentTypeStructureRegistry().getComponentTypeStructure(composedType.getCode())) //
				.map(componentTypeStructure -> componentTypeStructure.getAttributes()) //
				.orElse(Sets.newHashSet());

		// add extra populators defined on the registry 
		attributesOnRegistry //
				.stream() //
				.filter(attributeOnRegistry -> StringUtils.equals(attributeDescriptor.getQualifier(), attributeOnRegistry.getQualifier())) //
				.map(ComponentTypeAttributeStructure::getPopulators) //
				.findFirst() //
				.ifPresent(populatorsOnRegistry -> componentTypeAttributeStructure.getPopulators().addAll(populatorsOnRegistry));
				
	}

	protected ComponentTypeStructureRegistry getComponentTypeStructureRegistry()
	{
		return componentTypeStructureRegistry;
	}

	@Required
	public void setComponentTypeStructureRegistry(final ComponentTypeStructureRegistry componentTypeStructureRegistry)
	{
		this.componentTypeStructureRegistry = componentTypeStructureRegistry;
	}
}
