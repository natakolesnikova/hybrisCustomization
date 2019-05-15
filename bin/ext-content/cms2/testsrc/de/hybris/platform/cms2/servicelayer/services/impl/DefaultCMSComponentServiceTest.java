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
package de.hybris.platform.cms2.servicelayer.services.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;

import java.util.*;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCMSComponentServiceTest
{
	@Mock
	private AbstractCMSComponentModel componentModel;

	@Mock
	private ComposedTypeModel composedTypeModel;

	@Mock
	private AttributeDescriptorModel readableAttributeDescriptorModel;

	@Mock
	private AttributeDescriptorModel nonreadableAttributeDescriptorModel;

	@Mock
	private TypeService typeService;

	@Mock
	private List<String> systemProperties;

	@InjectMocks
	private final CMSComponentService cmsComponentService = new DefaultCMSComponentService();

	private Set<AttributeDescriptorModel> attributeDescriptorModelSet = new HashSet<>();


	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		attributeDescriptorModelSet.add(readableAttributeDescriptorModel);
		attributeDescriptorModelSet.add(nonreadableAttributeDescriptorModel);

		given(componentModel.getItemtype()).willReturn(AbstractCMSComponentModel.ITEMTYPE);
		given(componentModel.getTypeCode()).willReturn(AbstractCMSComponentModel.ITEMTYPE);
		given(typeService.getComposedTypeForCode(AbstractCMSComponentModel.ITEMTYPE)).willReturn(composedTypeModel);
		given(typeService.getAttributeDescriptorsForType(composedTypeModel)).willReturn(attributeDescriptorModelSet);
		given(readableAttributeDescriptorModel.getReadable()).willReturn(Boolean.TRUE);
		given(nonreadableAttributeDescriptorModel.getReadable()).willReturn(Boolean.FALSE);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSComponentService#isCmsComponentRestricted(de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)}
	 * .
	 */
	@Test
	public void shouldBeNotRestrictedWhenRestrictionsAreNull()
	{
		// given
		given(componentModel.getRestrictions()).willReturn(null);

		// when
		final boolean restricted = cmsComponentService.isComponentRestricted(componentModel);

		// then
		assertThat(restricted).isFalse();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSComponentService#isCmsComponentRestricted(de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)}
	 * .
	 */
	@Test
	public void shouldBeNotRestrictedWhenRestrictionsAreEmptyList()
	{
		// given
		given(componentModel.getRestrictions()).willReturn(Collections.EMPTY_LIST);

		// when
		final boolean restricted = cmsComponentService.isComponentRestricted(componentModel);

		// then
		assertThat(restricted).isFalse();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSComponentService#isCmsComponentRestricted(de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)}
	 * .
	 */
	@Test
	public void shouldBeRestrictedWhenRestrictionsAreNotEmptyList()
	{
		// given
		final List<AbstractRestrictionModel> restrictionsMock = mock(ArrayList.class);
		given(Boolean.valueOf(restrictionsMock.isEmpty())).willReturn(Boolean.FALSE);
		given(componentModel.getRestrictions()).willReturn(restrictionsMock);

		// when
		final boolean restricted = cmsComponentService.isComponentRestricted(componentModel);

		// then
		assertThat(restricted).isTrue();
	}

	@Test
	public void shouldReturnAllEditorProperties()
	{
		// when
		final Collection<String> properties = cmsComponentService.getEditorProperties(componentModel);

		// then
		assertThat(properties.size()).isEqualTo(2);
	}

	@Test
	public void shouldReturnOnlyReadableEditorProperties()
	{
		// when
		final Collection<String> properties = cmsComponentService.getReadableEditorProperties(componentModel);

		// then
		assertThat(properties.size()).isEqualTo(1);
	}
}
