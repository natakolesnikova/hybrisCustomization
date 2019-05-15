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
package de.hybris.platform.cmsfacades.cmsitems.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.cmsitems.service.CMSItemSearchService;
import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.servicelayer.services.admin.impl.DefaultCMSAdminSiteService;
import de.hybris.platform.cmsfacades.cmsitems.*;
import de.hybris.platform.cmsfacades.common.validator.FacadeValidationService;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrors;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrorsProvider;
import de.hybris.platform.cmsfacades.data.CMSItemSearchData;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.*;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.validation.Validator;

import com.google.common.collect.Lists;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.*;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSItemFacadeTest
{
	private static final String VALID_UID = "valid-item-uid";
	private static final String INVALID_UID = "invalid-item-uid";

	@InjectMocks
	private DefaultCMSItemFacade facade;

	@Mock
	private PlatformTransactionManager transactionManager;

	@Mock
	private ValidationErrorsProvider validationErrorsProvider;

	@Mock
	private CMSItemConverter CMSItemConverter;

	@Mock
	private ModelService modelService;

	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Mock
	private CMSItemModel itemModel;

	@Mock
	private ItemData invalidItemData;

	@Mock
	private ItemData validItemData;

	@Mock
	private ItemTypePopulatorProvider itemTypePopulatorProvider;

	@Mock
	private ItemDataPopulatorProvider itemDataPopulatorProvider;

	@Mock
	private Populator<Map<String, Object>, ItemModel> itemTypePopulator;

	@Mock
	private Populator<CMSItemModel, Map<String, Object>> itemDataPopulator;

	@Mock
	private CMSItemSearchService cmsItemSearchService;

	@Mock
	private Validator cmsItemSearchParamsDataValidator;

	@Mock
	private FacadeValidationService facadeValidationService;

	@Mock
	private DefaultCMSAdminSiteService defaultCMSAdminSiteService;

	@Mock
	private SessionSearchRestrictionsDisabler searchRestrictionsDisabler;

	@Mock
	private Converter<CMSItemSearchData, de.hybris.platform.cms2.data.CMSItemSearchData> cmsItemSearchDataConverter;

	@Mock
	private ValidationErrors validationErrors;

	@Before
	public void setup() throws CMSItemNotFoundException
	{
		when(validationErrors.getValidationErrors()).thenReturn(asList());
		when(validationErrorsProvider.getCurrentValidationErrors()).thenReturn(validationErrors);
		when(uniqueItemIdentifierService.getItemModel(invalidItemData)).thenReturn(Optional.empty());
		when(uniqueItemIdentifierService.getItemModel(validItemData)).thenReturn(Optional.of(itemModel));
		when(uniqueItemIdentifierService.getItemModel(any(), any())).thenReturn(Optional.empty());

		when(uniqueItemIdentifierService.getItemModel(INVALID_UID, CMSItemModel.class)).thenReturn(Optional.empty());
		when(uniqueItemIdentifierService.getItemModel(VALID_UID, CMSItemModel.class)).thenReturn(Optional.of(itemModel));

		when(itemTypePopulatorProvider.getItemTypePopulator(any())).thenReturn(Optional.of(itemTypePopulator));
		when(itemDataPopulatorProvider.getItemDataPopulators(any())).thenReturn(Arrays.asList(itemDataPopulator));

		when(CMSItemConverter.convert(any(Map.class))).thenReturn(itemModel);

		doAnswer(invocation -> {
			final Object[] args = invocation.getArguments();
			Supplier supplier = (Supplier)args[0];
			return supplier.get();
		}).when(searchRestrictionsDisabler).execute(any());

		facade.setCmsAdminSiteService(defaultCMSAdminSiteService);
	}

	@Test(expected = RuntimeException.class)
	public void whenFindByIdWithInvalidUidThenShouldThrowException() throws CMSItemNotFoundException
	{
		facade.getCMSItemByUuid(INVALID_UID);
	}

	@Test
	public void whenFindByIdWithValidUidShouldPerformConversion() throws CMSItemNotFoundException
	{
		facade.getCMSItemByUuid(VALID_UID);
		verify(CMSItemConverter).convert(itemModel);
	}

	@Test
	public void whenDeleteByIdWithValidUidShouldRemoveItem() throws CMSItemNotFoundException
	{
		facade.deleteCMSItemByUuid(VALID_UID);
		verify(modelService).remove(itemModel);
	}

	@Test
	public void shouldCreateItem() throws CMSItemNotFoundException
	{
		final Map<String, Object> map = new HashMap<>();
		facade.createItem(map);
		verify(CMSItemConverter).convert(map);
		verify(itemTypePopulator).populate(map, itemModel);
		verify(CMSItemConverter).convert(itemModel);
		verify(transactionManager, never()).rollback(any(TransactionStatus.class));
		verify(itemDataPopulator).populate(itemModel, map);
	}

	@Test
	public void shouldValidateItemForCreateAndRollbackTransaction() throws CMSItemNotFoundException
	{
		final Map<String, Object> map = new HashMap<>();
		facade.validateItemForCreate(map);
		verify(CMSItemConverter).convert(map);
		verify(itemTypePopulator).populate(map, itemModel);
		verify(CMSItemConverter).convert(itemModel);
		verify(itemDataPopulator).populate(itemModel, map);
		verify(modelService).saveAll();
		verify(modelService).refresh(itemModel);
		verify(modelService).detachAll();

		verify(transactionManager).rollback(any(TransactionStatus.class));

	}

	@Test(expected = CMSItemNotFoundException.class)
	public void updateShouldThrowExceptionWhenUuidIsInvalid() throws CMSItemNotFoundException
	{
		facade.updateItem(INVALID_UID, new HashMap<>());
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldThrowExceptionWhenUpdateItemHasInconsistentValue() throws CMSItemNotFoundException
	{
		final Map<String, Object> map = new HashMap<>();
		facade.updateItem(VALID_UID, map);
		verify(CMSItemConverter).convert(map);
		verify(itemTypePopulator).populate(map, itemModel);
		verify(CMSItemConverter).convert(itemModel);
	}

	@Test
	public void shouldUpdateItem() throws CMSItemNotFoundException
	{
		final Map<String, Object> map = new HashMap<>();
		map.put(FIELD_UUID, VALID_UID);
		facade.updateItem(VALID_UID, map);
		verify(CMSItemConverter).convert(map);
		verify(itemTypePopulator).populate(map, itemModel);
		verify(CMSItemConverter).convert(itemModel);
		verify(transactionManager, never()).rollback(any(TransactionStatus.class));
	}

	@Test
	public void shouldValidateItemForUpdateAndRollbackTransaction() throws CMSItemNotFoundException
	{
		final Map<String, Object> map = new HashMap<>();
		map.put(FIELD_UUID, VALID_UID);
		facade.validateItemForUpdate(VALID_UID, map);
		verify(CMSItemConverter).convert(map);
		verify(itemTypePopulator).populate(map, itemModel);
		verify(CMSItemConverter).convert(itemModel);
		verify(modelService).saveAll();
		verify(modelService).refresh(itemModel);
		verify(modelService).detachAll();

		verify(transactionManager).rollback(any(TransactionStatus.class));
	}

	@Test
	public void shouldSearchForItems()
	{

		// Input
		final CMSItemSearchData cmsItemSearchData = new CMSItemSearchData();
		final PageableData pageableData = new PageableData();

		// Intermediary data
		final SearchResult<CMSItemModel> modelResults = mock(SearchResult.class);
		final CMSItemModel mockItem1 = mock(CMSItemModel.class);
		final CMSItemModel mockItem2 = mock(CMSItemModel.class);
		final List<CMSItemModel> mockedResuls = Lists.newArrayList(mockItem1, mockItem2);

		final de.hybris.platform.cms2.data.CMSItemSearchData cms2ItemSearchData = mock(de.hybris.platform.cms2.data.CMSItemSearchData.class);
		when(cmsItemSearchDataConverter.convert(cmsItemSearchData)).thenReturn(cms2ItemSearchData);

		when(cmsItemSearchService.findCMSItems(cms2ItemSearchData, pageableData)).thenReturn(modelResults);
		when(modelResults.getResult()).thenReturn(mockedResuls);

		facade.findCMSItems(cmsItemSearchData, pageableData);

		verify(facadeValidationService).validate(cmsItemSearchParamsDataValidator, cmsItemSearchData);
		verify(cmsItemSearchService).findCMSItems(cms2ItemSearchData, pageableData);
		verify(CMSItemConverter).convert(mockItem1);
		verify(CMSItemConverter).convert(mockItem2);
	}

}
