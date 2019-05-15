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
package de.hybris.platform.cmsfacades.items.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.common.service.SearchResultConverter;
import de.hybris.platform.cmsfacades.common.validator.FacadeValidationService;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.cmsfacades.dto.UpdateComponentValidationDto;
import de.hybris.platform.cmsfacades.items.ComponentItemFacade;
import de.hybris.platform.cmsfacades.items.converter.CmsComponentConverterFactory;
import de.hybris.platform.cmsfacades.items.populator.ComponentDataPopulatorFactory;
import de.hybris.platform.cmsfacades.rendering.ComponentRenderingService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Validator;

import com.google.common.base.Strings;


/**
 * Default implementation of {@link ComponentItemFacade}.
 */
public class DefaultComponentItemFacade implements ComponentItemFacade
{
	private final String NEW_KEYWORD = "New";
	private final String SPACE = " ";

	private CMSAdminComponentService componentAdminService;
	private CMSAdminContentSlotService contentSlotAdminService;
	private CMSAdminSiteService adminSiteService;
	private ComponentRenderingService componentRenderingService;
	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	private CmsComponentConverterFactory cmsComponentConverterFactory;
	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	private ComponentDataPopulatorFactory cmsComponentPopulatorFactory;
	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	private AbstractPopulatingConverter<AbstractCMSComponentModel, AbstractCMSComponentData> basicCMSComponentModelConverter;
	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	private Comparator<AbstractCMSComponentData> cmsItemComparator;
	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	private Validator createComponentValidator;
	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	private Validator updateComponentValidator;
	private FacadeValidationService facadeValidationService;
	private ModelService modelService;
	private SearchResultConverter cmsSearchResultConverter;
	private SessionSearchRestrictionsDisabler sessionSearchRestrictionsDisabler;

	@Override
	public AbstractCMSComponentData getComponentById(final String componentId, final String categoryCode, final String productCode,
			final String catalogCode) throws CMSItemNotFoundException
	{
		return getComponentRenderingService().getComponentById(componentId, categoryCode, productCode, catalogCode);
	}

	@Override
	public SearchPageData<AbstractCMSComponentData> getComponentsByIds(final Collection<String> componentIds,
			final String categoryCode, final String productCode, final String catalogCode, final SearchPageData searchPageData)
	{
		return getComponentRenderingService().getComponentsByIds(componentIds, categoryCode, productCode, catalogCode,
				searchPageData);
	}

	@Override
	public AbstractCMSComponentData addComponentItem(final AbstractCMSComponentData component)
	{
		if (Strings.isNullOrEmpty(component.getUid()))
		{
			component.setUid(getComponentAdminService().generateCmsComponentUid());
		}

		if (Strings.isNullOrEmpty(component.getName()) && !Strings.isNullOrEmpty(component.getTypeCode()))
		{
			component.setName(generateComponentName(component.getTypeCode()));
		}

		getFacadeValidationService().validate(getCreateComponentValidator(), component);

		return getSessionSearchRestrictionsDisabler().execute(createComponentAndAssignToSlot(component));
	}

	/**
	 * Populates the component model from the component dto and saves the model using the {@link ModelService}
	 *
	 * @param component
	 *           - the component model to be created
	 * @return a {@link AbstractCMSComponentData} dto
	 */
	protected Supplier<AbstractCMSComponentData> createComponentAndAssignToSlot(final AbstractCMSComponentData component)
	{
		return () -> {
			ContentSlotModel contentSlotModel = null;
			if (!Strings.isNullOrEmpty(component.getSlotId()))
			{
				contentSlotModel = getContentSlotAdminService().getContentSlotForId(component.getSlotId());
			}

			final AbstractCMSComponentModel model = getComponentAdminService().createCmsComponent(contentSlotModel,
					component.getUid(), component.getName(), component.getTypeCode());

			if (!Objects.isNull(contentSlotModel))
			{
				getContentSlotAdminService().addCMSComponentToContentSlot(model, contentSlotModel, component.getPosition());
			}

			populateComponentModel(component, model);
			getModelService().saveAll();

			return getAbstractCMSComponentDataResponse(component, model);
		};
	}

	/**
	 * Converts the {@link AbstractCMSComponentModel} model object to {@link AbstractCMSComponentData} data object
	 * response.
	 *
	 * @param componentData
	 *           the Data object that contains information about the current slot, the current page and position.
	 * @param model
	 *           the object model to be converted.
	 * @return the {@link AbstractCMSComponentData} response object.
	 */
	protected AbstractCMSComponentData getAbstractCMSComponentDataResponse(final AbstractCMSComponentData componentData,
			final AbstractCMSComponentModel model)
	{
		final Class<? extends AbstractCMSComponentModel> aClass = model.getClass();

		final AbstractPopulatingConverter<AbstractCMSComponentModel, AbstractCMSComponentData> converter = getCmsComponentConverterFactory()
				.getConverter(aClass).orElseThrow(() -> new ConversionException(
						String.format("Converter not found for CMS Component Model [%s]", aClass.getName())));

		final AbstractCMSComponentData response = converter.convert(model);
		response.setSlotId(componentData.getSlotId());
		response.setPageId(componentData.getPageId());
		response.setPosition(componentData.getPosition());

		return response;
	}

	@Override
	public void removeComponentItem(final String componentUid) throws CMSItemNotFoundException
	{

		final AbstractCMSComponentModel componentModel;
		try
		{
			componentModel = getComponentAdminService().getCMSComponentForId(componentUid);
		}
		catch (final UnknownIdentifierException | AmbiguousIdentifierException ex)
		{
			throw new CMSItemNotFoundException("CMSComponent with id [" + componentUid + "] not found.", ex);
		}

		getModelService().remove(componentModel); // ModelRemovalException mapped in errors-config-spring.xml
	}

	@Override
	public List<AbstractCMSComponentData> getAllComponentItems()
	{
		final CatalogVersionModel catalogVersionModel = getAdminSiteService().getActiveCatalogVersion();
		final List<AbstractCMSComponentModel> allCMSComponentsForCatalogVersion = getComponentAdminService()
				.getAllCMSComponentsForCatalogVersion(catalogVersionModel);

		return allCMSComponentsForCatalogVersion.stream().map(model -> getBasicCMSComponentModelConverter().convert(model))
				.sorted(getCmsItemComparator()).collect(Collectors.toList());
	}

	@Override
	public SearchResult<AbstractCMSComponentData> findComponentByMask(final String mask, final PageableData pageableData)
	{
		final CatalogVersionModel catalogVersionModel = getAdminSiteService().getActiveCatalogVersion();
		final SearchResult<AbstractCMSComponentModel> pagedModels = getComponentAdminService()
				.findByCatalogVersionAndMask(catalogVersionModel, mask, pageableData);

		final Function<AbstractCMSComponentModel, AbstractCMSComponentData> convertFunction = //
				model -> getBasicCMSComponentModelConverter().convert(model);
		return getCmsSearchResultConverter().convert(pagedModels, convertFunction);
	}

	/*
	 * Suppress sonar warning (squid:S1166 | Exception handlers should preserve the original exceptions) : It is
	 * perfectly acceptable not to handle "e" here
	 */
	@SuppressWarnings("squid:S1166")
	@Override
	public AbstractCMSComponentData getComponentItemByUid(final String uid) throws CMSItemNotFoundException
	{
		final Supplier<Optional<AbstractCMSComponentData>> supplier = () -> {
			final AbstractCMSComponentModel abstractCMSComponentModel;
			try
			{
				abstractCMSComponentModel = getComponentAdminService().getCMSComponentForId(uid);
			}
			catch (AmbiguousIdentifierException | UnknownIdentifierException e)
			{
				//CMSItemNotFoundException
				return Optional.empty();
			}

			final Class<? extends AbstractCMSComponentModel> aClass = abstractCMSComponentModel.getClass();
			final AbstractPopulatingConverter<AbstractCMSComponentModel, AbstractCMSComponentData> converter = getCmsComponentConverterFactory()
					.getConverter(aClass).orElseThrow(() -> new ConversionException(
							String.format("Converter not found for CMS Component Model [%s]", aClass.getName())));

			final AbstractCMSComponentData abstractCMSComponentData = converter.convert(abstractCMSComponentModel);
			return Optional.ofNullable(abstractCMSComponentData);
		};
		return getSessionSearchRestrictionsDisabler().execute(supplier)
				.orElseThrow(() -> new CMSItemNotFoundException("Component not found for %s" + uid));
	}

	@Override
	public void updateComponentItem(final String componentUid, final AbstractCMSComponentData componentData)
			throws CMSItemNotFoundException
	{

		getFacadeValidationService().validate(getUpdateComponentValidator(),
				buildUpdateComponentValidationDto(componentUid, componentData), componentData);

		AbstractCMSComponentModel componentModel;
		try
		{
			componentModel = getComponentAdminService().getCMSComponentForId(componentUid);
		}
		catch (final UnknownIdentifierException | AmbiguousIdentifierException ex)
		{
			throw new CMSItemNotFoundException("CMSComponent with id [" + componentUid + "] not found.", ex);
		}

		populateComponentModel(componentData, componentModel);

		getModelService().saveAll(); // ModelSavingException mapped in errors-config-spring.xml
	}

	/**
	 * Populates the Component Model, preparing it to be saved.
	 *
	 * @param componentData
	 *           the {@link AbstractCMSComponentData} source data
	 * @param componentModel
	 *           the {@link AbstractCMSComponentModel} target model
	 *
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	protected void populateComponentModel(final AbstractCMSComponentData componentData,
			final AbstractCMSComponentModel componentModel)
	{
		final Class<? extends AbstractCMSComponentModel> aClass = componentModel.getClass();

		final Populator<AbstractCMSComponentData, AbstractCMSComponentModel> populator = getCmsComponentPopulatorFactory()
				.getPopulator(aClass)
				.orElseThrow(() -> new ConversionException("No populator found for [" + aClass.getName() + "]."));

		populator.populate(componentData, componentModel);
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	protected String generateComponentName(final String typeCode)
	{
		return NEW_KEYWORD + SPACE + String.join(SPACE, StringUtils.splitByCharacterTypeCamelCase(typeCode));
	}

	/**
	 * Build a DTO for validating a component to be updated.
	 *
	 * @param originalUid
	 * @param component
	 * @return the validation DTO
	 *
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	protected UpdateComponentValidationDto buildUpdateComponentValidationDto(final String originalUid,
			final AbstractCMSComponentData component)
	{
		final UpdateComponentValidationDto dto = new UpdateComponentValidationDto();
		dto.setOriginalUid(originalUid);
		dto.setComponent(component);
		return dto;
	}

	protected CMSAdminComponentService getComponentAdminService()
	{
		return componentAdminService;
	}

	@Required
	public void setComponentAdminService(final CMSAdminComponentService componentAdminService)
	{
		this.componentAdminService = componentAdminService;
	}

	protected CMSAdminContentSlotService getContentSlotAdminService()
	{
		return contentSlotAdminService;
	}

	@Required
	public void setContentSlotAdminService(final CMSAdminContentSlotService contentSlotAdminService)
	{
		this.contentSlotAdminService = contentSlotAdminService;
	}

	protected CMSAdminSiteService getAdminSiteService()
	{
		return adminSiteService;
	}

	@Required
	public void setAdminSiteService(final CMSAdminSiteService adminSiteService)
	{
		this.adminSiteService = adminSiteService;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setCmsComponentConverterFactory(final CmsComponentConverterFactory cmsComponentConverterFactory)
	{
		this.cmsComponentConverterFactory = cmsComponentConverterFactory;
	}

	protected CmsComponentConverterFactory getCmsComponentConverterFactory()
	{
		return cmsComponentConverterFactory;
	}

	protected ComponentDataPopulatorFactory getCmsComponentPopulatorFactory()
	{
		return cmsComponentPopulatorFactory;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setCmsComponentPopulatorFactory(final ComponentDataPopulatorFactory cmsComponentPopulatorFactory)
	{
		this.cmsComponentPopulatorFactory = cmsComponentPopulatorFactory;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setBasicCMSComponentModelConverter(
			final AbstractPopulatingConverter<AbstractCMSComponentModel, AbstractCMSComponentData> basicCMSComponentModelConverter)
	{
		this.basicCMSComponentModelConverter = basicCMSComponentModelConverter;
	}

	protected AbstractPopulatingConverter<AbstractCMSComponentModel, AbstractCMSComponentData> getBasicCMSComponentModelConverter()
	{
		return basicCMSComponentModelConverter;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setCmsItemComparator(final Comparator<AbstractCMSComponentData> cmsItemComparator)
	{
		this.cmsItemComparator = cmsItemComparator;
	}

	protected Comparator<AbstractCMSComponentData> getCmsItemComparator()
	{
		return cmsItemComparator;
	}

	protected Validator getCreateComponentValidator()
	{
		return createComponentValidator;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setCreateComponentValidator(final Validator createComponentValidator)
	{
		this.createComponentValidator = createComponentValidator;
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

	protected Validator getUpdateComponentValidator()
	{
		return updateComponentValidator;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setUpdateComponentValidator(final Validator updateComponentValidator)
	{
		this.updateComponentValidator = updateComponentValidator;
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

	protected SearchResultConverter getCmsSearchResultConverter()
	{
		return cmsSearchResultConverter;
	}

	@Required
	public void setCmsSearchResultConverter(final SearchResultConverter cmsSearchResultConverter)
	{
		this.cmsSearchResultConverter = cmsSearchResultConverter;
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

	protected ComponentRenderingService getComponentRenderingService()
	{
		return componentRenderingService;
	}

	@Required
	public void setComponentRenderingService(final ComponentRenderingService componentRenderingService)
	{
		this.componentRenderingService = componentRenderingService;
	}
}
