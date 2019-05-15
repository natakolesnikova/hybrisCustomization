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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cmsfacades.data.AbstractRestrictionData;
import de.hybris.platform.cmsfacades.restrictions.service.RestrictionModelToDataConverter;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class DefaultRestrictionModelToDataConverterRegistryTest
{

	private static final String INVALID = "invalid";
	private static final String TYPE_CODE_A = "TestTypecode-A";
	private static final String TYPE_CODE_B = "TestTypecode-B";

	private final Set<RestrictionModelToDataConverter> allConverters = new HashSet<>();
	private final DefaultRestrictionModelToDataConverterRegistry registry = new DefaultRestrictionModelToDataConverterRegistry();

	@Mock
	private AbstractPopulatingConverter<AbstractRestrictionModel, AbstractRestrictionData> converter;
	private DefaultRestrictionModelToDataConverter dataToModelConverter1;
	private DefaultRestrictionModelToDataConverter dataToModelConverter2;
	private DefaultRestrictionModelToDataConverter abstractDataToModelConverter;

	@Before
	public void setUp() throws Exception
	{
		dataToModelConverter1 = new DefaultRestrictionModelToDataConverter();
		dataToModelConverter1.setTypecode(TYPE_CODE_A);
		dataToModelConverter1.setConverter(converter);

		dataToModelConverter2 = new DefaultRestrictionModelToDataConverter();
		dataToModelConverter2.setTypecode(TYPE_CODE_B);
		dataToModelConverter2.setConverter(converter);

		abstractDataToModelConverter = new DefaultRestrictionModelToDataConverter();
		abstractDataToModelConverter.setTypecode(AbstractRestrictionModel._TYPECODE);
		abstractDataToModelConverter.setConverter(converter);

		allConverters.add(dataToModelConverter1);
		allConverters.add(dataToModelConverter2);
		allConverters.add(abstractDataToModelConverter);
		registry.setAllRestrictionModelToDataConverters(allConverters);
		registry.afterPropertiesSet();
	}

	@Test
	public void shouldPopulateConvertersInAfterPropertiesSet()
	{
		final Collection<RestrictionModelToDataConverter> result = registry.getAllRestrictionModelToDataConverters();
		assertThat(result.size(), equalTo(3));
		assertThat(result, containsInAnyOrder(abstractDataToModelConverter, dataToModelConverter1, dataToModelConverter2));
	}

	@Test
	public void shouldFindModelToDataConverter()
	{
		final Optional<RestrictionModelToDataConverter> result = registry.getRestrictionModelToDataConverter(TYPE_CODE_A);
		assertThat(result.isPresent(), equalTo(true));
		assertThat(result.get().getTypecode(), equalTo(TYPE_CODE_A));
		assertThat(result.get().getConverter(), equalTo(converter));
	}

	@Test
	public void shouldFindAbstractModelToDataConverter()
	{
		final Optional<RestrictionModelToDataConverter> result = registry.getRestrictionModelToDataConverter(INVALID);
		assertThat(result.isPresent(), equalTo(true));
		assertThat(result.get().getTypecode(), equalTo(AbstractRestrictionModel._TYPECODE));
		assertThat(result.get().getConverter(), equalTo(converter));
	}
}
