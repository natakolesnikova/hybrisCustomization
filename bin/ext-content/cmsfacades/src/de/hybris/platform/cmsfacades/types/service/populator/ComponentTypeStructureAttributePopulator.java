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
import de.hybris.platform.cmsfacades.data.ComponentTypeAttributeData;
import de.hybris.platform.cmsfacades.types.service.impl.DefaultComponentTypeAttributeStructure;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * The default attribute populator for a given {@link AttributeDescriptorModel} requires an attribute map, that maps a
 * {@link Predicate} to a list of {@link Populator}s.
 *
 * All {@link Predicate} instance in the Map will be tested and the populators will be added to the final list of populators if
 * the corresponding Predicate holds <code>true</code> for a given attribute.
 * @deprecated since 6.5
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.5")
public class ComponentTypeStructureAttributePopulator
		implements Populator<AttributeDescriptorModel, DefaultComponentTypeAttributeStructure>
{

	private Map<Predicate<AttributeDescriptorModel>, List<Populator<AttributeDescriptorModel, ComponentTypeAttributeData>>> attributePredicatePopulatorListMap;

	@Override
	public void populate(final AttributeDescriptorModel attributeDescriptor,
			final DefaultComponentTypeAttributeStructure componentTypeAttributeStructure) throws ConversionException
	{
		componentTypeAttributeStructure.setQualifier(attributeDescriptor.getQualifier());
		// do not set the type code on the attribute level, leave it for the parent populator to do set the value.

		final List<Populator<AttributeDescriptorModel, ComponentTypeAttributeData>> populators = Optional
				.ofNullable(componentTypeAttributeStructure.getPopulators())
				.orElse(new LinkedList<>());

		final List<Populator<AttributeDescriptorModel, ComponentTypeAttributeData>> typePopulators = getAttributePredicatePopulatorListMap()
				.entrySet() //
				.stream() //
				.filter(entry -> entry.getKey().test(attributeDescriptor)) //
				.flatMap(entry -> entry.getValue().stream()) //
				.collect(Collectors.toList());

		populators.addAll(typePopulators);

		componentTypeAttributeStructure.setPopulators(populators);
	}


	protected Map<Predicate<AttributeDescriptorModel>, List<Populator<AttributeDescriptorModel, ComponentTypeAttributeData>>> getAttributePredicatePopulatorListMap()
	{
		return attributePredicatePopulatorListMap;
	}

	@Required
	public void setAttributePredicatePopulatorListMap(
			final Map<Predicate<AttributeDescriptorModel>, List<Populator<AttributeDescriptorModel, ComponentTypeAttributeData>>> attributePredicatePopulatorListMap)
	{
		this.attributePredicatePopulatorListMap = attributePredicatePopulatorListMap;
	}
}
