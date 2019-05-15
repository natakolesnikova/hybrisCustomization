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
package de.hybris.platform.cmsfacades.restrictions.service.impl;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cmsfacades.data.AbstractRestrictionData;
import de.hybris.platform.cmsfacades.restrictions.service.RestrictionDataToModelConverter;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;


/**
 * Default implementation of {@link RestrictionDataToModelConverter}
 * 
 * @deprecated since 6.6
 */
@HybrisDeprecation(sinceVersion = "6.6")
@Deprecated
public class DefaultRestrictionDataToModelConverter implements RestrictionDataToModelConverter
{
	private String typecode;
	private AbstractPopulatingConverter<AbstractRestrictionData, AbstractRestrictionModel> converter;

	@Override
	public String getTypecode()
	{
		return typecode;
	}

	@Override
	public AbstractPopulatingConverter<AbstractRestrictionData, AbstractRestrictionModel> getConverter()
	{
		return this.converter;
	}

	public void setTypecode(final String typecode)
	{
		this.typecode = typecode;
	}

	public void setConverter(final AbstractPopulatingConverter<AbstractRestrictionData, AbstractRestrictionModel> converter)
	{
		this.converter = converter;
	}

}
