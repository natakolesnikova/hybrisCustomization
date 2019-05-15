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
package de.hybris.platform.acceleratorcms.jalo.components;

import de.hybris.platform.acceleratorcms.constants.AcceleratorCmsConstants;
import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.acceleratorcms.jalo.components.JspIncludeComponent JspIncludeComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedJspIncludeComponent extends SimpleCMSComponent
{
	/** Qualifier of the <code>JspIncludeComponent.page</code> attribute **/
	public static final String PAGE = "page";
	/** Qualifier of the <code>JspIncludeComponent.title</code> attribute **/
	public static final String TITLE = "title";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(PAGE, AttributeMode.INITIAL);
		tmp.put(TITLE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JspIncludeComponent.page</code> attribute.
	 * @return the page - A jsp view
	 */
	public String getPage(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JspIncludeComponent.page</code> attribute.
	 * @return the page - A jsp view
	 */
	public String getPage()
	{
		return getPage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JspIncludeComponent.page</code> attribute. 
	 * @param value the page - A jsp view
	 */
	public void setPage(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JspIncludeComponent.page</code> attribute. 
	 * @param value the page - A jsp view
	 */
	public void setPage(final String value)
	{
		setPage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JspIncludeComponent.title</code> attribute.
	 * @return the title - Attribute that represents tab title if the component exists in a tab container
	 */
	public String getTitle(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedJspIncludeComponent.getTitle requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, TITLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JspIncludeComponent.title</code> attribute.
	 * @return the title - Attribute that represents tab title if the component exists in a tab container
	 */
	public String getTitle()
	{
		return getTitle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JspIncludeComponent.title</code> attribute. 
	 * @return the localized title - Attribute that represents tab title if the component exists in a tab container
	 */
	public Map<Language,String> getAllTitle(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,TITLE,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JspIncludeComponent.title</code> attribute. 
	 * @return the localized title - Attribute that represents tab title if the component exists in a tab container
	 */
	public Map<Language,String> getAllTitle()
	{
		return getAllTitle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JspIncludeComponent.title</code> attribute. 
	 * @param value the title - Attribute that represents tab title if the component exists in a tab container
	 */
	public void setTitle(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedJspIncludeComponent.setTitle requires a session language", 0 );
		}
		setLocalizedProperty(ctx, TITLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JspIncludeComponent.title</code> attribute. 
	 * @param value the title - Attribute that represents tab title if the component exists in a tab container
	 */
	public void setTitle(final String value)
	{
		setTitle( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JspIncludeComponent.title</code> attribute. 
	 * @param value the title - Attribute that represents tab title if the component exists in a tab container
	 */
	public void setAllTitle(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,TITLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JspIncludeComponent.title</code> attribute. 
	 * @param value the title - Attribute that represents tab title if the component exists in a tab container
	 */
	public void setAllTitle(final Map<Language,String> value)
	{
		setAllTitle( getSession().getSessionContext(), value );
	}
	
}
