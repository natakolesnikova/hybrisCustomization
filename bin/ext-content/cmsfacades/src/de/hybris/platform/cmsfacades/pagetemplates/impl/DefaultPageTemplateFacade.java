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
package de.hybris.platform.cmsfacades.pagetemplates.impl;

import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmsfacades.data.PageTemplateDTO;
import de.hybris.platform.cmsfacades.data.PageTemplateData;
import de.hybris.platform.cmsfacades.pagetemplates.PageTemplateFacade;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link PageTemplateFacade}.
 */
public class DefaultPageTemplateFacade implements PageTemplateFacade
{
	private CMSAdminPageService cmsAdminPageService;

	private Converter<PageTemplateModel, PageTemplateData> pageTemplateModelConverter;

	private SessionSearchRestrictionsDisabler sessionSearchRestrictionsDisabler;

	@Override
	public List<PageTemplateData> findPageTemplates(final PageTemplateDTO pageTemplateDTO)
	{
		return getCmsAdminPageService().getPageTypeByCode(pageTemplateDTO.getPageTypeCode())
				.map(pageType -> getSessionSearchRestrictionsDisabler().execute(() -> getPageTemplateByPageType(pageType, pageTemplateDTO.getActive()))) //
				.orElseGet(Collections::emptyList);
	}

	protected List<PageTemplateData> getPageTemplateByPageType(final CMSPageTypeModel pageType, final Boolean active)
	{
		return Optional.ofNullable(active) //
				.map(optional -> getCmsAdminPageService().getAllRestrictedPageTemplates(optional, pageType)) //
				.orElseGet(() -> getAllPageTemplates(pageType)) //
				.stream() //
				.map(model -> getPageTemplateModelConverter().convert(model)) //
				.collect(Collectors.toList());
	}

	protected Collection<PageTemplateModel> getAllPageTemplates(final CMSPageTypeModel pageType)
	{
		final Collection<PageTemplateModel> pageTemplateModels = new ArrayList<>();
		pageTemplateModels.addAll(getSessionSearchRestrictionsDisabler().execute(() ->getCmsAdminPageService().getAllRestrictedPageTemplates(true, pageType)));
		pageTemplateModels.addAll(getSessionSearchRestrictionsDisabler().execute(() -> getCmsAdminPageService().getAllRestrictedPageTemplates(false, pageType)));
		return pageTemplateModels;
	}

	protected CMSAdminPageService getCmsAdminPageService()
	{
		return cmsAdminPageService;
	}

	@Required
	public void setCmsAdminPageService(final CMSAdminPageService cmsAdminPageService)
	{
		this.cmsAdminPageService = cmsAdminPageService;
	}

	protected Converter<PageTemplateModel, PageTemplateData> getPageTemplateModelConverter()
	{
		return pageTemplateModelConverter;
	}

	@Required
	public void setPageTemplateModelConverter(final Converter<PageTemplateModel, PageTemplateData> pageTemplateModelConverter)
	{
		this.pageTemplateModelConverter = pageTemplateModelConverter;
	}

	protected SessionSearchRestrictionsDisabler getSessionSearchRestrictionsDisabler()
	{
		return sessionSearchRestrictionsDisabler;
	}

	@Required
	public void setSessionSearchRestrictionsDisabler(final SessionSearchRestrictionsDisabler sessionSearchRestrictionsDisabler)
	{
		this.sessionSearchRestrictionsDisabler = sessionSearchRestrictionsDisabler;
	}


}
