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

import static de.hybris.platform.cmsfacades.common.validator.ValidationErrorBuilder.newValidationErrorBuilder;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_CATALOG_VERSION;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_REQUIRED;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_URI_CONTEXT;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_UUID;
import static java.util.stream.Collectors.toList;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.cmsitems.service.CMSItemSearchService;
import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.cmsitems.CMSItemConverter;
import de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade;
import de.hybris.platform.cmsfacades.cmsitems.ItemDataPopulatorProvider;
import de.hybris.platform.cmsfacades.cmsitems.ItemTypePopulatorProvider;
import de.hybris.platform.cmsfacades.cmsitems.OriginalClonedItemProvider;
import de.hybris.platform.cmsfacades.common.validator.FacadeValidationService;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrorsProvider;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.CMSItemSearchData;
import de.hybris.platform.cmsfacades.exception.RequiredRollbackException;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.Validator;


/**
 * Default implementation of the {@link CMSItemFacade}.
 */
public class DefaultCMSItemFacade implements CMSItemFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultCMSItemFacade.class);
	private static final String MODEL_SAVING_EXCEPTION_REGEX = "\\[\\w*\\]";

	private CMSItemConverter cmsItemConverter;

	private ModelService modelService;

	private ItemTypePopulatorProvider itemTypePopulatorProvider;

	private CMSItemSearchService cmsItemSearchService;

	private Validator cmsItemSearchDataValidator;

	private FacadeValidationService facadeValidationService;

	private UniqueItemIdentifierService uniqueItemIdentifierService;

	private CMSAdminSiteService cmsAdminSiteService;

	private CatalogVersionService catalogVersionService;

	private Converter<CMSItemSearchData, de.hybris.platform.cms2.data.CMSItemSearchData> cmsItemSearchDataConverter;

	private ValidationErrorsProvider validationErrorsProvider;

	private PlatformTransactionManager transactionManager;

	private TypeService typeService;

	private SessionSearchRestrictionsDisabler sessionSearchRestrictionsDisabler;

	private OriginalClonedItemProvider originalClonedItemProvider;

	private ItemDataPopulatorProvider itemDataPopulatorProvider;

	@Override
	public SearchResult<Map<String, Object>> findCMSItems(final CMSItemSearchData cmsItemSearchData,
			final PageableData pageableData)
	{
		return getSessionSearchRestrictionsDisabler().execute(() -> {
			getFacadeValidationService().validate(getCmsItemSearchDataValidator(), cmsItemSearchData);

			if (StringUtils.isNotBlank(cmsItemSearchData.getTypeCode())) {
				try
				{
					getTypeService().getComposedTypeForCode(cmsItemSearchData.getTypeCode());
				}
				catch (final UnknownIdentifierException e)
				{
					final List<Map<String, Object>> convertedResults = new ArrayList<>();
					return new SearchResultImpl<>(convertedResults, 0, pageableData.getPageSize(), pageableData.getCurrentPage());
				}
			}

			final SearchResult<CMSItemModel> searchResults = getCmsItemSearchService()
					.findCMSItems(getCmsItemSearchDataConverter().convert(cmsItemSearchData), pageableData);

			final List<Map<String, Object>> convertedResults = searchResults.getResult().stream().map(this::convertAndPopulate)
					.collect(Collectors.toList());
			return new SearchResultImpl<>(
					convertedResults, searchResults.getTotalCount(), searchResults.getRequestedCount(),
					searchResults.getRequestedStart());
		});
	}

	@Override
	public List<Map<String, Object>> findCMSItems(final List<String> uuids) throws CMSItemNotFoundException
	{
		return getSessionSearchRestrictionsDisabler().execute(() -> uuids.stream().map(uuid -> {
			try
			{
				return getCMSItemByUuid(uuid);
			}
			catch (final CMSItemNotFoundException e)
			{
				throw new RuntimeException(e);
			}

		}).collect(toList()));
	}

	@Override
	public Map<String, Object> getCMSItemByUuid(final String uuid) throws CMSItemNotFoundException
	{
		return getSessionSearchRestrictionsDisabler().execute(() -> {
			try
			{
				final CMSItemModel cmsItemModel = getUniqueItemIdentifierService() //
						.getItemModel(uuid, CMSItemModel.class) //
						.orElseThrow(() -> createCMSItemNotFoundException(uuid));

				return convertAndPopulate(cmsItemModel);
			}
			catch (final CMSItemNotFoundException e)
			{
				throw new RuntimeException(e);
			}
		});
	}

	@Override
	public Map<String, Object> createItem(final Map<String, Object> itemMap) throws CMSItemNotFoundException
	{
		return saveItem(itemMap);
	}


	@Override
	public Map<String, Object> updateItem(final String uuid, final Map<String, Object> itemMap) throws CMSItemNotFoundException
	{
		initialUpdateValidation(uuid, itemMap);

		return saveItem(itemMap);
	}

	/**
	 * Checks if an item exists. If the item exists, then stores it in the local session for further validation.
	 * @param uuid
	 * 			the item unique identifier
	 * @param itemMap
	 * 			the itemMap representation of the item model.
	 * @throws CMSItemNotFoundException when the item does not exist.
	 */
	protected void initialUpdateValidation(final String uuid, final Map<String, Object> itemMap) throws CMSItemNotFoundException
	{
		// checks if the item exists
		getUniqueItemIdentifierService() //
		.getItemModel(uuid, CMSItemModel.class) //
		.orElseThrow(() -> createCMSItemNotFoundException(uuid));

		if (!StringUtils.equals(uuid, (String) itemMap.get(FIELD_UUID)))
		{
			throw new CMSItemNotFoundException("Inconsistent CMS Item [" + uuid + "] - ["
					+ itemMap.get(FIELD_UUID) + "].");
		}
	}

	/**
	 * Saves Item performing task in local transaction.
	 * @param itemMap the itemMap to be saved
	 * @return the item Map representation after saving.
	 * @throws CMSItemNotFoundException
	 */
	protected Map<String, Object> saveItem(final Map<String, Object> itemMap) throws CMSItemNotFoundException
	{
		return getSessionSearchRestrictionsDisabler().execute(() -> {
			try
			{
				setCatalogInSession(itemMap);
				setCloneContext(itemMap);
				return new TransactionTemplate(getTransactionManager()).execute(status -> {
					final ItemModel itemModel = convertAndPopulate(itemMap);
					getModelService().saveAll();
					return convertAndPopulate((CMSItemModel) itemModel);
				});
			}
			catch (final CMSItemNotFoundException e)
			{
				throw new RuntimeException(e);
			}
			catch (final ModelSavingException e)
			{
				LOG.info("Failed to save the item model", e);
				try
				{
					getValidationErrorsProvider().initializeValidationErrors();
					transformValidationException(e);
					throw new ValidationException(getValidationErrorsProvider().getCurrentValidationErrors());
				}
				finally
				{
					getValidationErrorsProvider().finalizeValidationErrors();
				}
			}
		});
	}

	/**
	 * Parses the ModelSavingException and transforms it into a validation error.
	 * @param e the ModelSavingException
	 */
	protected void transformValidationException(final ModelSavingException e)
	{
		if (e.getMessage() != null)
		{
			final Pattern pattern = Pattern.compile(MODEL_SAVING_EXCEPTION_REGEX);
			final Matcher matcher = pattern.matcher(e.getMessage());
			final String qualifier = matcher.find() ? matcher.group().replaceAll("\\[", "").replaceAll("\\]", "") : null;
			getValidationErrorsProvider().getCurrentValidationErrors().add(
					newValidationErrorBuilder() //
					.field(qualifier) //
					.errorCode(FIELD_REQUIRED) //
					.exceptionMessage(e.getMessage()) //
					.build());
		}
	}


	/**
	 * Converts and populates model to save
	 * @param itemMap the Map representing the ItemModel to be converted and saved
	 * @return the model ready to be saved
	 */
	protected ItemModel convertAndPopulate(final Map<String, Object> itemMap)
	{
		final ItemModel itemModel = getCmsItemConverter().convert(itemMap);

		getItemTypePopulatorProvider().getItemTypePopulator(itemModel.getItemtype()) //
		.ifPresent(populator -> populator.populate(itemMap, itemModel));

		return itemModel;
	}

	/**
	 * Converts and populates Map to return to the frontend.
	 * @param itemModel the itemModel to be converted to the Map.
	 * @return the itemMap ready to be consumed by frontend.
	 */
	protected Map<String, Object> convertAndPopulate(final CMSItemModel itemModel)
	{
		final Map<String, Object> itemMap = getCmsItemConverter().convert(itemModel);
		getItemDataPopulatorProvider().getItemDataPopulators(itemModel).forEach(populator ->
		populator.populate(itemModel, itemMap));
		return itemMap;
	}

	@Override
	public void deleteCMSItemByUuid(final String uuid) throws CMSItemNotFoundException
	{
		final ItemModel cmsItem = getUniqueItemIdentifierService() //
				.getItemModel(uuid, CMSItemModel.class) //
				.orElseThrow(() -> createCMSItemNotFoundException(uuid));

		getModelService().remove(cmsItem);
	}

	@Override
	public Map<String, Object> validateItemForUpdate(final String uuid, final Map<String, Object> itemMap) throws CMSItemNotFoundException
	{
		initialUpdateValidation(uuid, itemMap);
		return validateItem(itemMap);
	}

	@Override
	public Map<String, Object> validateItemForCreate(final Map<String, Object> itemMap) throws CMSItemNotFoundException
	{
		return validateItem(itemMap);
	}

	/**
	 * Thread safe temporary storage of a convertedItem just before explicitly rollbacking the transaction in dryRun mode
	 */
	protected ThreadLocal<Map<String, Object>> convertedItem = new ThreadLocal<>();


	/**
	 * Validates and convert the item for the sole purpose of validation. The transaction will be rolled back at the end.
	 * @param itemMap the item model representation as a map
	 * @return the converted item model into its representation after validation and conversion.
	 * @throws CMSItemNotFoundException when any item in its map does not exist.
	 */
	@SuppressWarnings("squid:S1166")
	protected Map<String, Object> validateItem(final Map<String, Object> itemMap)
	{
		final Boolean uuidProvided = itemMap.containsKey(CmsfacadesConstants.FIELD_UUID);
		final Boolean uidProvided = itemMap.containsKey(CmsfacadesConstants.FIELD_UID);
		return getSessionSearchRestrictionsDisabler().execute(() -> {
			try
			{
				setCatalogInSession(itemMap);
				setCloneContext(itemMap);
				new TransactionTemplate(getTransactionManager()).execute(status -> {
					final ItemModel item = convertAndPopulate(itemMap);
					getModelService().saveAll();
					getModelService().refresh(item);
					convertedItem.set(convertAndPopulate((CMSItemModel) item));
					throw new RequiredRollbackException();
				});

			}
			catch (final CMSItemNotFoundException e)
			{
				throw new RuntimeException(e);
			}
			catch (final ModelSavingException e)
			{
				LOG.info("Failed to save the item model", e);
				try
				{
					getValidationErrorsProvider().initializeValidationErrors();
					transformValidationException(e);
					throw new ValidationException(getValidationErrorsProvider().getCurrentValidationErrors());
				}
				finally
				{
					getValidationErrorsProvider().finalizeValidationErrors();
				}
			}
			catch (final RequiredRollbackException e)
			{
				LOG.debug("required rollback in validation mode");
			}
			finally
			{
				if (convertedItem.get() != null)
				{
					if (!uuidProvided)
					{
						convertedItem.get().remove(CmsfacadesConstants.FIELD_UUID);
					}
					if (!uidProvided)
					{
						convertedItem.get().remove(CmsfacadesConstants.FIELD_UID);
					}
				}
				getModelService().detachAll();
			}

			return convertedItem.get();
		});
	}


	/**
	 * Creates a new {@link CMSItemNotFoundException}.
	 * @param uuid The string representing the UUID of the item not found.
	 * @return the new exception.
	 */
	protected CMSItemNotFoundException createCMSItemNotFoundException(final String uuid)
	{
		return new CMSItemNotFoundException("CMS Item [" + uuid + "] does not exist.");
	}

	protected CMSItemConverter getCmsItemConverter()
	{
		return cmsItemConverter;
	}

	@Required
	public void setCmsItemConverter(final CMSItemConverter cmsItemConverter)
	{
		this.cmsItemConverter = cmsItemConverter;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
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

	/**
	 * Sets the catalogVersion in the current session.
	 * @param source
	 * @throws CMSItemNotFoundException
	 */
	protected void setCatalogInSession(final Map<String, Object> source) throws CMSItemNotFoundException
	{
		if (source == null)
		{
			return;
		}
		final String catalogVersionUUID = (String) source.get(FIELD_CATALOG_VERSION);
		final Optional<CatalogVersionModel> catalogVersionOpt = //
				getUniqueItemIdentifierService().getItemModel(catalogVersionUUID, CatalogVersionModel.class);
		if (catalogVersionOpt.isPresent())
		{
			final CatalogVersionModel catalogVersion = catalogVersionOpt.get();
			getCmsAdminSiteService().setActiveCatalogVersion(catalogVersion.getCatalog().getId(), catalogVersion.getVersion());
			getCatalogVersionService().setSessionCatalogVersion(catalogVersion.getCatalog().getId(), catalogVersion.getVersion());
		}
	}

	/**
	 * Sets the clone context in the current session.
	 * The clone context contains information about original catalog version.
	 * @param source
	 */
	protected void setCloneContext(final Map<String, Object> source)
	{
		final Map<String, String> cloneContext = (HashMap<String, String>) source.get(FIELD_URI_CONTEXT);
		getCmsAdminSiteService().setCloneContext(cloneContext);
	}

	protected ItemTypePopulatorProvider getItemTypePopulatorProvider()
	{
		return itemTypePopulatorProvider;
	}

	@Required
	public void setItemTypePopulatorProvider(final ItemTypePopulatorProvider itemTypePopulatorProvider)
	{
		this.itemTypePopulatorProvider = itemTypePopulatorProvider;
	}

	protected CMSItemSearchService getCmsItemSearchService()
	{
		return cmsItemSearchService;
	}

	@Required
	public void setCmsItemSearchService(final CMSItemSearchService cmsItemSearchService)
	{
		this.cmsItemSearchService = cmsItemSearchService;
	}

	protected Validator getCmsItemSearchDataValidator()
	{
		return cmsItemSearchDataValidator;
	}

	@Required
	public void setCmsItemSearchDataValidator(final Validator cmsItemSearchDataValidator)
	{
		this.cmsItemSearchDataValidator = cmsItemSearchDataValidator;
	}

	protected FacadeValidationService getFacadeValidationService()
	{
		return facadeValidationService;
	}

	@Required
	public void setFacadeValidationService(final FacadeValidationService facadeValidationService)
	{
		this.facadeValidationService = facadeValidationService;
	}

	protected Converter<CMSItemSearchData, de.hybris.platform.cms2.data.CMSItemSearchData> getCmsItemSearchDataConverter()
	{
		return cmsItemSearchDataConverter;
	}

	@Required
	public void setCmsItemSearchDataConverter(
			final Converter<CMSItemSearchData, de.hybris.platform.cms2.data.CMSItemSearchData> cmsItemSearchDataConverter)
	{
		this.cmsItemSearchDataConverter = cmsItemSearchDataConverter;
	}

	protected ValidationErrorsProvider getValidationErrorsProvider()
	{
		return validationErrorsProvider;
	}

	@Required
	public void setValidationErrorsProvider(final ValidationErrorsProvider validationErrorsProvider)
	{
		this.validationErrorsProvider = validationErrorsProvider;
	}

	protected PlatformTransactionManager getTransactionManager()
	{
		return transactionManager;
	}

	@Required
	public void setTransactionManager(final PlatformTransactionManager transactionManager)
	{
		this.transactionManager = transactionManager;
	}
	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService) {
		this.catalogVersionService = catalogVersionService;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCmsAdminSiteService(final CMSAdminSiteService cmsAdminSiteService)
	{
		this.cmsAdminSiteService = cmsAdminSiteService;
	}

	protected CMSAdminSiteService getCmsAdminSiteService()
	{
		return cmsAdminSiteService;
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	public SessionSearchRestrictionsDisabler getSessionSearchRestrictionsDisabler()
	{
		return sessionSearchRestrictionsDisabler;
	}

	@Required
	public void setSessionSearchRestrictionsDisabler(final SessionSearchRestrictionsDisabler sessionSearchRestrictionsDisabler)
	{
		this.sessionSearchRestrictionsDisabler = sessionSearchRestrictionsDisabler;
	}

	protected OriginalClonedItemProvider getOriginalClonedItemProvider()
	{
		return originalClonedItemProvider;
	}

	@Required
	public void setOriginalClonedItemProvider(final OriginalClonedItemProvider originalClonedItemProvider)
	{
		this.originalClonedItemProvider = originalClonedItemProvider;
	}

	protected ItemDataPopulatorProvider getItemDataPopulatorProvider()
	{
		return itemDataPopulatorProvider;
	}

	@Required
	public void setItemDataPopulatorProvider(final ItemDataPopulatorProvider itemDataPopulatorProvider)
	{
		this.itemDataPopulatorProvider = itemDataPopulatorProvider;
	}
}
