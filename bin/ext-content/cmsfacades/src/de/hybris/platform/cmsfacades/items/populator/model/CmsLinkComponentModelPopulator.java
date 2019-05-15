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
package de.hybris.platform.cmsfacades.items.populator.model;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.enums.LinkTargets;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cmsfacades.common.populator.LocalizedPopulator;
import de.hybris.platform.cmsfacades.data.CMSLinkComponentData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default cms link component implementation of {@link Populator}.
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class CmsLinkComponentModelPopulator implements Populator<CMSLinkComponentModel, CMSLinkComponentData>
{
	private LocalizedPopulator localizedPopulator;
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Override
	public void populate(final CMSLinkComponentModel source, final CMSLinkComponentData target) throws ConversionException
	{
		final Map<String, String> linkNameMap = Optional.ofNullable(target.getLinkName())
				.orElseGet(() -> getNewLinkNameMap(target));

		getLocalizedPopulator().populate( //
				(locale, value) -> linkNameMap.put(getLocalizedPopulator().getLanguage(locale), value), //
				(locale) -> source.getLinkName(locale));

		if (Objects.nonNull(source.getCategory()))
		{
			getUniqueItemIdentifierService().getItemData(source.getCategory())
			.ifPresent(itemData -> target.setCategory(itemData.getItemId()));
		}
		else if (Objects.nonNull(source.getContentPage()))
		{
			target.setContentPage(source.getContentPage().getUid());
		}
		else if (Objects.nonNull(source.getProduct()))
		{
			getUniqueItemIdentifierService().getItemData(source.getProduct())
			.ifPresent(itemData -> target.setProduct(itemData.getItemId()));
		}
		else
		{
			target.setUrl(source.getUrl());
		}

		target.setExternal(source.isExternal());
		target.setTarget(source.getTarget().equals(LinkTargets.NEWWINDOW));
	}

	protected Map<String, String> getNewLinkNameMap(final CMSLinkComponentData target)
	{
		target.setLinkName(new LinkedHashMap<>());
		return target.getLinkName();
	}

	protected LocalizedPopulator getLocalizedPopulator()
	{
		return localizedPopulator;
	}

	@Required
	public void setLocalizedPopulator(final LocalizedPopulator localizedPopulator)
	{
		this.localizedPopulator = localizedPopulator;
	}

	protected UniqueItemIdentifierService getUniqueItemIdentifierService()
	{
		return uniqueItemIdentifierService;
	}

	@Required
	public void setUniqueItemIdentifierService(final UniqueItemIdentifierService uniqueItemIdentifierService)
	{
		this.uniqueItemIdentifierService = uniqueItemIdentifierService;
	}

}
