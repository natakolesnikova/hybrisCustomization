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
package de.hybris.platform.cmsfacades.pagetemplates;


import de.hybris.platform.cmsfacades.data.PageTemplateDTO;
import de.hybris.platform.cmsfacades.data.PageTemplateData;

import java.util.List;


/**
 * Component facade interface which deals with methods related to page operations.
 */
public interface PageTemplateFacade
{

	/**
	 * Retrieves the list of page templates fmatching the given filters
	 *
	 * @param pageTemplateDTO
	 *           dto containing search restrictions
	 * @return a list of {@link PageTemplateData}
	 */
	List<PageTemplateData> findPageTemplates(PageTemplateDTO pageTemplateDTO);

}
