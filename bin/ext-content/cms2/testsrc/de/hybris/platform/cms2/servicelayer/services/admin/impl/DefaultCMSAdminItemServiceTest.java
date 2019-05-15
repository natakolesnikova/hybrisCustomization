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
package de.hybris.platform.cms2.servicelayer.services.admin.impl;

import static de.hybris.platform.cms2.servicelayer.services.admin.impl.AbstractCMSAdminService.ACTIVECATALOGVERSION;
import static de.hybris.platform.core.PK.fromLong;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSItemDao;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSAdminItemServiceTest
{

	private static final String GENERATED_UID = "generatedUID";

	public static class CMSSubClass extends CMSItemModel
	{
		// Intentionally left empty.
	}

	@Mock
	private ModelService modelService;

	@Mock
	private PersistentKeyGenerator componentUidGenerator;

	@Mock
	private CMSItemDao cmsItemDao;

	@Mock
	private SessionService sessionService;

	@InjectMocks
	private DefaultCMSAdminItemService defaultCMSAdminItemService;

	private final String uid = "uid";

	@Mock
	private CatalogVersionModel catalogVersionInSession;

	private final PK versionPK = fromLong(123);

	@Mock
	private CatalogVersionModel catalogVersion;

	@Mock
	private CMSItemModel item;

	@Mock
	private CMSSubClass cmsSubItem;

	@Before
	public void setUp()
	{
		when(sessionService.getAttribute(ACTIVECATALOGVERSION)).thenReturn(versionPK);
		when(modelService.get(versionPK)).thenReturn(catalogVersionInSession);
		when(modelService.create(CMSSubClass.class)).thenReturn(cmsSubItem);
		when(cmsItemDao.findByUid(uid, catalogVersion)).thenReturn(item);
		when(cmsItemDao.findByUid(uid, catalogVersionInSession)).thenReturn(item);

		when(componentUidGenerator.generate()).thenReturn(GENERATED_UID);
	}

	@Test
	public void findByUidWillDelegateToTheDaoWithPassedVersion() throws CMSItemNotFoundException
	{
		//execute
		final CMSItemModel fetchedItem = defaultCMSAdminItemService.findByUid(uid, catalogVersion);

		//verify
		assertThat(fetchedItem, is(item));
		verify(cmsItemDao).findByUid(uid, catalogVersion);
	}

	@Test
	public void findByUidWillDelegateToTheDaoWithVersionInSession() throws CMSItemNotFoundException
	{
		//execute
		final CMSItemModel fetchedItem = defaultCMSAdminItemService.findByUid(uid);

		//verify
		assertThat(fetchedItem, is(item));
		verify(cmsItemDao).findByUid(uid, catalogVersionInSession);

	}

	@Test(expected = CMSItemNotFoundException.class)
	public void findByUidWillThrowExceptionWhenNotFound() throws CMSItemNotFoundException
	{
		//setup
		when(cmsItemDao.findByUid(uid, catalogVersionInSession)).thenReturn(null);

		//execute
		defaultCMSAdminItemService.findByUid(uid);

		verify(cmsItemDao).findByUid(uid, catalogVersionInSession);

	}

	@Test
	public void willCreateAnItemAndAssignAUID()
	{
		final CMSSubClass createdItem = defaultCMSAdminItemService.createItem(CMSSubClass.class);

		assertThat(createdItem, is(cmsSubItem));
		verify(createdItem, times(1)).setUid("cmsitem_" + GENERATED_UID);

	}

	@Test
	public void willCreateAnItemAndNotAssignAUID()
	{
		when(cmsSubItem.getUid()).thenReturn("existinguid");

		final CMSSubClass createdItem = defaultCMSAdminItemService.createItem(CMSSubClass.class);
		assertThat(createdItem, is(cmsSubItem));
		verify(createdItem, never()).setUid(anyString());

	}
}
