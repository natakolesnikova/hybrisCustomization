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

import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.order.delivery.DeliveryMode PickUpDeliveryMode}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedPickUpDeliveryMode extends DeliveryMode
{
	/** Qualifier of the <code>PickUpDeliveryMode.supportedMode</code> attribute **/
	public static final String SUPPORTEDMODE = "supportedMode";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(DeliveryMode.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(SUPPORTEDMODE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PickUpDeliveryMode.supportedMode</code> attribute.
	 * @return the supportedMode - The mode supported by the pickup delivery mode
	 */
	public EnumerationValue getSupportedMode(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, SUPPORTEDMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PickUpDeliveryMode.supportedMode</code> attribute.
	 * @return the supportedMode - The mode supported by the pickup delivery mode
	 */
	public EnumerationValue getSupportedMode()
	{
		return getSupportedMode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PickUpDeliveryMode.supportedMode</code> attribute. 
	 * @param value the supportedMode - The mode supported by the pickup delivery mode
	 */
	public void setSupportedMode(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, SUPPORTEDMODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PickUpDeliveryMode.supportedMode</code> attribute. 
	 * @param value the supportedMode - The mode supported by the pickup delivery mode
	 */
	public void setSupportedMode(final EnumerationValue value)
	{
		setSupportedMode( getSession().getSessionContext(), value );
	}
	
}
