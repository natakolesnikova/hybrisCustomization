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
package de.hybris.platform.acceleratorfacades.component.validator;

import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.ProductCarouselComponentData;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates fields of {@link ProductCarouselComponentData}
 */
public class ProductCarouselComponentValidator implements Validator
{

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return ProductCarouselComponentData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final ProductCarouselComponentData target = (ProductCarouselComponentData) obj;

		if (CollectionUtils.isEmpty(target.getProducts()) && CollectionUtils.isEmpty(target.getCategories()))
		{
			errors.rejectValue(ProductCarouselComponentModel.PRODUCTS, CmsfacadesConstants.PRODUCTS_OR_CATEGORIES_REQUIRED);
		}

	}

}
