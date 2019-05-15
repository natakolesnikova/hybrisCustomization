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
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.data.ComponentTypeData;
import de.hybris.platform.cmsfacades.types.service.ComponentTypeAttributeStructure;
import de.hybris.platform.cmsfacades.types.service.ComponentTypeStructure;
import de.hybris.platform.cmsfacades.types.service.ComponentTypeStructureRegistry;
import de.hybris.platform.cmsfacades.types.service.impl.DefaultComponentTypeStructure;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Sets;

/**
 * Populator used to add data from the registry to the auto-generated type structure.
 * @deprecated since 6.5
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.5")
public class ComponentTypeStructureRegistryPopulator implements Populator<ComposedTypeModel, DefaultComponentTypeStructure>
{

	private TypeService typeService;

	private ComponentTypeStructureRegistry componentTypeStructureRegistry;

	@Override
	public void populate(final ComposedTypeModel composedType, final DefaultComponentTypeStructure componentTypeStructure)
			throws ConversionException
	{
		// manually set the category from the registry
		Optional.ofNullable(getComponentTypeStructureRegistry().getComponentTypeStructure(composedType.getCode()))
		.ifPresent(componentTypeStructureOnRegistry ->
		componentTypeStructure.setCategory(componentTypeStructureOnRegistry.getCategory()));

		// add populators from the registry
		collectTypePopulatorsFromRegistry(composedType, componentTypeStructure.getPopulators());

		collectExtraAttributesFromRegistry(composedType, componentTypeStructure.getAttributes());
	}

	/**
	 * Recursively collects type populators from the registry until it reaches the root type (CMsItem)
	 * @param composedType the type being used to look for populators
	 * @param populators the list of populators that has to be modified
	 */
	protected void collectTypePopulatorsFromRegistry(final ComposedTypeModel composedType,
			final List<Populator<ComposedTypeModel, ComponentTypeData>> populators)
	{
		// add populators from the registry
		Optional.ofNullable(getComponentTypeStructureRegistry().getComponentTypeStructure(composedType.getCode()))
		.ifPresent(componentTypeStructureOnRegistry -> populators.addAll(componentTypeStructureOnRegistry.getPopulators()));

		// check if it is necessary to continue digging for new attributes
		if (!StringUtils.equals(composedType.getCode(), CMSItemModel._TYPECODE) && composedType.getSuperType() != null)
		{
			collectTypePopulatorsFromRegistry(composedType.getSuperType(), populators);
		}
	}


	/**
	 * Recursively collects extra attributes that are not in the Data Model, but it is present on the the registry
	 * @param composedType the type being used to look for populators
	 * @param attributes
	 */
	protected void collectExtraAttributesFromRegistry(final ComposedTypeModel composedType,
			final Set<ComponentTypeAttributeStructure> attributes)
	{

		final Set<ComponentTypeAttributeStructure> attributesFromRegistry = Optional //
				.ofNullable(getComponentTypeStructureRegistry().getComponentTypeStructure(composedType.getCode())) //
				.map(ComponentTypeStructure::getAttributes) //
				.orElse(Sets.newHashSet());

		final Set<String> attrQualifiers = attributes.stream() //
				.map(ComponentTypeAttributeStructure::getQualifier) //
				.collect(Collectors.toSet());

		attributes.addAll(attributesFromRegistry.stream() //
				.filter(attributeOnRegistry -> !attrQualifiers.contains(attributeOnRegistry.getQualifier())) //
				.collect(Collectors.toList()));

		// check if it is necessary to continue digging for new attributes
		if (!StringUtils.equals(composedType.getCode(), CMSItemModel._TYPECODE) && composedType.getSuperType() != null)
		{
			collectExtraAttributesFromRegistry(composedType.getSuperType(), attributes);
		}
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
