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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.promotions.jalo.PromotionsManager.AutoApplyMode;
import de.hybris.platform.promotions.result.PromotionOrderResults;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CachingPromotionsManagerTest
{

	@InjectMocks
	private MockCachingPromotionsManager cachingPromotionsManager;
	@Mock
	private CachingStrategy cache;
	@Mock
	private SessionContext ctx;
	@Mock
	private Collection<PromotionGroup> promotionGroups;

	private SessionContext sessionContext;
	private JaloSession jaloSession;

	@Before
	public void setUp()
	{
		cachingPromotionsManager = spy(cachingPromotionsManager);
		sessionContext = cachingPromotionsManager.getSessionContext();
		jaloSession = cachingPromotionsManager.getJaloSession();
	}

	@Test
	public void testUpdatePromotionsWithCache()
	{
		cachingPromotionsManager.allowCaching(Boolean.TRUE);

		final Cart cart = spy(new Cart());
		doReturn(new Object()).when(cart).getSyncObject();
		doReturn("cart_code").when(cart).getCode(sessionContext);

		final Date now = new Date();
		cachingPromotionsManager.updatePromotions(ctx, promotionGroups, cart, true, AutoApplyMode.APPLY_ALL,
				AutoApplyMode.APPLY_ALL, now);
		verify(cachingPromotionsManager).setCachingAllowed(sessionContext, cart);
		verify(jaloSession).removeLocalSessionContext();
		verify(cachingPromotionsManager).doUpdatePromotionsOutOfCache(sessionContext, promotionGroups, cart, true,
				AutoApplyMode.APPLY_ALL, AutoApplyMode.APPLY_ALL, now);
		verify(cachingPromotionsManager).isCachingAllowed(sessionContext);
		verify(cache).put("cart_code", Collections.EMPTY_LIST);
	}

	@Test
	public void testUpdatePromotionsNoCache()
	{
		cachingPromotionsManager.allowCaching(Boolean.FALSE);

		final Cart cart = spy(new Cart());
		doReturn(new Object()).when(cart).getSyncObject();
		doReturn("cart_code").when(cart).getCode(sessionContext);

		final Date now = new Date();
		cachingPromotionsManager.updatePromotions(ctx, promotionGroups, cart, true, AutoApplyMode.APPLY_ALL,
				AutoApplyMode.APPLY_ALL, now);
		verify(cachingPromotionsManager).setCachingAllowed(sessionContext, cart);
		verify(jaloSession).removeLocalSessionContext();
		verify(cachingPromotionsManager).doUpdatePromotionsOutOfCache(sessionContext, promotionGroups, cart, true,
				AutoApplyMode.APPLY_ALL, AutoApplyMode.APPLY_ALL, now);
		verify(cachingPromotionsManager).isCachingAllowed(sessionContext);
		verify(cache, times(0)).put(Matchers.anyString(), Matchers.anyListOf(PromotionResult.class));
	}

	@Test
	public void testGetPromotionResultsWithCache()
	{
		cachingPromotionsManager.allowCaching(Boolean.TRUE);

		final Cart cart = spy(new Cart());
		doReturn("cart_code").when(cart).getCode(sessionContext);

		cachingPromotionsManager.getPromotionResults(ctx, cart);
		verify(cachingPromotionsManager).setCachingAllowed(sessionContext, cart);
		verify(jaloSession).removeLocalSessionContext();
		verify(cachingPromotionsManager).getPromotionResultsInternal(sessionContext, cart);
		verify(cachingPromotionsManager).isCachingAllowed(sessionContext);
		verify(cache, times(0)).put(Matchers.anyString(), Matchers.anyListOf(PromotionResult.class));
	}

	@Test
	public void testCleanupCartWithCache() throws Exception
	{
		cachingPromotionsManager.allowCaching(Boolean.TRUE);

		final Cart cart = spy(new Cart());
		doReturn("cart_code").when(cart).getCode(sessionContext);

		cachingPromotionsManager.cleanupCart(ctx, cart);
		verify(cachingPromotionsManager).setCachingAllowed(sessionContext, cart);
		verify(jaloSession).removeLocalSessionContext();
		verify(cachingPromotionsManager).deleteStoredPromotionResults(sessionContext, cart, false);
		verify(cachingPromotionsManager).isCachingAllowed(sessionContext);
		verify(cache).remove("cart_code");
		verify(cart, times(0)).calculateTotals(Matchers.anyBoolean());
		verify(cachingPromotionsManager, times(0)).getPromotionResultsInternal(any(SessionContext.class), any(AbstractOrder.class));
	}

	@Test
	public void testCleanupCartNoCache() throws Exception
	{
		cachingPromotionsManager.allowCaching(Boolean.FALSE);

		final Cart cart = spy(new Cart());
		doReturn("cart_code").when(cart).getCode(sessionContext);

		cachingPromotionsManager.cleanupCart(ctx, cart);
		verify(cachingPromotionsManager).setCachingAllowed(sessionContext, cart);
		verify(jaloSession).removeLocalSessionContext();
		verify(cachingPromotionsManager).deleteStoredPromotionResults(sessionContext, cart, false);
		verify(cachingPromotionsManager, times(2)).isCachingAllowed(sessionContext);
		verify(cache, times(0)).remove("cart_code");
		verify(cart, times(0)).calculateTotals(Matchers.anyBoolean());
		verify(cachingPromotionsManager, times(1)).getPromotionResultsInternal(any(SessionContext.class), any(AbstractOrder.class));
	}

	public static class MockCachingPromotionsManager extends CachingPromotionsManager
	{
		private final JaloSession js = mock(JaloSession.class);
		private final SessionContext sessionContext;

		public MockCachingPromotionsManager()
		{
			sessionContext = mock(SessionContext.class);
			when(js.createLocalSessionContext(any(SessionContext.class))).thenReturn(sessionContext);
		}

		@Override
		public void setTenant(final Tenant tenant)
		{
			// do nothing
		}

		@Override
		protected JaloSession getCurrentJaloSession()
		{
			return js;
		}

		@Override
		protected List<PromotionResult> getNonCachedPromotionResultsInternal(final SessionContext ctx, final AbstractOrder order)
		{
			return Collections.EMPTY_LIST;
		}

		@Override
		protected PromotionOrderResults doUpdatePromotionsOutOfCache(final SessionContext ctx,
				final Collection<PromotionGroup> promotionGroups, final AbstractOrder order, final boolean evaluateRestrictions,
				final AutoApplyMode productPromotionMode, final AutoApplyMode orderPromotionMode, final Date date)
		{
			return new PromotionOrderResults(ctx, order, Collections.EMPTY_LIST, 0d);
		}

		public SessionContext getSessionContext()
		{
			return sessionContext;
		}

		public JaloSession getJaloSession()
		{
			return js;
		}

		public void allowCaching(final Boolean allowCaching)
		{
			when(sessionContext.getAttribute(CACHING_ALLOWED)).thenReturn(allowCaching);
		}

	}

}
