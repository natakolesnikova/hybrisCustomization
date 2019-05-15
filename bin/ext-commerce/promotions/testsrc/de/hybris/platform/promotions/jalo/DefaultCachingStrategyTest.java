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
package de.hybris.platform.promotions.jalo;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.promotions.jalo.DefaultCachingStrategy.SessionCacheContainer;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Test;


@IntegrationTest
public class DefaultCachingStrategyTest extends ServicelayerBaseTest
{
	private static final String TEST_CART_CODE = "test_cart";

	@Resource
	private SessionService sessionService;
	@Resource
	private DefaultCachingStrategy testPromotionsCachingStrategy;

	@Test
	public void testGetCache()
	{
		final Map<String, List<PromotionResult>> cache = testPromotionsCachingStrategy.getCache();
		assertThat(cache).isNotNull().isEmpty();
		final SessionCacheContainer sessionCacheContainer = sessionService
					 .getAttribute(DefaultCachingStrategy.SESSION_CACHE_ATTRIBUTE);
		assertThat(sessionCacheContainer).isNotNull();
		assertThat(sessionCacheContainer.getCache()).isNotNull().isEqualTo(cache);
	}

	@Test
	public void testPut()
	{
		final CachedPromotionResult promotionResult = new CachedPromotionResult();
		final List<PromotionResult> promotionResults = Collections.singletonList(promotionResult);
		testPromotionsCachingStrategy.put(TEST_CART_CODE, promotionResults);

		final SessionCacheContainer sessionCacheContainer = sessionService
					 .getAttribute(DefaultCachingStrategy.SESSION_CACHE_ATTRIBUTE);
		assertThat(sessionCacheContainer.getCache()).isNotNull();
		assertThat(sessionCacheContainer.getCache().get(TEST_CART_CODE)).isNotNull().hasSize(1);
		assertThat(sessionCacheContainer.getCache().get(TEST_CART_CODE).get(0)).isEqualTo(promotionResult);
	}

	@Test
	public void testGetWhenSessionDestroyed()
	{
		final Map<String, List<PromotionResult>> cache = testPromotionsCachingStrategy.getCache();
		assertThat(cache).isNotNull().isEmpty();

		sessionService.closeCurrentSession();

		final Map<String, List<PromotionResult>> cacheAsSessionAttribute = sessionService
					 .getAttribute(DefaultCachingStrategy.SESSION_CACHE_ATTRIBUTE);
		assertThat(cacheAsSessionAttribute).isNull();
	}

	@After
	@Override
	public void finish()
	{
		// DO NOTHIG
	}

}
