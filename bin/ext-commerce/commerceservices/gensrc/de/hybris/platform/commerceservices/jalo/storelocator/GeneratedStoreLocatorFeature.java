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
package de.hybris.platform.commerceservices.jalo.storelocator;

import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.storelocator.jalo.PointOfService;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.storelocator.StoreLocatorFeature StoreLocatorFeature}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedStoreLocatorFeature extends GenericItem
{
	/** Qualifier of the <code>StoreLocatorFeature.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>StoreLocatorFeature.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>StoreLocatorFeature.icon</code> attribute **/
	public static final String ICON = "icon";
	/** Qualifier of the <code>StoreLocatorFeature.pointOfServices</code> attribute **/
	public static final String POINTOFSERVICES = "pointOfServices";
	/** Relation ordering override parameter constants for StoreLocation2StoreLocatorFeature from ((commerceservices))*/
	protected static String STORELOCATION2STORELOCATORFEATURE_SRC_ORDERED = "relation.StoreLocation2StoreLocatorFeature.source.ordered";
	protected static String STORELOCATION2STORELOCATORFEATURE_TGT_ORDERED = "relation.StoreLocation2StoreLocatorFeature.target.ordered";
	/** Relation disable markmodifed parameter constants for StoreLocation2StoreLocatorFeature from ((commerceservices))*/
	protected static String STORELOCATION2STORELOCATORFEATURE_MARKMODIFIED = "relation.StoreLocation2StoreLocatorFeature.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(ICON, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreLocatorFeature.code</code> attribute.
	 * @return the code - Unique code of store locator feature.
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreLocatorFeature.code</code> attribute.
	 * @return the code - Unique code of store locator feature.
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreLocatorFeature.code</code> attribute. 
	 * @param value the code - Unique code of store locator feature.
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreLocatorFeature.code</code> attribute. 
	 * @param value the code - Unique code of store locator feature.
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreLocatorFeature.icon</code> attribute.
	 * @return the icon - Icon that can represent the feature
	 */
	public Media getIcon(final SessionContext ctx)
	{
		return (Media)getProperty( ctx, ICON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreLocatorFeature.icon</code> attribute.
	 * @return the icon - Icon that can represent the feature
	 */
	public Media getIcon()
	{
		return getIcon( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreLocatorFeature.icon</code> attribute. 
	 * @param value the icon - Icon that can represent the feature
	 */
	public void setIcon(final SessionContext ctx, final Media value)
	{
		setProperty(ctx, ICON,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreLocatorFeature.icon</code> attribute. 
	 * @param value the icon - Icon that can represent the feature
	 */
	public void setIcon(final Media value)
	{
		setIcon( getSession().getSessionContext(), value );
	}
	
	@Override
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("PointOfService");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(STORELOCATION2STORELOCATORFEATURE_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreLocatorFeature.name</code> attribute.
	 * @return the name - Description of the store locator feature
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedStoreLocatorFeature.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreLocatorFeature.name</code> attribute.
	 * @return the name - Description of the store locator feature
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreLocatorFeature.name</code> attribute. 
	 * @return the localized name - Description of the store locator feature
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreLocatorFeature.name</code> attribute. 
	 * @return the localized name - Description of the store locator feature
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreLocatorFeature.name</code> attribute. 
	 * @param value the name - Description of the store locator feature
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedStoreLocatorFeature.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreLocatorFeature.name</code> attribute. 
	 * @param value the name - Description of the store locator feature
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreLocatorFeature.name</code> attribute. 
	 * @param value the name - Description of the store locator feature
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreLocatorFeature.name</code> attribute. 
	 * @param value the name - Description of the store locator feature
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreLocatorFeature.pointOfServices</code> attribute.
	 * @return the pointOfServices
	 */
	public Collection<PointOfService> getPointOfServices(final SessionContext ctx)
	{
		final List<PointOfService> items = getLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.STORELOCATION2STORELOCATORFEATURE,
			"PointOfService",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreLocatorFeature.pointOfServices</code> attribute.
	 * @return the pointOfServices
	 */
	public Collection<PointOfService> getPointOfServices()
	{
		return getPointOfServices( getSession().getSessionContext() );
	}
	
	public long getPointOfServicesCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			CommerceServicesConstants.Relations.STORELOCATION2STORELOCATORFEATURE,
			"PointOfService",
			null
		);
	}
	
	public long getPointOfServicesCount()
	{
		return getPointOfServicesCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreLocatorFeature.pointOfServices</code> attribute. 
	 * @param value the pointOfServices
	 */
	public void setPointOfServices(final SessionContext ctx, final Collection<PointOfService> value)
	{
		setLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.STORELOCATION2STORELOCATORFEATURE,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(STORELOCATION2STORELOCATORFEATURE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreLocatorFeature.pointOfServices</code> attribute. 
	 * @param value the pointOfServices
	 */
	public void setPointOfServices(final Collection<PointOfService> value)
	{
		setPointOfServices( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to pointOfServices. 
	 * @param value the item to add to pointOfServices
	 */
	public void addToPointOfServices(final SessionContext ctx, final PointOfService value)
	{
		addLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.STORELOCATION2STORELOCATORFEATURE,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(STORELOCATION2STORELOCATORFEATURE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to pointOfServices. 
	 * @param value the item to add to pointOfServices
	 */
	public void addToPointOfServices(final PointOfService value)
	{
		addToPointOfServices( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from pointOfServices. 
	 * @param value the item to remove from pointOfServices
	 */
	public void removeFromPointOfServices(final SessionContext ctx, final PointOfService value)
	{
		removeLinkedItems( 
			ctx,
			false,
			CommerceServicesConstants.Relations.STORELOCATION2STORELOCATORFEATURE,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(STORELOCATION2STORELOCATORFEATURE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from pointOfServices. 
	 * @param value the item to remove from pointOfServices
	 */
	public void removeFromPointOfServices(final PointOfService value)
	{
		removeFromPointOfServices( getSession().getSessionContext(), value );
	}
	
}
