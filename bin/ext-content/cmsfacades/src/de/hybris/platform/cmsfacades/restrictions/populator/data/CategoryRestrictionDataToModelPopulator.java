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
package de.hybris.platform.cmsfacades.restrictions.populator.data;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.cmsfacades.common.service.ProductCatalogItemModelFinder;
import de.hybris.platform.cmsfacades.data.CategoryRestrictionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converts an {@link CategoryRestrictionData} dto to a {@link CMSCategoryRestrictionModel} restriction
 * 
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class CategoryRestrictionDataToModelPopulator implements Populator<CategoryRestrictionData, CMSCategoryRestrictionModel>
{
	private ProductCatalogItemModelFinder productCatalogItemModelFinder;

	@Override
	public void populate(final CategoryRestrictionData source, final CMSCategoryRestrictionModel target)
			throws ConversionException
	{
		target.setRecursive(source.isRecursive());
		target.setCategories(getProductCatalogItemModelFinder().getCategoriesForCompositeKeys(source.getCategories()));
	}

	protected ProductCatalogItemModelFinder getProductCatalogItemModelFinder()
	{
		return productCatalogItemModelFinder;
	}

	@Required
	public void setProductCatalogItemModelFinder(final ProductCatalogItemModelFinder productCatalogItemModelFinder)
	{
		this.productCatalogItemModelFinder = productCatalogItemModelFinder;
	}
}