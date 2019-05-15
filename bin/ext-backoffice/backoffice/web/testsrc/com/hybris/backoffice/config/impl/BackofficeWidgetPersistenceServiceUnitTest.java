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

import static com.hybris.backoffice.config.impl.BackofficeWidgetPersistenceService.WIDGET_CONFIG_MEDIA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.MockSessionService;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.daos.BackofficeConfigurationDao;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.persistence.impl.jaxb.Widgets;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.modules.CockpitModuleConnector;


@RunWith(MockitoJUnitRunner.class)
public class BackofficeWidgetPersistenceServiceUnitTest
{
	@Mock
	private ModelService modelService;
	@Mock
	private MediaService mediaService;
	@Spy
	@InjectMocks
	private BackofficeWidgetPersistenceServiceStub service;
	@Mock
	private WidgetLibUtils widgetLibUtils;
	@Mock
	private CockpitModuleConnector cockpitModuleConnector;
	@Mock
	private BackofficeConfigurationDao configurationDao;
	@Mock
	private SearchRestrictionService searchRestrictionService;
	@Mock(answer = Answers.CALLS_REAL_METHODS)
	private MockSessionService sessionService;

	@Test
	public void loadWidgetTree()
	{
		final MediaModel mediaModel = mock(MediaModel.class);
		final InputStream inputStream = mock(InputStream.class);
		final Widget widget = mock(Widget.class);
		doReturn(mediaModel).when(service).getOrCreateWidgetsConfigMedia();
		when(mediaService.getStreamFromMedia(mediaModel)).thenReturn(inputStream);
		doReturn(widget).when(service).loadWidgetTree("testId", inputStream);
		service.loadWidgetTree("testId");
		verify(service).getOrCreateWidgetsConfigMedia();
		verify(mediaService).getStreamFromMedia(mediaModel);
	}

	@Test
	public void testStoreWidgetTree()
	{
		final MediaModel mediaModel = mock(MediaModel.class);
		final InputStream inputStream = mock(InputStream.class);
		final Widget widget = mock(Widget.class);
		final Widgets widgets = mock(Widgets.class);
		doReturn(mediaModel).when(service).getOrCreateWidgetsConfigMedia();
		doReturn(Boolean.TRUE).when(service).isWidgetsConfigStoredInMedia();
		doReturn(widgets).when(service).loadWidgets(inputStream);
		when(mediaService.getStreamFromMedia(mediaModel)).thenReturn(inputStream);
		doNothing().when(service).storeWidgetTree(any(), any(), any());
		service.storeWidgetTree(widget);
		verify(service).isStoringEnabled();
		verify(service).isWidgetsConfigStoredInMedia();
		verify(service).loadWidgets(inputStream);
		verify(service).storeWidgetTree(any(), any(), any());
		verify(mediaService).setDataForMedia(any(), any());
	}

	@Test
	public void testDeleteWidgetTree()
	{
		final MediaModel mediaModel = mock(MediaModel.class);
		final InputStream inputStream = mock(InputStream.class);
		final Widget widget = mock(Widget.class);
		final Widgets widgets = mock(Widgets.class);
		doReturn(mediaModel).when(service).getOrCreateWidgetsConfigMedia();
		doReturn(widgets).when(service).loadWidgets(inputStream);
		when(mediaService.getStreamFromMedia(mediaModel)).thenReturn(inputStream);
		doNothing().when(service).storeWidgetTree(any(), any(), any());
		service.deleteWidgetTree(widget);
		verify(service).loadWidgets(inputStream);
		verify(service).deleteWidgetTreeRecursive(widgets, widget);
		verify(service).deleteOrphanedConnections(widgets);
		verify(service).storeWidgets(any(), any());
		verify(mediaService).setDataForMedia(any(), any());
	}

	@Test
	public void testResetToDefaults()
	{
		final MediaModel media = mock(MediaModel.class);
		doReturn(null).when(service).getWidgetsConfigMedia();
		doReturn(media).when(service).createWidgetsConfigMedia();
		doNothing().when(service).putDefaultWidgetsConfig(media);

		when(widgetLibUtils.libDirAbsolutePath()).thenReturn(StringUtils.EMPTY);
		when(cockpitModuleConnector.getCockpitModuleUrls()).thenReturn(Collections.emptyList());

		service.resetToDefaults();
		verify(service).getWidgetsConfigMedia();
		verify(service).createWidgetsConfigMedia();
		verify(mediaService).removeDataFromMediaQuietly(media);
		verify(service).putDefaultWidgetsConfig(media);
	}

	@Test
	public void testIsWidgetsConfigStoredInMedia()
	{
		final MediaModel media = mock(MediaModel.class);
		media.setCode(WIDGET_CONFIG_MEDIA);
		when(mediaService.getMedia(WIDGET_CONFIG_MEDIA)).thenReturn(media);
		when(Boolean.valueOf(mediaService.hasData(media))).thenReturn(Boolean.FALSE);
		assertFalse(service.isWidgetsConfigStoredInMedia());
		when(Boolean.valueOf(mediaService.hasData(media))).thenReturn(Boolean.TRUE);
		assertTrue(service.isWidgetsConfigStoredInMedia());
	}

	@Test
	public void testGetOrCreateWidgetsConfigMedia()
	{
		final MediaModel media = mock(MediaModel.class);
		when(modelService.create(CatalogUnawareMediaModel.class)).thenReturn(media);
		when(mediaService.getMedia(WIDGET_CONFIG_MEDIA)).thenReturn(null);
		doNothing().when(service).putDefaultWidgetsConfig(media);
		service.getOrCreateWidgetsConfigMedia();
		verify(service).createWidgetsConfigMedia();
		verify(service).putDefaultWidgetsConfig(media);
	}

	@Test
	public void testGetWidgetsConfigMedia()
	{
		final CatalogUnawareMediaModel media = new CatalogUnawareMediaModel();
		media.setCode(WIDGET_CONFIG_MEDIA);
		when(mediaService.getMedia(WIDGET_CONFIG_MEDIA)).thenReturn(media);
		final MediaModel widgetsConfigMedia = service.getWidgetsConfigMedia();
		assertEquals(WIDGET_CONFIG_MEDIA, widgetsConfigMedia.getCode());
		verify(mediaService).getMedia(WIDGET_CONFIG_MEDIA);
	}

	@Test
	public void testCreateWidgetsConfigMedia()
	{
		final CatalogUnawareMediaModel media = new CatalogUnawareMediaModel();
		when(modelService.create(CatalogUnawareMediaModel.class)).thenReturn(media);
		final MediaModel widgetsConfigMedia = service.createWidgetsConfigMedia();
		assertEquals(BackofficeWidgetPersistenceService.TEXT_XML_MIME_TYPE, widgetsConfigMedia.getMime());
		assertEquals(WIDGET_CONFIG_MEDIA, widgetsConfigMedia.getCode());
		verify(modelService).save(media);
	}

	@Test
	public void testPutDefaultWidgetsConfig()
	{
		final MediaModel media = mock(MediaModel.class);
		final Widgets widgets = mock(Widgets.class);
		final InputStream inputStream = mock(InputStream.class);
		doReturn(inputStream).when(service).getDefaultWidgetsConfigInputStream();
		doReturn(widgets).when(service).loadWidgets(inputStream);
		service.putDefaultWidgetsConfig(media);
		verify(service).loadWidgets(inputStream);
		verify(service).applyImports(new File("."), widgets, new HashSet<>());
		verify(service).applyExtensions(widgets);
		verify(service).storeWidgets(any(Widgets.class), any(OutputStream.class));
		verify(mediaService).setDataForMedia(any(MediaModel.class), any(byte[].class));
	}

	@Test
	public void shouldCleanUpWhenConfigurationIsAmbiguous()
	{
		// given
		when(mediaService.getMedia(WIDGET_CONFIG_MEDIA)).thenThrow(AmbiguousIdentifierException.class);

		// when
		final MediaModel widgetModel = service.getWidgetsConfigMedia();

		// then
		verify(service).removeAmbiguousConfiguration();
		assertNull(widgetModel);
	}

	@Test
	public void loadWidgetTreeTest()
	{
		// assign
		final String widgetId = "widget";
		final Widget widget = Mockito.mock(Widget.class);
		final MediaModel media = Mockito.mock(MediaModel.class);
		final InputStream input = Mockito.mock(InputStream.class);

		doReturn(media).when(mediaService).getMedia(WIDGET_CONFIG_MEDIA);
		doReturn(input).when(mediaService).getStreamFromMedia(media);
		doReturn(widget).when(service).loadWidgetTree(widgetId, input);

		// act
		final Widget result = service.loadWidgetTree(widgetId);

		// assert
		assertEquals(widget, result);
		verify(service).loadWidgetTree(widgetId);
		verify(service).loadWidgetTree(widgetId, input);
	}

	/**
	 * Class to stub protected methods from super class
	 */
	public static class BackofficeWidgetPersistenceServiceStub extends BackofficeWidgetPersistenceService
	{
		/**
		 * @deprecated since 6.5
		 */
		@Deprecated
		@Override
		protected boolean applyImports(final File currentPath, final Widgets widgets, final Set<String> alreadyImportedResources)
		{
			return true;
		}

		@Override
		protected void applyExtensions(final Widgets widgets)
		{
			// do nothing
		}

		@Override
		protected Widgets loadWidgets(final InputStream inputStream)
		{
			return new Widgets();
		}

		@Override
		protected void storeWidgets(final Widgets widgets, final OutputStream outputStream)
		{
			// do nothing
		}

		@Override
		protected void deleteWidgetTreeRecursive(final Widgets widgets, final Widget node)
		{
			// do nothing
		}

		@Override
		protected void deleteOrphanedConnections(final Widgets widgets)
		{
			// do nothing
		}
	}
}
