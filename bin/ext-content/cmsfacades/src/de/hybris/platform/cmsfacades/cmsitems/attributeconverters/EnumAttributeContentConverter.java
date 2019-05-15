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
package de.hybris.platform.cmsfacades.cmsitems.attributeconverters;

import de.hybris.platform.cms2.cmsitems.converter.AttributeContentConverter;
import de.hybris.platform.cms2.servicelayer.services.AttributeDescriptorModelHelperService;
import de.hybris.platform.cmsfacades.common.predicate.attributes.EnumTypeAttributePredicate;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;

import static java.lang.Enum.valueOf;


/**
 * Implementation of {@link AttributeContentConverter} that converts properties of type {@link Enum}
 */
public class EnumAttributeContentConverter implements AttributeContentConverter<AttributeDescriptorModel>
{

	private AttributeDescriptorModelHelperService attributeDescriptorModelHelperService;
	private EnumTypeAttributePredicate isEnumPredicate;

	@Override
	public Predicate<AttributeDescriptorModel> getConstrainedBy()
	{
		return getIsEnumPredicate();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertModelToData(AttributeDescriptorModel attribute, Object source)
	{
		if (source != null)
		{
			return ((Enum) source).name();
		}
		else
		{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object convertDataToModel(AttributeDescriptorModel attributeDescriptor, Object source)
	{

		if (source != null)
		{
			@SuppressWarnings("rawtypes")
			Class enumClass = getAttributeDescriptorModelHelperService().getAttributeClass(attributeDescriptor);

			if (enumClass.isEnum() && source instanceof String)
			{
				return valueOf(enumClass, (String) source);
			}
			else
			{
				throw new ConversionException("could not convert to enum");
			}
		}
		else
		{
			return null;
		}
	}


	@Required
	public void setAttributeDescriptorModelHelperService(
			AttributeDescriptorModelHelperService attributeDescriptorModelHelperService)
	{
		this.attributeDescriptorModelHelperService = attributeDescriptorModelHelperService;
	}

	protected AttributeDescriptorModelHelperService getAttributeDescriptorModelHelperService()
	{
		return attributeDescriptorModelHelperService;
	}

	@Required
	public void setIsEnumPredicate(EnumTypeAttributePredicate isEnumPredicate)
	{
		this.isEnumPredicate = isEnumPredicate;
	}

	protected EnumTypeAttributePredicate getIsEnumPredicate()
	{
		return isEnumPredicate;
	}
}
