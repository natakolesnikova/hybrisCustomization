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
package de.hybris.platform.commerceservices.jalo.user;

import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.storelocator.jalo.PointOfService;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.user.StoreEmployeeGroup StoreEmployeeGroup}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedStoreEmployeeGroup extends UserGroup
{
	/** Qualifier of the <code>StoreEmployeeGroup.store</code> attribute **/
	public static final String STORE = "store";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n STORE's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedStoreEmployeeGroup> STOREHANDLER = new BidirectionalOneToManyHandler<GeneratedStoreEmployeeGroup>(
	CommerceServicesConstants.TC.STOREEMPLOYEEGROUP,
	false,
	"store",
	null,
	false,
	true,
	CollectionType.SET
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(UserGroup.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(STORE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		STOREHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreEmployeeGroup.store</code> attribute.
	 * @return the store
	 */
	public PointOfService getStore(final SessionContext ctx)
	{
		return (PointOfService)getProperty( ctx, STORE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreEmployeeGroup.store</code> attribute.
	 * @return the store
	 */
	public PointOfService getStore()
	{
		return getStore( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreEmployeeGroup.store</code> attribute. 
	 * @param value the store
	 */
	public void setStore(final SessionContext ctx, final PointOfService value)
	{
		STOREHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreEmployeeGroup.store</code> attribute. 
	 * @param value the store
	 */
	public void setStore(final PointOfService value)
	{
		setStore( getSession().getSessionContext(), value );
	}
	
}
