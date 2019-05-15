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
package de.hybris.platform.commerceservices.jalo.process;

import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.processengine.jalo.BusinessProcess;
import de.hybris.platform.store.BaseStore;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.process.StoreFrontProcess StoreFrontProcess}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedStoreFrontProcess extends BusinessProcess
{
	/** Qualifier of the <code>StoreFrontProcess.site</code> attribute **/
	public static final String SITE = "site";
	/** Qualifier of the <code>StoreFrontProcess.store</code> attribute **/
	public static final String STORE = "store";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(BusinessProcess.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(SITE, AttributeMode.INITIAL);
		tmp.put(STORE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreFrontProcess.site</code> attribute.
	 * @return the site - Attribute contains base site object that will be used in the process.
	 */
	public BaseSite getSite(final SessionContext ctx)
	{
		return (BaseSite)getProperty( ctx, SITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreFrontProcess.site</code> attribute.
	 * @return the site - Attribute contains base site object that will be used in the process.
	 */
	public BaseSite getSite()
	{
		return getSite( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreFrontProcess.site</code> attribute. 
	 * @param value the site - Attribute contains base site object that will be used in the process.
	 */
	public void setSite(final SessionContext ctx, final BaseSite value)
	{
		setProperty(ctx, SITE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreFrontProcess.site</code> attribute. 
	 * @param value the site - Attribute contains base site object that will be used in the process.
	 */
	public void setSite(final BaseSite value)
	{
		setSite( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreFrontProcess.store</code> attribute.
	 * @return the store - Attribute contains base store object that will be used in the process.
	 */
	public BaseStore getStore(final SessionContext ctx)
	{
		return (BaseStore)getProperty( ctx, STORE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreFrontProcess.store</code> attribute.
	 * @return the store - Attribute contains base store object that will be used in the process.
	 */
	public BaseStore getStore()
	{
		return getStore( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreFrontProcess.store</code> attribute. 
	 * @param value the store - Attribute contains base store object that will be used in the process.
	 */
	public void setStore(final SessionContext ctx, final BaseStore value)
	{
		setProperty(ctx, STORE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreFrontProcess.store</code> attribute. 
	 * @param value the store - Attribute contains base store object that will be used in the process.
	 */
	public void setStore(final BaseStore value)
	{
		setStore( getSession().getSessionContext(), value );
	}
	
}
