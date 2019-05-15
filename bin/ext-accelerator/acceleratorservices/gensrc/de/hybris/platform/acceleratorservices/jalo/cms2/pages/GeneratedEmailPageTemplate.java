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
package de.hybris.platform.acceleratorservices.jalo.cms2.pages;

import de.hybris.platform.acceleratorservices.constants.AcceleratorServicesConstants;
import de.hybris.platform.acceleratorservices.jalo.cms2.pages.DocumentPageTemplate;
import de.hybris.platform.commons.jalo.renderer.RendererTemplate;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.acceleratorservices.jalo.cms2.pages.EmailPageTemplate EmailPageTemplate}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedEmailPageTemplate extends DocumentPageTemplate
{
	/** Qualifier of the <code>EmailPageTemplate.subject</code> attribute **/
	public static final String SUBJECT = "subject";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(DocumentPageTemplate.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(SUBJECT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailPageTemplate.subject</code> attribute.
	 * @return the subject - Contains renderer that generates subject of the email message.
	 */
	public RendererTemplate getSubject(final SessionContext ctx)
	{
		return (RendererTemplate)getProperty( ctx, SUBJECT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailPageTemplate.subject</code> attribute.
	 * @return the subject - Contains renderer that generates subject of the email message.
	 */
	public RendererTemplate getSubject()
	{
		return getSubject( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EmailPageTemplate.subject</code> attribute. 
	 * @param value the subject - Contains renderer that generates subject of the email message.
	 */
	public void setSubject(final SessionContext ctx, final RendererTemplate value)
	{
		setProperty(ctx, SUBJECT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EmailPageTemplate.subject</code> attribute. 
	 * @param value the subject - Contains renderer that generates subject of the email message.
	 */
	public void setSubject(final RendererTemplate value)
	{
		setSubject( getSession().getSessionContext(), value );
	}
	
}
