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
package de.hybris.platform.commerceservices.jalo.consent;

import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.consent.ConsentTemplate ConsentTemplate}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedConsentTemplate extends GenericItem
{
	/** Qualifier of the <code>ConsentTemplate.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>ConsentTemplate.version</code> attribute **/
	public static final String VERSION = "version";
	/** Qualifier of the <code>ConsentTemplate.baseSite</code> attribute **/
	public static final String BASESITE = "baseSite";
	/** Qualifier of the <code>ConsentTemplate.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>ConsentTemplate.exposed</code> attribute **/
	public static final String EXPOSED = "exposed";
	/** Qualifier of the <code>ConsentTemplate.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(VERSION, AttributeMode.INITIAL);
		tmp.put(BASESITE, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(EXPOSED, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.baseSite</code> attribute.
	 * @return the baseSite - BaseSite that this consent template belongs to
	 */
	public BaseSite getBaseSite(final SessionContext ctx)
	{
		return (BaseSite)getProperty( ctx, BASESITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.baseSite</code> attribute.
	 * @return the baseSite - BaseSite that this consent template belongs to
	 */
	public BaseSite getBaseSite()
	{
		return getBaseSite( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.baseSite</code> attribute. 
	 * @param value the baseSite - BaseSite that this consent template belongs to
	 */
	public void setBaseSite(final SessionContext ctx, final BaseSite value)
	{
		setProperty(ctx, BASESITE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.baseSite</code> attribute. 
	 * @param value the baseSite - BaseSite that this consent template belongs to
	 */
	public void setBaseSite(final BaseSite value)
	{
		setBaseSite( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.description</code> attribute.
	 * @return the description - Consent Template Description
	 */
	public String getDescription(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedConsentTemplate.getDescription requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.description</code> attribute.
	 * @return the description - Consent Template Description
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.description</code> attribute. 
	 * @return the localized description - Consent Template Description
	 */
	public Map<Language,String> getAllDescription(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,DESCRIPTION,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.description</code> attribute. 
	 * @return the localized description - Consent Template Description
	 */
	public Map<Language,String> getAllDescription()
	{
		return getAllDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.description</code> attribute. 
	 * @param value the description - Consent Template Description
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedConsentTemplate.setDescription requires a session language", 0 );
		}
		setLocalizedProperty(ctx, DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.description</code> attribute. 
	 * @param value the description - Consent Template Description
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.description</code> attribute. 
	 * @param value the description - Consent Template Description
	 */
	public void setAllDescription(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.description</code> attribute. 
	 * @param value the description - Consent Template Description
	 */
	public void setAllDescription(final Map<Language,String> value)
	{
		setAllDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.exposed</code> attribute.
	 * @return the exposed - Indicates whether the consent template should be exposed to integrators in a storefront implementation as part of cookie and session information
	 */
	public Boolean isExposed(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, EXPOSED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.exposed</code> attribute.
	 * @return the exposed - Indicates whether the consent template should be exposed to integrators in a storefront implementation as part of cookie and session information
	 */
	public Boolean isExposed()
	{
		return isExposed( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.exposed</code> attribute. 
	 * @return the exposed - Indicates whether the consent template should be exposed to integrators in a storefront implementation as part of cookie and session information
	 */
	public boolean isExposedAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isExposed( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.exposed</code> attribute. 
	 * @return the exposed - Indicates whether the consent template should be exposed to integrators in a storefront implementation as part of cookie and session information
	 */
	public boolean isExposedAsPrimitive()
	{
		return isExposedAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.exposed</code> attribute. 
	 * @param value the exposed - Indicates whether the consent template should be exposed to integrators in a storefront implementation as part of cookie and session information
	 */
	public void setExposed(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, EXPOSED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.exposed</code> attribute. 
	 * @param value the exposed - Indicates whether the consent template should be exposed to integrators in a storefront implementation as part of cookie and session information
	 */
	public void setExposed(final Boolean value)
	{
		setExposed( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.exposed</code> attribute. 
	 * @param value the exposed - Indicates whether the consent template should be exposed to integrators in a storefront implementation as part of cookie and session information
	 */
	public void setExposed(final SessionContext ctx, final boolean value)
	{
		setExposed( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.exposed</code> attribute. 
	 * @param value the exposed - Indicates whether the consent template should be exposed to integrators in a storefront implementation as part of cookie and session information
	 */
	public void setExposed(final boolean value)
	{
		setExposed( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.id</code> attribute.
	 * @return the id - Consent Template ID
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.id</code> attribute.
	 * @return the id - Consent Template ID
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.id</code> attribute. 
	 * @param value the id - Consent Template ID
	 */
	public void setId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.id</code> attribute. 
	 * @param value the id - Consent Template ID
	 */
	public void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.name</code> attribute.
	 * @return the name - Consent Template Name
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedConsentTemplate.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.name</code> attribute.
	 * @return the name - Consent Template Name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.name</code> attribute. 
	 * @return the localized name - Consent Template Name
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.name</code> attribute. 
	 * @return the localized name - Consent Template Name
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.name</code> attribute. 
	 * @param value the name - Consent Template Name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedConsentTemplate.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.name</code> attribute. 
	 * @param value the name - Consent Template Name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.name</code> attribute. 
	 * @param value the name - Consent Template Name
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.name</code> attribute. 
	 * @param value the name - Consent Template Name
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.version</code> attribute.
	 * @return the version - Consent Template Version
	 */
	public Integer getVersion(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, VERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.version</code> attribute.
	 * @return the version - Consent Template Version
	 */
	public Integer getVersion()
	{
		return getVersion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.version</code> attribute. 
	 * @return the version - Consent Template Version
	 */
	public int getVersionAsPrimitive(final SessionContext ctx)
	{
		Integer value = getVersion( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.version</code> attribute. 
	 * @return the version - Consent Template Version
	 */
	public int getVersionAsPrimitive()
	{
		return getVersionAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.version</code> attribute. 
	 * @param value the version - Consent Template Version
	 */
	public void setVersion(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, VERSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.version</code> attribute. 
	 * @param value the version - Consent Template Version
	 */
	public void setVersion(final Integer value)
	{
		setVersion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.version</code> attribute. 
	 * @param value the version - Consent Template Version
	 */
	public void setVersion(final SessionContext ctx, final int value)
	{
		setVersion( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ConsentTemplate.version</code> attribute. 
	 * @param value the version - Consent Template Version
	 */
	public void setVersion(final int value)
	{
		setVersion( getSession().getSessionContext(), value );
	}
	
}
