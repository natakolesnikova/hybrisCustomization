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
package de.hybris.platform.cmsfacades.pages.impl;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.pages.service.CMSPageSearchService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminRestrictionService;
import de.hybris.platform.cmsfacades.common.validator.CompositeValidator;
import de.hybris.platform.cmsfacades.common.validator.FacadeValidationService;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsfacades.data.PageTypeData;
import de.hybris.platform.cmsfacades.dto.UpdatePageValidationDto;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.pages.PageFacade;
import de.hybris.platform.cmsfacades.pages.service.PageInitializer;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolver;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolverTypeRegistry;
import de.hybris.platform.cmsfacades.rendering.PageRenderingService;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;


/**
 * Default implementation of {@link PageFacade}.
 */
public class DefaultPageFacade implements PageFacade
{
	public static final String DEFAULT_UID_PREFIX = "page";

	private String uidPrefix = DEFAULT_UID_PREFIX;
	private CMSAdminPageService adminPageService;
	private CMSAdminRestrictionService adminRestrictionService;
	/**
	 * @deprecated since 6.6, please use {@link de.hybris.platform.cms2.cmsitems.service.CMSItemSearchService} instead
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	private CMSPageSearchService pageSearchService;
	private PageInitializer pageInitializer;
	private FacadeValidationService facadeValidationService;
	private ModelService modelService;
	private TypeService typeService;
	private PageRenderingService pageRenderingService;

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	private Map<Class<?>, CompositeValidator> cmsCreatePageValidatorFactory;
	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	private Map<Class<?>, CompositeValidator> cmsUpdatePageValidatorFactory;
	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	private Map<Class<?>, AbstractPopulatingConverter<AbstractPageData, AbstractPageModel>> pageDataPopulatorFactory;
	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	private Map<Class<?>, AbstractPopulatingConverter<AbstractPageModel, AbstractPageData>> pageModelConverterFactory;

	private PageVariationResolverTypeRegistry pageVariationResolverTypeRegistry;

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	private Converter<CMSPageTypeModel, PageTypeData> pageTypeModelConverter;
	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	private Comparator<AbstractPageData> cmsPageComparator;

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	private Validator cmsFindVariationPageValidator;
	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	private KeyGenerator keyGenerator;
	private UniqueItemIdentifierService uniqueItemIdentifierService;
	private Map<String, List<String>> cmsItemSearchTypeBlacklistMap;

	/**
	 * @deprecated since 6.6, please use #cmsItemSearchTypeBlacklistMap instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	private Set<Class<?>> cmsSupportedPages;

	@Override
	public AbstractPageData getPageData(String pageType, String pageLabelOrId, String code) throws CMSItemNotFoundException
	{
		return getPageRenderingService().getPageRenderingData(pageType, pageLabelOrId, code);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Override
	public List<AbstractPageData> findAllPages()
	{
		final List<String> blacklistedPageTypes = getCmsItemSearchTypeBlacklistMap().get(AbstractPageModel._TYPECODE);
		final Predicate<AbstractPageModel> isNotBlacklistedPageType = pageModel -> {
			return !blacklistedPageTypes.contains(pageModel.getItemtype());
		};

		return getAdminPageService().getAllPages().stream().filter(isNotBlacklistedPageType)
				.filter(model -> getPageModelConverterFactory().containsKey(model.getClass()))
				.map(model -> getPageModelConverter(model.getClass()).convert(model)).sorted(getCmsPageComparator())
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Override
	public List<PageTypeData> findAllPageTypes()
	{
		final List<String> blacklistedPageTypes = getCmsItemSearchTypeBlacklistMap().get(AbstractPageModel._TYPECODE);
		final Predicate<CMSPageTypeModel> isNotBlacklistedPageType = pageTypeModel -> {
			return !blacklistedPageTypes.contains(pageTypeModel.getCode());
		};

		return getAdminPageService().getAllPageTypes().stream().filter(isNotBlacklistedPageType) //
				.map(pageType -> getPageTypeModelConverter().convert(pageType)) //
				.collect(Collectors.toList());
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Override
	public SearchResult<AbstractPageData> findPagesByMaskAndTypeCode(final String mask, final String typeCode,
			final PageableData pageableData)
	{
		final SearchResult<AbstractPageModel> pageSearchResult = getPageSearchService().findPages(mask, typeCode, pageableData);
		return new SearchResultImpl<>(
				pageSearchResult.getResult().stream() //
				.map(model -> getPageModelConverter(model.getClass()).convert(model)).collect(Collectors.toList()), //
				pageSearchResult.getTotalCount(), //
				pageSearchResult.getRequestedCount(), //
				pageSearchResult.getRequestedStart());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Override
	public List<AbstractPageData> findPagesByType(final String typeCode, final Boolean isDefaultPage)
	{
		final AbstractPageData pageData = new AbstractPageData();
		pageData.setTypeCode(typeCode);
		pageData.setDefaultPage(isDefaultPage);

		getFacadeValidationService().validate(getCmsFindVariationPageValidator(), pageData);

		return getPageVariationResolver(typeCode).findPagesByType(typeCode, isDefaultPage).stream() //
				.map(model -> {
					final AbstractPageData abstractPageData = getPageModelConverter(model.getClass()).convert(model);
					getUniqueItemIdentifierService().getItemData(model).ifPresent(itemData -> {
						abstractPageData.setUuid(itemData.getItemId());
					});
					return abstractPageData;
				}).sorted(getCmsPageComparator()).collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> findVariationPages(final String pageId) throws CMSItemNotFoundException
	{
		final AbstractPageModel page = getPageModelById(pageId);
		return getPageVariationResolver(page.getItemtype()).findVariationPages(page).stream().map(pageData -> pageData.getUid())
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> findFallbackPages(final String pageId) throws CMSItemNotFoundException
	{
		final AbstractPageModel page = getPageModelById(pageId);
		return getPageVariationResolver(page.getItemtype()).findDefaultPages(page).stream().map(pageData -> pageData.getUid())
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Override
	public AbstractPageData getPageByUid(final String uid) throws CMSItemNotFoundException
	{
		final AbstractPageModel pageModel = getPageModelById(uid);
		return getPageModelConverter(pageModel.getClass()).convert(pageModel);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Override
	@Transactional
	public AbstractPageData createPage(final AbstractPageData pageData) throws ConversionException
	{
		pageData.setUid(generateUID(pageData.getUid()));

		getFacadeValidationService().validate(getCreatePageDataValidator(pageData.getClass()), pageData);

		final AbstractPageModel pageModel = getPageDataConverter(pageData.getClass()).convert(pageData);

		getModelService().save(pageModel);
		getPageInitializer().initialize(pageModel);

		return getPageModelConverter(pageModel.getClass()).convert(pageModel);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Override
	public AbstractPageData updatePage(final String pageId, final AbstractPageData pageData) throws ValidationException
	{
		getFacadeValidationService().validate(getUpdatePageDataValidator(pageData.getClass()),
				buildUpdateComponentValidationDto(pageId, pageData), pageData);

		final AbstractPageModel pageModel = getAdminPageService().getPageForIdFromActiveCatalogVersion(pageId);
		getPageDataConverter(pageData.getClass()).populate(pageData, pageModel);
		getModelService().save(pageModel);

		return getPageModelConverter(pageModel.getClass()).convert(pageModel);
	}

	/**
	 * Generates and returns a new uid when the given UID is null or empty. Otherwise, returns the given UID.
	 *
	 * @param uid
	 *           the uid passed as parameter (can be null)
	 *
	 * @return the created {@link String} uid
	 *
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	protected String generateUID(final String uid)
	{
		String generatedUid = uid;

		if (StringUtils.isBlank(uid))
		{
			generatedUid = StringUtils.isNotBlank(getUidPrefix())
					? String.format("%s-%s", getUidPrefix(), getKeyGenerator().generate().toString())
							: getKeyGenerator().generate().toString();
		}
		return generatedUid;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	protected UpdatePageValidationDto buildUpdateComponentValidationDto(final String originalUid, final AbstractPageData page)
	{
		final UpdatePageValidationDto dto = new UpdatePageValidationDto();
		dto.setOriginalUid(originalUid);
		dto.setPage(page);
		return dto;
	}

	protected AbstractPageModel getPageModelById(final String pageId) throws CMSItemNotFoundException
	{
		try
		{
			return getAdminPageService().getPageForIdFromActiveCatalogVersion(pageId);
		}
		catch (UnknownIdentifierException | AmbiguousIdentifierException e)
		{
			throw new CMSItemNotFoundException("Cannot find page with uid [" + pageId + "].", e);
		}
	}

	protected PageVariationResolver<AbstractPageModel> getPageVariationResolver(final String typeCode)
	{
		return getPageVariationResolverTypeRegistry().getPageVariationResolverType(typeCode).get().getResolver();
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	protected Validator getCreatePageDataValidator(final Class<?> pageClass)
	{
		return Optional
				.ofNullable(Optional.ofNullable(getCmsCreatePageValidatorFactory().get(pageClass))
						.orElseGet(() -> getCmsCreatePageValidatorFactory().get(AbstractPageData.class)))
				.orElseThrow(() -> new IllegalArgumentException("The validator is required and must not be null."));
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	protected Validator getUpdatePageDataValidator(final Class<?> pageClass)
	{
		return Optional
				.ofNullable(Optional.ofNullable(getCmsUpdatePageValidatorFactory().get(pageClass))
						.orElseGet(() -> getCmsUpdatePageValidatorFactory().get(AbstractPageData.class)))
				.orElseThrow(() -> new IllegalArgumentException("The validator is required and must not be null."));
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	protected AbstractPopulatingConverter<AbstractPageData, AbstractPageModel> getPageDataConverter(final Class<?> pageClass)
	{
		return getPageDataPopulatorFactory().computeIfAbsent(pageClass, k -> {
			throw new ConversionException(String.format("Converter not found for CMS Page Data [%s]", pageClass.getName()));
		});
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	protected AbstractPopulatingConverter<AbstractPageModel, AbstractPageData> getPageModelConverter(final Class<?> pageClass)
	{
		return getPageModelConverterFactory().computeIfAbsent(pageClass, k -> {
			throw new ConversionException(String.format("Converter not found for CMS Page Model [%s]", pageClass.getName()));
		});
	}

	public CMSAdminPageService getAdminPageService()
	{
		return adminPageService;
	}

	@Required
	public void setAdminPageService(final CMSAdminPageService adminPageService)
	{
		this.adminPageService = adminPageService;
	}

	protected CMSPageSearchService getPageSearchService()
	{
		return pageSearchService;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setPageSearchService(final CMSPageSearchService pageSearchService)
	{
		this.pageSearchService = pageSearchService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
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

	protected Map<Class<?>, AbstractPopulatingConverter<AbstractPageData, AbstractPageModel>> getPageDataPopulatorFactory()
	{
		return pageDataPopulatorFactory;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setPageDataPopulatorFactory(
			final Map<Class<?>, AbstractPopulatingConverter<AbstractPageData, AbstractPageModel>> pageDataPopulatorFactory)
	{
		this.pageDataPopulatorFactory = pageDataPopulatorFactory;
	}

	protected Map<Class<?>, AbstractPopulatingConverter<AbstractPageModel, AbstractPageData>> getPageModelConverterFactory()
	{
		return pageModelConverterFactory;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setPageModelConverterFactory(
			final Map<Class<?>, AbstractPopulatingConverter<AbstractPageModel, AbstractPageData>> pageModelConverterFactory)
	{
		this.pageModelConverterFactory = pageModelConverterFactory;
	}

	protected String getUidPrefix()
	{
		return uidPrefix;
	}

	public void setUidPrefix(final String uidPrefix)
	{
		this.uidPrefix = uidPrefix;
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setKeyGenerator(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	/**
	 * @deprecated since 6.6, please use {@link #getCmsItemSearchTypeBlacklistMap()} instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	protected Set<Class<?>> getCmsSupportedPages()
	{
		return cmsSupportedPages;
	}

	/**
	 * @param cmsSupportedPages
	 * @deprecated since 6.6, please use {@link #setCmsItemSearchTypeBlacklistMap(Map)} instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setCmsSupportedPages(final Set<Class<?>> cmsSupportedPages)
	{
		this.cmsSupportedPages = cmsSupportedPages;
	}

	protected Converter<CMSPageTypeModel, PageTypeData> getPageTypeModelConverter()
	{
		return pageTypeModelConverter;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setPageTypeModelConverter(final Converter<CMSPageTypeModel, PageTypeData> pageTypeModelConverter)
	{
		this.pageTypeModelConverter = pageTypeModelConverter;
	}

	protected Validator getCmsFindVariationPageValidator()
	{
		return cmsFindVariationPageValidator;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setCmsFindVariationPageValidator(final Validator cmsFindVariationPageValidator)
	{
		this.cmsFindVariationPageValidator = cmsFindVariationPageValidator;
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

	protected CMSAdminRestrictionService getAdminRestrictionService()
	{
		return adminRestrictionService;
	}

	@Required
	public void setAdminRestrictionService(final CMSAdminRestrictionService adminRestrictionService)
	{
		this.adminRestrictionService = adminRestrictionService;
	}

	protected PageVariationResolverTypeRegistry getPageVariationResolverTypeRegistry()
	{
		return pageVariationResolverTypeRegistry;
	}

	@Required
	public void setPageVariationResolverTypeRegistry(final PageVariationResolverTypeRegistry pageVariationResolverTypeRegistry)
	{
		this.pageVariationResolverTypeRegistry = pageVariationResolverTypeRegistry;
	}

	protected Comparator<AbstractPageData> getCmsPageComparator()
	{
		return cmsPageComparator;
	}

	/**
	 * @deprecated since 6.6, please use {@link #getCmsItemSearchTypeBlacklistMap()} instead
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setCmsPageComparator(final Comparator<AbstractPageData> cmsPageComparator)
	{
		this.cmsPageComparator = cmsPageComparator;
	}

	protected Map<Class<?>, CompositeValidator> getCmsCreatePageValidatorFactory()
	{
		return cmsCreatePageValidatorFactory;
	}

	@Required
	public void setCmsCreatePageValidatorFactory(final Map<Class<?>, CompositeValidator> cmsCreatePageValidatorFactory)
	{
		this.cmsCreatePageValidatorFactory = cmsCreatePageValidatorFactory;
	}

	protected Map<Class<?>, CompositeValidator> getCmsUpdatePageValidatorFactory()
	{
		return cmsUpdatePageValidatorFactory;
	}

	@Required
	public void setCmsUpdatePageValidatorFactory(final Map<Class<?>, CompositeValidator> cmsUpdatePageValidatorFactory)
	{
		this.cmsUpdatePageValidatorFactory = cmsUpdatePageValidatorFactory;
	}

	protected PageInitializer getPageInitializer()
	{
		return pageInitializer;
	}

	@Required
	public void setPageInitializer(final PageInitializer pageInitializer)
	{
		this.pageInitializer = pageInitializer;
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

	protected Map<String, List<String>> getCmsItemSearchTypeBlacklistMap()
	{
		return cmsItemSearchTypeBlacklistMap;
	}

	@Required
	public void setCmsItemSearchTypeBlacklistMap(final Map<String, List<String>> cmsItemSearchTypeBlacklistMap)
	{
		this.cmsItemSearchTypeBlacklistMap = cmsItemSearchTypeBlacklistMap;
	}

	protected PageRenderingService getPageRenderingService()
	{
		return pageRenderingService;
	}

	@Required
	public void setPageRenderingService(PageRenderingService pageRenderingService)
	{
		this.pageRenderingService = pageRenderingService;
	}
}
