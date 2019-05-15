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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;
import static java.util.Objects.nonNull;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.consent.CommerceConsentService;
import de.hybris.platform.commerceservices.consent.dao.ConsentDao;
import de.hybris.platform.commerceservices.consent.dao.ConsentTemplateDao;
import de.hybris.platform.commerceservices.model.consent.ConsentModel;
import de.hybris.platform.commerceservices.model.consent.ConsentTemplateModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link CommerceConsentService}
 */
public class DefaultCommerceConsentService implements CommerceConsentService
{
	private ModelService modelService;

	private TimeService timeService;

	private ConsentDao consentDao;

	private ConsentTemplateDao consentTemplateDao;

	@Override
	public List<ConsentTemplateModel> getConsentTemplates(final BaseSiteModel baseSite)
	{
		validateParameterNotNullStandardMessage("baseSite", baseSite);

		return getConsentTemplateDao().findConsentTemplatesBySite(baseSite);
	}

	@Override
	public ConsentTemplateModel getConsentTemplate(final String consentTemplateId, final Integer consentTemplateVersion,
			final BaseSiteModel baseSite)
	{
		validateParameterNotNullStandardMessage("consentTemplateId", consentTemplateId);
		validateParameterNotNullStandardMessage("consentTemplateVersion", consentTemplateVersion);
		validateParameterNotNullStandardMessage("baseSite", baseSite);

		return getConsentTemplateDao().findConsentTemplateByIdAndVersionAndSite(consentTemplateId, consentTemplateVersion,
				baseSite);
	}

	@Override
	public ConsentTemplateModel getLatestConsentTemplate(final String consentTemplateId, final BaseSiteModel baseSite)
	{
		validateParameterNotNullStandardMessage("consentTemplateId", consentTemplateId);
		validateParameterNotNullStandardMessage("baseSite", baseSite);

		return getConsentTemplateDao().findLatestConsentTemplateByIdAndSite(consentTemplateId, baseSite);
	}

	@Override
	public ConsentModel getConsent(final String consentCode)
	{
		validateParameterNotNullStandardMessage("consentCode", consentCode);

		final Map<String, Object> queryParams = Collections.singletonMap(ConsentModel.CODE, consentCode);
		return getConsentDao().find(queryParams).stream().findFirst()
				.orElseThrow(() -> new ModelNotFoundException(String.format("Consent not found for code [%s]", consentCode)));
	}

	@Override
	public ConsentModel getActiveConsent(final CustomerModel customer, final ConsentTemplateModel consentTemplate)
	{
		validateParameterNotNullStandardMessage("customer", customer);
		validateParameterNotNullStandardMessage("consentTemplate", consentTemplate);

		return getConsentDao().findConsentByCustomerAndConsentTemplate(customer, consentTemplate);
	}

	@Override
	public boolean hasEffectivelyActiveConsent(final CustomerModel customer, final ConsentTemplateModel consentTemplate)
	{
		validateParameterNotNullStandardMessage("customer", customer);
		validateParameterNotNullStandardMessage("consentTemplate", consentTemplate);

		final ConsentModel consent = getActiveConsent(customer, consentTemplate);
		return consent != null ? consent.isActive() : false;
	}

	@Override
	public void giveConsent(final CustomerModel customer, final ConsentTemplateModel consentTemplate)
	{
		validateParameterNotNullStandardMessage("customer", customer);
		validateParameterNotNullStandardMessage("consentTemplate", consentTemplate);

		ConsentModel consent = getActiveConsent(customer, consentTemplate);
		if (consent == null || isConsentWithdrawn(consent))
		{
			consent = createConsentModel(customer, consentTemplate);
		}
		if (consent.getConsentGivenDate() == null)
		{
			consent.setConsentGivenDate(getTimeService().getCurrentTime());
			getModelService().save(consent);
		}
	}

	@Override
	public void withdrawConsent(final ConsentModel consent)
	{
		validateParameterNotNullStandardMessage("consent", consent);

		if (!isConsentWithdrawn(consent))
		{
			consent.setConsentWithdrawnDate(getTimeService().getCurrentTime());
			getModelService().save(consent);
		}
	}

	protected boolean isConsentWithdrawn(final ConsentModel consent)
	{
		return nonNull(consent.getConsentWithdrawnDate());
	}

	protected ConsentModel createConsentModel(final CustomerModel customer, final ConsentTemplateModel consentTemplate)
	{
		final ConsentModel consent = modelService.create(ConsentModel._TYPECODE);
		consent.setConsentTemplate(consentTemplate);
		consent.setCustomer(customer);
		return consent;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}

	@Required
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

	protected ConsentDao getConsentDao()
	{
		return consentDao;
	}

	@Required
	public void setConsentDao(final ConsentDao consentDao)
	{
		this.consentDao = consentDao;
	}

	protected ConsentTemplateDao getConsentTemplateDao()
	{
		return consentTemplateDao;
	}

	@Required
	public void setConsentTemplateDao(final ConsentTemplateDao consentTemplateDao)
	{
		this.consentTemplateDao = consentTemplateDao;
	}
}
