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
package de.hybris.platform.acceleratorfacades.component.populators.data;

import de.hybris.platform.acceleratorcms.model.components.NavigationComponentModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSNavigationService;
import de.hybris.platform.cmsfacades.data.NavigationComponentData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * This populator will populate the {@link NavigationComponentModel} from {@link NavigationComponentData}.
 */
public class NavigationComponentReversePopulator implements Populator<NavigationComponentData, NavigationComponentModel>
{

	private CMSNavigationService navigationService;

	@Override
	public void populate(final NavigationComponentData source, final NavigationComponentModel target)
	{
		try
		{
			target.setNavigationNode(getNavigationService().getNavigationNodeForId(source.getNavigationNode()));
		}
		catch (final CMSItemNotFoundException e)
		{
			throw new ConversionException("Navigation node not found: [" + source.getNavigationNode() + "]", e);
		}

		target.setWrapAfter(StringUtils.isNotBlank(source.getWrapAfter()) ? Integer.valueOf(source.getWrapAfter()) : null);
	}

	protected CMSNavigationService getNavigationService()
	{
		return navigationService;
	}

	@Required
	public void setNavigationService(final CMSNavigationService navigationService)
	{
		this.navigationService = navigationService;
	}
}
