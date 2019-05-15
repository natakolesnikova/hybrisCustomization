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
package de.hybris.platform.cmsfacades.restrictions.populator.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.restrictions.CMSUserGroupRestrictionModel;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.data.UserGroupRestrictionData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.ObjectFactory;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class UserGroupRestrictionDataToModelPopulatorTest
{

	@InjectMocks
	private UserGroupRestrictionDataToModelPopulator populator;

	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Mock
	private ObjectFactory<ItemData> cmsItemDataDataFactory;

	@Mock
	private UserGroupRestrictionData source;

	@Captor
	private ArgumentCaptor<ItemData> itemDataCaptor;

	private CMSUserGroupRestrictionModel target;

	private String validKey01 = "valid-key-01";
	private String validKey02 = "valid-key-02";
	private String invalidKey01 = "not-valid-key-01";
	private String invalidKey02 = "not-valid-key-02";
	@Mock
	private UserGroupModel validModel01;
	@Mock
	private UserGroupModel validModel02;

	@Before
	public void setUp()
	{
		target = new CMSUserGroupRestrictionModel();

		when(cmsItemDataDataFactory.getObject()).thenAnswer(invocationOnMock -> new ItemData());
	}

	@Test
	public void shouldPopulateTarget()
	{
		//prepare
		when(uniqueItemIdentifierService.getItemModel(validKey01, UserGroupModel.class)).thenReturn(Optional.of(validModel01));
		when(uniqueItemIdentifierService.getItemModel(validKey02, UserGroupModel.class)).thenReturn(Optional.of(validModel02));
		when(source.isIncludeSubgroups()).thenReturn(false);
		when(source.getUserGroups()).thenReturn(Arrays.asList(validKey01, validKey02));

		//execute
		populator.populate(source, target);

		//assert
		assertThat("UserGroupRestrictionDataToModelPopulatorTest target includeSubgroups should contain false",
				target.isIncludeSubgroups(), is(false));

		assertThat("UserGroupRestrictionDataToModelPopulatorTest target userGroups should contain 2 valid UserGroupModels",
				target.getUserGroups(),
				contains(Arrays.asList(validModel01, validModel02).toArray()));
	}

	@Test(expected = ConversionException.class)
	public void shouldThrowConversionExceptionBecauseOfUnknownIdentifierException()
	{
		//prepare
		when(uniqueItemIdentifierService.getItemModel(invalidKey02, UserGroupModel.class))
				.thenThrow(new UnknownIdentifierException("unknown exception"));
		when(source.isIncludeSubgroups()).thenReturn(false);
		when(source.getUserGroups()).thenReturn(Arrays.asList(invalidKey02, validKey02));

		//execute
		populator.populate(source, target);
	}

	@Test(expected = ConversionException.class)
	public void shouldThrowConversionExceptionBecauseOfConversionException()
	{
		//prepare
		when(uniqueItemIdentifierService.getItemModel(invalidKey02, UserGroupModel.class))
				.thenThrow(new ConversionException("conversion exception"));
		when(uniqueItemIdentifierService.getItemModel(validKey02, UserGroupModel.class)).thenReturn(Optional.of(validModel02));

		when(source.isIncludeSubgroups()).thenReturn(false);
		when(source.getUserGroups()).thenReturn(Arrays.asList(validKey02, invalidKey02));

		//execute
		populator.populate(source, target);
	}
}
