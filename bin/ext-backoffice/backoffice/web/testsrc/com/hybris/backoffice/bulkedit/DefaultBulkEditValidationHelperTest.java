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
package com.hybris.backoffice.bulkedit;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.ItemModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.hybris.backoffice.attributechooser.Attribute;
import com.hybris.backoffice.attributechooser.AttributeChooserForm;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.validation.ValidationContext;
import com.hybris.cockpitng.validation.ValidationHandler;


@RunWith(MockitoJUnitRunner.class)
public class DefaultBulkEditValidationHelperTest
{
	@Mock
	private ObjectValueService objectValueService;
	@Mock
	private ValidationHandler validationHandler;
	@InjectMocks
	private DefaultBulkEditValidationHelper helper;
	private BulkEditForm bulkEditForm;
	@Mock
	private ItemModel templateObject;
	private Set<Attribute> chooserAttributes;
	@Mock
	private ValidationContext validationContext;

	@Before
	public void setUp()
	{
		chooserAttributes = new HashSet<>();
		final AttributeChooserForm chooserForm = new AttributeChooserForm();
		chooserForm.setChosenAttributes(chooserAttributes);
		bulkEditForm = new BulkEditForm();
		bulkEditForm.setAttributesForm(chooserForm);
		bulkEditForm.setTemplateObject(templateObject);
	}

	@Test
	public void checkAttributesWithValueValidatable()
	{
		//given
		final HashMap<Locale, String> localizedMap = new HashMap<>();
		localizedMap.put(Locale.ENGLISH, "a");
		mockAttributeWithValue("notEmptyLocalizedMap", localizedMap);
		final HashMap<String, String> mapValue = new HashMap<>();
		mapValue.put("a", "a");
		mockAttributeWithValue("notEmptyMap", mapValue);
		mockAttributeWithValue("notEmptyCollection", Lists.newArrayList(1));
		mockAttributeWithValue("notEmpty", 1);
		mockAttributeWithValue("emptyLocalizedMap", new HashMap<>());
		mockAttributeWithValue("emptyMap", new HashMap<>());
		mockAttributeWithValue("emptyCollection", new ArrayList<>());
		mockAttributeWithValue("empty", null);
		//when
		final Set<String> validatableProperties = helper.getValidatableProperties(bulkEditForm);
		//then
		assertThat(validatableProperties).containsOnly("notEmptyLocalizedMap", "notEmptyMap", "notEmptyCollection", "notEmpty");
	}

	@Test
	public void checkAttributeToClearIsValidatable()
	{
		//given
		mockAttributeWithValue("empty", null);
		bulkEditForm.addQualifierToClear("empty");
		//when
		final Set<String> validatableProperties = helper.getValidatableProperties(bulkEditForm);
		//then
		assertThat(validatableProperties).containsOnly("empty");
	}

	@Test
	public void testValidationProxyValidatesAttributesWithValue()
	{
		//given
		final HashMap<Locale, String> localizedMap = new HashMap<>();
		localizedMap.put(Locale.ENGLISH, "a");
		mockAttributeWithValue("notEmptyLocalizedMap", localizedMap);
		final HashMap<String, String> mapValue = new HashMap<>();
		mapValue.put("a", "a");
		mockAttributeWithValue("notEmptyMap", mapValue);
		mockAttributeWithValue("notEmptyCollection", Lists.newArrayList(1));
		mockAttributeWithValue("notEmpty", 1);
		mockAttributeWithValue("emptyLocalizedMap", new HashMap<>());
		mockAttributeWithValue("emptyMap", new HashMap<>());
		mockAttributeWithValue("emptyCollection", new ArrayList<>());
		mockAttributeWithValue("empty", null);
		//when

		final ValidationHandler proxyValidationHandler = helper.createProxyValidationHandler(bulkEditForm);

		proxyValidationHandler.validate(templateObject, Lists.newArrayList("notEmptyLocalizedMap", "notEmptyMap",
				"notEmptyCollection", "notEmpty", "emptyLocalizedMap", "emptyMap", "emptyCollection", "empty"), validationContext);

		//then
		verify(validationHandler).validate(eq(templateObject), argThat(new ArgumentMatcher<List<String>>()
		{
			@Override
			public boolean matches(final Object o)
			{
				final ArrayList<String> correctQualifiers = Lists.newArrayList("notEmptyLocalizedMap", "notEmptyMap",
						"notEmptyCollection", "notEmpty");
				return ((List) o).size() == correctQualifiers.size() && ((List) o).containsAll(correctQualifiers);
			}
		}), same(validationContext));
	}

	private void mockAttributeWithValue(final String qualifier, final Object value)
	{
		chooserAttributes.add(new Attribute(qualifier, qualifier, false));
		when(objectValueService.getValue(qualifier, templateObject)).thenReturn(value);
	}

}
