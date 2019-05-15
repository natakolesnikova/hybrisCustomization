/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.storelocator.impl;

import static java.util.stream.Collectors.counting;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.Collection;
import java.util.Objects;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.storelocator.model.PointOfServiceModel;


@IntegrationTest
public class DefaultPointOfServiceDaoIT extends ServicelayerTest
{
	private static final int SELECT_ALL_ITEMS = 0;
	private static final int REQUESTED_ITEMS_COUNT = 2;
	@Resource
	private DefaultPointOfServiceDao pointOfServiceDao;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createTestPosEntries();
	}

	protected void createTestPosEntries() throws Exception
	{
		importCsv("/import/test/PointOfServiceSampleTestData.csv", "UTF-8");
	}

	@Test
	public void shouldSelectAllItemsForGeocoding() throws Exception
	{
		//when
		final Collection<PointOfServiceModel> posToGeocode = pointOfServiceDao.getPosToGeocode(SELECT_ALL_ITEMS);
		//then
		assertThat(posToGeocode).hasSize(5);
	}

	@Test
	public void shouldSelectRequestedNumberOfItemsForGeocoding() throws Exception
	{
		//when
		final Collection<PointOfServiceModel> posToGeocode = pointOfServiceDao.getPosToGeocode(REQUESTED_ITEMS_COUNT);
		//then
		assertThat(posToGeocode).hasSize(REQUESTED_ITEMS_COUNT);
	}

	@Test
	public void shouldHaveEmptyGeocodingTimestampIfQualifiesForGeocoding() throws Exception
	{
		//when
		final Collection<PointOfServiceModel> posToGeocode = pointOfServiceDao.getPosToGeocode(SELECT_ALL_ITEMS);
		//then
		final Long numberOfPosWithGeocodingTimestamp = posToGeocode.stream().map(PointOfServiceModel::getGeocodeTimestamp)
				.filter(Objects::nonNull).collect(counting());
		assertThat(numberOfPosWithGeocodingTimestamp).isZero();
	}
}
