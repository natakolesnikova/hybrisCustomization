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
package de.hybris.platform.cmsfacades.common.service.impl;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Required;


/**
 * @deprecated since 6.5 use {@link de.hybris.platform.cms2.common.service.impl.DefaultSessionSearchRestrictionDisabler}
 * Default implementation of {@link SessionSearchRestrictionsDisabler} by calling
 * {@link SearchRestrictionService#disableSearchRestrictions()} before executing the supplier code and re-enabling the search
 * restrictions upon supplier completion.
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.5")
public class DefaultSessionSearchRestrictionDisabler implements SessionSearchRestrictionsDisabler
{
	private SearchRestrictionService searchRestrictionService;
	private SessionService sessionService;

	@Override
	public <T> T execute(final Supplier<T> supplier)
	{
		return getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				try
				{
					getSearchRestrictionService().disableSearchRestrictions();
					return supplier.get();
				}
				finally
				{
					getSearchRestrictionService().enableSearchRestrictions();
				}
			}
		});
	}

	protected SearchRestrictionService getSearchRestrictionService()
	{
		return searchRestrictionService;
	}

	@Required
	public void setSearchRestrictionService(final SearchRestrictionService searchRestrictionService)
	{
		this.searchRestrictionService = searchRestrictionService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
