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
package de.hybris.platform.acceleratorfacades.cmsitems.attributeconverters;

import static java.util.stream.Collectors.toMap;

import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Required;


/**
 * Attribute Converter for {@link MediaContainerModel}
 */
public class MediaContainerAttributeToDataContentConverter implements Converter<MediaContainerModel, Map<String, String>>
{

	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Override
	public Map<String, String> convert(final MediaContainerModel source) throws ConversionException
	{
		if (Objects.isNull(source))
		{
			return null;
		}
		return source.getMedias()
				.stream()
				.filter(media -> getUniqueItemIdentifierService().getItemData(media).isPresent())
				.filter(media -> media.getMediaFormat() != null)
				.collect(
						toMap(
								media -> media.getMediaFormat().getQualifier(),
								media -> getUniqueItemIdentifierService().getItemData(media).get().getItemId()));
	}

	protected UniqueItemIdentifierService getUniqueItemIdentifierService()
	{
		return uniqueItemIdentifierService;
	}

	@Required
	public void setUniqueItemIdentifierService(final UniqueItemIdentifierService uniqueItemIdentifierService)
	{
		this.uniqueItemIdentifierService = uniqueItemIdentifierService;
	}
}
