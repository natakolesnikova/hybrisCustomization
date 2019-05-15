/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cmsfacades.rendering.predicates.attributes;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.core.model.type.*;
import de.hybris.platform.servicelayer.type.TypeService;
import org.springframework.beans.factory.annotation.Required;

import java.util.function.Predicate;


/**
 * This predicate is used to test whether an attribute is or has elements of type {@link AbstractCMSComponentModel}.
 */
public class AttributeContainsCMSComponentsPredicate implements Predicate<AttributeDescriptorModel>
{
	// --------------------------------------------------------------------------
	// Variables
	// --------------------------------------------------------------------------
	private TypeService typeService;

	// --------------------------------------------------------------------------
	// Public API
	// --------------------------------------------------------------------------
	@Override
	public boolean test(AttributeDescriptorModel attributeDescriptorModel)
	{
		return !attributeDescriptorModel.getPrimitive() &&
				isCmsComponentType(getAttributeContentType(attributeDescriptorModel));
	}

	// --------------------------------------------------------------------------
	// Helper Methods
	// --------------------------------------------------------------------------

	/**
	 * This method is used to determine whether a TypeModel is assignable from {@link AbstractCMSComponentModel}.
	 * @param attributeType The attribute to check.
	 * @return boolean specifying whether the type is assignable from AbstractCMSComponentModel or not.
	 */
	protected boolean isCmsComponentType(TypeModel attributeType)
	{
		TypeModel abstractCMSComponentTypeModel = getTypeService().getComposedTypeForCode(AbstractCMSComponentModel._TYPECODE);
		return getTypeService().isAssignableFrom(abstractCMSComponentTypeModel, attributeType);
	}

	/**
	 * Retrieves the type of the right element {@link TypeModel} depending on the type attribute.
	 * - If the attribute is not a collection, then the value returned is the type of the attribute itself.
	 * - If it's a list, the value returned is the type of the elements in the list.
	 * - If it's a map, the value returned is the type of the values in the map.
	 * @param attributeDescriptorModel The model describing the attribute whose type to find.
	 * @return the type of the right type of element.
	 */
	protected TypeModel getAttributeContentType(AttributeDescriptorModel attributeDescriptorModel)
	{
		TypeModel attributeType;
		if(attributeDescriptorModel.getLocalized())
		{
			attributeType = getMapElementComponentTypeModel(attributeDescriptorModel);
		}
		else if(isCollection(attributeDescriptorModel))
		{
			attributeType = getCollectionElementComponentTypeModel(attributeDescriptorModel);
		}
		else
		{
			attributeType = attributeDescriptorModel.getAttributeType();
		}

		return attributeType;
	}

	/**
	 * Returns the {@link TypeModel} of the elements within a collection.
	 * @param attributeDescriptorModel The model describing the attribute whose type to find.
	 * @return the type of the elements in the collection.
	 */
	protected TypeModel getCollectionElementComponentTypeModel(AttributeDescriptorModel attributeDescriptorModel)
	{
		CollectionTypeModel collectionTypeModel = (CollectionTypeModel) attributeDescriptorModel.getAttributeType();
		return collectionTypeModel.getElementType();
	}

	/**
	 * Returns the {@link TypeModel} of the elements within a map.
	 * @param attributeDescriptorModel The model describing the attribute whose type to find.
	 * @return the type of the elements in the map.
	 */
	protected TypeModel getMapElementComponentTypeModel(AttributeDescriptorModel attributeDescriptorModel)
	{
		MapTypeModel mapTypeModel = (MapTypeModel) attributeDescriptorModel.getAttributeType();
		return mapTypeModel.getReturntype();
	}

	/**
	 * This method can be used to determine whether an attribute contains a collection or not.
	 * @param attributeDescriptorModel The model describing the attribute to evaluate.
	 * @return true if the attribute describes a map. False otherwise.
	 */
	protected boolean isCollection(AttributeDescriptorModel attributeDescriptorModel)
	{
		return attributeDescriptorModel.getAttributeType() instanceof CollectionTypeModel;
	}

	// --------------------------------------------------------------------------
	// Getters/Setters
	// --------------------------------------------------------------------------
	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(TypeService typeService)
	{
		this.typeService = typeService;
	}
}
