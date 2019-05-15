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

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.restrictions.CMSUserGroupRestrictionModel;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.data.UserGroupRestrictionData;
import de.hybris.platform.cmsfacades.restrictions.populator.model.UserGroupRestrictionModelToDataPopulator;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.core.model.user.UserGroupModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class UserGroupRestrictionModelToDataPopulatorTest
{

	@InjectMocks
	private UserGroupRestrictionModelToDataPopulator populator;

	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Mock
	private CMSUserGroupRestrictionModel source;

	@Mock
	private ItemData itemData01;

	@Mock
	private ItemData itemData02;

	private UserGroupRestrictionData target;
	private UserGroupModel fakeUserGroupModel01;
	private UserGroupModel fakeUserGroupModel02;
	private String validKey01 = "valid-key-01";
	private String validKey02 = "valid-key-02";


	@Before
	public void setUp()
	{
		target = new UserGroupRestrictionData();
		fakeUserGroupModel01 = new UserGroupModel();
		fakeUserGroupModel02 = new UserGroupModel();

		when(itemData01.getItemId()).thenReturn(validKey01);
		when(itemData02.getItemId()).thenReturn(validKey02);

		when(uniqueItemIdentifierService.getItemData(fakeUserGroupModel01)).thenReturn(Optional.of(itemData01));
		when(uniqueItemIdentifierService.getItemData(fakeUserGroupModel02)).thenReturn(Optional.of(itemData02));
	}

	@Test
	public void shouldPopulateTarget()
	{
		//prepare
		when(source.isIncludeSubgroups()).thenReturn(false);
		when(source.getUserGroups()).thenReturn(Arrays.asList(fakeUserGroupModel01, fakeUserGroupModel02));
		List<String> userGroupKeys = Arrays.asList(validKey01, validKey02);

		//execute
		populator.populate(source, target);

		//assert
		verify(itemData01, times(1)).getItemId();
		verify(itemData02, times(1)).getItemId();

		assertThat("UserGroupRestrictionModelToDataPopulatorTest target includeSubgroups should" +
				"contain false", target.isIncludeSubgroups(), is(false));

		assertThat("UserGroupRestrictionModelToDataPopulatorTest target userGroups should contain " +
				"user group keys", target.getUserGroups(), contains(userGroupKeys.toArray()));
	}

	@Test
	public void userGroupsShouldBeEmpty()
	{
		//prepare
		when(source.isIncludeSubgroups()).thenReturn(true);
		when(source.getUserGroups()).thenReturn(new ArrayList<>());

		//execute
		populator.populate(source, target);

		//assert
		verify(itemData01, never()).getItemId();
		verify(itemData02, never()).getItemId();
		assertThat("UserGroupRestrictionModelToDataPopulatorTest target includeSubgroups should" +
				"contain true", target.isIncludeSubgroups(), is(true));

		assertThat("UserGroupRestrictionModelToDataPopulatorTest target userGroups should contain " +
				"empty list", target.getUserGroups().isEmpty(), is(true));
	}

}
