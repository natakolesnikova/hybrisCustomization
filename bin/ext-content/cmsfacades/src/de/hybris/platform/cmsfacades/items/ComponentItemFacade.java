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
package de.hybris.platform.cmsfacades.items;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.pagescontentslotscomponents.PageContentSlotComponentFacade;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.List;


/**
 * Component facade interface which deals with methods related to component operations.
 *
 */
public interface ComponentItemFacade
{

	/**
	 * Returns the {@link AbstractCMSComponentData} object by component id restricted by categoryCode or productCode or
	 * catalogCode.
	 *
	 * @param componentId
	 *           the component id
	 * @param categoryCode
	 *           the optional category code
	 * @param productCode
	 *           the optional product code
	 * @param catalogCode
	 *           the optional catalog code
	 * @return the {@link AbstractCMSComponentData} object
	 * @throws CMSItemNotFoundException
	 *            if the component is restricted or not visible.
	 */
	AbstractCMSComponentData getComponentById(final String componentId, final String categoryCode, final String productCode,
			final String catalogCode) throws CMSItemNotFoundException;

	/**
	 * Returns the list of found {@link AbstractCMSComponentData} in {@link SearchPageData}. The result is restricted by
	 * categoryCode or productCode or catalogCode.
	 *
	 * @param componentIds
	 *           the list of component id
	 * @param categoryCode
	 *           the optional category code
	 * @param productCode
	 *           the optional product code
	 * @param catalogCode
	 *           the optional catalog code
	 * @param searchPageData
	 *           the searchPageData contains requested pagination and sorting information
	 * @return the list of found {@link AbstractCMSComponentData} in {@link SearchPageData}.  If nothing is found the empty list is returned.
	 */
	SearchPageData<AbstractCMSComponentData> getComponentsByIds(final Collection<String> componentIds, final String categoryCode,
			final String productCode, final String catalogCode, final SearchPageData searchPageData);

	/**
	 * Find all component items.
	 *
	 * @return list of {@link AbstractCMSComponentData}; never <tt>null</tt>
	 *
	 * @deprecated since 6.6. Please use
	 *             {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade#findCMSItems(de.hybris.platform.cmsfacades.data.CMSItemSearchData, PageableData)}
	 *             instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	List<AbstractCMSComponentData> getAllComponentItems();

	/**
	 * For a given mask used as filter and a {@link PageableData} query, will return a page object consisting of the
	 * content list of the requested page number and the total number of entities for the given mask
	 *
	 * @param mask
	 *           the string value on which components will be filtered, implementations may choose to filter on the
	 *           component name
	 * @param pageableData
	 *           the {@link PageableData} object containing the page request details. PageableData may contain a String
	 *           value for sort, this will be used by a sorting strategy to select the most appropriate query or resort
	 *           to a default one.
	 * @return list of component data fulfilling search query
	 *
	 * @deprecated since 6.6. Please use
	 *             {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade#findCMSItems(de.hybris.platform.cmsfacades.data.CMSItemSearchData, PageableData)}
	 *             instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	SearchResult<AbstractCMSComponentData> findComponentByMask(String mask, PageableData pageableData);

	/**
	 * Adds a new component instance and assigns it to a slot.
	 *
	 * @param component
	 *           the {@link AbstractCMSComponentData}
	 * @return the {@link AbstractCMSComponentData}
	 * @throws ValidationException
	 *            throws it in case of validation failures.
	 *
	 * @deprecated since 6.6. Please use
	 *             {@link PageContentSlotComponentFacade#addComponentToContentSlot(de.hybris.platform.cmsfacades.data.PageContentSlotComponentData)}
	 *             instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	AbstractCMSComponentData addComponentItem(final AbstractCMSComponentData component) throws ValidationException;

	/**
	 * Remove an existing component instance.
	 *
	 * @param componentUid
	 *           - the uid of the cms component
	 * @throws CMSItemNotFoundException
	 *            - when no component exists for the given UID
	 *
	 * @deprecated since 6.6. Please use
	 *             {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade#deleteCMSItemByUuid(String)} instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	void removeComponentItem(final String componentUid) throws CMSItemNotFoundException;

	/**
	 * Get a abstract component item by uid.
	 *
	 * @param uid
	 *           the uid of the cms component
	 * @return AbstractCMSComponentData the {@link AbstractCMSComponentData}
	 * @throws CMSItemNotFoundException
	 *            when the component is unable to be found from the id
	 *
	 * @deprecated since 6.6. Please use
	 *             {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade#getCMSItemByUuid(String)} instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	AbstractCMSComponentData getComponentItemByUid(final String uid) throws CMSItemNotFoundException;

	/**
	 * Update a component item.
	 *
	 * @param componentUid
	 *           - the uid of the cms component
	 * @param componentData
	 *           - the dto containing component attribute values
	 * @throws CMSItemNotFoundException
	 *            - when the component is unable to be found from the id
	 *
	 * @deprecated since 6.6. Please use
	 *             {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade#updateItem(String, java.util.Map)}
	 *             instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	void updateComponentItem(final String componentUid, final AbstractCMSComponentData componentData)
			throws CMSItemNotFoundException;
}
