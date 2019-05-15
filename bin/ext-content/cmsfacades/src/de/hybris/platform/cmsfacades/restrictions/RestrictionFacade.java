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
package de.hybris.platform.cmsfacades.restrictions;


import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.data.AbstractRestrictionData;
import de.hybris.platform.cmsfacades.data.NamedQueryData;
import de.hybris.platform.cmsfacades.data.RestrictionTypeData;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;


/**
 * Restriction facade interface which deals with methods related to restriction operations.
 *
 * @deprecated since 6.6. Please use {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade} instead.
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public interface RestrictionFacade
{

	/**
	 * Find all restrictions.
	 *
	 * @return list of {@link AbstractRestrictionData} ordered by name in ascending order; never <code>null</code>
	 *
	 * @deprecated since 6.6. Please use {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade} instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	List<AbstractRestrictionData> findAllRestrictions();

	/**
	 * For a given mask used as filter and a {@link NamedQueryData} query, will return a page object consisting of the
	 * content list of the requested page number and the total number of entities for the given mask
	 * <p>
	 * Some default values will be assigned to the {@link NamedQueryData} if they are not provided in the request. When
	 * {@link NamedQueryData#getParams()} defines a value for 'name' (e.g. params=name:hello) and a <code>mask</code> is
	 * also defined (e.g. mask=bye), because the filter is on the name field, the named query data will ignore the
	 * <code>mask</code> value and perform a search for all restrictions with name 'hello'.
	 *
	 * @param mask
	 *           the string value on which restrictions will be filtered, implementations may choose to filter on the
	 *           restriction name
	 * @param namedQuery
	 *           - the {@link NamedQueryData} object containing the page request details. NamedQuery may contain a String
	 *           value for sort, this will be used by a sorting strategy to select the most appropriate query or resort
	 *           to a default one.
	 * @return list of component data fulfilling search query
	 *
	 * @deprecated since 6.6. Please use {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade} instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	SearchResult<AbstractRestrictionData> findRestrictionsByMask(String mask, NamedQueryData namedQuery);

	/**
	 * Search for a single page of restrictions using a named query.
	 *
	 * @param namedQuery
	 *           - the {@link NamedQueryData} object containing the page request details. NamedQuery may contain a String
	 *           value for sort, this will be used by a sorting strategy to select the most appropriate query or resort
	 *           to a default one.
	 * @return the list of search results or empty collection
	 * @throws ValidationException
	 *            when the input contains validation errors
	 * 
	 * @deprecated since 6.6. Please use {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade} instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	SearchResult<AbstractRestrictionData> findRestrictionsByNamedQuery(NamedQueryData namedQuery) throws ValidationException;

	/**
	 * Find all restriction types.
	 *
	 * @return list of all {@link RestrictionTypeData}; never <code>null</code>
	 *
	 * @deprecated since 6.6. Please use
	 *             {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade#findCMSItems(de.hybris.platform.cmsfacades.data.CMSItemSearchData, de.hybris.platform.cms2.data.PageableData)}
	 *             instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	List<RestrictionTypeData> findAllRestrictionTypes();

	/**
	 * Create a new restriction
	 *
	 * @param restrictionData
	 *           the {@link AbstractRestrictionData}
	 * @return the created restriction
	 * @throws ValidationException
	 *            if there are validation errors
	 * 
	 * @deprecated since 6.6. Please use
	 *             {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade#createItem(java.util.Map)} instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	AbstractRestrictionData createRestriction(AbstractRestrictionData restrictionData) throws ValidationException;

	/**
	 * Find a restriction based on its id
	 *
	 * @param id
	 *           the string value of the restrictionId
	 * @return the found restriction
	 * @throws CMSItemNotFoundException
	 *            when restriction id is not valid
	 * 
	 * @deprecated since 6.6. Please use {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade#findCMSItems(List)}
	 *             instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	AbstractRestrictionData findRestrictionById(String id) throws CMSItemNotFoundException;

	/**
	 * Update a restriction
	 *
	 * @param id
	 *           the restrictionId
	 *
	 * @param restrictionData
	 *           the {@link AbstractRestrictionData}
	 * @return the updated {@link AbstractRestrictionData}
	 * @throws CMSItemNotFoundException
	 *
	 * @deprecated since 6.6. Please use
	 *             {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade#updateItem(String, java.util.Map)}
	 *             instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	AbstractRestrictionData updateRestriction(String id, AbstractRestrictionData restrictionData) throws CMSItemNotFoundException;

}
