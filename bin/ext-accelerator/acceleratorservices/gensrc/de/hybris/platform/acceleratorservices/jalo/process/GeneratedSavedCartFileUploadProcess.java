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
package de.hybris.platform.acceleratorservices.jalo.process;

import de.hybris.platform.acceleratorservices.constants.AcceleratorServicesConstants;
import de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.order.Cart;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.acceleratorservices.jalo.process.SavedCartFileUploadProcess SavedCartFileUploadProcess}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedSavedCartFileUploadProcess extends StoreFrontCustomerProcess
{
	/** Qualifier of the <code>SavedCartFileUploadProcess.uploadedFile</code> attribute **/
	public static final String UPLOADEDFILE = "uploadedFile";
	/** Qualifier of the <code>SavedCartFileUploadProcess.savedCart</code> attribute **/
	public static final String SAVEDCART = "savedCart";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(StoreFrontCustomerProcess.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(UPLOADEDFILE, AttributeMode.INITIAL);
		tmp.put(SAVEDCART, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedCartFileUploadProcess.savedCart</code> attribute.
	 * @return the savedCart - The saved cart which business process creates.
	 */
	public Cart getSavedCart(final SessionContext ctx)
	{
		return (Cart)getProperty( ctx, SAVEDCART);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedCartFileUploadProcess.savedCart</code> attribute.
	 * @return the savedCart - The saved cart which business process creates.
	 */
	public Cart getSavedCart()
	{
		return getSavedCart( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SavedCartFileUploadProcess.savedCart</code> attribute. 
	 * @param value the savedCart - The saved cart which business process creates.
	 */
	public void setSavedCart(final SessionContext ctx, final Cart value)
	{
		setProperty(ctx, SAVEDCART,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SavedCartFileUploadProcess.savedCart</code> attribute. 
	 * @param value the savedCart - The saved cart which business process creates.
	 */
	public void setSavedCart(final Cart value)
	{
		setSavedCart( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedCartFileUploadProcess.uploadedFile</code> attribute.
	 * @return the uploadedFile - The CSV file for upload
	 */
	public Media getUploadedFile(final SessionContext ctx)
	{
		return (Media)getProperty( ctx, UPLOADEDFILE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedCartFileUploadProcess.uploadedFile</code> attribute.
	 * @return the uploadedFile - The CSV file for upload
	 */
	public Media getUploadedFile()
	{
		return getUploadedFile( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SavedCartFileUploadProcess.uploadedFile</code> attribute. 
	 * @param value the uploadedFile - The CSV file for upload
	 */
	public void setUploadedFile(final SessionContext ctx, final Media value)
	{
		setProperty(ctx, UPLOADEDFILE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SavedCartFileUploadProcess.uploadedFile</code> attribute. 
	 * @param value the uploadedFile - The CSV file for upload
	 */
	public void setUploadedFile(final Media value)
	{
		setUploadedFile( getSession().getSessionContext(), value );
	}
	
}
