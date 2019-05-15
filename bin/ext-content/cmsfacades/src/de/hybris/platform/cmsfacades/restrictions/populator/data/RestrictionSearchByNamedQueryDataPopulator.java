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
package de.hybris.platform.cmsfacades.restrictions.populator.data;

import static de.hybris.platform.cms2.model.contents.CMSItemModel.CATALOGVERSION;
import static de.hybris.platform.cms2.model.contents.CMSItemModel.NAME;
import static de.hybris.platform.core.model.ItemModel.ITEMTYPE;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.common.populator.impl.AbstractNamedQueryDataPopulator;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Required;


/**
 * This populator reads parameters from the NamedQueryData and add them to the NamedQuery parameter's Map for
 * restriction search.
 * 
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class RestrictionSearchByNamedQueryDataPopulator extends AbstractNamedQueryDataPopulator
{
	private static final String TYPECODE = "typeCode";

	private TypeService typeService;

	@Override
	public Map<String, ? extends Object> convertParameters(final String params)
	{
		final Map<String, String> paramMap = buildParameterStringMap(params);
		final String name = Objects.nonNull(paramMap.get(NAME)) ? paramMap.get(NAME) : "";
		final ComposedTypeModel itemType = getTypeService().getComposedTypeForCode(paramMap.get(TYPECODE));

		final Map<String, Object> namedQueryParameterMap = new HashMap<>();
		namedQueryParameterMap.put(NAME, PERCENT + name + PERCENT);
		namedQueryParameterMap.put(ITEMTYPE, itemType);
		namedQueryParameterMap.put(CATALOGVERSION, getActiveCatalogVersion());
		return namedQueryParameterMap;
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

}