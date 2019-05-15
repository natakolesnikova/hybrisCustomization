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
package de.hybris.platform.cmsfacades.items.validator.consumer;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.common.function.ValidationConsumer;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.Locale;
import java.util.Objects;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;


/**
 * Validates localized media code attributes
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class LocalizedFormatMediaAttributeValidationConsumer implements ValidationConsumer<LocalizedValidationData>
{

	private MediaService mediaService;
	private CMSAdminSiteService cmsAdminSiteService;


	/**
	 * Tests if the Media Code is valid.
	 *
	 * @param target
	 *           {@link LocalizedValidationData} validatee
	 */
	@Override
	public void accept(final LocalizedValidationData target, final Errors errors)
	{
		String mediaFormat = null;
		if (target.getOptionals().containsKey(CmsfacadesConstants.MEDIA_FORMAT))
		{
			mediaFormat = target.getOptionals().get(CmsfacadesConstants.MEDIA_FORMAT);
		}

		validate(target.getFieldName(), target.getValue(), errors, target.getLocale(), target.isRequiredLanguage(), mediaFormat);
	}


	/**
	 * Default implementation to validate Media code values.<br>
	 * An "invalid media code" error is created if the given mediaCode is invalid.
	 *
	 * @param mediaFieldName
	 *           the attribute field's name
	 * @param mediaCode
	 *           the Media's code being validated
	 * @param errors
	 *           the error object holding the validation errors
	 * @param locale
	 *           the locale in which the current value has significance
	 */
	/*
	 * Suppress sonar warning (squid:S1166 | Exception handlers should preserve the original exceptions) : It is
	 * perfectly acceptable not to handle "e" here
	 */
	@SuppressWarnings("squid:S1166")
	protected void validate(final String mediaFieldName, final String mediaCode, final Errors errors, final Locale locale,
			final Boolean requiredLanguage, final String mediaFormat)
	{
		try
		{
			if (!Objects.isNull(mediaCode) && Strings.isNotEmpty(mediaCode.trim()))
			{
				final MediaModel media = getMediaService().getMedia(getCmsAdminSiteService().getActiveCatalogVersion(), mediaCode);

				if (Objects.isNull(media))
				{
					reject(mediaFieldName, errors, locale, requiredLanguage, mediaCode);
				}
			}

		}
		catch (UnknownIdentifierException | AmbiguousIdentifierException e)
		{
			reject(mediaFieldName, errors, locale, requiredLanguage, mediaCode);
		}
	}

	/**
	 * Reject Media Code' default implementation.
	 *
	 * @param mediaFieldName
	 *           the attribute field's name
	 * @param errors
	 *           the error object holding the validation errors
	 * @param locale
	 *           the locale in which the current value has significance
	 * @param requiredLanguage
	 */
	protected void reject(final String mediaFieldName, final Errors errors, final Locale locale, final Boolean requiredLanguage,
			final String mediaFormat)
	{
		if (locale != null && requiredLanguage)
		{
			errors.rejectValue(mediaFieldName, CmsfacadesConstants.INVALID_MEDIA_FORMAT_MEDIA_CODE_L10N, new Object[]
					{ locale, mediaFormat }, null);
		}
		else if (locale == null)
		{
			errors.rejectValue(mediaFieldName, CmsfacadesConstants.INVALID_MEDIA_FORMAT_MEDIA_CODE, new Object[]
					{ mediaFormat }, null);
		}
	}

	protected MediaService getMediaService()
	{
		return mediaService;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
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
