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

import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.commerceservices.jalo.consent.ConsentTemplate;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.Customer;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.consent.Consent Consent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedConsent extends GenericItem
{
	/** Qualifier of the <code>Consent.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>Consent.customer</code> attribute **/
	public static final String CUSTOMER = "customer";
	/** Qualifier of the <code>Consent.consentTemplate</code> attribute **/
	public static final String CONSENTTEMPLATE = "consentTemplate";
	/** Qualifier of the <code>Consent.consentGivenDate</code> attribute **/
	public static final String CONSENTGIVENDATE = "consentGivenDate";
	/** Qualifier of the <code>Consent.consentWithdrawnDate</code> attribute **/
	public static final String CONSENTWITHDRAWNDATE = "consentWithdrawnDate";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(CUSTOMER, AttributeMode.INITIAL);
		tmp.put(CONSENTTEMPLATE, AttributeMode.INITIAL);
		tmp.put(CONSENTGIVENDATE, AttributeMode.INITIAL);
		tmp.put(CONSENTWITHDRAWNDATE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consent.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consent.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consent.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consent.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consent.consentGivenDate</code> attribute.
	 * @return the consentGivenDate - The timestamp when consent was given by the customer
	 */
	public Date getConsentGivenDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, CONSENTGIVENDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consent.consentGivenDate</code> attribute.
	 * @return the consentGivenDate - The timestamp when consent was given by the customer
	 */
	public Date getConsentGivenDate()
	{
		return getConsentGivenDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consent.consentGivenDate</code> attribute. 
	 * @param value the consentGivenDate - The timestamp when consent was given by the customer
	 */
	public void setConsentGivenDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, CONSENTGIVENDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consent.consentGivenDate</code> attribute. 
	 * @param value the consentGivenDate - The timestamp when consent was given by the customer
	 */
	public void setConsentGivenDate(final Date value)
	{
		setConsentGivenDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consent.consentTemplate</code> attribute.
	 * @return the consentTemplate - The Consent Reference
	 */
	public ConsentTemplate getConsentTemplate(final SessionContext ctx)
	{
		return (ConsentTemplate)getProperty( ctx, CONSENTTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consent.consentTemplate</code> attribute.
	 * @return the consentTemplate - The Consent Reference
	 */
	public ConsentTemplate getConsentTemplate()
	{
		return getConsentTemplate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consent.consentTemplate</code> attribute. 
	 * @param value the consentTemplate - The Consent Reference
	 */
	public void setConsentTemplate(final SessionContext ctx, final ConsentTemplate value)
	{
		setProperty(ctx, CONSENTTEMPLATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consent.consentTemplate</code> attribute. 
	 * @param value the consentTemplate - The Consent Reference
	 */
	public void setConsentTemplate(final ConsentTemplate value)
	{
		setConsentTemplate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consent.consentWithdrawnDate</code> attribute.
	 * @return the consentWithdrawnDate - The timestamp when consent was withdrawn by the customer
	 */
	public Date getConsentWithdrawnDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, CONSENTWITHDRAWNDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consent.consentWithdrawnDate</code> attribute.
	 * @return the consentWithdrawnDate - The timestamp when consent was withdrawn by the customer
	 */
	public Date getConsentWithdrawnDate()
	{
		return getConsentWithdrawnDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consent.consentWithdrawnDate</code> attribute. 
	 * @param value the consentWithdrawnDate - The timestamp when consent was withdrawn by the customer
	 */
	public void setConsentWithdrawnDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, CONSENTWITHDRAWNDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consent.consentWithdrawnDate</code> attribute. 
	 * @param value the consentWithdrawnDate - The timestamp when consent was withdrawn by the customer
	 */
	public void setConsentWithdrawnDate(final Date value)
	{
		setConsentWithdrawnDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consent.customer</code> attribute.
	 * @return the customer - The customer for which the consent is recorded
	 */
	public Customer getCustomer(final SessionContext ctx)
	{
		return (Customer)getProperty( ctx, CUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consent.customer</code> attribute.
	 * @return the customer - The customer for which the consent is recorded
	 */
	public Customer getCustomer()
	{
		return getCustomer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consent.customer</code> attribute. 
	 * @param value the customer - The customer for which the consent is recorded
	 */
	public void setCustomer(final SessionContext ctx, final Customer value)
	{
		setProperty(ctx, CUSTOMER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consent.customer</code> attribute. 
	 * @param value the customer - The customer for which the consent is recorded
	 */
	public void setCustomer(final Customer value)
	{
		setCustomer( getSession().getSessionContext(), value );
	}
	
}
