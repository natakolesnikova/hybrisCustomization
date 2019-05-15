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
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cmsfacades.restrictions.service.RestrictionModelToDataConverter;
import de.hybris.platform.cmsfacades.restrictions.service.RestrictionModelToDataConverterRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Default implementation of the <code>RestrictionModelToDataConverterRegistry</code>. This implementation uses
 * autowire-by-type to inject all beans implementing {@link RestrictionModelToDataConverter}.
 * 
 * @deprecated since 6.6
 */
@HybrisDeprecation(sinceVersion = "6.6")
@Deprecated
public class DefaultRestrictionModelToDataConverterRegistry implements RestrictionModelToDataConverterRegistry, InitializingBean
{
	@Autowired
	private Set<RestrictionModelToDataConverter> allRestrictionModelToDataConverters;
	private final Map<String, RestrictionModelToDataConverter> convertersByType = new HashMap<>();

	@Override
	public Optional<RestrictionModelToDataConverter> getRestrictionModelToDataConverter(final String typecode)
	{
		return Optional.ofNullable(Optional.ofNullable(convertersByType.get(typecode))
				.orElseGet(() -> convertersByType.get(AbstractRestrictionModel._TYPECODE)));
	}

	@Override
	public Collection<RestrictionModelToDataConverter> getRestrictionModelToDataConverters()
	{
		return convertersByType.values();
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		getAllRestrictionModelToDataConverters().stream().forEach(converter -> putOrUpdateConverter(converter));
	}

	protected void putOrUpdateConverter(final RestrictionModelToDataConverter converter)
	{
		convertersByType.put(converter.getTypecode(), converter);
	}

	protected Set<RestrictionModelToDataConverter> getAllRestrictionModelToDataConverters()
	{
		return allRestrictionModelToDataConverters;
	}

	public void setAllRestrictionModelToDataConverters(
			final Set<RestrictionModelToDataConverter> allRestrictionDataToModelConverters)
	{
		this.allRestrictionModelToDataConverters = allRestrictionDataToModelConverters;
	}

}
