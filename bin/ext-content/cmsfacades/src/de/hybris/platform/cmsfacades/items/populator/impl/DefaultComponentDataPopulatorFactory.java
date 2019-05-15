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
package de.hybris.platform.cmsfacades.items.populator.impl;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.cmsfacades.items.populator.ComponentDataPopulatorFactory;
import de.hybris.platform.converters.Populator;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link ComponentDataPopulatorFactory}
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public final class DefaultComponentDataPopulatorFactory implements ComponentDataPopulatorFactory
{

	private Map<Class<? extends AbstractCMSComponentModel>, Populator<AbstractCMSComponentData, AbstractCMSComponentModel>> populators;

	@Override
	public Optional<Populator<AbstractCMSComponentData, AbstractCMSComponentModel>> getPopulator(
			final Class<? extends AbstractCMSComponentModel> classType)
	{
		return Optional.ofNullable(Optional.ofNullable(getPopulators().get(classType))
				.orElseGet(() -> getPopulators().get(AbstractCMSComponentModel.class)));
	}

	protected Map<Class<? extends AbstractCMSComponentModel>, Populator<AbstractCMSComponentData, AbstractCMSComponentModel>> getPopulators()
	{
		return populators;
	}

	@Required
	public void setPopulators(
			final Map<Class<? extends AbstractCMSComponentModel>, Populator<AbstractCMSComponentData, AbstractCMSComponentModel>> populators)
	{
		this.populators = populators;
	}

}
