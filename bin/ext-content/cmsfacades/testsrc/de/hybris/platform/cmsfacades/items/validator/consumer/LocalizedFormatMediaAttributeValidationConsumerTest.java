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
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.SimpleResponsiveBannerComponentData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
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
public class LocalizedFormatMediaAttributeValidationConsumerTest
{
	private static final String MEDIA_FIELD_NAME = "media";
	private static final String MEDIA_FORMAT_MOBILE = "mobile";

	@Mock
	private MediaService mediaService;
	@Mock
	private CMSAdminSiteService cmsAdminSiteService;

	@InjectMocks
	private final LocalizedFormatMediaAttributeValidationConsumer mediaAttributePredicate = new LocalizedFormatMediaAttributeValidationConsumer();

	@Spy
	private final Errors errors = new BeanPropertyBindingResult(new SimpleResponsiveBannerComponentData(),
			SimpleResponsiveBannerComponentData.class.getSimpleName());

	@Mock
	private CatalogVersionModel catalogVersion;

	private final Map<String, String> optionalValidationData = new HashMap<>();

	@Before
	public void setup()
	{
		when(cmsAdminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersion);
		optionalValidationData.put(CmsfacadesConstants.MEDIA_FORMAT, MEDIA_FORMAT_MOBILE);
	}

	@Test
	public void testValidNonLocalizedMedia()
	{
		final String mediaCode = "my-media-code";

		when(mediaService.getMedia(catalogVersion, mediaCode)).thenReturn(new MediaModel());

		final LocalizedValidationData errorContainer = new LocalizedValidationData.Builder().setFieldName(MEDIA_FIELD_NAME)
				.setValue(mediaCode).setRequiredLanguage(false).setLocale(null).setOptionals(optionalValidationData).build();

		mediaAttributePredicate.accept(errorContainer, errors);
		verifyZeroInteractions(errors);

	}

	@Test
	public void testInvalidMedia()
	{
		final String mediaCode = "my-media-code";

		when(mediaService.getMedia(catalogVersion, mediaCode)).thenReturn(null);

		final LocalizedValidationData errorContainer = new LocalizedValidationData.Builder().setFieldName(MEDIA_FIELD_NAME)
				.setValue(mediaCode).setRequiredLanguage(false).setLocale(null).setOptionals(optionalValidationData).build();

		mediaAttributePredicate.accept(errorContainer, errors);
		verify(errors).rejectValue(anyString(), anyString(), any(), any());
	}

	@Test
	public void testValidLocalizedMediaRequiredLanguage()
	{
		final String mediaCode = "my-media-code";

		when(mediaService.getMedia(catalogVersion, mediaCode)).thenReturn(new MediaModel());

		final LocalizedValidationData errorContainer = new LocalizedValidationData.Builder().setFieldName(MEDIA_FIELD_NAME)
				.setValue(mediaCode).setRequiredLanguage(true).setLocale(Locale.ENGLISH).setOptionals(optionalValidationData).build();

		mediaAttributePredicate.accept(errorContainer, errors);
		verifyZeroInteractions(errors);
	}


	@Test
	public void testInvalidLocalizedMediaRequiredLanguage()
	{
		final String mediaCode = "my-media-code";

		when(mediaService.getMedia(catalogVersion, mediaCode)).thenReturn(null);

		final LocalizedValidationData errorContainer = new LocalizedValidationData.Builder().setFieldName(MEDIA_FIELD_NAME)
				.setValue(mediaCode).setRequiredLanguage(true).setLocale(Locale.ENGLISH).setOptionals(optionalValidationData).build();

		mediaAttributePredicate.accept(errorContainer, errors);
		verify(errors).rejectValue(anyString(), anyString(), any(), any());

	}

	@Test
	public void testInvalidLocalizedMediaExceptionThrown()
	{
		final String mediaCode = "my-media-code";

		when(mediaService.getMedia(catalogVersion, mediaCode)).thenThrow(new UnknownIdentifierException(""));

		final LocalizedValidationData errorContainer = new LocalizedValidationData.Builder().setFieldName(MEDIA_FIELD_NAME)
				.setValue(mediaCode).setRequiredLanguage(true).setLocale(Locale.ENGLISH).setOptionals(optionalValidationData).build();

		mediaAttributePredicate.accept(errorContainer, errors);
		verify(errors).rejectValue(anyString(), anyString(), any(), any());

	}

	@Test
	public void testInvalidLocalizedMediaEmptyCode()
	{
		final LocalizedValidationData errorContainer = new LocalizedValidationData.Builder().setFieldName(MEDIA_FIELD_NAME)
				.setValue("        ").setRequiredLanguage(true).setLocale(Locale.ENGLISH).setOptionals(optionalValidationData)
				.build();

		mediaAttributePredicate.accept(errorContainer, errors);
		verifyZeroInteractions(mediaService);
		verifyZeroInteractions(errors);
	}
}
