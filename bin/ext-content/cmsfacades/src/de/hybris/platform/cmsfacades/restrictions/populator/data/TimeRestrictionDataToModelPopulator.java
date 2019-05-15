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
import de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel;
import de.hybris.platform.cmsfacades.data.TimeRestrictionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Converts an {@link TimeRestrictionData} dto to a {@link CMSTimeRestrictionModel} restriction
 * 
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class TimeRestrictionDataToModelPopulator implements Populator<TimeRestrictionData, CMSTimeRestrictionModel>
{

	@Override
	public void populate(final TimeRestrictionData source, final CMSTimeRestrictionModel target) throws ConversionException
	{
		target.setActiveFrom(source.getActiveFrom());
		target.setActiveUntil(source.getActiveUntil());
	}

}
