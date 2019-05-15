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
package de.hybris.platform.cmsfacades.cmsitems.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.contains;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel;
import de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.cmsfacades.util.BaseIntegrationTest;
import de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultCMSItemFacadeIntegrationTest extends BaseIntegrationTest
{
	private static final String ACTIVE_UNTIL = "activeUntil";
	private static final String ACTIVE_FROM = "activeFrom";
	private static final String CATALOG_VERSION = "catalogVersion";
	private static final String NAME = "name";
	private static final String ITEMTYPE = "itemtype";
	private static final String UUID = "uuid";

	@Resource
	private CMSItemFacade defaultCMSItemFacade;
	@Resource
	private ModelService modelService;
	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	@Resource
	private UniqueItemIdentifierService cmsUniqueItemIdentifierService;

	private String onlineAppleUUID;

	@Before
	public void setup()
	{
		final CatalogVersionModel appleOnlineCV = catalogVersionModelMother.createAppleOnlineCatalogVersionModel();
		final Optional<ItemData> onlineAppleCatalog = cmsUniqueItemIdentifierService.getItemData(appleOnlineCV);
		onlineAppleUUID = onlineAppleCatalog.get().getItemId();
	}

	/*
	 * CMSTimeRestrictionModel.USESTORETIMEZONE is blacklisted and has a default value. 
	 * Should be able to create a new CMSTimeRestriction without passing a USESTORETIMEZONE value.
	 */
	@Test
	public void shouldCreateCMSTimeRestrictionWithoutUseStoreTimeZone() throws CMSItemNotFoundException
	{
		final Map<String, Object> timeRestriction = new HashMap<>();
		timeRestriction.put(ITEMTYPE, CMSTimeRestrictionModel._TYPECODE);
		timeRestriction.put(NAME, "TestTime");
		timeRestriction.put(CATALOG_VERSION, onlineAppleUUID);
		timeRestriction.put(ACTIVE_FROM, "2017-08-03T14:33:00+0000");
		timeRestriction.put(ACTIVE_UNTIL, "2017-08-17T14:33:00+0000");

		final Map<String, Object> createdItem = defaultCMSItemFacade.createItem(timeRestriction);

		assertThat(createdItem.keySet(), hasSize(greaterThan(5)));
		assertThat(createdItem.keySet(), not(contains(CMSTimeRestrictionModel.USESTORETIMEZONE)));
	}

	/*
	 * CMSTimeRestrictionModel.USESTORETIMEZONE is blacklisted and has a default value.
	 * Should be able to update a new CMSTimeRestriction without passing a USESTORETIMEZONE value.
	 */
	@Test
	public void shouldUpdateCMSTimeRestrictionWithoutUseStoreTimeZone() throws CMSItemNotFoundException
	{
		final Map<String, Object> timeRestriction = new HashMap<>();
		timeRestriction.put(ITEMTYPE, CMSTimeRestrictionModel._TYPECODE);
		timeRestriction.put(NAME, "TestTime");
		timeRestriction.put(CATALOG_VERSION, onlineAppleUUID);
		timeRestriction.put(ACTIVE_FROM, "2017-10-03T08:33:00+0000");
		timeRestriction.put(ACTIVE_UNTIL, "2017-10-17T08:33:00+0000");

		final Map<String, Object> createdItem = defaultCMSItemFacade.createItem(timeRestriction);

		final String newName = "TestTime-Renamed";
		createdItem.put(NAME, newName);
		createdItem.put(ACTIVE_FROM, "2017-12-15T10:33:00+0000");
		createdItem.put(ACTIVE_UNTIL, "2017-12-20T10:33:00+0000");
		final Map<String, Object> updatedItem = defaultCMSItemFacade.updateItem(createdItem.get(UUID).toString(), createdItem);

		assertThat(updatedItem.get(NAME), equalTo(newName));
	}

}
