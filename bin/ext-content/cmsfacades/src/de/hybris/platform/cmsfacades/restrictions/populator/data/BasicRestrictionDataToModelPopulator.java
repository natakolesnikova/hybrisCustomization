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
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.data.AbstractRestrictionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converts an {@link AbstractRestrictionData} dto to a {@link AbstractRestrictionModel} restriction
 * 
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class BasicRestrictionDataToModelPopulator implements Populator<AbstractRestrictionData, AbstractRestrictionModel>
{
	private CMSAdminSiteService cmsAdminSiteService;

	@Override
	public void populate(final AbstractRestrictionData source, final AbstractRestrictionModel target) throws ConversionException
	{
		target.setUid(source.getUid());
		target.setName(source.getName());
		target.setCatalogVersion(getCmsAdminSiteService().getActiveCatalogVersion());
	}

	protected CMSAdminSiteService getCmsAdminSiteService()
	{
		return cmsAdminSiteService;
	}

	@Required
	public void setCmsAdminSiteService(final CMSAdminSiteService cmsAdminSiteService)
	{
		this.cmsAdminSiteService = cmsAdminSiteService;
	}

}
