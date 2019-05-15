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
package de.hybris.platform.cmsfacades.types.populator;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.AttributeDescriptorModelHelperService;
import de.hybris.platform.cmsfacades.data.ComponentTypeAttributeData;
import de.hybris.platform.cmsfacades.data.ComponentTypeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.*;
import java.util.stream.Collectors;

import de.hybris.platform.servicelayer.type.TypeService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populator aimed at setting all necessary information for the receiving end to build a cms item dropdown widget:
 * <ul>
 * <li>identifies the cmsStructureType as {@link #CMS_ITEM_DROPDOWN}</li>
 * <li>marks the dropdown to use {@link #ID_ATTRIBUTE} as idAttribute</li>
 * </ul>
 */
public class CMSItemDropdownComponentTypeAttributePopulator implements
		Populator<AttributeDescriptorModel, ComponentTypeAttributeData>
{

	private static final String MODEL_CLASSES_PATERN = "(.*)Model$";

	private TypeService typeService;
	private ObjectFactory<ComponentTypeData> componentTypeDataFactory;
	private AttributeDescriptorModelHelperService attributeDescriptorModelHelperService;
	private I18nComponentTypePopulator i18nComponentTypePopulator;

	private static final String ID_ATTRIBUTE = "uuid";
	private static final String LABEL_ATTRIBUTE_NAME = "name";
	private static final String LABEL_ATTRIBUTE_UID = "uid";
	private final String TYPE_CODE = "typeCode";
	private final String ITEM_SEARCH_PARAMS_KEY = "itemSearchParams";
	private final String PAGE_STATUS = "pageStatus";
	private final String ACTIVE = "active";

	private static final String CMS_ITEM_DROPDOWN = "CMSItemDropdown";

	@Override
	public void populate(final AttributeDescriptorModel source, final ComponentTypeAttributeData target)
			throws ConversionException
	{
		target.setCmsStructureType(CMS_ITEM_DROPDOWN);
		target.setIdAttribute(ID_ATTRIBUTE);
		target.setLabelAttributes(asList(LABEL_ATTRIBUTE_NAME, LABEL_ATTRIBUTE_UID));

		final Class<?> type = getAttributeDescriptorModelHelperService().getAttributeClass(source);

		final Map<String, String> paramsMap = ofNullable(target.getParams()).orElse(new HashMap<String, String>());
		paramsMap.put(TYPE_CODE, type.getSimpleName().replaceAll(MODEL_CLASSES_PATERN, "$1"));

		if (AbstractPageModel.class.isAssignableFrom(type))
		{
			paramsMap.put(ITEM_SEARCH_PARAMS_KEY, PAGE_STATUS + ":" + ACTIVE);
		}

		target.setParams(paramsMap);
		target.setSubTypes(this.getComponentSubTypes(type));
	}

	/**
	 * This method retrieves a map of concrete subtypes of the provided type. (If the provided type
	 * is concrete it will also be included in the map).
	 * @param type The type for which to retrieve its map of subtypes.
	 * @return map Map of concrete component subtypes. The key is the code of the sub-type and the value is its
	 * i18n key.
	 */
	protected Map<String, String> getComponentSubTypes(final Class<?> type)
	{
		ComposedTypeModel composedTypeModel = this.getTypeService().getComposedTypeForClass(type);

		ArrayList<ComposedTypeModel> supportedSubTypes = new ArrayList<>(composedTypeModel.getAllSubTypes());
		if( !composedTypeModel.getAbstract() )
		{
			// If the original type itself is not abstract it should also be returned as a supported SubType.
			supportedSubTypes.add(composedTypeModel);
		}

		return supportedSubTypes.stream().collect(Collectors.toMap(ComposedTypeModel::getCode,
				typeModel -> getComponentTypeI18nKey(typeModel)));
	}

	/**
	 * This method retrieves the i18n key of the provided component type.
	 *
	 * @param typeModel The type for which to retrieve its map of subtypes.
	 * @return String The i18n key of the provided component type.
	 */
	protected String getComponentTypeI18nKey(ComposedTypeModel typeModel)
	{
		final ComponentTypeData componentTypeData  = getComponentTypeDataFactory().getObject();
		getI18nComponentTypePopulator().populate(typeModel, componentTypeData);
		return componentTypeData.getI18nKey();
	}

	@Required
	public void setAttributeDescriptorModelHelperService(
			final AttributeDescriptorModelHelperService attributeDescriptorModelHelperService)
	{
		this.attributeDescriptorModelHelperService = attributeDescriptorModelHelperService;
	}

	protected AttributeDescriptorModelHelperService getAttributeDescriptorModelHelperService()
	{
		return attributeDescriptorModelHelperService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	protected  TypeService getTypeService()
	{
		return this.typeService;
	}

	@Required
	public void setI18nComponentTypePopulator(I18nComponentTypePopulator i18nComponentTypePopulator)
	{
		this.i18nComponentTypePopulator = i18nComponentTypePopulator;
	}

	protected I18nComponentTypePopulator getI18nComponentTypePopulator()
	{
		return i18nComponentTypePopulator;
	}

	@Required
	public void setComponentTypeDataFactory(final ObjectFactory<ComponentTypeData> componentTypeDataFactory)
	{
		this.componentTypeDataFactory = componentTypeDataFactory;
	}

	protected ObjectFactory<ComponentTypeData> getComponentTypeDataFactory()
	{
		return componentTypeDataFactory;
	}
}
