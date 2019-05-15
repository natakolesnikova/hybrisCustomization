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
package de.hybris.platform.cmsfacades.synchronization.itemvisitors.impl;

import static com.google.common.collect.Lists.newArrayList;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.model.visitor.ItemVisitor;

import java.util.List;
import java.util.Map;

/**
 * Concrete implementation of {@link ItemVisitor} to visit items of the {@link MediaContainerModel} types.
 *
 * Collects the items from {@link MediaContainerModel#getMedias()} 
 */
public class MediaContainerModelVisitor implements ItemVisitor<MediaContainerModel>
{

	@Override
	public List<ItemModel> visit(MediaContainerModel source, List<ItemModel> path, Map<String, Object> ctx)
	{
		return newArrayList(source.getMedias());
	}

}
