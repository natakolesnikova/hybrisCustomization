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

import de.hybris.platform.catalog.jalo.Company;
import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.OrgUnit OrgUnit}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedOrgUnit extends Company
{
	/** Qualifier of the <code>OrgUnit.active</code> attribute **/
	public static final String ACTIVE = "active";
	/** Qualifier of the <code>OrgUnit.path</code> attribute **/
	public static final String PATH = "path";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(Company.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(ACTIVE, AttributeMode.INITIAL);
		tmp.put(PATH, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrgUnit.active</code> attribute.
	 * @return the active
	 */
	public Boolean isActive(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrgUnit.active</code> attribute.
	 * @return the active
	 */
	public Boolean isActive()
	{
		return isActive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrgUnit.active</code> attribute. 
	 * @return the active
	 */
	public boolean isActiveAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isActive( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrgUnit.active</code> attribute. 
	 * @return the active
	 */
	public boolean isActiveAsPrimitive()
	{
		return isActiveAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OrgUnit.active</code> attribute. 
	 * @param value the active
	 */
	public void setActive(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ACTIVE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OrgUnit.active</code> attribute. 
	 * @param value the active
	 */
	public void setActive(final Boolean value)
	{
		setActive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OrgUnit.active</code> attribute. 
	 * @param value the active
	 */
	public void setActive(final SessionContext ctx, final boolean value)
	{
		setActive( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OrgUnit.active</code> attribute. 
	 * @param value the active
	 */
	public void setActive(final boolean value)
	{
		setActive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrgUnit.path</code> attribute.
	 * @return the path - Flat representation of the path of traversal to reach the OrgUnit from the root of its organization.
	 */
	public String getPath(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PATH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrgUnit.path</code> attribute.
	 * @return the path - Flat representation of the path of traversal to reach the OrgUnit from the root of its organization.
	 */
	public String getPath()
	{
		return getPath( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OrgUnit.path</code> attribute. 
	 * @param value the path - Flat representation of the path of traversal to reach the OrgUnit from the root of its organization.
	 */
	public void setPath(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PATH,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OrgUnit.path</code> attribute. 
	 * @param value the path - Flat representation of the path of traversal to reach the OrgUnit from the root of its organization.
	 */
	public void setPath(final String value)
	{
		setPath( getSession().getSessionContext(), value );
	}
	
}
