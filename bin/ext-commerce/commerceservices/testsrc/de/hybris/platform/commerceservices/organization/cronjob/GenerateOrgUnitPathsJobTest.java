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
package de.hybris.platform.commerceservices.organization.cronjob;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.model.OrgUnitModel;
import de.hybris.platform.commerceservices.organization.services.OrgUnitHierarchyService;
import de.hybris.platform.commerceservices.organization.services.impl.OrgUnitHierarchyException;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.junit.Before;
import org.junit.Test;


/**
 * Unit test for {@link GenerateOrgUnitPathsJob}.
 */
@UnitTest
public class GenerateOrgUnitPathsJobTest
{
	private GenerateOrgUnitPathsJob generateOrgUnitPathJob;

	private OrgUnitHierarchyService orgUnitHierarchyService;

	private final Class<? extends OrgUnitModel> type = OrgUnitModel.class;

	@Before
	public void setUp() throws Exception
	{
		orgUnitHierarchyService = mock(OrgUnitHierarchyService.class);
		generateOrgUnitPathJob = new GenerateOrgUnitPathsJob(type);
		generateOrgUnitPathJob.setOrgUnitHierarchyService(orgUnitHierarchyService);
	}

	@Test
	public void shouldPerformWithSuccess()
	{
		doNothing().when(orgUnitHierarchyService).generateUnitPaths(type);
		final PerformResult result = generateOrgUnitPathJob.perform(new CronJobModel());

		assertEquals("Unexpexted cron job status", CronJobStatus.FINISHED, result.getStatus());
		assertEquals("Unexpexted cron job result", CronJobResult.SUCCESS, result.getResult());
	}

	@Test
	public void shouldPerformWithFailure()
	{
		doThrow(new OrgUnitHierarchyException()).when(orgUnitHierarchyService).generateUnitPaths(OrgUnitModel.class);
		final PerformResult result = generateOrgUnitPathJob.perform(new CronJobModel());

		assertEquals("Unexpexted cron job status", CronJobStatus.FINISHED, result.getStatus());
		assertEquals("Unexpexted cron job result", CronJobResult.FAILURE, result.getResult());
	}
}
