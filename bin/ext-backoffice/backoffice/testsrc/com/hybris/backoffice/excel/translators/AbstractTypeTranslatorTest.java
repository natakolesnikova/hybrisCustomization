/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.hybris.backoffice.excel.translators;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;


public abstract class AbstractTypeTranslatorTest
{
	public abstract void shouldExportDataBeNullSafe();

	public abstract void shouldExportedDataBeInProperFormat();

	public abstract void shouldGivenTypeBeHandled();

	public CatalogVersionModel generateMock(final String id, final String version)
	{
		final CatalogVersionModel catalogVersion = mock(CatalogVersionModel.class);
		final CatalogModel catalog = mock(CatalogModel.class);

		given(catalog.getId()).willReturn(id);

		given(catalogVersion.getVersion()).willReturn(version);
		given(catalogVersion.getCatalog()).willReturn(catalog);

		return catalogVersion;
	}

}
