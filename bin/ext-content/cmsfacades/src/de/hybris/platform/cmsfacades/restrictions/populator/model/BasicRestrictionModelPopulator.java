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
package de.hybris.platform.cmsfacades.restrictions.populator.model;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cmsfacades.data.AbstractRestrictionData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converts an {@link AbstractRestrictionModel} Restriction to a {@link AbstractRestrictionData} dto
 * 
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class BasicRestrictionModelPopulator implements Populator<AbstractRestrictionModel, AbstractRestrictionData>
{
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Override
	public void populate(final AbstractRestrictionModel source, final AbstractRestrictionData target) throws ConversionException
	{
		target.setUid(source.getUid());
		target.setName(source.getName());
		target.setDescription(source.getDescription());
		target.setTypeCode(source.getItemtype());
		getUniqueItemIdentifierService().getItemData(source).ifPresent(itemData ->
		{
			target.setUuid(itemData.getItemId());
		});
	}

	@Required
	public void setUniqueItemIdentifierService(final UniqueItemIdentifierService uniqueItemIdentifierService)
	{
		this.uniqueItemIdentifierService = uniqueItemIdentifierService;
	}

	protected UniqueItemIdentifierService getUniqueItemIdentifierService()
	{
		return uniqueItemIdentifierService;
	}

}
