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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.data.BannerComponentData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LocalizedMediaAttributeValidationConsumerTest
{

	private static final String MEDIA_FIELD_NAME = "media";

	@Mock
	private MediaService mediaService;
	@Mock
	private CMSAdminSiteService cmsAdminSiteService;

	@InjectMocks
	private final LocalizedMediaAttributeValidationConsumer mediaAttributePredicate = new LocalizedMediaAttributeValidationConsumer();

	@Spy
	private final Errors errors = new BeanPropertyBindingResult(new BannerComponentData(),
			BannerComponentData.class.getSimpleName());

	@Test
	public void testValidNonLocalizedMedia()
	{
		final String mediaCode = "my-media-code";

		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), mediaCode)).thenReturn(new MediaModel());

		final LocalizedValidationData errorContainer = new LocalizedValidationData.Builder().setFieldName(MEDIA_FIELD_NAME)
				.setValue(mediaCode).setRequiredLanguage(false).setLocale(null).build();

		mediaAttributePredicate.accept(errorContainer, errors);
		verifyZeroInteractions(errors);

	}

	@Test
	public void testInvalidMedia()
	{
		final String mediaCode = "my-media-code";

		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), mediaCode)).thenReturn(null);

		final LocalizedValidationData errorContainer = new LocalizedValidationData.Builder().setFieldName(MEDIA_FIELD_NAME)
				.setValue(mediaCode).setRequiredLanguage(false).setLocale(null).build();

		mediaAttributePredicate.accept(errorContainer, errors);
		verify(errors).rejectValue(anyString(), anyString());
	}

	@Test
	public void testValidLocalizedMediaRequiredLanguage()
	{
		final String mediaCode = "my-media-code";

		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), mediaCode)).thenReturn(new MediaModel());

		final LocalizedValidationData errorContainer = new LocalizedValidationData.Builder().setFieldName(MEDIA_FIELD_NAME)
				.setValue(mediaCode).setRequiredLanguage(true).setLocale(Locale.ENGLISH).build();

		mediaAttributePredicate.accept(errorContainer, errors);
		verifyZeroInteractions(errors);
	}


	@Test
	public void testInvalidLocalizedMediaRequiredLanguage()
	{
		final String mediaCode = "my-media-code";

		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), mediaCode)).thenReturn(null);

		final LocalizedValidationData errorContainer = new LocalizedValidationData.Builder().setFieldName(MEDIA_FIELD_NAME)
				.setValue(mediaCode).setRequiredLanguage(true).setLocale(Locale.ENGLISH).build();

		mediaAttributePredicate.accept(errorContainer, errors);
		verify(errors).rejectValue(anyString(), anyString(), any(), any());

	}


	@Test
	public void testInvalidLocalizedMediaExceptionThrown()
	{
		final String mediaCode = "my-media-code";

		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), mediaCode))
		.thenThrow(new UnknownIdentifierException(""));

		final LocalizedValidationData errorContainer = new LocalizedValidationData.Builder().setFieldName(MEDIA_FIELD_NAME)
				.setValue(mediaCode).setRequiredLanguage(true).setLocale(Locale.ENGLISH).build();

		mediaAttributePredicate.accept(errorContainer, errors);
		verify(errors).rejectValue(anyString(), anyString(), any(), any());

	}
}
