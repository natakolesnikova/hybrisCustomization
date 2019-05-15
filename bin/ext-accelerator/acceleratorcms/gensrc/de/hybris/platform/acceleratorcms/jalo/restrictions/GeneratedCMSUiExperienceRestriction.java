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
package de.hybris.platform.acceleratorcms.jalo.restrictions;

import de.hybris.platform.acceleratorcms.constants.AcceleratorCmsConstants;
import de.hybris.platform.cms2.jalo.restrictions.AbstractRestriction;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.acceleratorcms.jalo.restrictions.CMSUiExperienceRestriction CMSUiExperienceRestriction}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedCMSUiExperienceRestriction extends AbstractRestriction
{
	/** Qualifier of the <code>CMSUiExperienceRestriction.uiExperience</code> attribute **/
	public static final String UIEXPERIENCE = "uiExperience";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AbstractRestriction.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(UIEXPERIENCE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSUiExperienceRestriction.uiExperience</code> attribute.
	 * @return the uiExperience
	 */
	public EnumerationValue getUiExperience(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, UIEXPERIENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSUiExperienceRestriction.uiExperience</code> attribute.
	 * @return the uiExperience
	 */
	public EnumerationValue getUiExperience()
	{
		return getUiExperience( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSUiExperienceRestriction.uiExperience</code> attribute. 
	 * @param value the uiExperience
	 */
	public void setUiExperience(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, UIEXPERIENCE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSUiExperienceRestriction.uiExperience</code> attribute. 
	 * @param value the uiExperience
	 */
	public void setUiExperience(final EnumerationValue value)
	{
		setUiExperience( getSession().getSessionContext(), value );
	}
	
}
