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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class RestrictionSearchByNamedQueryDataPopulatorTest
{
	private static final String TEST_TYPECODE = "TestTypeCode";

	@InjectMocks
	private RestrictionSearchByNamedQueryDataPopulator populator;

	@Mock
	private TypeService typeService;
	@Mock
	private CMSAdminSiteService adminSiteService;
	@Mock
	private CatalogVersionModel catalogVersion;
	@Mock
	private ComposedTypeModel composedTypeModel;

	@Before
	public void setUp()
	{
		when(adminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersion);
		when(typeService.getComposedTypeForCode(TEST_TYPECODE)).thenReturn(composedTypeModel);
		doThrow(new IllegalArgumentException()).when(typeService).getComposedTypeForCode(null);
	}

	@Test
	public void shouldConvertParameters()
	{
		final Map<String, ?> map = populator.convertParameters("name:help,typeCode:TestTypeCode");

		assertThat(map.get(AbstractRestrictionModel.CATALOGVERSION), equalTo(catalogVersion));
		assertThat(map.get(AbstractRestrictionModel.NAME), equalTo("%help%"));
		assertThat(map.get(ItemModel.ITEMTYPE), equalTo(composedTypeModel));
	}

	@Test
	public void shouldConvertParametersWithEmptyNameValue()
	{
		final Map<String, ?> map = populator.convertParameters("name:,typeCode:TestTypeCode");

		assertThat(map.get(AbstractRestrictionModel.CATALOGVERSION), equalTo(catalogVersion));
		assertThat(map.get(AbstractRestrictionModel.NAME), equalTo("%%"));
		assertThat(map.get(ItemModel.ITEMTYPE), equalTo(composedTypeModel));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldConvertParametersWithEmptyParams()
	{
		// typeCode cannot be null
		populator.convertParameters("");
	}

}
