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
import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.types.service.ComponentTypeAttributeStructure;
import de.hybris.platform.cmsfacades.types.service.impl.DefaultComponentTypeStructure;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.*;
import java.util.function.Function;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import com.google.common.collect.Sets;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Default populator from {@link ComposedTypeModel} types to {@link DefaultComponentTypeStructure}. 
 * 
 * Requires an list of attributes to be ignored per type, the blacklist, also requires a function to identify the 
 * corresponding Java Data bean for the {@link ComposedTypeModel}.
 * 
 * For each attribute {@link AttributeDescriptorModel} defined for a given type, 
 * it invokes the another converter to produce an instance of {@link ComponentTypeAttributeStructure}.
 * 
 * This Populator expects to populate only {@code CMSItem} types.  
 * @deprecated since 6.5
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.5")
public class ComponentTypeStructurePopulator
		implements Populator<ComposedTypeModel, DefaultComponentTypeStructure>, InitializingBean
{

	private TypeService typeService;
	private Function<ComposedTypeModel, Class> typeDataClassFunction;
	private Converter<AttributeDescriptorModel, ComponentTypeAttributeStructure> attributeStructureConverter;

	private Map<String, String> structureTypeBlacklistAttributeMap;

	private Map<String, Set<String>> blacklistAttributes;
	
	@Override
	public void populate(final ComposedTypeModel composedType, final DefaultComponentTypeStructure componentTypeStructure)
			throws ConversionException
	{
		componentTypeStructure.setTypecode(composedType.getCode());
		componentTypeStructure.setTypeDataClass(getTypeDataClassFunction().apply(composedType));

		final List<ComponentTypeAttributeStructure> attributes = new LinkedList<>();

		collectAttributes(composedType, attributes);

		// set type code for all attributes
		attributes.stream().forEach(componentTypeAttributeStructure -> {
			componentTypeAttributeStructure.setTypecode(composedType.getCode());
		});

		componentTypeStructure.getAttributes().addAll(attributes);
	}

	/**
	 * Recursively collects attributes of a given type that is not blacklisted until it reaches the root type (CMSItem). 
	 * @param composedType the type which the attributes are collected from.
	 * @param attributes the attribute list that will be augmented by introspecting the <code>composedType</code>. 
	 */
	protected void collectAttributes(final ComposedTypeModel composedType,
			final List<ComponentTypeAttributeStructure> attributes)
	{
		final Set<String> blackListAttributes = Optional //
				.ofNullable(getBlacklistAttributes().get(composedType.getCode())) //
				.orElse(Sets.newHashSet());
		attributes.addAll(composedType.getDeclaredattributedescriptors() //
							.stream() //
							.filter(attribute -> !blackListAttributes.contains(attribute.getQualifier())) //
							.map(getAttributeStructureConverter()::convert) //
							.collect(toList()));
		
		// check if it is necessary to continue digging for new attributes
		if (!StringUtils.equals(composedType.getCode(), CMSItemModel._TYPECODE) && composedType.getSuperType() != null)
		{
			collectAttributes(composedType.getSuperType(), attributes);	
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

	protected Function<ComposedTypeModel, Class> getTypeDataClassFunction()
	{
		return typeDataClassFunction;
	}

	@Required
	public void setTypeDataClassFunction(final Function<ComposedTypeModel, Class> typeDataClassFunction)
	{
		this.typeDataClassFunction = typeDataClassFunction;
	}

	protected Converter<AttributeDescriptorModel, ComponentTypeAttributeStructure> getAttributeStructureConverter()
	{
		return attributeStructureConverter;
	}

	@Required
	public void setAttributeStructureConverter(
			final Converter<AttributeDescriptorModel, ComponentTypeAttributeStructure> attributeStructureConverter)
	{
		this.attributeStructureConverter = attributeStructureConverter;
	}

	public Map<String, String> getStructureTypeBlacklistAttributeMap()
	{
		return structureTypeBlacklistAttributeMap;
	}

	@Required
	public void setStructureTypeBlacklistAttributeMap(final Map<String, String> structureTypeBlacklistAttributeMap)
	{
		this.structureTypeBlacklistAttributeMap = structureTypeBlacklistAttributeMap;
	}

	protected Map<String, Set<String>> getBlacklistAttributes()
	{
		return blacklistAttributes;
	}

	public void setBlacklistAttributes(final Map<String, Set<String>> blacklistAttributes)
	{
		this.blacklistAttributes = blacklistAttributes;
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		blacklistAttributes = getStructureTypeBlacklistAttributeMap()
				.entrySet().stream() //
				.collect(toMap(entry -> entry.getKey(), entry -> {
					final String[] attributes = entry.getValue().replaceAll("^[,\\s]+", "").split("[,\\s]+");
					return Sets.newHashSet(attributes);	
				}));
	}
}
