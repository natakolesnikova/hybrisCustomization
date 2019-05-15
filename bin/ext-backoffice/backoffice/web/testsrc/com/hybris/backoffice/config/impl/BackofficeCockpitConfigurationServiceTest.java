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
package com.hybris.backoffice.config.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.MockSessionService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.cache.ConfigurationCache;


public class BackofficeCockpitConfigurationServiceTest
{

	@InjectMocks
	private final BackofficeCockpitConfigurationService backofficeCockpitConfigurationService = new BackofficeCockpitConfigurationService();

	@Mock
	private MediaService mediaService;

	@Mock
	private ModelService modelService;

	@Mock
	private CatalogVersionService catalogVersionService;

	@Mock
	private UserService userService;

	@Mock(answer = Answers.CALLS_REAL_METHODS)
	private MockSessionService sessionService;

	@Mock
	private EmployeeModel admin;

	@Mock
	private TimeService timeService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		backofficeCockpitConfigurationService.setSessionService(sessionService);
		when(userService.getAdminUser()).thenReturn(admin);
	}

	@Test
	public void getConfigFileInputStreamTest() throws FileNotFoundException, CockpitConfigurationException
	{
		final InputStream inputStream = Mockito.mock(InputStream.class);
		final MediaModel media = Mockito.mock(MediaModel.class);
		media.setCode(Mockito.anyString());
		when(mediaService.getMedia(Mockito.anyString())).thenReturn(media);
		when(backofficeCockpitConfigurationService.getCockpitNGConfig()).thenReturn(media);
		when(mediaService.getStreamFromMedia(media)).thenReturn(inputStream);
		assertNotNull(backofficeCockpitConfigurationService.getConfigFileInputStream());
	}

	@Test
	public void getCockpitNGConfigTest() throws CockpitConfigurationException
	{
		final MediaModel media = Mockito.mock(MediaModel.class);
		media.setCode(Mockito.anyString());
		when(mediaService.getMedia(Mockito.anyString())).thenReturn(media);
		assertNotNull(backofficeCockpitConfigurationService.getCockpitNGConfig());
		assertEquals(media, backofficeCockpitConfigurationService.getCockpitNGConfig());
	}

	@Test
	public void createConfigFileTest()
	{
		final CatalogUnawareMediaModel media = Mockito.mock(CatalogUnawareMediaModel.class);
		when(modelService.create(CatalogUnawareMediaModel.class)).thenReturn(media);
		assertNotNull(backofficeCockpitConfigurationService.createConfigFile());
		assertEquals(media, backofficeCockpitConfigurationService.createConfigFile());
	}

	@Test
	public void storeRootConfig()
	{
		final ConfigurationCache cache = Mockito.mock(ConfigurationCache.class);
		final SessionService sessionService = Mockito.mock(SessionService.class);
		final com.hybris.cockpitng.core.config.impl.jaxb.Config config = Mockito
				.mock(com.hybris.cockpitng.core.config.impl.jaxb.Config.class);

		backofficeCockpitConfigurationService.setConfigurationCache(cache);
		backofficeCockpitConfigurationService.setSessionService(sessionService);

		final long timeInMills = System.currentTimeMillis();
		when(timeService.getCurrentTime()).thenReturn(new Date(timeInMills));
		Mockito.when(backofficeCockpitConfigurationService.getSessionService().executeInLocalView(any(), any()))
				.thenReturn(new MediaModel());

		try
		{
			backofficeCockpitConfigurationService.storeRootConfig(config);
		}
		catch (final CockpitConfigurationException e)
		{
			e.printStackTrace();
			Assert.fail("Could not store Configuration");
		}

		verify(cache, times(2)).cacheRootConfiguration(eq(config), anyLong());
		verify(cache, times(1)).cacheRootConfiguration(config, timeInMills);
		verify(cache, times(1)).cacheRootConfiguration(config, 0);
	}
}
