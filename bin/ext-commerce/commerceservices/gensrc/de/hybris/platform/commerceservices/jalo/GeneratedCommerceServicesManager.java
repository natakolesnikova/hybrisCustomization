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
package de.hybris.platform.commerceservices.jalo;

import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.commerceservices.jalo.CustomerList;
import de.hybris.platform.commerceservices.jalo.OrgUnit;
import de.hybris.platform.commerceservices.jalo.PickUpDeliveryMode;
import de.hybris.platform.commerceservices.jalo.consent.Consent;
import de.hybris.platform.commerceservices.jalo.consent.ConsentTemplate;
import de.hybris.platform.commerceservices.jalo.process.ForgottenPasswordProcess;
import de.hybris.platform.commerceservices.jalo.process.QuoteProcess;
import de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess;
import de.hybris.platform.commerceservices.jalo.process.StoreFrontProcess;
import de.hybris.platform.commerceservices.jalo.promotions.PromotionOrderRestriction;
import de.hybris.platform.commerceservices.jalo.storelocator.StoreLocatorFeature;
import de.hybris.platform.commerceservices.jalo.user.StoreEmployeeGroup;
import de.hybris.platform.constants.CoreConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LItem;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.media.MediaContainer;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.Quote;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.ordersplitting.jalo.Consignment;
import de.hybris.platform.ordersplitting.jalo.Warehouse;
import de.hybris.platform.promotions.jalo.PromotionGroup;
import de.hybris.platform.returns.jalo.ReturnRequest;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrFacetSearchConfig;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexedProperty;
import de.hybris.platform.store.BaseStore;
import de.hybris.platform.storelocator.jalo.PointOfService;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type <code>CommerceServicesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedCommerceServicesManager extends Extension
{
	/** Relation ordering override parameter constants for PromotionRestriction2OrderRel from ((commerceservices))*/
	protected static String PROMOTIONRESTRICTION2ORDERREL_SRC_ORDERED = "relation.PromotionRestriction2OrderRel.source.ordered";
	protected static String PROMOTIONRESTRICTION2ORDERREL_TGT_ORDERED = "relation.PromotionRestriction2OrderRel.target.ordered";
	/** Relation disable markmodifed parameter constants for PromotionRestriction2OrderRel from ((commerceservices))*/
	protected static String PROMOTIONRESTRICTION2ORDERREL_MARKMODIFIED = "relation.PromotionRestriction2OrderRel.markmodified";
	/** Relation ordering override parameter constants for BaseStore2CurrencyRel from ((commerceservices))*/
	protected static String BASESTORE2CURRENCYREL_SRC_ORDERED = "relation.BaseStore2CurrencyRel.source.ordered";
	protected static String BASESTORE2CURRENCYREL_TGT_ORDERED = "relation.BaseStore2CurrencyRel.target.ordered";
	/** Relation disable markmodifed parameter constants for BaseStore2CurrencyRel from ((commerceservices))*/
	protected static String BASESTORE2CURRENCYREL_MARKMODIFIED = "relation.BaseStore2CurrencyRel.markmodified";
	/** Relation ordering override parameter constants for BaseStore2LanguageRel from ((commerceservices))*/
	protected static String BASESTORE2LANGUAGEREL_SRC_ORDERED = "relation.BaseStore2LanguageRel.source.ordered";
	protected static String BASESTORE2LANGUAGEREL_TGT_ORDERED = "relation.BaseStore2LanguageRel.target.ordered";
	/** Relation disable markmodifed parameter constants for BaseStore2LanguageRel from ((commerceservices))*/
	protected static String BASESTORE2LANGUAGEREL_MARKMODIFIED = "relation.BaseStore2LanguageRel.markmodified";
	/** Relation ordering override parameter constants for BaseStore2CountryRel from ((commerceservices))*/
	protected static String BASESTORE2COUNTRYREL_SRC_ORDERED = "relation.BaseStore2CountryRel.source.ordered";
	protected static String BASESTORE2COUNTRYREL_TGT_ORDERED = "relation.BaseStore2CountryRel.target.ordered";
	/** Relation disable markmodifed parameter constants for BaseStore2CountryRel from ((commerceservices))*/
	protected static String BASESTORE2COUNTRYREL_MARKMODIFIED = "relation.BaseStore2CountryRel.markmodified";
	/** Relation ordering override parameter constants for BaseStore2WarehouseRel from ((commerceservices))*/
	protected static String BASESTORE2WAREHOUSEREL_SRC_ORDERED = "relation.BaseStore2WarehouseRel.source.ordered";
	protected static String BASESTORE2WAREHOUSEREL_TGT_ORDERED = "relation.BaseStore2WarehouseRel.target.ordered";
	/** Relation disable markmodifed parameter constants for BaseStore2WarehouseRel from ((commerceservices))*/
	protected static String BASESTORE2WAREHOUSEREL_MARKMODIFIED = "relation.BaseStore2WarehouseRel.markmodified";
	/** Relation ordering override parameter constants for BaseStore2DeliveryModeRel from ((commerceservices))*/
	protected static String BASESTORE2DELIVERYMODEREL_SRC_ORDERED = "relation.BaseStore2DeliveryModeRel.source.ordered";
	protected static String BASESTORE2DELIVERYMODEREL_TGT_ORDERED = "relation.BaseStore2DeliveryModeRel.target.ordered";
	/** Relation disable markmodifed parameter constants for BaseStore2DeliveryModeRel from ((commerceservices))*/
	protected static String BASESTORE2DELIVERYMODEREL_MARKMODIFIED = "relation.BaseStore2DeliveryModeRel.markmodified";
	/** Relation ordering override parameter constants for StoreLocation2StoreLocatorFeature from ((commerceservices))*/
	protected static String STORELOCATION2STORELOCATORFEATURE_SRC_ORDERED = "relation.StoreLocation2StoreLocatorFeature.source.ordered";
	protected static String STORELOCATION2STORELOCATORFEATURE_TGT_ORDERED = "relation.StoreLocation2StoreLocatorFeature.target.ordered";
	/** Relation disable markmodifed parameter constants for StoreLocation2StoreLocatorFeature from ((commerceservices))*/
	protected static String STORELOCATION2STORELOCATORFEATURE_MARKMODIFIED = "relation.StoreLocation2StoreLocatorFeature.markmodified";
	/** Relation ordering override parameter constants for PoS2WarehouseRel from ((commerceservices))*/
	protected static String POS2WAREHOUSEREL_SRC_ORDERED = "relation.PoS2WarehouseRel.source.ordered";
	protected static String POS2WAREHOUSEREL_TGT_ORDERED = "relation.PoS2WarehouseRel.target.ordered";
	/** Relation disable markmodifed parameter constants for PoS2WarehouseRel from ((commerceservices))*/
	protected static String POS2WAREHOUSEREL_MARKMODIFIED = "relation.PoS2WarehouseRel.markmodified";
	/**
	* {@link OneToManyHandler} for handling 1:n STOREEMPLOYEEGROUPS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<StoreEmployeeGroup> STOREEMPLGROUP2POSRELSTOREEMPLOYEEGROUPSHANDLER = new OneToManyHandler<StoreEmployeeGroup>(
	CommerceServicesConstants.TC.STOREEMPLOYEEGROUP,
	false,
	"store",
	null,
	false,
	true,
	CollectionType.SET
	);
	/** Relation ordering override parameter constants for QuoteToNotificationRel from ((commerceservices))*/
	protected static String QUOTETONOTIFICATIONREL_SRC_ORDERED = "relation.QuoteToNotificationRel.source.ordered";
	protected static String QUOTETONOTIFICATIONREL_TGT_ORDERED = "relation.QuoteToNotificationRel.target.ordered";
	/** Relation disable markmodifed parameter constants for QuoteToNotificationRel from ((commerceservices))*/
	protected static String QUOTETONOTIFICATIONREL_MARKMODIFIED = "relation.QuoteToNotificationRel.markmodified";
	/**
	* {@link OneToManyHandler} for handling 1:n ASSIGNEDQUOTES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<Quote> ASSIGNEE2QUOTESASSIGNEDQUOTESHANDLER = new OneToManyHandler<Quote>(
	CoreConstants.TC.QUOTE,
	true,
	"assignee",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("summary", AttributeMode.INITIAL);
		tmp.put("galleryImages", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.product.Product", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("site", AttributeMode.INITIAL);
		tmp.put("store", AttributeMode.INITIAL);
		tmp.put("guid", AttributeMode.INITIAL);
		tmp.put("quoteDiscountValuesInternal", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.AbstractOrder", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("deliveryPointOfService", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.AbstractOrderEntry", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("salesApplication", AttributeMode.INITIAL);
		tmp.put("language", AttributeMode.INITIAL);
		tmp.put("placedBy", AttributeMode.INITIAL);
		tmp.put("quoteReference", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.Order", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("deliveryPointOfService", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.ordersplitting.jalo.Consignment", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("saved", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.payment.PaymentInfo", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("issueNumber", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.payment.PaymentInfo", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("refundDeliveryCost", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.returns.jalo.ReturnRequest", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("saveTime", AttributeMode.INITIAL);
		tmp.put("savedBy", AttributeMode.INITIAL);
		tmp.put("quoteReference", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.Cart", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("title", AttributeMode.INITIAL);
		tmp.put("defaultPaymentInfo", AttributeMode.INITIAL);
		tmp.put("token", AttributeMode.INITIAL);
		tmp.put("originalUid", AttributeMode.INITIAL);
		tmp.put("type", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.user.Customer", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("visibleInAddressBook", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.user.Address", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("isocodeShort", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.c2l.Region", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("theme", AttributeMode.INITIAL);
		tmp.put("defaultLanguage", AttributeMode.INITIAL);
		tmp.put("locale", AttributeMode.INITIAL);
		tmp.put("channel", AttributeMode.INITIAL);
		tmp.put("defaultPromotionGroup", AttributeMode.INITIAL);
		tmp.put("solrFacetSearchConfiguration", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.basecommerce.jalo.site.BaseSite", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("net", AttributeMode.INITIAL);
		tmp.put("taxGroup", AttributeMode.INITIAL);
		tmp.put("defaultLanguage", AttributeMode.INITIAL);
		tmp.put("defaultCurrency", AttributeMode.INITIAL);
		tmp.put("defaultDeliveryOrigin", AttributeMode.INITIAL);
		tmp.put("solrFacetSearchConfiguration", AttributeMode.INITIAL);
		tmp.put("submitOrderProcessCode", AttributeMode.INITIAL);
		tmp.put("createReturnProcessCode", AttributeMode.INITIAL);
		tmp.put("externalTaxEnabled", AttributeMode.INITIAL);
		tmp.put("pickupInStoreMode", AttributeMode.INITIAL);
		tmp.put("maxRadiusForPoSSearch", AttributeMode.INITIAL);
		tmp.put("customerAllowedToIgnoreSuggestions", AttributeMode.INITIAL);
		tmp.put("paymentProvider", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.store.BaseStore", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("displayName", AttributeMode.INITIAL);
		tmp.put("nearbyStoreRadius", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.storelocator.jalo.PointOfService", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("facetSort", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexedProperty", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("cartReference", AttributeMode.INITIAL);
		tmp.put("previousEstimatedTotal", AttributeMode.INITIAL);
		tmp.put("assignee", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.Quote", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.assignedQuotes</code> attribute.
	 * @return the assignedQuotes
	 */
	public Collection<Quote> getAssignedQuotes(final SessionContext ctx, final User item)
	{
		return ASSIGNEE2QUOTESASSIGNEDQUOTESHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.assignedQuotes</code> attribute.
	 * @return the assignedQuotes
	 */
	public Collection<Quote> getAssignedQuotes(final User item)
	{
		return getAssignedQuotes( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.assignedQuotes</code> attribute. 
	 * @param value the assignedQuotes
	 */
	public void setAssignedQuotes(final SessionContext ctx, final User item, final Collection<Quote> value)
	{
		ASSIGNEE2QUOTESASSIGNEDQUOTESHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.assignedQuotes</code> attribute. 
	 * @param value the assignedQuotes
	 */
	public void setAssignedQuotes(final User item, final Collection<Quote> value)
	{
		setAssignedQuotes( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to assignedQuotes. 
	 * @param value the item to add to assignedQuotes
	 */
	public void addToAssignedQuotes(final SessionContext ctx, final User item, final Quote value)
	{
		ASSIGNEE2QUOTESASSIGNEDQUOTESHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to assignedQuotes. 
	 * @param value the item to add to assignedQuotes
	 */
	public void addToAssignedQuotes(final User item, final Quote value)
	{
		addToAssignedQuotes( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from assignedQuotes. 
	 * @param value the item to remove from assignedQuotes
	 */
	public void removeFromAssignedQuotes(final SessionContext ctx, final User item, final Quote value)
	{
		ASSIGNEE2QUOTESASSIGNEDQUOTESHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from assignedQuotes. 
	 * @param value the item to remove from assignedQuotes
	 */
	public void removeFromAssignedQuotes(final User item, final Quote value)
	{
		removeFromAssignedQuotes( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Quote.assignee</code> attribute.
	 * @return the assignee
	 */
	public User getAssignee(final SessionContext ctx, final Quote item)
	{
		return (User)item.getProperty( ctx, CommerceServicesConstants.Attributes.Quote.ASSIGNEE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Quote.assignee</code> attribute.
	 * @return the assignee
	 */
	public User getAssignee(final Quote item)
	{
		return getAssignee( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Quote.assignee</code> attribute. 
	 * @param value the assignee
	 */
	public void setAssignee(final SessionContext ctx, final Quote item, final User value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Quote.ASSIGNEE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Quote.assignee</code> attribute. 
	 * @param value the assignee
	 */
	public void setAssignee(final Quote item, final User value)
	{
		setAssignee( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Currency.baseStores</code> attribute.
	 * @return the baseStores
	 */
	public Collection<BaseStore> getBaseStores(final SessionContext ctx, final Currency item)
	{
		final List<BaseStore> items = item.getLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2CURRENCYREL,
			"BaseStore",
			null,
			Utilities.getRelationOrderingOverride(BASESTORE2CURRENCYREL_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Currency.baseStores</code> attribute.
	 * @return the baseStores
	 */
	public Collection<BaseStore> getBaseStores(final Currency item)
	{
		return getBaseStores( getSession().getSessionContext(), item );
	}
	
	public long getBaseStoresCount(final SessionContext ctx, final Currency item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2CURRENCYREL,
			"BaseStore",
			null
		);
	}
	
	public long getBaseStoresCount(final Currency item)
	{
		return getBaseStoresCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Currency.baseStores</code> attribute. 
	 * @param value the baseStores
	 */
	public void setBaseStores(final SessionContext ctx, final Currency item, final Collection<BaseStore> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2CURRENCYREL,
			null,
			value,
			Utilities.getRelationOrderingOverride(BASESTORE2CURRENCYREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2CURRENCYREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Currency.baseStores</code> attribute. 
	 * @param value the baseStores
	 */
	public void setBaseStores(final Currency item, final Collection<BaseStore> value)
	{
		setBaseStores( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to baseStores. 
	 * @param value the item to add to baseStores
	 */
	public void addToBaseStores(final SessionContext ctx, final Currency item, final BaseStore value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2CURRENCYREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(BASESTORE2CURRENCYREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2CURRENCYREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to baseStores. 
	 * @param value the item to add to baseStores
	 */
	public void addToBaseStores(final Currency item, final BaseStore value)
	{
		addToBaseStores( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from baseStores. 
	 * @param value the item to remove from baseStores
	 */
	public void removeFromBaseStores(final SessionContext ctx, final Currency item, final BaseStore value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2CURRENCYREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(BASESTORE2CURRENCYREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2CURRENCYREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from baseStores. 
	 * @param value the item to remove from baseStores
	 */
	public void removeFromBaseStores(final Currency item, final BaseStore value)
	{
		removeFromBaseStores( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Language.baseStores</code> attribute.
	 * @return the baseStores
	 */
	public Collection<BaseStore> getBaseStores(final SessionContext ctx, final Language item)
	{
		final List<BaseStore> items = item.getLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2LANGUAGEREL,
			"BaseStore",
			null,
			Utilities.getRelationOrderingOverride(BASESTORE2LANGUAGEREL_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Language.baseStores</code> attribute.
	 * @return the baseStores
	 */
	public Collection<BaseStore> getBaseStores(final Language item)
	{
		return getBaseStores( getSession().getSessionContext(), item );
	}
	
	public long getBaseStoresCount(final SessionContext ctx, final Language item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2LANGUAGEREL,
			"BaseStore",
			null
		);
	}
	
	public long getBaseStoresCount(final Language item)
	{
		return getBaseStoresCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Language.baseStores</code> attribute. 
	 * @param value the baseStores
	 */
	public void setBaseStores(final SessionContext ctx, final Language item, final Collection<BaseStore> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2LANGUAGEREL,
			null,
			value,
			Utilities.getRelationOrderingOverride(BASESTORE2LANGUAGEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2LANGUAGEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Language.baseStores</code> attribute. 
	 * @param value the baseStores
	 */
	public void setBaseStores(final Language item, final Collection<BaseStore> value)
	{
		setBaseStores( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to baseStores. 
	 * @param value the item to add to baseStores
	 */
	public void addToBaseStores(final SessionContext ctx, final Language item, final BaseStore value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2LANGUAGEREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(BASESTORE2LANGUAGEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2LANGUAGEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to baseStores. 
	 * @param value the item to add to baseStores
	 */
	public void addToBaseStores(final Language item, final BaseStore value)
	{
		addToBaseStores( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from baseStores. 
	 * @param value the item to remove from baseStores
	 */
	public void removeFromBaseStores(final SessionContext ctx, final Language item, final BaseStore value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2LANGUAGEREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(BASESTORE2LANGUAGEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2LANGUAGEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from baseStores. 
	 * @param value the item to remove from baseStores
	 */
	public void removeFromBaseStores(final Language item, final BaseStore value)
	{
		removeFromBaseStores( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Country.baseStores</code> attribute.
	 * @return the baseStores
	 */
	public Set<BaseStore> getBaseStores(final SessionContext ctx, final Country item)
	{
		final List<BaseStore> items = item.getLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2COUNTRYREL,
			"BaseStore",
			null,
			Utilities.getRelationOrderingOverride(BASESTORE2COUNTRYREL_SRC_ORDERED, true),
			false
		);
		return new LinkedHashSet<BaseStore>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Country.baseStores</code> attribute.
	 * @return the baseStores
	 */
	public Set<BaseStore> getBaseStores(final Country item)
	{
		return getBaseStores( getSession().getSessionContext(), item );
	}
	
	public long getBaseStoresCount(final SessionContext ctx, final Country item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2COUNTRYREL,
			"BaseStore",
			null
		);
	}
	
	public long getBaseStoresCount(final Country item)
	{
		return getBaseStoresCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Country.baseStores</code> attribute. 
	 * @param value the baseStores
	 */
	public void setBaseStores(final SessionContext ctx, final Country item, final Set<BaseStore> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2COUNTRYREL,
			null,
			value,
			Utilities.getRelationOrderingOverride(BASESTORE2COUNTRYREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2COUNTRYREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Country.baseStores</code> attribute. 
	 * @param value the baseStores
	 */
	public void setBaseStores(final Country item, final Set<BaseStore> value)
	{
		setBaseStores( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to baseStores. 
	 * @param value the item to add to baseStores
	 */
	public void addToBaseStores(final SessionContext ctx, final Country item, final BaseStore value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2COUNTRYREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(BASESTORE2COUNTRYREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2COUNTRYREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to baseStores. 
	 * @param value the item to add to baseStores
	 */
	public void addToBaseStores(final Country item, final BaseStore value)
	{
		addToBaseStores( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from baseStores. 
	 * @param value the item to remove from baseStores
	 */
	public void removeFromBaseStores(final SessionContext ctx, final Country item, final BaseStore value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2COUNTRYREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(BASESTORE2COUNTRYREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2COUNTRYREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from baseStores. 
	 * @param value the item to remove from baseStores
	 */
	public void removeFromBaseStores(final Country item, final BaseStore value)
	{
		removeFromBaseStores( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Warehouse.baseStores</code> attribute.
	 * @return the baseStores
	 */
	public Collection<BaseStore> getBaseStores(final SessionContext ctx, final Warehouse item)
	{
		final List<BaseStore> items = item.getLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2WAREHOUSEREL,
			"BaseStore",
			null,
			Utilities.getRelationOrderingOverride(BASESTORE2WAREHOUSEREL_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Warehouse.baseStores</code> attribute.
	 * @return the baseStores
	 */
	public Collection<BaseStore> getBaseStores(final Warehouse item)
	{
		return getBaseStores( getSession().getSessionContext(), item );
	}
	
	public long getBaseStoresCount(final SessionContext ctx, final Warehouse item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2WAREHOUSEREL,
			"BaseStore",
			null
		);
	}
	
	public long getBaseStoresCount(final Warehouse item)
	{
		return getBaseStoresCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Warehouse.baseStores</code> attribute. 
	 * @param value the baseStores
	 */
	public void setBaseStores(final SessionContext ctx, final Warehouse item, final Collection<BaseStore> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2WAREHOUSEREL,
			null,
			value,
			Utilities.getRelationOrderingOverride(BASESTORE2WAREHOUSEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2WAREHOUSEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Warehouse.baseStores</code> attribute. 
	 * @param value the baseStores
	 */
	public void setBaseStores(final Warehouse item, final Collection<BaseStore> value)
	{
		setBaseStores( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to baseStores. 
	 * @param value the item to add to baseStores
	 */
	public void addToBaseStores(final SessionContext ctx, final Warehouse item, final BaseStore value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2WAREHOUSEREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(BASESTORE2WAREHOUSEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2WAREHOUSEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to baseStores. 
	 * @param value the item to add to baseStores
	 */
	public void addToBaseStores(final Warehouse item, final BaseStore value)
	{
		addToBaseStores( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from baseStores. 
	 * @param value the item to remove from baseStores
	 */
	public void removeFromBaseStores(final SessionContext ctx, final Warehouse item, final BaseStore value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2WAREHOUSEREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(BASESTORE2WAREHOUSEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2WAREHOUSEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from baseStores. 
	 * @param value the item to remove from baseStores
	 */
	public void removeFromBaseStores(final Warehouse item, final BaseStore value)
	{
		removeFromBaseStores( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Quote.cartReference</code> attribute.
	 * @return the cartReference - The reference to cart used to manipulate the quote.
	 */
	public Cart getCartReference(final SessionContext ctx, final Quote item)
	{
		return (Cart)item.getProperty( ctx, CommerceServicesConstants.Attributes.Quote.CARTREFERENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Quote.cartReference</code> attribute.
	 * @return the cartReference - The reference to cart used to manipulate the quote.
	 */
	public Cart getCartReference(final Quote item)
	{
		return getCartReference( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Quote.cartReference</code> attribute. 
	 * @param value the cartReference - The reference to cart used to manipulate the quote.
	 */
	public void setCartReference(final SessionContext ctx, final Quote item, final Cart value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Quote.CARTREFERENCE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Quote.cartReference</code> attribute. 
	 * @param value the cartReference - The reference to cart used to manipulate the quote.
	 */
	public void setCartReference(final Quote item, final Cart value)
	{
		setCartReference( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.channel</code> attribute.
	 * @return the channel - The channel for this site.
	 */
	public EnumerationValue getChannel(final SessionContext ctx, final BaseSite item)
	{
		return (EnumerationValue)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseSite.CHANNEL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.channel</code> attribute.
	 * @return the channel - The channel for this site.
	 */
	public EnumerationValue getChannel(final BaseSite item)
	{
		return getChannel( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.channel</code> attribute. 
	 * @param value the channel - The channel for this site.
	 */
	public void setChannel(final SessionContext ctx, final BaseSite item, final EnumerationValue value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseSite.CHANNEL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.channel</code> attribute. 
	 * @param value the channel - The channel for this site.
	 */
	public void setChannel(final BaseSite item, final EnumerationValue value)
	{
		setChannel( getSession().getSessionContext(), item, value );
	}
	
	public Consent createConsent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CommerceServicesConstants.TC.CONSENT );
			return (Consent)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating Consent : "+e.getMessage(), 0 );
		}
	}
	
	public Consent createConsent(final Map attributeValues)
	{
		return createConsent( getSession().getSessionContext(), attributeValues );
	}
	
	public ConsentTemplate createConsentTemplate(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CommerceServicesConstants.TC.CONSENTTEMPLATE );
			return (ConsentTemplate)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating ConsentTemplate : "+e.getMessage(), 0 );
		}
	}
	
	public ConsentTemplate createConsentTemplate(final Map attributeValues)
	{
		return createConsentTemplate( getSession().getSessionContext(), attributeValues );
	}
	
	public CustomerList createCustomerList(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CommerceServicesConstants.TC.CUSTOMERLIST );
			return (CustomerList)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating CustomerList : "+e.getMessage(), 0 );
		}
	}
	
	public CustomerList createCustomerList(final Map attributeValues)
	{
		return createCustomerList( getSession().getSessionContext(), attributeValues );
	}
	
	public ForgottenPasswordProcess createForgottenPasswordProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CommerceServicesConstants.TC.FORGOTTENPASSWORDPROCESS );
			return (ForgottenPasswordProcess)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating ForgottenPasswordProcess : "+e.getMessage(), 0 );
		}
	}
	
	public ForgottenPasswordProcess createForgottenPasswordProcess(final Map attributeValues)
	{
		return createForgottenPasswordProcess( getSession().getSessionContext(), attributeValues );
	}
	
	public OrgUnit createOrgUnit(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CommerceServicesConstants.TC.ORGUNIT );
			return (OrgUnit)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating OrgUnit : "+e.getMessage(), 0 );
		}
	}
	
	public OrgUnit createOrgUnit(final Map attributeValues)
	{
		return createOrgUnit( getSession().getSessionContext(), attributeValues );
	}
	
	public PickUpDeliveryMode createPickUpDeliveryMode(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CommerceServicesConstants.TC.PICKUPDELIVERYMODE );
			return (PickUpDeliveryMode)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating PickUpDeliveryMode : "+e.getMessage(), 0 );
		}
	}
	
	public PickUpDeliveryMode createPickUpDeliveryMode(final Map attributeValues)
	{
		return createPickUpDeliveryMode( getSession().getSessionContext(), attributeValues );
	}
	
	public PromotionOrderRestriction createPromotionOrderRestriction(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CommerceServicesConstants.TC.PROMOTIONORDERRESTRICTION );
			return (PromotionOrderRestriction)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating PromotionOrderRestriction : "+e.getMessage(), 0 );
		}
	}
	
	public PromotionOrderRestriction createPromotionOrderRestriction(final Map attributeValues)
	{
		return createPromotionOrderRestriction( getSession().getSessionContext(), attributeValues );
	}
	
	public QuoteProcess createQuoteProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CommerceServicesConstants.TC.QUOTEPROCESS );
			return (QuoteProcess)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating QuoteProcess : "+e.getMessage(), 0 );
		}
	}
	
	public QuoteProcess createQuoteProcess(final Map attributeValues)
	{
		return createQuoteProcess( getSession().getSessionContext(), attributeValues );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.createReturnProcessCode</code> attribute.
	 * @return the createReturnProcessCode - The process name for return business process.
	 */
	public String getCreateReturnProcessCode(final SessionContext ctx, final BaseStore item)
	{
		return (String)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseStore.CREATERETURNPROCESSCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.createReturnProcessCode</code> attribute.
	 * @return the createReturnProcessCode - The process name for return business process.
	 */
	public String getCreateReturnProcessCode(final BaseStore item)
	{
		return getCreateReturnProcessCode( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.createReturnProcessCode</code> attribute. 
	 * @param value the createReturnProcessCode - The process name for return business process.
	 */
	public void setCreateReturnProcessCode(final SessionContext ctx, final BaseStore item, final String value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseStore.CREATERETURNPROCESSCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.createReturnProcessCode</code> attribute. 
	 * @param value the createReturnProcessCode - The process name for return business process.
	 */
	public void setCreateReturnProcessCode(final BaseStore item, final String value)
	{
		setCreateReturnProcessCode( getSession().getSessionContext(), item, value );
	}
	
	public StoreEmployeeGroup createStoreEmployeeGroup(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CommerceServicesConstants.TC.STOREEMPLOYEEGROUP );
			return (StoreEmployeeGroup)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating StoreEmployeeGroup : "+e.getMessage(), 0 );
		}
	}
	
	public StoreEmployeeGroup createStoreEmployeeGroup(final Map attributeValues)
	{
		return createStoreEmployeeGroup( getSession().getSessionContext(), attributeValues );
	}
	
	public StoreFrontCustomerProcess createStoreFrontCustomerProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CommerceServicesConstants.TC.STOREFRONTCUSTOMERPROCESS );
			return (StoreFrontCustomerProcess)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating StoreFrontCustomerProcess : "+e.getMessage(), 0 );
		}
	}
	
	public StoreFrontCustomerProcess createStoreFrontCustomerProcess(final Map attributeValues)
	{
		return createStoreFrontCustomerProcess( getSession().getSessionContext(), attributeValues );
	}
	
	public StoreFrontProcess createStoreFrontProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CommerceServicesConstants.TC.STOREFRONTPROCESS );
			return (StoreFrontProcess)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating StoreFrontProcess : "+e.getMessage(), 0 );
		}
	}
	
	public StoreFrontProcess createStoreFrontProcess(final Map attributeValues)
	{
		return createStoreFrontProcess( getSession().getSessionContext(), attributeValues );
	}
	
	public StoreLocatorFeature createStoreLocatorFeature(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CommerceServicesConstants.TC.STORELOCATORFEATURE );
			return (StoreLocatorFeature)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating StoreLocatorFeature : "+e.getMessage(), 0 );
		}
	}
	
	public StoreLocatorFeature createStoreLocatorFeature(final Map attributeValues)
	{
		return createStoreLocatorFeature( getSession().getSessionContext(), attributeValues );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.currencies</code> attribute.
	 * @return the currencies
	 */
	public Set<Currency> getCurrencies(final SessionContext ctx, final BaseStore item)
	{
		final List<Currency> items = item.getLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2CURRENCYREL,
			"Currency",
			null,
			Utilities.getRelationOrderingOverride(BASESTORE2CURRENCYREL_SRC_ORDERED, true),
			false
		);
		return new LinkedHashSet<Currency>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.currencies</code> attribute.
	 * @return the currencies
	 */
	public Set<Currency> getCurrencies(final BaseStore item)
	{
		return getCurrencies( getSession().getSessionContext(), item );
	}
	
	public long getCurrenciesCount(final SessionContext ctx, final BaseStore item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2CURRENCYREL,
			"Currency",
			null
		);
	}
	
	public long getCurrenciesCount(final BaseStore item)
	{
		return getCurrenciesCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.currencies</code> attribute. 
	 * @param value the currencies
	 */
	public void setCurrencies(final SessionContext ctx, final BaseStore item, final Set<Currency> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2CURRENCYREL,
			null,
			value,
			Utilities.getRelationOrderingOverride(BASESTORE2CURRENCYREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2CURRENCYREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.currencies</code> attribute. 
	 * @param value the currencies
	 */
	public void setCurrencies(final BaseStore item, final Set<Currency> value)
	{
		setCurrencies( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to currencies. 
	 * @param value the item to add to currencies
	 */
	public void addToCurrencies(final SessionContext ctx, final BaseStore item, final Currency value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2CURRENCYREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(BASESTORE2CURRENCYREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2CURRENCYREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to currencies. 
	 * @param value the item to add to currencies
	 */
	public void addToCurrencies(final BaseStore item, final Currency value)
	{
		addToCurrencies( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from currencies. 
	 * @param value the item to remove from currencies
	 */
	public void removeFromCurrencies(final SessionContext ctx, final BaseStore item, final Currency value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2CURRENCYREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(BASESTORE2CURRENCYREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2CURRENCYREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from currencies. 
	 * @param value the item to remove from currencies
	 */
	public void removeFromCurrencies(final BaseStore item, final Currency value)
	{
		removeFromCurrencies( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.customerAllowedToIgnoreSuggestions</code> attribute.
	 * @return the customerAllowedToIgnoreSuggestions - Determines whether the customer is allowed to bypass the list of suggested addresses and proceed with their unverified entry.
	 */
	public Boolean isCustomerAllowedToIgnoreSuggestions(final SessionContext ctx, final BaseStore item)
	{
		return (Boolean)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseStore.CUSTOMERALLOWEDTOIGNORESUGGESTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.customerAllowedToIgnoreSuggestions</code> attribute.
	 * @return the customerAllowedToIgnoreSuggestions - Determines whether the customer is allowed to bypass the list of suggested addresses and proceed with their unverified entry.
	 */
	public Boolean isCustomerAllowedToIgnoreSuggestions(final BaseStore item)
	{
		return isCustomerAllowedToIgnoreSuggestions( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.customerAllowedToIgnoreSuggestions</code> attribute. 
	 * @return the customerAllowedToIgnoreSuggestions - Determines whether the customer is allowed to bypass the list of suggested addresses and proceed with their unverified entry.
	 */
	public boolean isCustomerAllowedToIgnoreSuggestionsAsPrimitive(final SessionContext ctx, final BaseStore item)
	{
		Boolean value = isCustomerAllowedToIgnoreSuggestions( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.customerAllowedToIgnoreSuggestions</code> attribute. 
	 * @return the customerAllowedToIgnoreSuggestions - Determines whether the customer is allowed to bypass the list of suggested addresses and proceed with their unverified entry.
	 */
	public boolean isCustomerAllowedToIgnoreSuggestionsAsPrimitive(final BaseStore item)
	{
		return isCustomerAllowedToIgnoreSuggestionsAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.customerAllowedToIgnoreSuggestions</code> attribute. 
	 * @param value the customerAllowedToIgnoreSuggestions - Determines whether the customer is allowed to bypass the list of suggested addresses and proceed with their unverified entry.
	 */
	public void setCustomerAllowedToIgnoreSuggestions(final SessionContext ctx, final BaseStore item, final Boolean value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseStore.CUSTOMERALLOWEDTOIGNORESUGGESTIONS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.customerAllowedToIgnoreSuggestions</code> attribute. 
	 * @param value the customerAllowedToIgnoreSuggestions - Determines whether the customer is allowed to bypass the list of suggested addresses and proceed with their unverified entry.
	 */
	public void setCustomerAllowedToIgnoreSuggestions(final BaseStore item, final Boolean value)
	{
		setCustomerAllowedToIgnoreSuggestions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.customerAllowedToIgnoreSuggestions</code> attribute. 
	 * @param value the customerAllowedToIgnoreSuggestions - Determines whether the customer is allowed to bypass the list of suggested addresses and proceed with their unverified entry.
	 */
	public void setCustomerAllowedToIgnoreSuggestions(final SessionContext ctx, final BaseStore item, final boolean value)
	{
		setCustomerAllowedToIgnoreSuggestions( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.customerAllowedToIgnoreSuggestions</code> attribute. 
	 * @param value the customerAllowedToIgnoreSuggestions - Determines whether the customer is allowed to bypass the list of suggested addresses and proceed with their unverified entry.
	 */
	public void setCustomerAllowedToIgnoreSuggestions(final BaseStore item, final boolean value)
	{
		setCustomerAllowedToIgnoreSuggestions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.defaultCurrency</code> attribute.
	 * @return the defaultCurrency - The default currency for the site.
	 */
	public Currency getDefaultCurrency(final SessionContext ctx, final BaseStore item)
	{
		return (Currency)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseStore.DEFAULTCURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.defaultCurrency</code> attribute.
	 * @return the defaultCurrency - The default currency for the site.
	 */
	public Currency getDefaultCurrency(final BaseStore item)
	{
		return getDefaultCurrency( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.defaultCurrency</code> attribute. 
	 * @param value the defaultCurrency - The default currency for the site.
	 */
	public void setDefaultCurrency(final SessionContext ctx, final BaseStore item, final Currency value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseStore.DEFAULTCURRENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.defaultCurrency</code> attribute. 
	 * @param value the defaultCurrency - The default currency for the site.
	 */
	public void setDefaultCurrency(final BaseStore item, final Currency value)
	{
		setDefaultCurrency( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.defaultDeliveryOrigin</code> attribute.
	 * @return the defaultDeliveryOrigin - The default delivery origin for the site.
	 */
	public PointOfService getDefaultDeliveryOrigin(final SessionContext ctx, final BaseStore item)
	{
		return (PointOfService)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseStore.DEFAULTDELIVERYORIGIN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.defaultDeliveryOrigin</code> attribute.
	 * @return the defaultDeliveryOrigin - The default delivery origin for the site.
	 */
	public PointOfService getDefaultDeliveryOrigin(final BaseStore item)
	{
		return getDefaultDeliveryOrigin( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.defaultDeliveryOrigin</code> attribute. 
	 * @param value the defaultDeliveryOrigin - The default delivery origin for the site.
	 */
	public void setDefaultDeliveryOrigin(final SessionContext ctx, final BaseStore item, final PointOfService value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseStore.DEFAULTDELIVERYORIGIN,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.defaultDeliveryOrigin</code> attribute. 
	 * @param value the defaultDeliveryOrigin - The default delivery origin for the site.
	 */
	public void setDefaultDeliveryOrigin(final BaseStore item, final PointOfService value)
	{
		setDefaultDeliveryOrigin( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.defaultLanguage</code> attribute.
	 * @return the defaultLanguage - The default language for the site.
	 */
	public Language getDefaultLanguage(final SessionContext ctx, final BaseSite item)
	{
		return (Language)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseSite.DEFAULTLANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.defaultLanguage</code> attribute.
	 * @return the defaultLanguage - The default language for the site.
	 */
	public Language getDefaultLanguage(final BaseSite item)
	{
		return getDefaultLanguage( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.defaultLanguage</code> attribute. 
	 * @param value the defaultLanguage - The default language for the site.
	 */
	public void setDefaultLanguage(final SessionContext ctx, final BaseSite item, final Language value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseSite.DEFAULTLANGUAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.defaultLanguage</code> attribute. 
	 * @param value the defaultLanguage - The default language for the site.
	 */
	public void setDefaultLanguage(final BaseSite item, final Language value)
	{
		setDefaultLanguage( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.defaultLanguage</code> attribute.
	 * @return the defaultLanguage - The default language for the site.
	 */
	public Language getDefaultLanguage(final SessionContext ctx, final BaseStore item)
	{
		return (Language)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseStore.DEFAULTLANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.defaultLanguage</code> attribute.
	 * @return the defaultLanguage - The default language for the site.
	 */
	public Language getDefaultLanguage(final BaseStore item)
	{
		return getDefaultLanguage( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.defaultLanguage</code> attribute. 
	 * @param value the defaultLanguage - The default language for the site.
	 */
	public void setDefaultLanguage(final SessionContext ctx, final BaseStore item, final Language value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseStore.DEFAULTLANGUAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.defaultLanguage</code> attribute. 
	 * @param value the defaultLanguage - The default language for the site.
	 */
	public void setDefaultLanguage(final BaseStore item, final Language value)
	{
		setDefaultLanguage( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.defaultPaymentInfo</code> attribute.
	 * @return the defaultPaymentInfo - It holds information about default payment that is used by the customer.
	 */
	public PaymentInfo getDefaultPaymentInfo(final SessionContext ctx, final Customer item)
	{
		return (PaymentInfo)item.getProperty( ctx, CommerceServicesConstants.Attributes.Customer.DEFAULTPAYMENTINFO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.defaultPaymentInfo</code> attribute.
	 * @return the defaultPaymentInfo - It holds information about default payment that is used by the customer.
	 */
	public PaymentInfo getDefaultPaymentInfo(final Customer item)
	{
		return getDefaultPaymentInfo( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.defaultPaymentInfo</code> attribute. 
	 * @param value the defaultPaymentInfo - It holds information about default payment that is used by the customer.
	 */
	public void setDefaultPaymentInfo(final SessionContext ctx, final Customer item, final PaymentInfo value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Customer.DEFAULTPAYMENTINFO,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.defaultPaymentInfo</code> attribute. 
	 * @param value the defaultPaymentInfo - It holds information about default payment that is used by the customer.
	 */
	public void setDefaultPaymentInfo(final Customer item, final PaymentInfo value)
	{
		setDefaultPaymentInfo( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.defaultPromotionGroup</code> attribute.
	 * @return the defaultPromotionGroup - The default promotion group for the site.
	 */
	public PromotionGroup getDefaultPromotionGroup(final SessionContext ctx, final BaseSite item)
	{
		return (PromotionGroup)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseSite.DEFAULTPROMOTIONGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.defaultPromotionGroup</code> attribute.
	 * @return the defaultPromotionGroup - The default promotion group for the site.
	 */
	public PromotionGroup getDefaultPromotionGroup(final BaseSite item)
	{
		return getDefaultPromotionGroup( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.defaultPromotionGroup</code> attribute. 
	 * @param value the defaultPromotionGroup - The default promotion group for the site.
	 */
	public void setDefaultPromotionGroup(final SessionContext ctx, final BaseSite item, final PromotionGroup value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseSite.DEFAULTPROMOTIONGROUP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.defaultPromotionGroup</code> attribute. 
	 * @param value the defaultPromotionGroup - The default promotion group for the site.
	 */
	public void setDefaultPromotionGroup(final BaseSite item, final PromotionGroup value)
	{
		setDefaultPromotionGroup( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.deliveryCountries</code> attribute.
	 * @return the deliveryCountries
	 */
	public Collection<Country> getDeliveryCountries(final SessionContext ctx, final BaseStore item)
	{
		final List<Country> items = item.getLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2COUNTRYREL,
			"Country",
			null,
			Utilities.getRelationOrderingOverride(BASESTORE2COUNTRYREL_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.deliveryCountries</code> attribute.
	 * @return the deliveryCountries
	 */
	public Collection<Country> getDeliveryCountries(final BaseStore item)
	{
		return getDeliveryCountries( getSession().getSessionContext(), item );
	}
	
	public long getDeliveryCountriesCount(final SessionContext ctx, final BaseStore item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2COUNTRYREL,
			"Country",
			null
		);
	}
	
	public long getDeliveryCountriesCount(final BaseStore item)
	{
		return getDeliveryCountriesCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.deliveryCountries</code> attribute. 
	 * @param value the deliveryCountries
	 */
	public void setDeliveryCountries(final SessionContext ctx, final BaseStore item, final Collection<Country> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2COUNTRYREL,
			null,
			value,
			Utilities.getRelationOrderingOverride(BASESTORE2COUNTRYREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2COUNTRYREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.deliveryCountries</code> attribute. 
	 * @param value the deliveryCountries
	 */
	public void setDeliveryCountries(final BaseStore item, final Collection<Country> value)
	{
		setDeliveryCountries( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to deliveryCountries. 
	 * @param value the item to add to deliveryCountries
	 */
	public void addToDeliveryCountries(final SessionContext ctx, final BaseStore item, final Country value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2COUNTRYREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(BASESTORE2COUNTRYREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2COUNTRYREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to deliveryCountries. 
	 * @param value the item to add to deliveryCountries
	 */
	public void addToDeliveryCountries(final BaseStore item, final Country value)
	{
		addToDeliveryCountries( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from deliveryCountries. 
	 * @param value the item to remove from deliveryCountries
	 */
	public void removeFromDeliveryCountries(final SessionContext ctx, final BaseStore item, final Country value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2COUNTRYREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(BASESTORE2COUNTRYREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2COUNTRYREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from deliveryCountries. 
	 * @param value the item to remove from deliveryCountries
	 */
	public void removeFromDeliveryCountries(final BaseStore item, final Country value)
	{
		removeFromDeliveryCountries( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.deliveryModes</code> attribute.
	 * @return the deliveryModes
	 */
	public Set<DeliveryMode> getDeliveryModes(final SessionContext ctx, final BaseStore item)
	{
		final List<DeliveryMode> items = item.getLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2DELIVERYMODEREL,
			"DeliveryMode",
			null,
			false,
			false
		);
		return new LinkedHashSet<DeliveryMode>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.deliveryModes</code> attribute.
	 * @return the deliveryModes
	 */
	public Set<DeliveryMode> getDeliveryModes(final BaseStore item)
	{
		return getDeliveryModes( getSession().getSessionContext(), item );
	}
	
	public long getDeliveryModesCount(final SessionContext ctx, final BaseStore item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2DELIVERYMODEREL,
			"DeliveryMode",
			null
		);
	}
	
	public long getDeliveryModesCount(final BaseStore item)
	{
		return getDeliveryModesCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.deliveryModes</code> attribute. 
	 * @param value the deliveryModes
	 */
	public void setDeliveryModes(final SessionContext ctx, final BaseStore item, final Set<DeliveryMode> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2DELIVERYMODEREL,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2DELIVERYMODEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.deliveryModes</code> attribute. 
	 * @param value the deliveryModes
	 */
	public void setDeliveryModes(final BaseStore item, final Set<DeliveryMode> value)
	{
		setDeliveryModes( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to deliveryModes. 
	 * @param value the item to add to deliveryModes
	 */
	public void addToDeliveryModes(final SessionContext ctx, final BaseStore item, final DeliveryMode value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2DELIVERYMODEREL,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2DELIVERYMODEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to deliveryModes. 
	 * @param value the item to add to deliveryModes
	 */
	public void addToDeliveryModes(final BaseStore item, final DeliveryMode value)
	{
		addToDeliveryModes( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from deliveryModes. 
	 * @param value the item to remove from deliveryModes
	 */
	public void removeFromDeliveryModes(final SessionContext ctx, final BaseStore item, final DeliveryMode value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2DELIVERYMODEREL,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2DELIVERYMODEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from deliveryModes. 
	 * @param value the item to remove from deliveryModes
	 */
	public void removeFromDeliveryModes(final BaseStore item, final DeliveryMode value)
	{
		removeFromDeliveryModes( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.deliveryPointOfService</code> attribute.
	 * @return the deliveryPointOfService - The point of service to deliver to/collect from.
	 */
	public PointOfService getDeliveryPointOfService(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (PointOfService)item.getProperty( ctx, CommerceServicesConstants.Attributes.AbstractOrderEntry.DELIVERYPOINTOFSERVICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.deliveryPointOfService</code> attribute.
	 * @return the deliveryPointOfService - The point of service to deliver to/collect from.
	 */
	public PointOfService getDeliveryPointOfService(final AbstractOrderEntry item)
	{
		return getDeliveryPointOfService( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.deliveryPointOfService</code> attribute. 
	 * @param value the deliveryPointOfService - The point of service to deliver to/collect from.
	 */
	public void setDeliveryPointOfService(final SessionContext ctx, final AbstractOrderEntry item, final PointOfService value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.AbstractOrderEntry.DELIVERYPOINTOFSERVICE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.deliveryPointOfService</code> attribute. 
	 * @param value the deliveryPointOfService - The point of service to deliver to/collect from.
	 */
	public void setDeliveryPointOfService(final AbstractOrderEntry item, final PointOfService value)
	{
		setDeliveryPointOfService( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consignment.deliveryPointOfService</code> attribute.
	 * @return the deliveryPointOfService - The point of service to deliver to/collect from.
	 */
	public PointOfService getDeliveryPointOfService(final SessionContext ctx, final Consignment item)
	{
		return (PointOfService)item.getProperty( ctx, CommerceServicesConstants.Attributes.Consignment.DELIVERYPOINTOFSERVICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consignment.deliveryPointOfService</code> attribute.
	 * @return the deliveryPointOfService - The point of service to deliver to/collect from.
	 */
	public PointOfService getDeliveryPointOfService(final Consignment item)
	{
		return getDeliveryPointOfService( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consignment.deliveryPointOfService</code> attribute. 
	 * @param value the deliveryPointOfService - The point of service to deliver to/collect from.
	 */
	public void setDeliveryPointOfService(final SessionContext ctx, final Consignment item, final PointOfService value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Consignment.DELIVERYPOINTOFSERVICE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consignment.deliveryPointOfService</code> attribute. 
	 * @param value the deliveryPointOfService - The point of service to deliver to/collect from.
	 */
	public void setDeliveryPointOfService(final Consignment item, final PointOfService value)
	{
		setDeliveryPointOfService( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.displayName</code> attribute.
	 * @return the displayName - Used as the display name for search results etc.
	 */
	public String getDisplayName(final SessionContext ctx, final PointOfService item)
	{
		return (String)item.getProperty( ctx, CommerceServicesConstants.Attributes.PointOfService.DISPLAYNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.displayName</code> attribute.
	 * @return the displayName - Used as the display name for search results etc.
	 */
	public String getDisplayName(final PointOfService item)
	{
		return getDisplayName( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.displayName</code> attribute. 
	 * @param value the displayName - Used as the display name for search results etc.
	 */
	public void setDisplayName(final SessionContext ctx, final PointOfService item, final String value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.PointOfService.DISPLAYNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.displayName</code> attribute. 
	 * @param value the displayName - Used as the display name for search results etc.
	 */
	public void setDisplayName(final PointOfService item, final String value)
	{
		setDisplayName( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.externalTaxEnabled</code> attribute.
	 * @return the externalTaxEnabled - Determines whether the site should use external tax calculation
	 */
	public Boolean isExternalTaxEnabled(final SessionContext ctx, final BaseStore item)
	{
		return (Boolean)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseStore.EXTERNALTAXENABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.externalTaxEnabled</code> attribute.
	 * @return the externalTaxEnabled - Determines whether the site should use external tax calculation
	 */
	public Boolean isExternalTaxEnabled(final BaseStore item)
	{
		return isExternalTaxEnabled( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.externalTaxEnabled</code> attribute. 
	 * @return the externalTaxEnabled - Determines whether the site should use external tax calculation
	 */
	public boolean isExternalTaxEnabledAsPrimitive(final SessionContext ctx, final BaseStore item)
	{
		Boolean value = isExternalTaxEnabled( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.externalTaxEnabled</code> attribute. 
	 * @return the externalTaxEnabled - Determines whether the site should use external tax calculation
	 */
	public boolean isExternalTaxEnabledAsPrimitive(final BaseStore item)
	{
		return isExternalTaxEnabledAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.externalTaxEnabled</code> attribute. 
	 * @param value the externalTaxEnabled - Determines whether the site should use external tax calculation
	 */
	public void setExternalTaxEnabled(final SessionContext ctx, final BaseStore item, final Boolean value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseStore.EXTERNALTAXENABLED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.externalTaxEnabled</code> attribute. 
	 * @param value the externalTaxEnabled - Determines whether the site should use external tax calculation
	 */
	public void setExternalTaxEnabled(final BaseStore item, final Boolean value)
	{
		setExternalTaxEnabled( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.externalTaxEnabled</code> attribute. 
	 * @param value the externalTaxEnabled - Determines whether the site should use external tax calculation
	 */
	public void setExternalTaxEnabled(final SessionContext ctx, final BaseStore item, final boolean value)
	{
		setExternalTaxEnabled( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.externalTaxEnabled</code> attribute. 
	 * @param value the externalTaxEnabled - Determines whether the site should use external tax calculation
	 */
	public void setExternalTaxEnabled(final BaseStore item, final boolean value)
	{
		setExternalTaxEnabled( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexedProperty.facetSort</code> attribute.
	 * @return the facetSort - Deprecated - please use custom sort providers. The sort option to use to order the
	 * 							facet values.
	 */
	public EnumerationValue getFacetSort(final SessionContext ctx, final SolrIndexedProperty item)
	{
		return (EnumerationValue)item.getProperty( ctx, CommerceServicesConstants.Attributes.SolrIndexedProperty.FACETSORT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexedProperty.facetSort</code> attribute.
	 * @return the facetSort - Deprecated - please use custom sort providers. The sort option to use to order the
	 * 							facet values.
	 */
	public EnumerationValue getFacetSort(final SolrIndexedProperty item)
	{
		return getFacetSort( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrIndexedProperty.facetSort</code> attribute. 
	 * @param value the facetSort - Deprecated - please use custom sort providers. The sort option to use to order the
	 * 							facet values.
	 */
	public void setFacetSort(final SessionContext ctx, final SolrIndexedProperty item, final EnumerationValue value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.SolrIndexedProperty.FACETSORT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrIndexedProperty.facetSort</code> attribute. 
	 * @param value the facetSort - Deprecated - please use custom sort providers. The sort option to use to order the
	 * 							facet values.
	 */
	public void setFacetSort(final SolrIndexedProperty item, final EnumerationValue value)
	{
		setFacetSort( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.features</code> attribute.
	 * @return the features
	 */
	public Set<StoreLocatorFeature> getFeatures(final SessionContext ctx, final PointOfService item)
	{
		final List<StoreLocatorFeature> items = item.getLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.STORELOCATION2STORELOCATORFEATURE,
			"StoreLocatorFeature",
			null,
			false,
			false
		);
		return new LinkedHashSet<StoreLocatorFeature>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.features</code> attribute.
	 * @return the features
	 */
	public Set<StoreLocatorFeature> getFeatures(final PointOfService item)
	{
		return getFeatures( getSession().getSessionContext(), item );
	}
	
	public long getFeaturesCount(final SessionContext ctx, final PointOfService item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			CommerceServicesConstants.Relations.STORELOCATION2STORELOCATORFEATURE,
			"StoreLocatorFeature",
			null
		);
	}
	
	public long getFeaturesCount(final PointOfService item)
	{
		return getFeaturesCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.features</code> attribute. 
	 * @param value the features
	 */
	public void setFeatures(final SessionContext ctx, final PointOfService item, final Set<StoreLocatorFeature> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.STORELOCATION2STORELOCATORFEATURE,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(STORELOCATION2STORELOCATORFEATURE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.features</code> attribute. 
	 * @param value the features
	 */
	public void setFeatures(final PointOfService item, final Set<StoreLocatorFeature> value)
	{
		setFeatures( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to features. 
	 * @param value the item to add to features
	 */
	public void addToFeatures(final SessionContext ctx, final PointOfService item, final StoreLocatorFeature value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.STORELOCATION2STORELOCATORFEATURE,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(STORELOCATION2STORELOCATORFEATURE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to features. 
	 * @param value the item to add to features
	 */
	public void addToFeatures(final PointOfService item, final StoreLocatorFeature value)
	{
		addToFeatures( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from features. 
	 * @param value the item to remove from features
	 */
	public void removeFromFeatures(final SessionContext ctx, final PointOfService item, final StoreLocatorFeature value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.STORELOCATION2STORELOCATORFEATURE,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(STORELOCATION2STORELOCATORFEATURE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from features. 
	 * @param value the item to remove from features
	 */
	public void removeFromFeatures(final PointOfService item, final StoreLocatorFeature value)
	{
		removeFromFeatures( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.galleryImages</code> attribute.
	 * @return the galleryImages - A list of additional images for the product.
	 */
	public List<MediaContainer> getGalleryImages(final SessionContext ctx, final Product item)
	{
		List<MediaContainer> coll = (List<MediaContainer>)item.getProperty( ctx, CommerceServicesConstants.Attributes.Product.GALLERYIMAGES);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.galleryImages</code> attribute.
	 * @return the galleryImages - A list of additional images for the product.
	 */
	public List<MediaContainer> getGalleryImages(final Product item)
	{
		return getGalleryImages( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.galleryImages</code> attribute. 
	 * @param value the galleryImages - A list of additional images for the product.
	 */
	public void setGalleryImages(final SessionContext ctx, final Product item, final List<MediaContainer> value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Product.GALLERYIMAGES,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.galleryImages</code> attribute. 
	 * @param value the galleryImages - A list of additional images for the product.
	 */
	public void setGalleryImages(final Product item, final List<MediaContainer> value)
	{
		setGalleryImages( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Quote.generatedNotifications</code> attribute.
	 * @return the generatedNotifications
	 */
	public Set<EnumerationValue> getGeneratedNotifications(final SessionContext ctx, final Quote item)
	{
		final List<EnumerationValue> items = item.getLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.QUOTETONOTIFICATIONREL,
			"QuoteNotificationType",
			null,
			false,
			false
		);
		return new LinkedHashSet<EnumerationValue>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Quote.generatedNotifications</code> attribute.
	 * @return the generatedNotifications
	 */
	public Set<EnumerationValue> getGeneratedNotifications(final Quote item)
	{
		return getGeneratedNotifications( getSession().getSessionContext(), item );
	}
	
	public long getGeneratedNotificationsCount(final SessionContext ctx, final Quote item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			CommerceServicesConstants.Relations.QUOTETONOTIFICATIONREL,
			"QuoteNotificationType",
			null
		);
	}
	
	public long getGeneratedNotificationsCount(final Quote item)
	{
		return getGeneratedNotificationsCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Quote.generatedNotifications</code> attribute. 
	 * @param value the generatedNotifications
	 */
	public void setGeneratedNotifications(final SessionContext ctx, final Quote item, final Set<EnumerationValue> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.QUOTETONOTIFICATIONREL,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(QUOTETONOTIFICATIONREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Quote.generatedNotifications</code> attribute. 
	 * @param value the generatedNotifications
	 */
	public void setGeneratedNotifications(final Quote item, final Set<EnumerationValue> value)
	{
		setGeneratedNotifications( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to generatedNotifications. 
	 * @param value the item to add to generatedNotifications
	 */
	public void addToGeneratedNotifications(final SessionContext ctx, final Quote item, final EnumerationValue value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.QUOTETONOTIFICATIONREL,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(QUOTETONOTIFICATIONREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to generatedNotifications. 
	 * @param value the item to add to generatedNotifications
	 */
	public void addToGeneratedNotifications(final Quote item, final EnumerationValue value)
	{
		addToGeneratedNotifications( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from generatedNotifications. 
	 * @param value the item to remove from generatedNotifications
	 */
	public void removeFromGeneratedNotifications(final SessionContext ctx, final Quote item, final EnumerationValue value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.QUOTETONOTIFICATIONREL,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(QUOTETONOTIFICATIONREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from generatedNotifications. 
	 * @param value the item to remove from generatedNotifications
	 */
	public void removeFromGeneratedNotifications(final Quote item, final EnumerationValue value)
	{
		removeFromGeneratedNotifications( getSession().getSessionContext(), item, value );
	}
	
	@Override
	public String getName()
	{
		return CommerceServicesConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.guid</code> attribute.
	 * @return the guid - The guid for the anonymous cart used to lookup stored carts.
	 * 							The order guid is used as a non-authenticated deep link to the order history page.
	 */
	public String getGuid(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, CommerceServicesConstants.Attributes.AbstractOrder.GUID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.guid</code> attribute.
	 * @return the guid - The guid for the anonymous cart used to lookup stored carts.
	 * 							The order guid is used as a non-authenticated deep link to the order history page.
	 */
	public String getGuid(final AbstractOrder item)
	{
		return getGuid( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.guid</code> attribute. 
	 * @param value the guid - The guid for the anonymous cart used to lookup stored carts.
	 * 							The order guid is used as a non-authenticated deep link to the order history page.
	 */
	public void setGuid(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.AbstractOrder.GUID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.guid</code> attribute. 
	 * @param value the guid - The guid for the anonymous cart used to lookup stored carts.
	 * 							The order guid is used as a non-authenticated deep link to the order history page.
	 */
	public void setGuid(final AbstractOrder item, final String value)
	{
		setGuid( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.issueNumber</code> attribute.
	 * @return the issueNumber - Issue number is the reference information for the credit cart data.
	 */
	public Integer getIssueNumber(final SessionContext ctx, final PaymentInfo item)
	{
		return (Integer)item.getProperty( ctx, CommerceServicesConstants.Attributes.CreditCardPaymentInfo.ISSUENUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.issueNumber</code> attribute.
	 * @return the issueNumber - Issue number is the reference information for the credit cart data.
	 */
	public Integer getIssueNumber(final PaymentInfo item)
	{
		return getIssueNumber( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.issueNumber</code> attribute. 
	 * @return the issueNumber - Issue number is the reference information for the credit cart data.
	 */
	public int getIssueNumberAsPrimitive(final SessionContext ctx, final PaymentInfo item)
	{
		Integer value = getIssueNumber( ctx,item );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.issueNumber</code> attribute. 
	 * @return the issueNumber - Issue number is the reference information for the credit cart data.
	 */
	public int getIssueNumberAsPrimitive(final PaymentInfo item)
	{
		return getIssueNumberAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CreditCardPaymentInfo.issueNumber</code> attribute. 
	 * @param value the issueNumber - Issue number is the reference information for the credit cart data.
	 */
	public void setIssueNumber(final SessionContext ctx, final PaymentInfo item, final Integer value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.CreditCardPaymentInfo.ISSUENUMBER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CreditCardPaymentInfo.issueNumber</code> attribute. 
	 * @param value the issueNumber - Issue number is the reference information for the credit cart data.
	 */
	public void setIssueNumber(final PaymentInfo item, final Integer value)
	{
		setIssueNumber( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CreditCardPaymentInfo.issueNumber</code> attribute. 
	 * @param value the issueNumber - Issue number is the reference information for the credit cart data.
	 */
	public void setIssueNumber(final SessionContext ctx, final PaymentInfo item, final int value)
	{
		setIssueNumber( ctx, item, Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CreditCardPaymentInfo.issueNumber</code> attribute. 
	 * @param value the issueNumber - Issue number is the reference information for the credit cart data.
	 */
	public void setIssueNumber(final PaymentInfo item, final int value)
	{
		setIssueNumber( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.language</code> attribute.
	 * @return the language - The language used when the order is placed.
	 */
	public Language getLanguage(final SessionContext ctx, final Order item)
	{
		return (Language)item.getProperty( ctx, CommerceServicesConstants.Attributes.Order.LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.language</code> attribute.
	 * @return the language - The language used when the order is placed.
	 */
	public Language getLanguage(final Order item)
	{
		return getLanguage( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.language</code> attribute. 
	 * @param value the language - The language used when the order is placed.
	 */
	public void setLanguage(final SessionContext ctx, final Order item, final Language value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Order.LANGUAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.language</code> attribute. 
	 * @param value the language - The language used when the order is placed.
	 */
	public void setLanguage(final Order item, final Language value)
	{
		setLanguage( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.languages</code> attribute.
	 * @return the languages
	 */
	public Set<Language> getLanguages(final SessionContext ctx, final BaseStore item)
	{
		final List<Language> items = item.getLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2LANGUAGEREL,
			"Language",
			null,
			Utilities.getRelationOrderingOverride(BASESTORE2LANGUAGEREL_SRC_ORDERED, true),
			false
		);
		return new LinkedHashSet<Language>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.languages</code> attribute.
	 * @return the languages
	 */
	public Set<Language> getLanguages(final BaseStore item)
	{
		return getLanguages( getSession().getSessionContext(), item );
	}
	
	public long getLanguagesCount(final SessionContext ctx, final BaseStore item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2LANGUAGEREL,
			"Language",
			null
		);
	}
	
	public long getLanguagesCount(final BaseStore item)
	{
		return getLanguagesCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.languages</code> attribute. 
	 * @param value the languages
	 */
	public void setLanguages(final SessionContext ctx, final BaseStore item, final Set<Language> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2LANGUAGEREL,
			null,
			value,
			Utilities.getRelationOrderingOverride(BASESTORE2LANGUAGEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2LANGUAGEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.languages</code> attribute. 
	 * @param value the languages
	 */
	public void setLanguages(final BaseStore item, final Set<Language> value)
	{
		setLanguages( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to languages. 
	 * @param value the item to add to languages
	 */
	public void addToLanguages(final SessionContext ctx, final BaseStore item, final Language value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2LANGUAGEREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(BASESTORE2LANGUAGEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2LANGUAGEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to languages. 
	 * @param value the item to add to languages
	 */
	public void addToLanguages(final BaseStore item, final Language value)
	{
		addToLanguages( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from languages. 
	 * @param value the item to remove from languages
	 */
	public void removeFromLanguages(final SessionContext ctx, final BaseStore item, final Language value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2LANGUAGEREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(BASESTORE2LANGUAGEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2LANGUAGEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from languages. 
	 * @param value the item to remove from languages
	 */
	public void removeFromLanguages(final BaseStore item, final Language value)
	{
		removeFromLanguages( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.locale</code> attribute.
	 * @return the locale - The locale to use for each language.
	 */
	public String getLocale(final SessionContext ctx, final BaseSite item)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedBaseSite.getLocale requires a session language", 0 );
		}
		return (String)item.getLocalizedProperty( ctx, CommerceServicesConstants.Attributes.BaseSite.LOCALE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.locale</code> attribute.
	 * @return the locale - The locale to use for each language.
	 */
	public String getLocale(final BaseSite item)
	{
		return getLocale( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.locale</code> attribute. 
	 * @return the localized locale - The locale to use for each language.
	 */
	public Map<Language,String> getAllLocale(final SessionContext ctx, final BaseSite item)
	{
		return (Map<Language,String>)item.getAllLocalizedProperties(ctx,CommerceServicesConstants.Attributes.BaseSite.LOCALE,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.locale</code> attribute. 
	 * @return the localized locale - The locale to use for each language.
	 */
	public Map<Language,String> getAllLocale(final BaseSite item)
	{
		return getAllLocale( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.locale</code> attribute. 
	 * @param value the locale - The locale to use for each language.
	 */
	public void setLocale(final SessionContext ctx, final BaseSite item, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedBaseSite.setLocale requires a session language", 0 );
		}
		item.setLocalizedProperty(ctx, CommerceServicesConstants.Attributes.BaseSite.LOCALE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.locale</code> attribute. 
	 * @param value the locale - The locale to use for each language.
	 */
	public void setLocale(final BaseSite item, final String value)
	{
		setLocale( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.locale</code> attribute. 
	 * @param value the locale - The locale to use for each language.
	 */
	public void setAllLocale(final SessionContext ctx, final BaseSite item, final Map<Language,String> value)
	{
		item.setAllLocalizedProperties(ctx,CommerceServicesConstants.Attributes.BaseSite.LOCALE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.locale</code> attribute. 
	 * @param value the locale - The locale to use for each language.
	 */
	public void setAllLocale(final BaseSite item, final Map<Language,String> value)
	{
		setAllLocale( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.maxRadiusForPoSSearch</code> attribute.
	 * @return the maxRadiusForPoSSearch - The max radius when searching for PoS for a basestore.
	 */
	public Double getMaxRadiusForPoSSearch(final SessionContext ctx, final BaseStore item)
	{
		return (Double)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseStore.MAXRADIUSFORPOSSEARCH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.maxRadiusForPoSSearch</code> attribute.
	 * @return the maxRadiusForPoSSearch - The max radius when searching for PoS for a basestore.
	 */
	public Double getMaxRadiusForPoSSearch(final BaseStore item)
	{
		return getMaxRadiusForPoSSearch( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.maxRadiusForPoSSearch</code> attribute. 
	 * @return the maxRadiusForPoSSearch - The max radius when searching for PoS for a basestore.
	 */
	public double getMaxRadiusForPoSSearchAsPrimitive(final SessionContext ctx, final BaseStore item)
	{
		Double value = getMaxRadiusForPoSSearch( ctx,item );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.maxRadiusForPoSSearch</code> attribute. 
	 * @return the maxRadiusForPoSSearch - The max radius when searching for PoS for a basestore.
	 */
	public double getMaxRadiusForPoSSearchAsPrimitive(final BaseStore item)
	{
		return getMaxRadiusForPoSSearchAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.maxRadiusForPoSSearch</code> attribute. 
	 * @param value the maxRadiusForPoSSearch - The max radius when searching for PoS for a basestore.
	 */
	public void setMaxRadiusForPoSSearch(final SessionContext ctx, final BaseStore item, final Double value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseStore.MAXRADIUSFORPOSSEARCH,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.maxRadiusForPoSSearch</code> attribute. 
	 * @param value the maxRadiusForPoSSearch - The max radius when searching for PoS for a basestore.
	 */
	public void setMaxRadiusForPoSSearch(final BaseStore item, final Double value)
	{
		setMaxRadiusForPoSSearch( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.maxRadiusForPoSSearch</code> attribute. 
	 * @param value the maxRadiusForPoSSearch - The max radius when searching for PoS for a basestore.
	 */
	public void setMaxRadiusForPoSSearch(final SessionContext ctx, final BaseStore item, final double value)
	{
		setMaxRadiusForPoSSearch( ctx, item, Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.maxRadiusForPoSSearch</code> attribute. 
	 * @param value the maxRadiusForPoSSearch - The max radius when searching for PoS for a basestore.
	 */
	public void setMaxRadiusForPoSSearch(final BaseStore item, final double value)
	{
		setMaxRadiusForPoSSearch( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.nearbyStoreRadius</code> attribute.
	 * @return the nearbyStoreRadius - Overrides the default search radius when performing a search for nearby PoS from this PoS. 
	 * 							Unit is BaseStore's StorelocatorDistanceUnit.
	 */
	public Double getNearbyStoreRadius(final SessionContext ctx, final PointOfService item)
	{
		return (Double)item.getProperty( ctx, CommerceServicesConstants.Attributes.PointOfService.NEARBYSTORERADIUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.nearbyStoreRadius</code> attribute.
	 * @return the nearbyStoreRadius - Overrides the default search radius when performing a search for nearby PoS from this PoS. 
	 * 							Unit is BaseStore's StorelocatorDistanceUnit.
	 */
	public Double getNearbyStoreRadius(final PointOfService item)
	{
		return getNearbyStoreRadius( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.nearbyStoreRadius</code> attribute. 
	 * @return the nearbyStoreRadius - Overrides the default search radius when performing a search for nearby PoS from this PoS. 
	 * 							Unit is BaseStore's StorelocatorDistanceUnit.
	 */
	public double getNearbyStoreRadiusAsPrimitive(final SessionContext ctx, final PointOfService item)
	{
		Double value = getNearbyStoreRadius( ctx,item );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.nearbyStoreRadius</code> attribute. 
	 * @return the nearbyStoreRadius - Overrides the default search radius when performing a search for nearby PoS from this PoS. 
	 * 							Unit is BaseStore's StorelocatorDistanceUnit.
	 */
	public double getNearbyStoreRadiusAsPrimitive(final PointOfService item)
	{
		return getNearbyStoreRadiusAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.nearbyStoreRadius</code> attribute. 
	 * @param value the nearbyStoreRadius - Overrides the default search radius when performing a search for nearby PoS from this PoS. 
	 * 							Unit is BaseStore's StorelocatorDistanceUnit.
	 */
	public void setNearbyStoreRadius(final SessionContext ctx, final PointOfService item, final Double value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.PointOfService.NEARBYSTORERADIUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.nearbyStoreRadius</code> attribute. 
	 * @param value the nearbyStoreRadius - Overrides the default search radius when performing a search for nearby PoS from this PoS. 
	 * 							Unit is BaseStore's StorelocatorDistanceUnit.
	 */
	public void setNearbyStoreRadius(final PointOfService item, final Double value)
	{
		setNearbyStoreRadius( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.nearbyStoreRadius</code> attribute. 
	 * @param value the nearbyStoreRadius - Overrides the default search radius when performing a search for nearby PoS from this PoS. 
	 * 							Unit is BaseStore's StorelocatorDistanceUnit.
	 */
	public void setNearbyStoreRadius(final SessionContext ctx, final PointOfService item, final double value)
	{
		setNearbyStoreRadius( ctx, item, Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.nearbyStoreRadius</code> attribute. 
	 * @param value the nearbyStoreRadius - Overrides the default search radius when performing a search for nearby PoS from this PoS. 
	 * 							Unit is BaseStore's StorelocatorDistanceUnit.
	 */
	public void setNearbyStoreRadius(final PointOfService item, final double value)
	{
		setNearbyStoreRadius( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.net</code> attribute.
	 * @return the net - Determines if the prices are treated as net or gross prices in the given base
	 * 							store.
	 */
	public Boolean isNet(final SessionContext ctx, final BaseStore item)
	{
		return (Boolean)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseStore.NET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.net</code> attribute.
	 * @return the net - Determines if the prices are treated as net or gross prices in the given base
	 * 							store.
	 */
	public Boolean isNet(final BaseStore item)
	{
		return isNet( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.net</code> attribute. 
	 * @return the net - Determines if the prices are treated as net or gross prices in the given base
	 * 							store.
	 */
	public boolean isNetAsPrimitive(final SessionContext ctx, final BaseStore item)
	{
		Boolean value = isNet( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.net</code> attribute. 
	 * @return the net - Determines if the prices are treated as net or gross prices in the given base
	 * 							store.
	 */
	public boolean isNetAsPrimitive(final BaseStore item)
	{
		return isNetAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.net</code> attribute. 
	 * @param value the net - Determines if the prices are treated as net or gross prices in the given base
	 * 							store.
	 */
	public void setNet(final SessionContext ctx, final BaseStore item, final Boolean value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseStore.NET,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.net</code> attribute. 
	 * @param value the net - Determines if the prices are treated as net or gross prices in the given base
	 * 							store.
	 */
	public void setNet(final BaseStore item, final Boolean value)
	{
		setNet( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.net</code> attribute. 
	 * @param value the net - Determines if the prices are treated as net or gross prices in the given base
	 * 							store.
	 */
	public void setNet(final SessionContext ctx, final BaseStore item, final boolean value)
	{
		setNet( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.net</code> attribute. 
	 * @param value the net - Determines if the prices are treated as net or gross prices in the given base
	 * 							store.
	 */
	public void setNet(final BaseStore item, final boolean value)
	{
		setNet( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.originalUid</code> attribute.
	 * @return the originalUid
	 */
	public String getOriginalUid(final SessionContext ctx, final Customer item)
	{
		return (String)item.getProperty( ctx, CommerceServicesConstants.Attributes.Customer.ORIGINALUID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.originalUid</code> attribute.
	 * @return the originalUid
	 */
	public String getOriginalUid(final Customer item)
	{
		return getOriginalUid( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.originalUid</code> attribute. 
	 * @param value the originalUid
	 */
	public void setOriginalUid(final SessionContext ctx, final Customer item, final String value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Customer.ORIGINALUID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.originalUid</code> attribute. 
	 * @param value the originalUid
	 */
	public void setOriginalUid(final Customer item, final String value)
	{
		setOriginalUid( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.paymentProvider</code> attribute.
	 * @return the paymentProvider - The name of the payment provider that will get used for credit card subscriptions and any psp interaction
	 */
	public String getPaymentProvider(final SessionContext ctx, final BaseStore item)
	{
		return (String)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseStore.PAYMENTPROVIDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.paymentProvider</code> attribute.
	 * @return the paymentProvider - The name of the payment provider that will get used for credit card subscriptions and any psp interaction
	 */
	public String getPaymentProvider(final BaseStore item)
	{
		return getPaymentProvider( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.paymentProvider</code> attribute. 
	 * @param value the paymentProvider - The name of the payment provider that will get used for credit card subscriptions and any psp interaction
	 */
	public void setPaymentProvider(final SessionContext ctx, final BaseStore item, final String value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseStore.PAYMENTPROVIDER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.paymentProvider</code> attribute. 
	 * @param value the paymentProvider - The name of the payment provider that will get used for credit card subscriptions and any psp interaction
	 */
	public void setPaymentProvider(final BaseStore item, final String value)
	{
		setPaymentProvider( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.pickupInStoreMode</code> attribute.
	 * @return the pickupInStoreMode - The pickup mode for this store.
	 */
	public EnumerationValue getPickupInStoreMode(final SessionContext ctx, final BaseStore item)
	{
		return (EnumerationValue)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseStore.PICKUPINSTOREMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.pickupInStoreMode</code> attribute.
	 * @return the pickupInStoreMode - The pickup mode for this store.
	 */
	public EnumerationValue getPickupInStoreMode(final BaseStore item)
	{
		return getPickupInStoreMode( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.pickupInStoreMode</code> attribute. 
	 * @param value the pickupInStoreMode - The pickup mode for this store.
	 */
	public void setPickupInStoreMode(final SessionContext ctx, final BaseStore item, final EnumerationValue value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseStore.PICKUPINSTOREMODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.pickupInStoreMode</code> attribute. 
	 * @param value the pickupInStoreMode - The pickup mode for this store.
	 */
	public void setPickupInStoreMode(final BaseStore item, final EnumerationValue value)
	{
		setPickupInStoreMode( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.placedBy</code> attribute.
	 * @return the placedBy - The sales agent which the order was placed by.
	 */
	public User getPlacedBy(final SessionContext ctx, final Order item)
	{
		return (User)item.getProperty( ctx, CommerceServicesConstants.Attributes.Order.PLACEDBY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.placedBy</code> attribute.
	 * @return the placedBy - The sales agent which the order was placed by.
	 */
	public User getPlacedBy(final Order item)
	{
		return getPlacedBy( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.placedBy</code> attribute. 
	 * @param value the placedBy - The sales agent which the order was placed by.
	 */
	public void setPlacedBy(final SessionContext ctx, final Order item, final User value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Order.PLACEDBY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.placedBy</code> attribute. 
	 * @param value the placedBy - The sales agent which the order was placed by.
	 */
	public void setPlacedBy(final Order item, final User value)
	{
		setPlacedBy( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Warehouse.pointsOfService</code> attribute.
	 * @return the pointsOfService
	 */
	public Collection<PointOfService> getPointsOfService(final SessionContext ctx, final Warehouse item)
	{
		final List<PointOfService> items = item.getLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.POS2WAREHOUSEREL,
			"PointOfService",
			null,
			Utilities.getRelationOrderingOverride(POS2WAREHOUSEREL_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Warehouse.pointsOfService</code> attribute.
	 * @return the pointsOfService
	 */
	public Collection<PointOfService> getPointsOfService(final Warehouse item)
	{
		return getPointsOfService( getSession().getSessionContext(), item );
	}
	
	public long getPointsOfServiceCount(final SessionContext ctx, final Warehouse item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			CommerceServicesConstants.Relations.POS2WAREHOUSEREL,
			"PointOfService",
			null
		);
	}
	
	public long getPointsOfServiceCount(final Warehouse item)
	{
		return getPointsOfServiceCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Warehouse.pointsOfService</code> attribute. 
	 * @param value the pointsOfService
	 */
	public void setPointsOfService(final SessionContext ctx, final Warehouse item, final Collection<PointOfService> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.POS2WAREHOUSEREL,
			null,
			value,
			Utilities.getRelationOrderingOverride(POS2WAREHOUSEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(POS2WAREHOUSEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Warehouse.pointsOfService</code> attribute. 
	 * @param value the pointsOfService
	 */
	public void setPointsOfService(final Warehouse item, final Collection<PointOfService> value)
	{
		setPointsOfService( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to pointsOfService. 
	 * @param value the item to add to pointsOfService
	 */
	public void addToPointsOfService(final SessionContext ctx, final Warehouse item, final PointOfService value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.POS2WAREHOUSEREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(POS2WAREHOUSEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(POS2WAREHOUSEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to pointsOfService. 
	 * @param value the item to add to pointsOfService
	 */
	public void addToPointsOfService(final Warehouse item, final PointOfService value)
	{
		addToPointsOfService( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from pointsOfService. 
	 * @param value the item to remove from pointsOfService
	 */
	public void removeFromPointsOfService(final SessionContext ctx, final Warehouse item, final PointOfService value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.POS2WAREHOUSEREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(POS2WAREHOUSEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(POS2WAREHOUSEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from pointsOfService. 
	 * @param value the item to remove from pointsOfService
	 */
	public void removeFromPointsOfService(final Warehouse item, final PointOfService value)
	{
		removeFromPointsOfService( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Quote.previousEstimatedTotal</code> attribute.
	 * @return the previousEstimatedTotal - Previously estimated total
	 */
	public Double getPreviousEstimatedTotal(final SessionContext ctx, final Quote item)
	{
		return (Double)item.getProperty( ctx, CommerceServicesConstants.Attributes.Quote.PREVIOUSESTIMATEDTOTAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Quote.previousEstimatedTotal</code> attribute.
	 * @return the previousEstimatedTotal - Previously estimated total
	 */
	public Double getPreviousEstimatedTotal(final Quote item)
	{
		return getPreviousEstimatedTotal( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Quote.previousEstimatedTotal</code> attribute. 
	 * @return the previousEstimatedTotal - Previously estimated total
	 */
	public double getPreviousEstimatedTotalAsPrimitive(final SessionContext ctx, final Quote item)
	{
		Double value = getPreviousEstimatedTotal( ctx,item );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Quote.previousEstimatedTotal</code> attribute. 
	 * @return the previousEstimatedTotal - Previously estimated total
	 */
	public double getPreviousEstimatedTotalAsPrimitive(final Quote item)
	{
		return getPreviousEstimatedTotalAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Quote.previousEstimatedTotal</code> attribute. 
	 * @param value the previousEstimatedTotal - Previously estimated total
	 */
	public void setPreviousEstimatedTotal(final SessionContext ctx, final Quote item, final Double value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Quote.PREVIOUSESTIMATEDTOTAL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Quote.previousEstimatedTotal</code> attribute. 
	 * @param value the previousEstimatedTotal - Previously estimated total
	 */
	public void setPreviousEstimatedTotal(final Quote item, final Double value)
	{
		setPreviousEstimatedTotal( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Quote.previousEstimatedTotal</code> attribute. 
	 * @param value the previousEstimatedTotal - Previously estimated total
	 */
	public void setPreviousEstimatedTotal(final SessionContext ctx, final Quote item, final double value)
	{
		setPreviousEstimatedTotal( ctx, item, Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Quote.previousEstimatedTotal</code> attribute. 
	 * @param value the previousEstimatedTotal - Previously estimated total
	 */
	public void setPreviousEstimatedTotal(final Quote item, final double value)
	{
		setPreviousEstimatedTotal( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.promotionOrderRestrictions</code> attribute.
	 * @return the promotionOrderRestrictions - Promotion restrictions for order
	 */
	public Collection<PromotionOrderRestriction> getPromotionOrderRestrictions(final SessionContext ctx, final AbstractOrder item)
	{
		final List<PromotionOrderRestriction> items = item.getLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.PROMOTIONRESTRICTION2ORDERREL,
			"PromotionOrderRestriction",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.promotionOrderRestrictions</code> attribute.
	 * @return the promotionOrderRestrictions - Promotion restrictions for order
	 */
	public Collection<PromotionOrderRestriction> getPromotionOrderRestrictions(final AbstractOrder item)
	{
		return getPromotionOrderRestrictions( getSession().getSessionContext(), item );
	}
	
	public long getPromotionOrderRestrictionsCount(final SessionContext ctx, final AbstractOrder item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			CommerceServicesConstants.Relations.PROMOTIONRESTRICTION2ORDERREL,
			"PromotionOrderRestriction",
			null
		);
	}
	
	public long getPromotionOrderRestrictionsCount(final AbstractOrder item)
	{
		return getPromotionOrderRestrictionsCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.promotionOrderRestrictions</code> attribute. 
	 * @param value the promotionOrderRestrictions - Promotion restrictions for order
	 */
	public void setPromotionOrderRestrictions(final SessionContext ctx, final AbstractOrder item, final Collection<PromotionOrderRestriction> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.PROMOTIONRESTRICTION2ORDERREL,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONRESTRICTION2ORDERREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.promotionOrderRestrictions</code> attribute. 
	 * @param value the promotionOrderRestrictions - Promotion restrictions for order
	 */
	public void setPromotionOrderRestrictions(final AbstractOrder item, final Collection<PromotionOrderRestriction> value)
	{
		setPromotionOrderRestrictions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to promotionOrderRestrictions. 
	 * @param value the item to add to promotionOrderRestrictions - Promotion restrictions for order
	 */
	public void addToPromotionOrderRestrictions(final SessionContext ctx, final AbstractOrder item, final PromotionOrderRestriction value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.PROMOTIONRESTRICTION2ORDERREL,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONRESTRICTION2ORDERREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to promotionOrderRestrictions. 
	 * @param value the item to add to promotionOrderRestrictions - Promotion restrictions for order
	 */
	public void addToPromotionOrderRestrictions(final AbstractOrder item, final PromotionOrderRestriction value)
	{
		addToPromotionOrderRestrictions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from promotionOrderRestrictions. 
	 * @param value the item to remove from promotionOrderRestrictions - Promotion restrictions for order
	 */
	public void removeFromPromotionOrderRestrictions(final SessionContext ctx, final AbstractOrder item, final PromotionOrderRestriction value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.PROMOTIONRESTRICTION2ORDERREL,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONRESTRICTION2ORDERREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from promotionOrderRestrictions. 
	 * @param value the item to remove from promotionOrderRestrictions - Promotion restrictions for order
	 */
	public void removeFromPromotionOrderRestrictions(final AbstractOrder item, final PromotionOrderRestriction value)
	{
		removeFromPromotionOrderRestrictions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.quoteDiscountValuesInternal</code> attribute.
	 * @return the quoteDiscountValuesInternal
	 */
	public String getQuoteDiscountValuesInternal(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, CommerceServicesConstants.Attributes.AbstractOrder.QUOTEDISCOUNTVALUESINTERNAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.quoteDiscountValuesInternal</code> attribute.
	 * @return the quoteDiscountValuesInternal
	 */
	public String getQuoteDiscountValuesInternal(final AbstractOrder item)
	{
		return getQuoteDiscountValuesInternal( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.quoteDiscountValuesInternal</code> attribute. 
	 * @param value the quoteDiscountValuesInternal
	 */
	public void setQuoteDiscountValuesInternal(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.AbstractOrder.QUOTEDISCOUNTVALUESINTERNAL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.quoteDiscountValuesInternal</code> attribute. 
	 * @param value the quoteDiscountValuesInternal
	 */
	public void setQuoteDiscountValuesInternal(final AbstractOrder item, final String value)
	{
		setQuoteDiscountValuesInternal( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.quoteReference</code> attribute.
	 * @return the quoteReference - The reference to quote from which the order was placed.
	 */
	public Quote getQuoteReference(final SessionContext ctx, final Order item)
	{
		return (Quote)item.getProperty( ctx, CommerceServicesConstants.Attributes.Order.QUOTEREFERENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.quoteReference</code> attribute.
	 * @return the quoteReference - The reference to quote from which the order was placed.
	 */
	public Quote getQuoteReference(final Order item)
	{
		return getQuoteReference( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.quoteReference</code> attribute. 
	 * @param value the quoteReference - The reference to quote from which the order was placed.
	 */
	public void setQuoteReference(final SessionContext ctx, final Order item, final Quote value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Order.QUOTEREFERENCE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.quoteReference</code> attribute. 
	 * @param value the quoteReference - The reference to quote from which the order was placed.
	 */
	public void setQuoteReference(final Order item, final Quote value)
	{
		setQuoteReference( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Cart.quoteReference</code> attribute.
	 * @return the quoteReference - The reference to quote from which the cart was created.
	 */
	public Quote getQuoteReference(final SessionContext ctx, final Cart item)
	{
		return (Quote)item.getProperty( ctx, CommerceServicesConstants.Attributes.Cart.QUOTEREFERENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Cart.quoteReference</code> attribute.
	 * @return the quoteReference - The reference to quote from which the cart was created.
	 */
	public Quote getQuoteReference(final Cart item)
	{
		return getQuoteReference( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Cart.quoteReference</code> attribute. 
	 * @param value the quoteReference - The reference to quote from which the cart was created.
	 */
	public void setQuoteReference(final SessionContext ctx, final Cart item, final Quote value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Cart.QUOTEREFERENCE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Cart.quoteReference</code> attribute. 
	 * @param value the quoteReference - The reference to quote from which the cart was created.
	 */
	public void setQuoteReference(final Cart item, final Quote value)
	{
		setQuoteReference( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.refundDeliveryCost</code> attribute.
	 * @return the refundDeliveryCost - Include Delivery Cost in the Refund Amount
	 */
	public Boolean isRefundDeliveryCost(final SessionContext ctx, final ReturnRequest item)
	{
		return (Boolean)item.getProperty( ctx, CommerceServicesConstants.Attributes.ReturnRequest.REFUNDDELIVERYCOST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.refundDeliveryCost</code> attribute.
	 * @return the refundDeliveryCost - Include Delivery Cost in the Refund Amount
	 */
	public Boolean isRefundDeliveryCost(final ReturnRequest item)
	{
		return isRefundDeliveryCost( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.refundDeliveryCost</code> attribute. 
	 * @return the refundDeliveryCost - Include Delivery Cost in the Refund Amount
	 */
	public boolean isRefundDeliveryCostAsPrimitive(final SessionContext ctx, final ReturnRequest item)
	{
		Boolean value = isRefundDeliveryCost( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.refundDeliveryCost</code> attribute. 
	 * @return the refundDeliveryCost - Include Delivery Cost in the Refund Amount
	 */
	public boolean isRefundDeliveryCostAsPrimitive(final ReturnRequest item)
	{
		return isRefundDeliveryCostAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReturnRequest.refundDeliveryCost</code> attribute. 
	 * @param value the refundDeliveryCost - Include Delivery Cost in the Refund Amount
	 */
	public void setRefundDeliveryCost(final SessionContext ctx, final ReturnRequest item, final Boolean value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.ReturnRequest.REFUNDDELIVERYCOST,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReturnRequest.refundDeliveryCost</code> attribute. 
	 * @param value the refundDeliveryCost - Include Delivery Cost in the Refund Amount
	 */
	public void setRefundDeliveryCost(final ReturnRequest item, final Boolean value)
	{
		setRefundDeliveryCost( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReturnRequest.refundDeliveryCost</code> attribute. 
	 * @param value the refundDeliveryCost - Include Delivery Cost in the Refund Amount
	 */
	public void setRefundDeliveryCost(final SessionContext ctx, final ReturnRequest item, final boolean value)
	{
		setRefundDeliveryCost( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReturnRequest.refundDeliveryCost</code> attribute. 
	 * @param value the refundDeliveryCost - Include Delivery Cost in the Refund Amount
	 */
	public void setRefundDeliveryCost(final ReturnRequest item, final boolean value)
	{
		setRefundDeliveryCost( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.salesApplication</code> attribute.
	 * @return the salesApplication - The sales application for which the order was placed.
	 */
	public EnumerationValue getSalesApplication(final SessionContext ctx, final Order item)
	{
		return (EnumerationValue)item.getProperty( ctx, CommerceServicesConstants.Attributes.Order.SALESAPPLICATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.salesApplication</code> attribute.
	 * @return the salesApplication - The sales application for which the order was placed.
	 */
	public EnumerationValue getSalesApplication(final Order item)
	{
		return getSalesApplication( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.salesApplication</code> attribute. 
	 * @param value the salesApplication - The sales application for which the order was placed.
	 */
	public void setSalesApplication(final SessionContext ctx, final Order item, final EnumerationValue value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Order.SALESAPPLICATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.salesApplication</code> attribute. 
	 * @param value the salesApplication - The sales application for which the order was placed.
	 */
	public void setSalesApplication(final Order item, final EnumerationValue value)
	{
		setSalesApplication( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfo.saved</code> attribute.
	 * @return the saved - Indicates the item is saved for reuse.
	 */
	public Boolean isSaved(final SessionContext ctx, final PaymentInfo item)
	{
		return (Boolean)item.getProperty( ctx, CommerceServicesConstants.Attributes.PaymentInfo.SAVED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfo.saved</code> attribute.
	 * @return the saved - Indicates the item is saved for reuse.
	 */
	public Boolean isSaved(final PaymentInfo item)
	{
		return isSaved( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfo.saved</code> attribute. 
	 * @return the saved - Indicates the item is saved for reuse.
	 */
	public boolean isSavedAsPrimitive(final SessionContext ctx, final PaymentInfo item)
	{
		Boolean value = isSaved( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfo.saved</code> attribute. 
	 * @return the saved - Indicates the item is saved for reuse.
	 */
	public boolean isSavedAsPrimitive(final PaymentInfo item)
	{
		return isSavedAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentInfo.saved</code> attribute. 
	 * @param value the saved - Indicates the item is saved for reuse.
	 */
	public void setSaved(final SessionContext ctx, final PaymentInfo item, final Boolean value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.PaymentInfo.SAVED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentInfo.saved</code> attribute. 
	 * @param value the saved - Indicates the item is saved for reuse.
	 */
	public void setSaved(final PaymentInfo item, final Boolean value)
	{
		setSaved( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentInfo.saved</code> attribute. 
	 * @param value the saved - Indicates the item is saved for reuse.
	 */
	public void setSaved(final SessionContext ctx, final PaymentInfo item, final boolean value)
	{
		setSaved( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentInfo.saved</code> attribute. 
	 * @param value the saved - Indicates the item is saved for reuse.
	 */
	public void setSaved(final PaymentInfo item, final boolean value)
	{
		setSaved( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Cart.savedBy</code> attribute.
	 * @return the savedBy - The user who saved the cart.
	 */
	public User getSavedBy(final SessionContext ctx, final Cart item)
	{
		return (User)item.getProperty( ctx, CommerceServicesConstants.Attributes.Cart.SAVEDBY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Cart.savedBy</code> attribute.
	 * @return the savedBy - The user who saved the cart.
	 */
	public User getSavedBy(final Cart item)
	{
		return getSavedBy( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Cart.savedBy</code> attribute. 
	 * @param value the savedBy - The user who saved the cart.
	 */
	public void setSavedBy(final SessionContext ctx, final Cart item, final User value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Cart.SAVEDBY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Cart.savedBy</code> attribute. 
	 * @param value the savedBy - The user who saved the cart.
	 */
	public void setSavedBy(final Cart item, final User value)
	{
		setSavedBy( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Cart.saveTime</code> attribute.
	 * @return the saveTime - The date/time when the cart was saved.
	 */
	public Date getSaveTime(final SessionContext ctx, final Cart item)
	{
		return (Date)item.getProperty( ctx, CommerceServicesConstants.Attributes.Cart.SAVETIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Cart.saveTime</code> attribute.
	 * @return the saveTime - The date/time when the cart was saved.
	 */
	public Date getSaveTime(final Cart item)
	{
		return getSaveTime( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Cart.saveTime</code> attribute. 
	 * @param value the saveTime - The date/time when the cart was saved.
	 */
	public void setSaveTime(final SessionContext ctx, final Cart item, final Date value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Cart.SAVETIME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Cart.saveTime</code> attribute. 
	 * @param value the saveTime - The date/time when the cart was saved.
	 */
	public void setSaveTime(final Cart item, final Date value)
	{
		setSaveTime( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.site</code> attribute.
	 * @return the site - The site on which the cart was created and the order was placed.
	 */
	public BaseSite getSite(final SessionContext ctx, final AbstractOrder item)
	{
		return (BaseSite)item.getProperty( ctx, CommerceServicesConstants.Attributes.AbstractOrder.SITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.site</code> attribute.
	 * @return the site - The site on which the cart was created and the order was placed.
	 */
	public BaseSite getSite(final AbstractOrder item)
	{
		return getSite( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.site</code> attribute. 
	 * @param value the site - The site on which the cart was created and the order was placed.
	 */
	public void setSite(final SessionContext ctx, final AbstractOrder item, final BaseSite value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.AbstractOrder.SITE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.site</code> attribute. 
	 * @param value the site - The site on which the cart was created and the order was placed.
	 */
	public void setSite(final AbstractOrder item, final BaseSite value)
	{
		setSite( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.solrFacetSearchConfiguration</code> attribute.
	 * @return the solrFacetSearchConfiguration - Solr search configuration for this site.
	 */
	public SolrFacetSearchConfig getSolrFacetSearchConfiguration(final SessionContext ctx, final BaseSite item)
	{
		return (SolrFacetSearchConfig)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseSite.SOLRFACETSEARCHCONFIGURATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.solrFacetSearchConfiguration</code> attribute.
	 * @return the solrFacetSearchConfiguration - Solr search configuration for this site.
	 */
	public SolrFacetSearchConfig getSolrFacetSearchConfiguration(final BaseSite item)
	{
		return getSolrFacetSearchConfiguration( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.solrFacetSearchConfiguration</code> attribute. 
	 * @param value the solrFacetSearchConfiguration - Solr search configuration for this site.
	 */
	public void setSolrFacetSearchConfiguration(final SessionContext ctx, final BaseSite item, final SolrFacetSearchConfig value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseSite.SOLRFACETSEARCHCONFIGURATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.solrFacetSearchConfiguration</code> attribute. 
	 * @param value the solrFacetSearchConfiguration - Solr search configuration for this site.
	 */
	public void setSolrFacetSearchConfiguration(final BaseSite item, final SolrFacetSearchConfig value)
	{
		setSolrFacetSearchConfiguration( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.solrFacetSearchConfiguration</code> attribute.
	 * @return the solrFacetSearchConfiguration - Solr search configuration for this store.
	 */
	public SolrFacetSearchConfig getSolrFacetSearchConfiguration(final SessionContext ctx, final BaseStore item)
	{
		return (SolrFacetSearchConfig)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseStore.SOLRFACETSEARCHCONFIGURATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.solrFacetSearchConfiguration</code> attribute.
	 * @return the solrFacetSearchConfiguration - Solr search configuration for this store.
	 */
	public SolrFacetSearchConfig getSolrFacetSearchConfiguration(final BaseStore item)
	{
		return getSolrFacetSearchConfiguration( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.solrFacetSearchConfiguration</code> attribute. 
	 * @param value the solrFacetSearchConfiguration - Solr search configuration for this store.
	 */
	public void setSolrFacetSearchConfiguration(final SessionContext ctx, final BaseStore item, final SolrFacetSearchConfig value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseStore.SOLRFACETSEARCHCONFIGURATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.solrFacetSearchConfiguration</code> attribute. 
	 * @param value the solrFacetSearchConfiguration - Solr search configuration for this store.
	 */
	public void setSolrFacetSearchConfiguration(final BaseStore item, final SolrFacetSearchConfig value)
	{
		setSolrFacetSearchConfiguration( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.store</code> attribute.
	 * @return the store - The store for which the cart was created and the order was placed.
	 */
	public BaseStore getStore(final SessionContext ctx, final AbstractOrder item)
	{
		return (BaseStore)item.getProperty( ctx, CommerceServicesConstants.Attributes.AbstractOrder.STORE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.store</code> attribute.
	 * @return the store - The store for which the cart was created and the order was placed.
	 */
	public BaseStore getStore(final AbstractOrder item)
	{
		return getStore( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.store</code> attribute. 
	 * @param value the store - The store for which the cart was created and the order was placed.
	 */
	public void setStore(final SessionContext ctx, final AbstractOrder item, final BaseStore value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.AbstractOrder.STORE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.store</code> attribute. 
	 * @param value the store - The store for which the cart was created and the order was placed.
	 */
	public void setStore(final AbstractOrder item, final BaseStore value)
	{
		setStore( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.storeEmployeeGroups</code> attribute.
	 * @return the storeEmployeeGroups
	 */
	public Set<StoreEmployeeGroup> getStoreEmployeeGroups(final SessionContext ctx, final PointOfService item)
	{
		return (Set<StoreEmployeeGroup>)STOREEMPLGROUP2POSRELSTOREEMPLOYEEGROUPSHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.storeEmployeeGroups</code> attribute.
	 * @return the storeEmployeeGroups
	 */
	public Set<StoreEmployeeGroup> getStoreEmployeeGroups(final PointOfService item)
	{
		return getStoreEmployeeGroups( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.storeEmployeeGroups</code> attribute. 
	 * @param value the storeEmployeeGroups
	 */
	public void setStoreEmployeeGroups(final SessionContext ctx, final PointOfService item, final Set<StoreEmployeeGroup> value)
	{
		STOREEMPLGROUP2POSRELSTOREEMPLOYEEGROUPSHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.storeEmployeeGroups</code> attribute. 
	 * @param value the storeEmployeeGroups
	 */
	public void setStoreEmployeeGroups(final PointOfService item, final Set<StoreEmployeeGroup> value)
	{
		setStoreEmployeeGroups( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to storeEmployeeGroups. 
	 * @param value the item to add to storeEmployeeGroups
	 */
	public void addToStoreEmployeeGroups(final SessionContext ctx, final PointOfService item, final StoreEmployeeGroup value)
	{
		STOREEMPLGROUP2POSRELSTOREEMPLOYEEGROUPSHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to storeEmployeeGroups. 
	 * @param value the item to add to storeEmployeeGroups
	 */
	public void addToStoreEmployeeGroups(final PointOfService item, final StoreEmployeeGroup value)
	{
		addToStoreEmployeeGroups( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from storeEmployeeGroups. 
	 * @param value the item to remove from storeEmployeeGroups
	 */
	public void removeFromStoreEmployeeGroups(final SessionContext ctx, final PointOfService item, final StoreEmployeeGroup value)
	{
		STOREEMPLGROUP2POSRELSTOREEMPLOYEEGROUPSHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from storeEmployeeGroups. 
	 * @param value the item to remove from storeEmployeeGroups
	 */
	public void removeFromStoreEmployeeGroups(final PointOfService item, final StoreEmployeeGroup value)
	{
		removeFromStoreEmployeeGroups( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeliveryMode.stores</code> attribute.
	 * @return the stores
	 */
	public Set<BaseStore> getStores(final SessionContext ctx, final DeliveryMode item)
	{
		final List<BaseStore> items = item.getLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2DELIVERYMODEREL,
			"BaseStore",
			null,
			false,
			false
		);
		return new LinkedHashSet<BaseStore>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeliveryMode.stores</code> attribute.
	 * @return the stores
	 */
	public Set<BaseStore> getStores(final DeliveryMode item)
	{
		return getStores( getSession().getSessionContext(), item );
	}
	
	public long getStoresCount(final SessionContext ctx, final DeliveryMode item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2DELIVERYMODEREL,
			"BaseStore",
			null
		);
	}
	
	public long getStoresCount(final DeliveryMode item)
	{
		return getStoresCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeliveryMode.stores</code> attribute. 
	 * @param value the stores
	 */
	public void setStores(final SessionContext ctx, final DeliveryMode item, final Set<BaseStore> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2DELIVERYMODEREL,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2DELIVERYMODEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeliveryMode.stores</code> attribute. 
	 * @param value the stores
	 */
	public void setStores(final DeliveryMode item, final Set<BaseStore> value)
	{
		setStores( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to stores. 
	 * @param value the item to add to stores
	 */
	public void addToStores(final SessionContext ctx, final DeliveryMode item, final BaseStore value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2DELIVERYMODEREL,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2DELIVERYMODEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to stores. 
	 * @param value the item to add to stores
	 */
	public void addToStores(final DeliveryMode item, final BaseStore value)
	{
		addToStores( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from stores. 
	 * @param value the item to remove from stores
	 */
	public void removeFromStores(final SessionContext ctx, final DeliveryMode item, final BaseStore value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.BASESTORE2DELIVERYMODEREL,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2DELIVERYMODEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from stores. 
	 * @param value the item to remove from stores
	 */
	public void removeFromStores(final DeliveryMode item, final BaseStore value)
	{
		removeFromStores( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.submitOrderProcessCode</code> attribute.
	 * @return the submitOrderProcessCode - The process name for fullfilment business process.
	 */
	public String getSubmitOrderProcessCode(final SessionContext ctx, final BaseStore item)
	{
		return (String)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseStore.SUBMITORDERPROCESSCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.submitOrderProcessCode</code> attribute.
	 * @return the submitOrderProcessCode - The process name for fullfilment business process.
	 */
	public String getSubmitOrderProcessCode(final BaseStore item)
	{
		return getSubmitOrderProcessCode( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.submitOrderProcessCode</code> attribute. 
	 * @param value the submitOrderProcessCode - The process name for fullfilment business process.
	 */
	public void setSubmitOrderProcessCode(final SessionContext ctx, final BaseStore item, final String value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseStore.SUBMITORDERPROCESSCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.submitOrderProcessCode</code> attribute. 
	 * @param value the submitOrderProcessCode - The process name for fullfilment business process.
	 */
	public void setSubmitOrderProcessCode(final BaseStore item, final String value)
	{
		setSubmitOrderProcessCode( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.summary</code> attribute.
	 * @return the summary - Additional text attribute that holds localized brief description.
	 */
	public String getSummary(final SessionContext ctx, final Product item)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedProduct.getSummary requires a session language", 0 );
		}
		return (String)item.getLocalizedProperty( ctx, CommerceServicesConstants.Attributes.Product.SUMMARY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.summary</code> attribute.
	 * @return the summary - Additional text attribute that holds localized brief description.
	 */
	public String getSummary(final Product item)
	{
		return getSummary( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.summary</code> attribute. 
	 * @return the localized summary - Additional text attribute that holds localized brief description.
	 */
	public Map<Language,String> getAllSummary(final SessionContext ctx, final Product item)
	{
		return (Map<Language,String>)item.getAllLocalizedProperties(ctx,CommerceServicesConstants.Attributes.Product.SUMMARY,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.summary</code> attribute. 
	 * @return the localized summary - Additional text attribute that holds localized brief description.
	 */
	public Map<Language,String> getAllSummary(final Product item)
	{
		return getAllSummary( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.summary</code> attribute. 
	 * @param value the summary - Additional text attribute that holds localized brief description.
	 */
	public void setSummary(final SessionContext ctx, final Product item, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedProduct.setSummary requires a session language", 0 );
		}
		item.setLocalizedProperty(ctx, CommerceServicesConstants.Attributes.Product.SUMMARY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.summary</code> attribute. 
	 * @param value the summary - Additional text attribute that holds localized brief description.
	 */
	public void setSummary(final Product item, final String value)
	{
		setSummary( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.summary</code> attribute. 
	 * @param value the summary - Additional text attribute that holds localized brief description.
	 */
	public void setAllSummary(final SessionContext ctx, final Product item, final Map<Language,String> value)
	{
		item.setAllLocalizedProperties(ctx,CommerceServicesConstants.Attributes.Product.SUMMARY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.summary</code> attribute. 
	 * @param value the summary - Additional text attribute that holds localized brief description.
	 */
	public void setAllSummary(final Product item, final Map<Language,String> value)
	{
		setAllSummary( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.taxGroup</code> attribute.
	 * @return the taxGroup - The site specific tax group.
	 */
	public EnumerationValue getTaxGroup(final SessionContext ctx, final BaseStore item)
	{
		return (EnumerationValue)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseStore.TAXGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.taxGroup</code> attribute.
	 * @return the taxGroup - The site specific tax group.
	 */
	public EnumerationValue getTaxGroup(final BaseStore item)
	{
		return getTaxGroup( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.taxGroup</code> attribute. 
	 * @param value the taxGroup - The site specific tax group.
	 */
	public void setTaxGroup(final SessionContext ctx, final BaseStore item, final EnumerationValue value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseStore.TAXGROUP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.taxGroup</code> attribute. 
	 * @param value the taxGroup - The site specific tax group.
	 */
	public void setTaxGroup(final BaseStore item, final EnumerationValue value)
	{
		setTaxGroup( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.theme</code> attribute.
	 * @return the theme - The site theme that is used in this site.
	 */
	public EnumerationValue getTheme(final SessionContext ctx, final BaseSite item)
	{
		return (EnumerationValue)item.getProperty( ctx, CommerceServicesConstants.Attributes.BaseSite.THEME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.theme</code> attribute.
	 * @return the theme - The site theme that is used in this site.
	 */
	public EnumerationValue getTheme(final BaseSite item)
	{
		return getTheme( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.theme</code> attribute. 
	 * @param value the theme - The site theme that is used in this site.
	 */
	public void setTheme(final SessionContext ctx, final BaseSite item, final EnumerationValue value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.BaseSite.THEME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.theme</code> attribute. 
	 * @param value the theme - The site theme that is used in this site.
	 */
	public void setTheme(final BaseSite item, final EnumerationValue value)
	{
		setTheme( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.title</code> attribute.
	 * @return the title - It holds information about customer title (i.e. Mr, Dr, etc.)
	 */
	public Title getTitle(final SessionContext ctx, final Customer item)
	{
		return (Title)item.getProperty( ctx, CommerceServicesConstants.Attributes.Customer.TITLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.title</code> attribute.
	 * @return the title - It holds information about customer title (i.e. Mr, Dr, etc.)
	 */
	public Title getTitle(final Customer item)
	{
		return getTitle( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.title</code> attribute. 
	 * @param value the title - It holds information about customer title (i.e. Mr, Dr, etc.)
	 */
	public void setTitle(final SessionContext ctx, final Customer item, final Title value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Customer.TITLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.title</code> attribute. 
	 * @param value the title - It holds information about customer title (i.e. Mr, Dr, etc.)
	 */
	public void setTitle(final Customer item, final Title value)
	{
		setTitle( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.token</code> attribute.
	 * @return the token - Attribute is used during forgotten password to ensure that the link can be used
	 * 							only once.
	 */
	public String getToken(final SessionContext ctx, final Customer item)
	{
		return (String)item.getProperty( ctx, CommerceServicesConstants.Attributes.Customer.TOKEN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.token</code> attribute.
	 * @return the token - Attribute is used during forgotten password to ensure that the link can be used
	 * 							only once.
	 */
	public String getToken(final Customer item)
	{
		return getToken( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.token</code> attribute. 
	 * @param value the token - Attribute is used during forgotten password to ensure that the link can be used
	 * 							only once.
	 */
	public void setToken(final SessionContext ctx, final Customer item, final String value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Customer.TOKEN,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.token</code> attribute. 
	 * @param value the token - Attribute is used during forgotten password to ensure that the link can be used
	 * 							only once.
	 */
	public void setToken(final Customer item, final String value)
	{
		setToken( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.type</code> attribute.
	 * @return the type - Customer type
	 */
	public EnumerationValue getType(final SessionContext ctx, final Customer item)
	{
		return (EnumerationValue)item.getProperty( ctx, CommerceServicesConstants.Attributes.Customer.TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.type</code> attribute.
	 * @return the type - Customer type
	 */
	public EnumerationValue getType(final Customer item)
	{
		return getType( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.type</code> attribute. 
	 * @param value the type - Customer type
	 */
	public void setType(final SessionContext ctx, final Customer item, final EnumerationValue value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Customer.TYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.type</code> attribute. 
	 * @param value the type - Customer type
	 */
	public void setType(final Customer item, final EnumerationValue value)
	{
		setType( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.visibleInAddressBook</code> attribute.
	 * @return the visibleInAddressBook - Indicates if the address will be displayed to the user in the address book.
	 */
	public Boolean isVisibleInAddressBook(final SessionContext ctx, final Address item)
	{
		return (Boolean)item.getProperty( ctx, CommerceServicesConstants.Attributes.Address.VISIBLEINADDRESSBOOK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.visibleInAddressBook</code> attribute.
	 * @return the visibleInAddressBook - Indicates if the address will be displayed to the user in the address book.
	 */
	public Boolean isVisibleInAddressBook(final Address item)
	{
		return isVisibleInAddressBook( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.visibleInAddressBook</code> attribute. 
	 * @return the visibleInAddressBook - Indicates if the address will be displayed to the user in the address book.
	 */
	public boolean isVisibleInAddressBookAsPrimitive(final SessionContext ctx, final Address item)
	{
		Boolean value = isVisibleInAddressBook( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.visibleInAddressBook</code> attribute. 
	 * @return the visibleInAddressBook - Indicates if the address will be displayed to the user in the address book.
	 */
	public boolean isVisibleInAddressBookAsPrimitive(final Address item)
	{
		return isVisibleInAddressBookAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Address.visibleInAddressBook</code> attribute. 
	 * @param value the visibleInAddressBook - Indicates if the address will be displayed to the user in the address book.
	 */
	public void setVisibleInAddressBook(final SessionContext ctx, final Address item, final Boolean value)
	{
		item.setProperty(ctx, CommerceServicesConstants.Attributes.Address.VISIBLEINADDRESSBOOK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Address.visibleInAddressBook</code> attribute. 
	 * @param value the visibleInAddressBook - Indicates if the address will be displayed to the user in the address book.
	 */
	public void setVisibleInAddressBook(final Address item, final Boolean value)
	{
		setVisibleInAddressBook( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Address.visibleInAddressBook</code> attribute. 
	 * @param value the visibleInAddressBook - Indicates if the address will be displayed to the user in the address book.
	 */
	public void setVisibleInAddressBook(final SessionContext ctx, final Address item, final boolean value)
	{
		setVisibleInAddressBook( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Address.visibleInAddressBook</code> attribute. 
	 * @param value the visibleInAddressBook - Indicates if the address will be displayed to the user in the address book.
	 */
	public void setVisibleInAddressBook(final Address item, final boolean value)
	{
		setVisibleInAddressBook( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.warehouses</code> attribute.
	 * @return the warehouses
	 */
	public List<Warehouse> getWarehouses(final SessionContext ctx, final BaseStore item)
	{
		final List<Warehouse> items = item.getLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2WAREHOUSEREL,
			"Warehouse",
			null,
			Utilities.getRelationOrderingOverride(BASESTORE2WAREHOUSEREL_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.warehouses</code> attribute.
	 * @return the warehouses
	 */
	public List<Warehouse> getWarehouses(final BaseStore item)
	{
		return getWarehouses( getSession().getSessionContext(), item );
	}
	
	public long getWarehousesCount(final SessionContext ctx, final BaseStore item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2WAREHOUSEREL,
			"Warehouse",
			null
		);
	}
	
	public long getWarehousesCount(final BaseStore item)
	{
		return getWarehousesCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.warehouses</code> attribute. 
	 * @param value the warehouses
	 */
	public void setWarehouses(final SessionContext ctx, final BaseStore item, final List<Warehouse> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2WAREHOUSEREL,
			null,
			value,
			Utilities.getRelationOrderingOverride(BASESTORE2WAREHOUSEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2WAREHOUSEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.warehouses</code> attribute. 
	 * @param value the warehouses
	 */
	public void setWarehouses(final BaseStore item, final List<Warehouse> value)
	{
		setWarehouses( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to warehouses. 
	 * @param value the item to add to warehouses
	 */
	public void addToWarehouses(final SessionContext ctx, final BaseStore item, final Warehouse value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2WAREHOUSEREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(BASESTORE2WAREHOUSEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2WAREHOUSEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to warehouses. 
	 * @param value the item to add to warehouses
	 */
	public void addToWarehouses(final BaseStore item, final Warehouse value)
	{
		addToWarehouses( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from warehouses. 
	 * @param value the item to remove from warehouses
	 */
	public void removeFromWarehouses(final SessionContext ctx, final BaseStore item, final Warehouse value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.BASESTORE2WAREHOUSEREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(BASESTORE2WAREHOUSEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(BASESTORE2WAREHOUSEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from warehouses. 
	 * @param value the item to remove from warehouses
	 */
	public void removeFromWarehouses(final BaseStore item, final Warehouse value)
	{
		removeFromWarehouses( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.warehouses</code> attribute.
	 * @return the warehouses
	 */
	public List<Warehouse> getWarehouses(final SessionContext ctx, final PointOfService item)
	{
		final List<Warehouse> items = item.getLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.POS2WAREHOUSEREL,
			"Warehouse",
			null,
			Utilities.getRelationOrderingOverride(POS2WAREHOUSEREL_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.warehouses</code> attribute.
	 * @return the warehouses
	 */
	public List<Warehouse> getWarehouses(final PointOfService item)
	{
		return getWarehouses( getSession().getSessionContext(), item );
	}
	
	public long getWarehousesCount(final SessionContext ctx, final PointOfService item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			CommerceServicesConstants.Relations.POS2WAREHOUSEREL,
			"Warehouse",
			null
		);
	}
	
	public long getWarehousesCount(final PointOfService item)
	{
		return getWarehousesCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.warehouses</code> attribute. 
	 * @param value the warehouses
	 */
	public void setWarehouses(final SessionContext ctx, final PointOfService item, final List<Warehouse> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.POS2WAREHOUSEREL,
			null,
			value,
			Utilities.getRelationOrderingOverride(POS2WAREHOUSEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(POS2WAREHOUSEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.warehouses</code> attribute. 
	 * @param value the warehouses
	 */
	public void setWarehouses(final PointOfService item, final List<Warehouse> value)
	{
		setWarehouses( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to warehouses. 
	 * @param value the item to add to warehouses
	 */
	public void addToWarehouses(final SessionContext ctx, final PointOfService item, final Warehouse value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.POS2WAREHOUSEREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(POS2WAREHOUSEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(POS2WAREHOUSEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to warehouses. 
	 * @param value the item to add to warehouses
	 */
	public void addToWarehouses(final PointOfService item, final Warehouse value)
	{
		addToWarehouses( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from warehouses. 
	 * @param value the item to remove from warehouses
	 */
	public void removeFromWarehouses(final SessionContext ctx, final PointOfService item, final Warehouse value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			CommerceServicesConstants.Relations.POS2WAREHOUSEREL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(POS2WAREHOUSEREL_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(POS2WAREHOUSEREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from warehouses. 
	 * @param value the item to remove from warehouses
	 */
	public void removeFromWarehouses(final PointOfService item, final Warehouse value)
	{
		removeFromWarehouses( getSession().getSessionContext(), item, value );
	}
	
}
