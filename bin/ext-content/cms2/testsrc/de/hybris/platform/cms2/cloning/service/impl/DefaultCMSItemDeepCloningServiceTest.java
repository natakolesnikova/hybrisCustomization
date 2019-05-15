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
package de.hybris.platform.cms2.cloning.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSItemDeepCloningServiceTest
{
	private static final Object TEST_UID = "1122";

	@InjectMocks
	private DefaultCMSItemDeepCloningService itemDeepCloningService;
	@Mock
	private PersistentKeyGenerator cloneUidGenerator;

	@Test
	public void generateCloneUid()
	{
		when(cloneUidGenerator.generate()).thenReturn(TEST_UID);

		final String newUid = itemDeepCloningService.generateCloneItemUid();

		assertThat(newUid, equalTo("clone_" + TEST_UID));
	}

}
