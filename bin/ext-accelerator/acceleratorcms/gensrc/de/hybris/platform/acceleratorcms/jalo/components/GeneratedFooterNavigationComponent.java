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
import de.hybris.platform.acceleratorcms.jalo.components.NavigationComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.acceleratorcms.jalo.components.FooterNavigationComponent FooterNavigationComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedFooterNavigationComponent extends NavigationComponent
{
	/** Qualifier of the <code>FooterNavigationComponent.showLanguageCurrency</code> attribute **/
	public static final String SHOWLANGUAGECURRENCY = "showLanguageCurrency";
	/** Qualifier of the <code>FooterNavigationComponent.notice</code> attribute **/
	public static final String NOTICE = "notice";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(NavigationComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(SHOWLANGUAGECURRENCY, AttributeMode.INITIAL);
		tmp.put(NOTICE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterNavigationComponent.notice</code> attribute.
	 * @return the notice - Intended to store a copyright notice or other text to be displayed in the footer
	 */
	public String getNotice(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedFooterNavigationComponent.getNotice requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NOTICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterNavigationComponent.notice</code> attribute.
	 * @return the notice - Intended to store a copyright notice or other text to be displayed in the footer
	 */
	public String getNotice()
	{
		return getNotice( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterNavigationComponent.notice</code> attribute. 
	 * @return the localized notice - Intended to store a copyright notice or other text to be displayed in the footer
	 */
	public Map<Language,String> getAllNotice(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NOTICE,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterNavigationComponent.notice</code> attribute. 
	 * @return the localized notice - Intended to store a copyright notice or other text to be displayed in the footer
	 */
	public Map<Language,String> getAllNotice()
	{
		return getAllNotice( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FooterNavigationComponent.notice</code> attribute. 
	 * @param value the notice - Intended to store a copyright notice or other text to be displayed in the footer
	 */
	public void setNotice(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedFooterNavigationComponent.setNotice requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NOTICE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FooterNavigationComponent.notice</code> attribute. 
	 * @param value the notice - Intended to store a copyright notice or other text to be displayed in the footer
	 */
	public void setNotice(final String value)
	{
		setNotice( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FooterNavigationComponent.notice</code> attribute. 
	 * @param value the notice - Intended to store a copyright notice or other text to be displayed in the footer
	 */
	public void setAllNotice(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NOTICE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FooterNavigationComponent.notice</code> attribute. 
	 * @param value the notice - Intended to store a copyright notice or other text to be displayed in the footer
	 */
	public void setAllNotice(final Map<Language,String> value)
	{
		setAllNotice( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterNavigationComponent.showLanguageCurrency</code> attribute.
	 * @return the showLanguageCurrency - Determines whether or not to show the language currency selection.
	 */
	public Boolean isShowLanguageCurrency(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SHOWLANGUAGECURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterNavigationComponent.showLanguageCurrency</code> attribute.
	 * @return the showLanguageCurrency - Determines whether or not to show the language currency selection.
	 */
	public Boolean isShowLanguageCurrency()
	{
		return isShowLanguageCurrency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterNavigationComponent.showLanguageCurrency</code> attribute. 
	 * @return the showLanguageCurrency - Determines whether or not to show the language currency selection.
	 */
	public boolean isShowLanguageCurrencyAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isShowLanguageCurrency( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterNavigationComponent.showLanguageCurrency</code> attribute. 
	 * @return the showLanguageCurrency - Determines whether or not to show the language currency selection.
	 */
	public boolean isShowLanguageCurrencyAsPrimitive()
	{
		return isShowLanguageCurrencyAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FooterNavigationComponent.showLanguageCurrency</code> attribute. 
	 * @param value the showLanguageCurrency - Determines whether or not to show the language currency selection.
	 */
	public void setShowLanguageCurrency(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SHOWLANGUAGECURRENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FooterNavigationComponent.showLanguageCurrency</code> attribute. 
	 * @param value the showLanguageCurrency - Determines whether or not to show the language currency selection.
	 */
	public void setShowLanguageCurrency(final Boolean value)
	{
		setShowLanguageCurrency( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FooterNavigationComponent.showLanguageCurrency</code> attribute. 
	 * @param value the showLanguageCurrency - Determines whether or not to show the language currency selection.
	 */
	public void setShowLanguageCurrency(final SessionContext ctx, final boolean value)
	{
		setShowLanguageCurrency( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FooterNavigationComponent.showLanguageCurrency</code> attribute. 
	 * @param value the showLanguageCurrency - Determines whether or not to show the language currency selection.
	 */
	public void setShowLanguageCurrency(final boolean value)
	{
		setShowLanguageCurrency( getSession().getSessionContext(), value );
	}
	
}
