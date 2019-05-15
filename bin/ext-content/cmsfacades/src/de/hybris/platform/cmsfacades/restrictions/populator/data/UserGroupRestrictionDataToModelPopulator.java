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

import static java.util.stream.Collectors.toList;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.model.restrictions.CMSUserGroupRestrictionModel;
import de.hybris.platform.cmsfacades.data.UserGroupRestrictionData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converts an {@link UserGroupRestrictionData} dto to a {@link CMSUserGroupRestrictionModel} restriction
 * 
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class UserGroupRestrictionDataToModelPopulator implements Populator<UserGroupRestrictionData, CMSUserGroupRestrictionModel>
{
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Override
	public void populate(final UserGroupRestrictionData source, final CMSUserGroupRestrictionModel target)
			throws ConversionException
	{
		try
		{
			target.setIncludeSubgroups(source.isIncludeSubgroups());
			target.setUserGroups(source.getUserGroups().stream()
					.map(itemId -> getUniqueItemIdentifierService().getItemModel(itemId, UserGroupModel.class).get()).collect(toList()));
		}
		catch (UnknownIdentifierException | ConversionException | NoSuchElementException e)
		{
			throw new ConversionException("Conversion failed", e);
		}
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
