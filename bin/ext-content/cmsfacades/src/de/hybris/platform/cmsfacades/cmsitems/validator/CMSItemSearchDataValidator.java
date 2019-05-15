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
package de.hybris.platform.cmsfacades.cmsitems.validator;

import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.CMSItemSearchData;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * CMSItemSearchDataValidator validates CMSItemSearchData objects, which are the search parameters
 * used to perform {@link de.hybris.platform.cms2.cmsitems.service.CMSItemSearchService} searches
 */
public class CMSItemSearchDataValidator implements Validator
{

	public static final String FIELD_NAME_CATALOG_ID = "catalogId";
	public static final String FIELD_NAME_CATALOG_VERSION = "catalogVersion";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return CMSItemSearchData.class.isAssignableFrom(aClass);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		ValidationUtils.rejectIfEmpty(errors, FIELD_NAME_CATALOG_ID, CmsfacadesConstants.FIELD_REQUIRED);
		ValidationUtils.rejectIfEmpty(errors, FIELD_NAME_CATALOG_VERSION, CmsfacadesConstants.FIELD_REQUIRED);
	}
}
