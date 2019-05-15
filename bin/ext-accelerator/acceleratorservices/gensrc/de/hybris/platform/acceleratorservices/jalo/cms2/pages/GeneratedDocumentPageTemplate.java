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
import de.hybris.platform.cms2.jalo.pages.PageTemplate;
import de.hybris.platform.commons.jalo.renderer.RendererTemplate;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.acceleratorservices.jalo.cms2.pages.DocumentPageTemplate DocumentPageTemplate}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedDocumentPageTemplate extends PageTemplate
{
	/** Qualifier of the <code>DocumentPageTemplate.htmlTemplate</code> attribute **/
	public static final String HTMLTEMPLATE = "htmlTemplate";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(PageTemplate.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(HTMLTEMPLATE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DocumentPageTemplate.htmlTemplate</code> attribute.
	 * @return the htmlTemplate - Contains renderer that generates body of the document.
	 */
	public RendererTemplate getHtmlTemplate(final SessionContext ctx)
	{
		return (RendererTemplate)getProperty( ctx, HTMLTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DocumentPageTemplate.htmlTemplate</code> attribute.
	 * @return the htmlTemplate - Contains renderer that generates body of the document.
	 */
	public RendererTemplate getHtmlTemplate()
	{
		return getHtmlTemplate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DocumentPageTemplate.htmlTemplate</code> attribute. 
	 * @param value the htmlTemplate - Contains renderer that generates body of the document.
	 */
	public void setHtmlTemplate(final SessionContext ctx, final RendererTemplate value)
	{
		setProperty(ctx, HTMLTEMPLATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DocumentPageTemplate.htmlTemplate</code> attribute. 
	 * @param value the htmlTemplate - Contains renderer that generates body of the document.
	 */
	public void setHtmlTemplate(final RendererTemplate value)
	{
		setHtmlTemplate( getSession().getSessionContext(), value );
	}
	
}
