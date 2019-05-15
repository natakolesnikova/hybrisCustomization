/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at May 15, 2019 12:44:13 PM                    ---
 * ----------------------------------------------------------------
 *  
 * [y] hybris Platform
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.commerceservices.jalo.promotions;

import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.promotions.PromotionOrderRestriction PromotionOrderRestriction}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedPromotionOrderRestriction extends AbstractPromotionRestriction
{
	/** Qualifier of the <code>PromotionOrderRestriction.orders</code> attribute **/
	public static final String ORDERS = "orders";
	/** Relation ordering override parameter constants for PromotionRestriction2OrderRel from ((commerceservices))*/
	protected static String PROMOTIONRESTRICTION2ORDERREL_SRC_ORDERED = "relation.PromotionRestriction2OrderRel.source.ordered";
	protected static String PROMOTIONRESTRICTION2ORDERREL_TGT_ORDERED = "relation.PromotionRestriction2OrderRel.target.ordered";
	/** Relation disable markmodifed parameter constants for PromotionRestriction2OrderRel from ((commerceservices))*/
	protected static String PROMOTIONRESTRICTION2ORDERREL_MARKMODIFIED = "relation.PromotionRestriction2OrderRel.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AbstractPromotionRestriction.DEFAULT_INITIAL_ATTRIBUTES);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	@Override
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("AbstractOrder");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(PROMOTIONRESTRICTION2ORDERREL_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionOrderRestriction.orders</code> attribute.
	 * @return the orders - Orders
	 */
	public Collection<AbstractOrder> getOrders(final SessionContext ctx)
	{
		final List<AbstractOrder> items = getLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.PROMOTIONRESTRICTION2ORDERREL,
			"AbstractOrder",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionOrderRestriction.orders</code> attribute.
	 * @return the orders - Orders
	 */
	public Collection<AbstractOrder> getOrders()
	{
		return getOrders( getSession().getSessionContext() );
	}
	
	public long getOrdersCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			CommerceServicesConstants.Relations.PROMOTIONRESTRICTION2ORDERREL,
			"AbstractOrder",
			null
		);
	}
	
	public long getOrdersCount()
	{
		return getOrdersCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionOrderRestriction.orders</code> attribute. 
	 * @param value the orders - Orders
	 */
	public void setOrders(final SessionContext ctx, final Collection<AbstractOrder> value)
	{
		setLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.PROMOTIONRESTRICTION2ORDERREL,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONRESTRICTION2ORDERREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionOrderRestriction.orders</code> attribute. 
	 * @param value the orders - Orders
	 */
	public void setOrders(final Collection<AbstractOrder> value)
	{
		setOrders( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to orders. 
	 * @param value the item to add to orders - Orders
	 */
	public void addToOrders(final SessionContext ctx, final AbstractOrder value)
	{
		addLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.PROMOTIONRESTRICTION2ORDERREL,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONRESTRICTION2ORDERREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to orders. 
	 * @param value the item to add to orders - Orders
	 */
	public void addToOrders(final AbstractOrder value)
	{
		addToOrders( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from orders. 
	 * @param value the item to remove from orders - Orders
	 */
	public void removeFromOrders(final SessionContext ctx, final AbstractOrder value)
	{
		removeLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.PROMOTIONRESTRICTION2ORDERREL,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONRESTRICTION2ORDERREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from orders. 
	 * @param value the item to remove from orders - Orders
	 */
	public void removeFromOrders(final AbstractOrder value)
	{
		removeFromOrders( getSession().getSessionContext(), value );
	}
	
}
