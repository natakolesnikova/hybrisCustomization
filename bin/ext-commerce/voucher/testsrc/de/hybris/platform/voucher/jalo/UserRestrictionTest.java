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
package de.hybris.platform.voucher.jalo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


@UnitTest
public class UserRestrictionTest
{

	private UserRestriction userRestriction;
	private Collection<Principal> users;
	@Mock
	private Tenant tenant;

	@Before
	public void setUp() throws Exception
	{
		initMocks(this);
		userRestriction = new UserRestriction();
		userRestriction = spy(userRestriction);
		users = new HashSet<>();
		doReturn(users).when(userRestriction).getUsers();
		doReturn(tenant).when(userRestriction).getTenant();
	}

	@Test
	public void testIsFulfilledInternalAbstractOrderWithEmptyUserCollection() throws Exception
	{

		final AbstractOrder anOrder = null;
		assertFalse(userRestriction.isFulfilledInternal(anOrder));
	}

	@Test
	public void testIsFulfilledInternalAbstractOrderMatchUserAndIsPositive() throws Exception
	{
		doReturn(Boolean.TRUE).when(userRestriction).isPositiveAsPrimitive();
		final long longPk = 1L;
		final User user = mockUser(longPk);
		final AbstractOrder anOrder = mockOrderWith(user);

		users.add(user);
		assertTrue(userRestriction.isFulfilledInternal(anOrder));
	}

	@Test
	public void testIsFulfilledInternalAbstractOrderMatchUserAndNotPositive() throws Exception
	{
		doReturn(Boolean.FALSE).when(userRestriction).isPositiveAsPrimitive();
		final User user = mockUser(3L);
		final AbstractOrder anOrder = mockOrderWith(user);

		users.add(user);
		assertFalse(userRestriction.isFulfilledInternal(anOrder));
	}

	@Test
	public void testIsFulfilledInternalAbstractOrderNoMatchUserAndIsPositive() throws Exception
	{
		doReturn(Boolean.TRUE).when(userRestriction).isPositiveAsPrimitive();
		final User user = mockUser(1L);
		final User differentUser = mockUser(2L);
		final AbstractOrder anOrder = mockOrderWith(differentUser);

		users.add(user);
		assertFalse(userRestriction.isFulfilledInternal(anOrder));
	}

	@Test
	public void testIsFulfilledInternalAbstractOrderNoMatchUserAndNotPositive() throws Exception
	{
		doReturn(Boolean.FALSE).when(userRestriction).isPositiveAsPrimitive();
		final User user = mockUser(1L);
		final User differentUser = mockUser(2L);
		final AbstractOrder anOrder = mockOrderWith(differentUser);

		users.add(user);
		assertTrue(userRestriction.isFulfilledInternal(anOrder));
	}

	@Test
	public void testIsFulfilledInternalAbstractOrderMatchUserGroupAndIsPositive() throws Exception
	{
		doReturn(Boolean.TRUE).when(userRestriction).isPositiveAsPrimitive();
		final User user = mockUser(1L);

		final AbstractOrder anOrder = mockOrderWith(user);
		final UserGroup userGroup = mock(UserGroup.class);
		doReturn(PK.fromLong(123L)).when(userGroup).getPK();
		doReturn(Boolean.TRUE).when(userGroup).containsMember(user);
		users.add(userGroup);

		assertTrue(userRestriction.isFulfilledInternal(anOrder));
	}


	@Test
	public void testIsFulfilledInternalAbstractOrderNoMatchUserGroupAndNotPositive() throws Exception
	{
		doReturn(Boolean.FALSE).when(userRestriction).isPositiveAsPrimitive();
		final User orderUser = mockUser(1L);
		final AbstractOrder anOrder = mockOrderWith(orderUser);

		final UserGroup userGroup = mock(UserGroup.class);
		doReturn(PK.fromLong(123L)).when(userGroup).getPK();
		doReturn(Boolean.FALSE).when(userGroup).containsMember(orderUser);

		users.add(userGroup);
		//!isPositive && !userGroup.containsMember(orderUser)
		assertTrue(userRestriction.isFulfilledInternal(anOrder));
	}

	@Test
	public void testIsFulfilledInternalAbstractOrderNoMatchUserGroupAndIsPositive() throws Exception
	{
		doReturn(Boolean.TRUE).when(userRestriction).isPositiveAsPrimitive();
		final User orderUser = mockUser(1L);
		final AbstractOrder anOrder = mockOrderWith(orderUser);
		final UserGroup userGroup = mock(UserGroup.class);
		doReturn(PK.fromLong(123L)).when(userGroup).getPK();
		doReturn(Boolean.FALSE).when(userGroup).containsMember(orderUser);

		users.add(userGroup);
		assertFalse(userRestriction.isFulfilledInternal(anOrder));
	}

	@Test
	public void testIsFulfilledInternalAbstractOrderMatchUserGroupAndNotPositive() throws Exception
	{
		doReturn(Boolean.TRUE).when(userRestriction).isPositiveAsPrimitive();
		final User orderUser = mockUser(1L);
		final AbstractOrder anOrder = mockOrderWith(orderUser);

		final UserGroup userGroup = mock(UserGroup.class);
		doReturn(PK.fromLong(123L)).when(userGroup).getPK();
		doReturn(Boolean.FALSE).when(userGroup).containsMember(orderUser);

		users.add(userGroup);
		assertFalse(userRestriction.isFulfilledInternal(anOrder));
	}

	protected AbstractOrder mockOrderWith(final User user)
	{
		final AbstractOrder anOrder = mock(AbstractOrder.class);
		doReturn(user).when(anOrder).getUser();

		doReturn(tenant).when(anOrder).getTenant();
		return anOrder;
	}

	protected User mockUser(final long longPk)
	{
		final User user = mock(User.class);
		when(user.getPK()).thenReturn(PK.fromLong(longPk));
		doReturn(tenant).when(user).getTenant();
		return user;
	}

}
