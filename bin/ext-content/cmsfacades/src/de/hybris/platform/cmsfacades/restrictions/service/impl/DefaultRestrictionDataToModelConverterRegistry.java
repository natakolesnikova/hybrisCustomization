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
package de.hybris.platform.cmsfacades.restrictions.service.impl;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.restrictions.service.RestrictionDataToModelConverter;
import de.hybris.platform.cmsfacades.restrictions.service.RestrictionDataToModelConverterRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Default implementation of the <code>RestrictionDataToModelConverterRegistry</code>. This implementation uses
 * autowire-by-type to inject all beans implementing {@link RestrictionDataToModelConverter}.
 * 
 * @deprecated since 6.6
 */
@HybrisDeprecation(sinceVersion = "6.6")
@Deprecated
public class DefaultRestrictionDataToModelConverterRegistry implements RestrictionDataToModelConverterRegistry, InitializingBean
{
	@Autowired
	private Set<RestrictionDataToModelConverter> allRestrictionDataToModelConverters;
	private final Map<String, RestrictionDataToModelConverter> convertersByType = new HashMap<>();

	@Override
	public Optional<RestrictionDataToModelConverter> getRestrictionDataToModelConverter(final String typecode)
	{
		return Optional.ofNullable(convertersByType.get(typecode));
	}

	@Override
	public Collection<RestrictionDataToModelConverter> getRestrictionDataToModelConverters()
	{
		return convertersByType.values();
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		getAllRestrictionDataToModelConverters().stream().forEach(converter -> putOrUpdateConverter(converter));
	}

	protected void putOrUpdateConverter(final RestrictionDataToModelConverter converter)
	{
		convertersByType.put(converter.getTypecode(), converter);
	}

	protected Set<RestrictionDataToModelConverter> getAllRestrictionDataToModelConverters()
	{
		return allRestrictionDataToModelConverters;
	}

	public void setAllRestrictionDataToModelConverters(
			final Set<RestrictionDataToModelConverter> allRestrictionDataToModelConverters)
	{
		this.allRestrictionDataToModelConverters = allRestrictionDataToModelConverters;
	}

}
