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
package de.hybris.platform.cmsfacades.restrictions.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminRestrictionService;
import de.hybris.platform.cmsfacades.common.predicate.HasRestrictionNameChangedUpdatePredicate;
import de.hybris.platform.cmsfacades.data.AbstractRestrictionData;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class HasRestrictionNameChangedUpdatePredicateTest
{

	private static final String UID = "test-restriction-id";
	private static final String TYPE_CODE = "CMSCategoryRestriction";
	private static final String NAME = "My Test Restriction";
	private static final String NEW_NAME = "My New Test Restriction";


	@InjectMocks
	private HasRestrictionNameChangedUpdatePredicate predicate;

	@Mock
	private CMSAdminRestrictionService adminRestrictionService;
	@Mock
	private AbstractRestrictionModel abstractRestrictionModel;
	@Mock
	private AbstractRestrictionData abstractRestrictionData;

	@Before
	public void setUp()
	{
		abstractRestrictionData = new AbstractRestrictionData();
		abstractRestrictionData.setName(NAME);
		abstractRestrictionData.setUid(UID);
		abstractRestrictionData.setTypeCode(TYPE_CODE);
	}

	@Test
	public void testNameHasChanged() throws CMSItemNotFoundException
	{
		when(adminRestrictionService.getAllRestrictionsByType(abstractRestrictionData.getTypeCode())).thenReturn(
				Arrays.asList(abstractRestrictionModel));
		when(abstractRestrictionModel.getUid()).thenReturn(UID);
		when(abstractRestrictionModel.getName()).thenReturn(NEW_NAME);

		final boolean result = predicate.test(abstractRestrictionData);

		assertTrue(result);
	}

	@Test
	public void testNameNotChanged() throws CMSItemNotFoundException
	{
		when(adminRestrictionService.getAllRestrictionsByType(abstractRestrictionData.getTypeCode())).thenReturn(
				Arrays.asList(abstractRestrictionModel));
		when(abstractRestrictionModel.getUid()).thenReturn(UID);
		when(abstractRestrictionModel.getName()).thenReturn(NAME);

		final boolean result = predicate.test(abstractRestrictionData);

		assertFalse(result);
	}

	@Test
	public void testNameNotChanged_NoRestrictionsFoundForName() throws CMSItemNotFoundException
	{
		when(adminRestrictionService.getAllRestrictionsByType(abstractRestrictionData.getTypeCode())).thenReturn(
				Collections.emptyList());

		final boolean result = predicate.test(abstractRestrictionData);

		assertFalse(result);
	}

}
