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
package com.hybris.backoffice.excel.export.wizard.renderer;

import static com.hybris.backoffice.excel.ExcelConstants.EXCEL_FORM_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hybris.backoffice.attributechooser.Attribute;
import com.hybris.backoffice.attributechooser.AttributeChooserForm;
import com.hybris.backoffice.attributechooser.AttributesChooserConfig;
import com.hybris.backoffice.excel.export.wizard.ExcelExportWizardForm;
import com.hybris.backoffice.excel.translators.ExcelTranslatorRegistry;
import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;


@RunWith(MockitoJUnitRunner.class)
public class ExcelExportRendererTest
{
	public static final String TYPE_NAME = "Product";
	@Mock
	private Pageable pageable;
	@Mock
	private TypeService typeService;
	@Mock
	private PermissionFacade permissionFacade;
	@Mock
	private LabelService labelService;
	@Mock
	private CockpitLocaleService cockpitLocaleService;
	@Mock
	private ExcelTranslatorRegistry excelTranslatorRegistry;
	@Mock
	private LanguageModel english;
	@Mock
	private LanguageModel german;
	@Mock
	private AttributeDescriptorModel code;
	@Mock
	private AttributeDescriptorModel name;
	@Mock
	private NotificationService notificationService;
	private WidgetInstanceManager wim;
	private Map<String, String> params;
	@Mock
	private WidgetComponentRenderer<Component, AttributesChooserConfig, AttributeChooserForm> attributesChooserRenderer;
	@Mock
	private WidgetComponentRenderer<Component, Object, com.hybris.backoffice.excel.export.wizard.renderer.attributechooser.AttributeChooserForm> attributeChooserRenderer;
	@InjectMocks
	private ExcelExportRenderer renderer;

	private Component parent;
	private final ExcelExportWizardForm form = new ExcelExportWizardForm();


	@Before
	public void setUp()
	{
		CockpitTestUtil.mockZkEnvironment();
		wim = CockpitTestUtil.mockWidgetInstanceManager();
		params = new HashMap<>();
		when(code.getQualifier()).thenReturn("code");
		when(code.getReadable()).thenReturn(true);
		when(code.getWritable()).thenReturn(true);
		when(name.getQualifier()).thenReturn("name");
		when(name.getReadable()).thenReturn(true);
		when(name.getWritable()).thenReturn(true);
		when(name.getLocalized()).thenReturn(true);

		parent = new Div();
		wim.getModel().setValue(EXCEL_FORM_PROPERTY, form);
		form.setPageable(pageable);
		when(pageable.getTypeCode()).thenReturn(TYPE_NAME);

		when(labelService.getObjectLabel(any())).thenReturn("label");
		when(permissionFacade.canReadProperty(any(), any())).thenReturn(true);

		when(english.getActive()).thenReturn(true);
		when(english.getIsocode()).thenReturn("en");
		when(german.getActive()).thenReturn(true);
		when(german.getIsocode()).thenReturn("de");

		final List<LanguageModel> languages = Lists.newArrayList(english, german);
		final Set<Locale> locales = languages.stream().map(lang -> Locale.forLanguageTag(lang.getIsocode()))
				.collect(Collectors.toSet());
		when(permissionFacade.getAllReadableLocalesForCurrentUser()).thenReturn(locales);
		final Locale currentLocale = Locale.forLanguageTag(english.getIsocode());
		when(cockpitLocaleService.getCurrentLocale()).thenReturn(currentLocale);

		final Set<AttributeDescriptorModel> attributes = Sets.newHashSet(code, name);
		when(typeService.getAttributesForModifiers(any(), any())).thenReturn(attributes);
		when(typeService.getAttributeDescriptor(TYPE_NAME, "code")).thenReturn(code);
		when(typeService.getAttributeDescriptor(TYPE_NAME, "name")).thenReturn(name);

		when(excelTranslatorRegistry.canHandle(any())).thenReturn(true);

	}


	@Test
	public void shouldCreateLocalizedChildren()
	{
		// given
		when(code.getReadable()).thenReturn(true);
		when(name.getReadable()).thenReturn(true);

		// when
		renderer.render(parent, null, params, null, wim);

		// then
		final ArgumentCaptor<AttributeChooserForm> pickerForm = ArgumentCaptor.forClass(AttributeChooserForm.class);
		verify(attributesChooserRenderer).render(eq(parent), any(), pickerForm.capture(), any(), eq(wim));

		assertThat(pickerForm.getValue().getChosenAttributes()).hasSize(2);
		assertThat(pickerForm.getValue().getChosenAttributes().stream().map(Attribute::getQualifier))
				.containsOnly(code.getQualifier(), name.getQualifier());
		final Optional<Attribute> chosenName = pickerForm.getValue().getChosenAttributes().stream()
				.filter(attr -> attr.getQualifier().equals(this.name.getQualifier())).findAny();
		assertThat(chosenName.get().getSubAttributes()).hasSize(1);
		assertThat(chosenName.get().getSubAttributes().iterator().next().getIsoCode()).isEqualTo(english.getIsocode());

		assertThat(pickerForm.getValue().getAvailableAttributes()).hasSize(1);
		assertThat(pickerForm.getValue().getAvailableAttributes().stream().map(Attribute::getQualifier))
				.containsOnly(name.getQualifier());
		final Optional<Attribute> availableName = pickerForm.getValue().getAvailableAttributes().stream()
				.filter(attr -> attr.getQualifier().equals(this.name.getQualifier())).findAny();
		assertThat(availableName.get().getSubAttributes()).hasSize(1);
		assertThat(availableName.get().getSubAttributes().iterator().next().getIsoCode()).isEqualTo(german.getIsocode());
	}

	@Test
	public void shouldCheckReadPermission()
	{
		// given
		when(code.getReadable()).thenReturn(true);
		when(name.getReadable()).thenReturn(false);

		// when
		renderer.render(parent, null, params, null, wim);

		// then
		final ArgumentCaptor<AttributeChooserForm> pickerForm = ArgumentCaptor.forClass(AttributeChooserForm.class);
		verify(attributesChooserRenderer).render(eq(parent), any(), pickerForm.capture(), any(), eq(wim));

		assertThat(pickerForm.getValue().getChosenAttributes()).hasSize(1);
		assertThat(pickerForm.getValue().getChosenAttributes().stream().map(Attribute::getQualifier))
				.containsOnly(code.getQualifier());
	}

	@Test
	public void shouldCheckWritePermission()
	{
		// given
		when(code.getWritable()).thenReturn(true);
		when(name.getWritable()).thenReturn(false);

		// when
		renderer.render(parent, null, params, null, wim);

		// then
		final ArgumentCaptor<AttributeChooserForm> pickerForm = ArgumentCaptor.forClass(AttributeChooserForm.class);
		verify(attributesChooserRenderer).render(eq(parent), any(), pickerForm.capture(), any(), eq(wim));

		assertThat(pickerForm.getValue().getChosenAttributes()).hasSize(1);
		assertThat(pickerForm.getValue().getChosenAttributes().stream().map(Attribute::getQualifier))
				.containsOnly(code.getQualifier());
	}

	@Test
	public void shouldUseTranslatorRegistryToFilterAttributes()
	{
		// given
		when(excelTranslatorRegistry.canHandle(code)).thenReturn(true);
		when(excelTranslatorRegistry.canHandle(name)).thenReturn(false);

		// when
		renderer.render(parent, null, params, null, wim);

		// then
		final ArgumentCaptor<AttributeChooserForm> pickerForm = ArgumentCaptor.forClass(AttributeChooserForm.class);
		verify(attributesChooserRenderer).render(eq(parent), any(), pickerForm.capture(), any(), eq(wim));

		assertThat(pickerForm.getValue().getChosenAttributes()).hasSize(1);
		assertThat(pickerForm.getValue().getChosenAttributes().stream().map(Attribute::getQualifier))
				.containsOnly(code.getQualifier());
	}

}
