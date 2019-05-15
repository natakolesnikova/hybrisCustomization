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
package de.hybris.platform.cmsfacades.restrictions.service;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;

import java.util.Collection;
import java.util.Optional;


/**
 * Registry that stores a collection of <code>RestrictionConverterType</code> elements.
 * 
 * @deprecated since 6.6
 */
@HybrisDeprecation(sinceVersion = "6.6")
@Deprecated
public interface RestrictionModelToDataConverterRegistry
{
	/**
	 * Get a specific <code>RestrictionModelToDataConverter</code> by type code. If none is found matching the type code,
	 * return the converter for <code>AbstractRestrictionModel</code>.
	 *
	 * @param typecode
	 *           - the model type code of the element to retrieve from the registry
	 * @return the element matching the restriction type
	 */
	Optional<RestrictionModelToDataConverter> getRestrictionModelToDataConverter(String typecode);

	/**
	 * Get all elements in the registry.
	 *
	 * @return all items in the registry; never <tt>null</tt>
	 */
	Collection<RestrictionModelToDataConverter> getRestrictionModelToDataConverters();
}
