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
package de.hybris.platform.cmsfacades.items.populator.model;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Default abstract implementation of {@link Populator}, will serve the first conversion step for each component.
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class BasicCMSComponentModelPopulator implements Populator<AbstractCMSComponentModel, AbstractCMSComponentData>
{

	@Override
	public void populate(final AbstractCMSComponentModel source, final AbstractCMSComponentData target) throws ConversionException
	{
		target.setPk(source.getPk().toString());
		target.setCreationtime(source.getCreationtime());
		target.setModifiedtime(source.getModifiedtime());
		target.setUid(source.getUid());
		target.setName(source.getName());
		target.setVisible(source.getVisible());
		target.setTypeCode(source.getItemtype());
	}
}
