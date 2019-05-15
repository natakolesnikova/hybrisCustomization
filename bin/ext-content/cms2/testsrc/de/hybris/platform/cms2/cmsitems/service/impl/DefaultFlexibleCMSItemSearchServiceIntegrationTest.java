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
package de.hybris.platform.cms2.cmsitems.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class DefaultFlexibleCMSItemSearchServiceIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private DefaultFlexibleCMSItemSearchService cmsItemSearchService;

	@Test
	public void shouldAppendTypeExclusions()
	{
		final StringBuilder queryBuilder = new StringBuilder();
		final HashMap<String, Object> queryParameters = new HashMap<>();

		cmsItemSearchService.appendTypeExclusions(AbstractPageModel._TYPECODE, queryBuilder, queryParameters);

		assertThat(queryBuilder.toString(), equalTo(" AND {type.code} NOT IN (?excludedTypes)"));
		assertThat(((List<?>) queryParameters.get("excludedTypes")), hasSize(greaterThan(0)));
	}

}
