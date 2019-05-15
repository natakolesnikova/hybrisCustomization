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
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.cmsfacades.data.CategoryRestrictionData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converts an {@link CMSCategoryRestrictionModel} Restriction to a {@link CategoryRestrictionData} dto
 * 
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class CategoryRestrictionModelToDataPopulator implements Populator<CMSCategoryRestrictionModel, CategoryRestrictionData>
{
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Override
	public void populate(final CMSCategoryRestrictionModel source, final CategoryRestrictionData target) throws ConversionException
	{
		target.setRecursive(source.isRecursive());
		final List<String> categories = source.getCategories().stream().map(category ->
		{
			return getUniqueItemIdentifierService().getItemData(category).map(itemData ->
			{
				return itemData.getItemId();
			}).get();
		}).collect(Collectors.toList());
		target.setCategories(categories);
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