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
package de.hybris.platform.commerceservices.consent.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.consent.dao.ConsentDao;
import de.hybris.platform.commerceservices.consent.dao.ConsentTemplateDao;
import de.hybris.platform.commerceservices.model.consent.ConsentModel;
import de.hybris.platform.commerceservices.model.consent.ConsentTemplateModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCommerceConsentServiceTest
{
	public static final Date currentTime = new Date();
	public static final String consentTemplateId = "consentTemplateId";
	public static final Integer consentTemplateVersion = Integer.valueOf(0);

	@Mock
	private ConsentDao consentDao;

	@Mock
	private ConsentTemplateDao consentTemplateDao;

	@Mock
	private ModelService modelService;

	@Mock
	private TimeService timeService;

	@Mock
	private ConsentTemplateModel consentTemplateModel, consentTemplateModel2;

	@Mock
	private ConsentModel existingConsentModel, newConsentModel;

	@Mock
	private BaseSiteModel baseSite;

	@Mock
	private CustomerModel customerModel;

	@Spy
	@InjectMocks
	private DefaultCommerceConsentService commerceConsentService;

	@Before
	public void setup()
	{
		doReturn(currentTime).when(timeService).getCurrentTime();
		doReturn("1").when(consentTemplateModel).getId();
		doReturn(consentTemplateModel).when(existingConsentModel).getConsentTemplate();
		doReturn(newConsentModel).when(modelService).create(ConsentModel._TYPECODE);
		doReturn(consentTemplateModel2).when(consentTemplateDao).findLatestConsentTemplateByIdAndSite(consentTemplateId, baseSite);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetConsentTemplateWhenCosentTemplateIdNull()
	{
		commerceConsentService.getConsentTemplate(null, consentTemplateVersion, baseSite);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetConsentTemplateWhenCosentTemplateVersionNull()
	{
		commerceConsentService.getConsentTemplate(consentTemplateId, null, baseSite);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetConsentTemplateWhenBaseSiteNull()
	{
		commerceConsentService.getConsentTemplate(consentTemplateId, consentTemplateVersion, null);
	}

	@Test
	public void testGetConsentTemplateWhenConsentTemplateDoesNotExist()
	{
		doReturn(null).when(consentTemplateDao).findConsentTemplateByIdAndVersionAndSite(consentTemplateId, consentTemplateVersion,
				baseSite);

		final ConsentTemplateModel consentTemplateModel = commerceConsentService.getConsentTemplate(consentTemplateId,
				consentTemplateVersion, baseSite);

		assertNull(consentTemplateModel);
	}

	@Test
	public void testGetConsentTemplateWhenConsentTemplateExists()
	{
		doReturn(consentTemplateModel).when(consentTemplateDao).findConsentTemplateByIdAndVersionAndSite(consentTemplateId,
				consentTemplateVersion, baseSite);

		final ConsentTemplateModel retrievedConsentTemplateModel = commerceConsentService.getConsentTemplate(consentTemplateId,
				consentTemplateVersion, baseSite);

		assertSame(consentTemplateModel, retrievedConsentTemplateModel);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetConsentTemplateWhenConsentTemplateIdIsNull()
	{
		commerceConsentService.getLatestConsentTemplate(null, baseSite);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetConsentTemplateWhenBaseSiteIsNull()
	{
		commerceConsentService.getLatestConsentTemplate(consentTemplateId, null);
	}

	@Test
	public void testGetLatestConsentTemplateWhenConsentTemplateIdAndBaseSiteProvided()
	{
		final ConsentTemplateModel retrievedConsentModel = commerceConsentService.getLatestConsentTemplate(consentTemplateId,
				baseSite);

		verify(consentTemplateDao).findLatestConsentTemplateByIdAndSite(consentTemplateId, baseSite);
		assertEquals(consentTemplateModel2, retrievedConsentModel);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetConsentTemplatesWhenBaseSiteIsNull()
	{
		commerceConsentService.getConsentTemplates(null);
	}

	@Test
	public void testGetConsentTemplates()
	{
		commerceConsentService.getConsentTemplates(baseSite);

		verify(consentTemplateDao).findConsentTemplatesBySite(baseSite);
	}

	@Test
	public void testGetActiveConsentByCustomer()
	{
		commerceConsentService.getActiveConsent(customerModel, consentTemplateModel);

		verify(consentDao).findConsentByCustomerAndConsentTemplate(customerModel, consentTemplateModel);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGiveConsentWhenCustomerIsNull()
	{
		commerceConsentService.giveConsent(null, consentTemplateModel);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGiveConsentWhenConsentTemplateIsNull()
	{
		commerceConsentService.giveConsent(customerModel, null);
	}

	@Test
	public void testGiveConsentWhenNoActiveConsents()
	{
		doReturn(null).when(commerceConsentService).getActiveConsent(customerModel, consentTemplateModel);

		commerceConsentService.giveConsent(customerModel, consentTemplateModel);

		verify(commerceConsentService).createConsentModel(customerModel, consentTemplateModel);
		verify(modelService, times(1)).save(newConsentModel);
	}

	@Test
	public void testGiveConsentWhenConsentPreviouslyWithdrawn()
	{
		doReturn(new Date()).when(existingConsentModel).getConsentWithdrawnDate();
		doReturn(existingConsentModel).when(commerceConsentService).getActiveConsent(customerModel, consentTemplateModel);

		commerceConsentService.giveConsent(customerModel, consentTemplateModel);

		verify(commerceConsentService).createConsentModel(customerModel, consentTemplateModel);
		verify(modelService, times(1)).save(newConsentModel);
	}

	@Test
	public void testGiveConsentWhenConsentAlreadyGiven()
	{
		doReturn(null).when(existingConsentModel).getConsentWithdrawnDate();
		doReturn(existingConsentModel).when(commerceConsentService).getActiveConsent(customerModel, consentTemplateModel);

		commerceConsentService.giveConsent(customerModel, consentTemplateModel);

		verify(commerceConsentService, times(0)).createConsentModel(customerModel, consentTemplateModel);
		verify(modelService, times(0)).save(newConsentModel);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithdrawConsentWhenConsentTemplateIsNull()
	{
		commerceConsentService.withdrawConsent(null);
	}

	@Test
	public void testWithdrawConsentWhenConsentAlreadyWithdrawn()
	{
		doReturn(new Date()).when(existingConsentModel).getConsentWithdrawnDate();

		commerceConsentService.withdrawConsent(existingConsentModel);

		verify(modelService, never()).save(existingConsentModel);
	}

	@Test
	public void testGiveConsentWhenConsentAvailableAndNotWithdrawnYet()
	{
		doReturn(null).when(existingConsentModel).getConsentWithdrawnDate();

		commerceConsentService.withdrawConsent(existingConsentModel);

		verify(existingConsentModel).setConsentWithdrawnDate(currentTime);
		verify(modelService).save(existingConsentModel);
	}
}
