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
package de.hybris.platform.cmsfacades.items.converter.impl;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.cmsfacades.items.converter.CmsComponentConverterFactory;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;

import java.util.Map;
import java.util.Optional;


/**
 * Default implementation of {@link CmsComponentConverterFactory}
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public final class DefaultCmsComponentConverterFactory implements CmsComponentConverterFactory
{

	private Map<Class<? extends AbstractCMSComponentModel>, AbstractPopulatingConverter<AbstractCMSComponentModel, AbstractCMSComponentData>> converters;

	public DefaultCmsComponentConverterFactory(
			final Map<Class<? extends AbstractCMSComponentModel>, AbstractPopulatingConverter<AbstractCMSComponentModel, AbstractCMSComponentData>> converters)
	{
		this.converters = converters;
	}

	@Override
	public Optional<AbstractPopulatingConverter<AbstractCMSComponentModel, AbstractCMSComponentData>> getConverter(
			final Class<? extends AbstractCMSComponentModel> classType)
	{
		return Optional.ofNullable(Optional.ofNullable(getConverters().get(classType))
				.orElseGet(() -> getConverters().get(AbstractCMSComponentModel.class)));
	}

	public Map<Class<? extends AbstractCMSComponentModel>, AbstractPopulatingConverter<AbstractCMSComponentModel, AbstractCMSComponentData>> getConverters()
	{
		return converters;
	}

	public void setConverters(
			final Map<Class<? extends AbstractCMSComponentModel>, AbstractPopulatingConverter<AbstractCMSComponentModel, AbstractCMSComponentData>> converters)
	{
		this.converters = converters;
	}
}
