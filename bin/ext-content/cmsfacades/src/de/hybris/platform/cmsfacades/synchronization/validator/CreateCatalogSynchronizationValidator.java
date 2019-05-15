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
package de.hybris.platform.cmsfacades.synchronization.validator;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.UNAUTHORIZED_SYNCHRONIZATION_READ;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.UNAUTHORIZED_SYNCHRONIZATION_WRITE;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmsfacades.data.SyncJobRequestData;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates the request of synchronization between two catalog versions.
 *
 * @see SyncJobRequestData
 */
public class CreateCatalogSynchronizationValidator implements Validator
{
	private static final String SOURCE_NAME_ATTR = "sourceVersionId";

	private CatalogVersionService catalogVersionService;
	private UserService userService;

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return SyncJobRequestData.class.isAssignableFrom(aClass);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final SyncJobRequestData syncJobRequestData = (SyncJobRequestData) target;

		try
		{
			final CatalogVersionModel sourceCatalog = getCatalogVersionModel(syncJobRequestData.getCatalogId(),
					syncJobRequestData.getSourceVersionId());

			final UserModel userModel = getUserService().getCurrentUser();
			final boolean canWriteSource = getCatalogVersionService().canWrite(sourceCatalog, userModel);
			if (!canWriteSource) {
				errors.rejectValue(SOURCE_NAME_ATTR, UNAUTHORIZED_SYNCHRONIZATION_WRITE);
			}
		}
		catch (final UnknownIdentifierException e)
		{
			/**
			 * catalogId, sourceVersionId and/or targetVersionId could be invalid. No need to log this error again because
			 * it should be caught by the {@link SyncJobRequestValidator} already.
			 */
		}
	}

	/**
	 * Gets the catalogVersionModel
	 *
	 * @param catalog
	 *           the catalog name
	 * @param catalogVersion
	 *           the catalog version name
	 * @return the catalogVersionModel
	 */
	protected CatalogVersionModel getCatalogVersionModel(final String catalog, final String catalogVersion)
			throws UnknownIdentifierException
	{
		return getCatalogVersionService().getCatalogVersion(catalog, catalogVersion);
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}
}
