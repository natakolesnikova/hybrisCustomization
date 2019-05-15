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
package de.hybris.platform.cmsfacades.rendering.predicates.attributes;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.core.model.type.*;
import de.hybris.platform.servicelayer.type.TypeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AttributeContainsCMSComponentsPredicatePredicateTest
{
	// --------------------------------------------------------------------------
	// Variables
	// --------------------------------------------------------------------------
	@Mock
	private ComposedTypeModel nonCmsComponent;

	@Mock
	private ComposedTypeModel childCmsComponent;

	@Mock
	private MapTypeModel mapTypeModel;

	@Mock
	private CollectionTypeModel collectionTypeModel;

	@Mock
	private AttributeDescriptorModel attributeDescriptorModel;

	@Mock
	private TypeService typeService;

	@Mock
	private ComposedTypeModel typeModel;

	@InjectMocks
	private AttributeContainsCMSComponentsPredicate predicate;

	// --------------------------------------------------------------------------
	// Test Setup
	// --------------------------------------------------------------------------
	@Before
	public void setUp()
	{
		when(typeService.getComposedTypeForCode(AbstractCMSComponentModel._TYPECODE)).thenReturn(typeModel);
		when(typeService.isAssignableFrom(typeModel, nonCmsComponent)).thenReturn(false);
		when(typeService.isAssignableFrom(typeModel, childCmsComponent)).thenReturn(true);
	}

	// --------------------------------------------------------------------------
	// Tests
	// --------------------------------------------------------------------------
	@Test
	public void givenPrimitiveAttributeDescriptorModel_WhenTested_ThenItReturnsFalse()
	{
		// GIVEN
		when(attributeDescriptorModel.getPrimitive()).thenReturn(true);

		// WHEN
		boolean result = predicate.test(attributeDescriptorModel);

		// THEN
		assertThat(result, is(false));
	}

	@Test
	public void givenNonCmsItem_WhenTested_ThenItReturnsFalse()
	{
		// GIVEN
		when(attributeDescriptorModel.getAttributeType()).thenReturn(nonCmsComponent);

		// WHEN
		boolean result = predicate.test(attributeDescriptorModel);

		// THEN
		assertThat(result, is(false));
	}

	@Test
	public void givenCmsItem_WhenTested_ThenItReturnsTrue()
	{
		// GIVEN
		when(attributeDescriptorModel.getAttributeType()).thenReturn(childCmsComponent);

		// WHEN
		boolean result = predicate.test(attributeDescriptorModel);

		// THEN
		assertThat(result, is(true));
	}

	@Test
	public void givenCollectionWithNonCmsComponents_WhenTested_ThenItReturnsFalse()
	{
		// GIVEN
		when(attributeDescriptorModel.getAttributeType()).thenReturn(collectionTypeModel);
		when(collectionTypeModel.getElementType()).thenReturn(nonCmsComponent);

		// WHEN
		boolean result = predicate.test(attributeDescriptorModel);

		// THEN
		assertThat(result, is(false));
	}

	@Test
	public void givenCollectionWithCmsComponents_WhenTested_ThenItReturnsTrue()
	{
		// GIVEN
		when(attributeDescriptorModel.getAttributeType()).thenReturn(collectionTypeModel);
		when(collectionTypeModel.getElementType()).thenReturn(childCmsComponent);

		// WHEN
		boolean result = predicate.test(attributeDescriptorModel);

		// THEN
		assertThat(result, is(true));
	}

	@Test
	public void givenLocalizedElementWithoutCmsComponents_WhenTested_ThenItReturnsFalse()
	{
		// GIVEN
		when(attributeDescriptorModel.getLocalized()).thenReturn(true);
		when(attributeDescriptorModel.getAttributeType()).thenReturn(mapTypeModel);
		when(mapTypeModel.getReturntype()).thenReturn(nonCmsComponent);

		// WHEN
		boolean result = predicate.test(attributeDescriptorModel);

		// THEN
		assertThat(result, is(false));
	}

	@Test
	public void givenLocalizedElementWithCmsComponents_WhenTested_ThenItReturnsTrue()
	{
		// GIVEN
		when(attributeDescriptorModel.getLocalized()).thenReturn(true);
		when(attributeDescriptorModel.getAttributeType()).thenReturn(mapTypeModel);
		when(mapTypeModel.getReturntype()).thenReturn(childCmsComponent);

		// WHEN
		boolean result = predicate.test(attributeDescriptorModel);

		// THEN
		assertThat(result, is(true));
	}
}
