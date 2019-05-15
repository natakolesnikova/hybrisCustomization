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
package de.hybris.platform.cmsfacades.types.populator;

import de.hybris.platform.cms2.servicelayer.services.AttributeDescriptorModelHelperService;
import de.hybris.platform.cmsfacades.data.ComponentTypeAttributeData;
import de.hybris.platform.cmsfacades.data.OptionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;


/**
 * Populator aimed at setting all necessary information for the receiving end to build a dropdown widget for a
 * {@link HybrisEnumValue}
 */
public class CmsStructureEnumTypeComponentTypeAttributePopulator
		implements Populator<AttributeDescriptorModel, ComponentTypeAttributeData>
{

	private static final String DOT = ".";
	private String prefix;
	private String suffix;

	private AttributeDescriptorModelHelperService attributeDescriptorModelHelperService;

	@Override
	public void populate(final AttributeDescriptorModel source, final ComponentTypeAttributeData target)
			throws ConversionException
	{
		Class enumClass = getAttributeDescriptorModelHelperService().getAttributeClass(source);

		if (enumClass.getEnumConstants() != null)
		{
			List<OptionData> options = asList(enumClass.getEnumConstants())
					.stream()
					.map(enumValue ->
					{
						OptionData optionData = new OptionData();
						Enum val = (Enum) enumValue;
						optionData.setId(val.name());
						optionData.setLabel(getPrefix() + DOT + enumClass.getSimpleName().toLowerCase() + DOT
								+ val.name().toLowerCase() + DOT + getSuffix());

						return optionData;
					}).collect(toList());

			target.setOptions(options);
		}
		target.setIdAttribute("value");
		target.setLabelAttributes(asList("label"));
		target.setPaged(false);

	}


	protected AttributeDescriptorModelHelperService getAttributeDescriptorModelHelperService()
	{
		return attributeDescriptorModelHelperService;
	}

	@Required
	public void setAttributeDescriptorModelHelperService(
			AttributeDescriptorModelHelperService attributeDescriptorModelHelperService)
	{
		this.attributeDescriptorModelHelperService = attributeDescriptorModelHelperService;
	}

	protected String getPrefix()
	{
		return prefix;
	}

	@Required
	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	protected String getSuffix()
	{
		return suffix;
	}

	@Required
	public void setSuffix(String suffix)
	{
		this.suffix = suffix;
	}

}
