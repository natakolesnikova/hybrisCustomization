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
package de.hybris.platform.cms2.registry.impl;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.ABTestCMSComponentContainerModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cms2.strategies.CMSComponentContainerStrategy;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


@UnitTest
public class DefaultCMSComponentContainerRegistryTest
{
	private final DefaultCMSComponentContainerRegistry registry = new DefaultCMSComponentContainerRegistry();

	private CMSComponentContainerStrategy defaultStrategy;
	private CMSComponentContainerStrategy strategy1;
	private CMSComponentContainerStrategy strategy2;

	@Before
	public void setUp()
	{
		defaultStrategy = new DummyCMSComponentContainerStrategy(null);
		strategy1 = new DummyCMSComponentContainerStrategy(OtherDummyContainerModel.class);
		strategy2 = new DummyCMSComponentContainerStrategy(ABTestCMSComponentContainerModel.class);

		registry.setDefaultStrategy(defaultStrategy);
		registry.setCmsComponentContainerStrategies(Sets.newHashSet(strategy1, strategy2));
	}

	@Test
	public void shouldPopulateAllStrategies() throws Exception
	{
		registry.afterPropertiesSet();
		assertThat(registry.getCmsComponentContainerStrategies(), containsInAnyOrder(strategy1, strategy2));
	}

	@Test
	public void shouldRemoveDefaultStrategyFromSet() throws Exception
	{
		strategy1 = defaultStrategy;
		registry.afterPropertiesSet();
		assertThat(registry.getCmsComponentContainerStrategies(), not(contains(strategy1)));
	}

	@Test
	public void shouldGetStrategyForContainer()
	{
		final OtherDummyContainerModel container = new OtherDummyContainerModel();
		final CMSComponentContainerStrategy strategy = registry.getStrategy(container);
		assertThat(strategy, is(strategy1));
	}

	@Test
	public void shouldGetDefaultStrategy_NoMatchingClassType()
	{
		final DummyContainerModel container = new DummyContainerModel();
		final CMSComponentContainerStrategy strategy = registry.getStrategy(container);
		assertThat(strategy, is(defaultStrategy));
	}

	/**
	 * Dummy strategy that simply returns the <code>AbstractCMSComponentContainerModel</code> class that was given in the
	 * constructor.
	 */
	private class DummyCMSComponentContainerStrategy implements CMSComponentContainerStrategy
	{
		private final Class<? extends AbstractCMSComponentContainerModel> clazz;

		public DummyCMSComponentContainerStrategy(final Class<? extends AbstractCMSComponentContainerModel> clazz)
		{
			this.clazz = clazz;
		}

		@Override
		public List<AbstractCMSComponentModel> getDisplayComponentsForContainer(final AbstractCMSComponentContainerModel container)
		{
			return null;
		}

		@Override
		public Class<? extends AbstractCMSComponentContainerModel> getContainerClass()
		{
			return clazz;
		}
	}

	/**
	 * Dummy class extending AbstractCMSComponentContainerModel used for testing
	 */
	private class DummyContainerModel extends AbstractCMSComponentContainerModel
	{
		// Marker only
	}

	/**
	 * Dummy class extending AbstractCMSComponentContainerModel used for testing
	 */
	private class OtherDummyContainerModel extends AbstractCMSComponentContainerModel
	{
		// Marker Only
	}
}
