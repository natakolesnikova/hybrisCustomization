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
package de.hybris.platform.cmsfacades.catalogversions.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.catalogversions.service.PageDisplayConditionService;
import de.hybris.platform.cmsfacades.common.service.ClassFieldFinder;
import de.hybris.platform.cmsfacades.data.DisplayConditionData;
import de.hybris.platform.cmsfacades.data.OptionData;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolverTypeRegistry;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link PageDisplayConditionService} to retrieve all display conditions available for a given
 * catalog version. A display condition determines if a page is: <br>
 * - a PRIMARY page ({@link AbstractPageModel#getDefaultPage()} {@code = true}) or <br>
 * - a VARIATION page ({@link AbstractPageModel#getDefaultPage()} {@code = false}).
 */
public class DefaultPageDisplayConditionService implements PageDisplayConditionService
{
	private CMSAdminSiteService cmsAdminSiteService;
	private PageVariationResolverTypeRegistry cmsPageVariationResolverTypeRegistry;
	private Set<Class<?>> cmsSupportedPages;

	@Override
	public List<DisplayConditionData> getDisplayConditions()
	{
		final Map<String, List<OptionData>> optionDataMap = getCmsSupportedPages().stream()
				.map(clazz -> ClassFieldFinder.getTypeCode(clazz))
				.collect(Collectors.toMap(Function.identity(), typecode -> getDisplayCondition(typecode)));
		final List<DisplayConditionData> displayConditions = optionDataMap.entrySet().stream()
				.map(entry -> convertToDisplayConditionData(entry)).collect(Collectors.toList());
		return displayConditions;
	}

	@Override
	public List<DisplayConditionData> getDisplayConditions(final CatalogVersionModel catalogVersion)
	{
		getCmsAdminSiteService().setActiveCatalogVersion(catalogVersion);
		return getDisplayConditions();
	}

	protected List<OptionData> getDisplayCondition(final String typecode)
	{
		return getCmsPageVariationResolverTypeRegistry().getPageVariationResolverType(typecode).get().getResolver()
				.findDisplayConditions(typecode);
	}

	protected DisplayConditionData convertToDisplayConditionData(final Entry<String, List<OptionData>> optionEntry)
	{
		final DisplayConditionData displayCondition = new DisplayConditionData();
		displayCondition.setTypecode(optionEntry.getKey());
		displayCondition.setOptions(optionEntry.getValue());
		return displayCondition;
	}

	protected CMSAdminSiteService getCmsAdminSiteService()
	{
		return cmsAdminSiteService;
	}

	@Required
	public void setCmsAdminSiteService(final CMSAdminSiteService cmsAdminSiteService)
	{
		this.cmsAdminSiteService = cmsAdminSiteService;
	}

	protected PageVariationResolverTypeRegistry getCmsPageVariationResolverTypeRegistry()
	{
		return cmsPageVariationResolverTypeRegistry;
	}

	@Required
	public void setCmsPageVariationResolverTypeRegistry(
			final PageVariationResolverTypeRegistry cmsPageVariationResolverTypeRegistry)
	{
		this.cmsPageVariationResolverTypeRegistry = cmsPageVariationResolverTypeRegistry;
	}

	protected Set<Class<?>> getCmsSupportedPages()
	{
		return cmsSupportedPages;
	}

	@Required
	public void setCmsSupportedPages(final Set<Class<?>> cmsSupportedPages)
	{
		this.cmsSupportedPages = cmsSupportedPages;
	}


}
