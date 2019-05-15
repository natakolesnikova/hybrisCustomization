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
package de.hybris.platform.cmsfacades.restrictions.impl;

import static de.hybris.platform.cms2.model.contents.CMSItemModel.NAME;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.exceptions.InvalidNamedQueryException;
import de.hybris.platform.cms2.exceptions.SearchExecutionNamedQueryException;
import de.hybris.platform.cms2.model.RestrictionTypeModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.namedquery.NamedQuery;
import de.hybris.platform.cms2.namedquery.service.NamedQueryService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminRestrictionService;
import de.hybris.platform.cmsfacades.common.service.SearchResultConverter;
import de.hybris.platform.cmsfacades.common.validator.CompositeValidator;
import de.hybris.platform.cmsfacades.common.validator.FacadeValidationService;
import de.hybris.platform.cmsfacades.data.AbstractRestrictionData;
import de.hybris.platform.cmsfacades.data.NamedQueryData;
import de.hybris.platform.cmsfacades.data.RestrictionTypeData;
import de.hybris.platform.cmsfacades.dto.UpdateRestrictionValidationDto;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.restrictions.RestrictionFacade;
import de.hybris.platform.cmsfacades.restrictions.service.RestrictionDataToModelConverter;
import de.hybris.platform.cmsfacades.restrictions.service.RestrictionDataToModelConverterRegistry;
import de.hybris.platform.cmsfacades.restrictions.service.RestrictionModelToDataConverter;
import de.hybris.platform.cmsfacades.restrictions.service.RestrictionModelToDataConverterRegistry;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Validator;


/**
 * Default implementation of the <code>RestrictionFacade</code>.
 *
 * @deprecated since 6.6. Please use {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade} instead.
 */
@HybrisDeprecation(sinceVersion = "6.6")
@Deprecated
public class DefaultRestrictionFacade implements RestrictionFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultRestrictionFacade.class);

	public static final String NAMED_QUERY_RESTRICTION_SEARCH_BY_NAME = "namedQueryRestrictionSearchByName";
	public static final String DEFAULT_UID_PREFIX = "restriction";

	private String uidPrefix = DEFAULT_UID_PREFIX;
	private CMSAdminRestrictionService cmsAdminRestrictionService;
	private FacadeValidationService facadeValidationService;
	private ModelService modelService;
	private PersistentKeyGenerator cmsRestrictionModelUidGenerator;

	private Map<Class<?>, CompositeValidator> cmsCreateRestrictionValidatorFactory;
	private Map<Class<?>, CompositeValidator> cmsUpdateRestrictionValidatorFactory;
	private RestrictionDataToModelConverterRegistry restrictionDataToModelConverterRegistry;
	private RestrictionModelToDataConverterRegistry restrictionModelToDataConverterRegistry;
	private Converter<RestrictionTypeModel, RestrictionTypeData> restrictionTypeModelConverter;
	private Comparator<AbstractRestrictionData> restrictionComparator;

	private NamedQueryService namedQueryService;
	private Validator namedQueryDataValidator;
	private Converter<NamedQueryData, NamedQuery> restrictionNamedQueryConverter;
	private SearchResultConverter cmsSearchResultConverter;

	private SessionSearchRestrictionsDisabler sessionSearchRestrictionsDisabler;

	@Override
	public List<AbstractRestrictionData> findAllRestrictions()
	{
		final Supplier<List<AbstractRestrictionData>> supplier = () -> getCmsAdminRestrictionService().getAllRestrictions().stream() //
				.map(restriction -> getModelToDataConverter(restriction.getItemtype()).convert(restriction)) //
				.sorted(getRestrictionComparator()) //
				.collect(Collectors.toList());
		return getSessionSearchRestrictionsDisabler().execute(supplier);
	}

	@Override
	public List<RestrictionTypeData> findAllRestrictionTypes()
	{
		return getCmsAdminRestrictionService().getAllRestrictionTypes().stream() //
				.map(restrictionType -> getRestrictionTypeModelConverter().convert(restrictionType)) //
				.collect(Collectors.toList());
	}

	@Override
	public AbstractRestrictionData createRestriction(final AbstractRestrictionData restrictionData) throws ValidationException
	{
		generateRestrictionUid(restrictionData);

		final Validator validator = Optional.ofNullable(getCmsCreateRestrictionValidatorFactory().get(restrictionData.getClass()))
				.orElseThrow(() -> new IllegalArgumentException("The validator is required and must not be null."));

		getFacadeValidationService().validate(validator, restrictionData);

		final Supplier<AbstractRestrictionData> supplier = () ->
		{
			final AbstractRestrictionModel restrictionModel = getDataToModelConverter(restrictionData.getClass().getSimpleName())
					.convert(restrictionData);
			getModelService().save(restrictionModel);

			return getModelToDataConverter(restrictionModel.getItemtype()).convert(restrictionModel);
		};
		return getSessionSearchRestrictionsDisabler().execute(supplier);
	}

	/**
	 * Get model to data converter for a given typecode.
	 *
	 * @param typecode
	 *           the type code of the model object
	 * @return model to data converter
	 * @throws ConversionException
	 *            when no converter is found
	 */
	protected AbstractPopulatingConverter<AbstractRestrictionModel, AbstractRestrictionData> getModelToDataConverter(
			final String typecode)
	{
		final RestrictionModelToDataConverter modelToDataConverter = getRestrictionModelToDataConverterRegistry()
				.getRestrictionModelToDataConverter(typecode)//
				.orElseThrow(
						() -> new ConversionException(String.format("Converter not found for CMS Restriction Model [%s]", typecode)));
		return modelToDataConverter.getConverter();
	}

	/**
	 * Get data to model converter for a given typecode.
	 *
	 * @param typecode
	 *           the type code of the data object
	 * @return data to model converter
	 * @throws ConversionException
	 *            when no converter is found
	 */
	protected AbstractPopulatingConverter<AbstractRestrictionData, AbstractRestrictionModel> getDataToModelConverter(
			final String typecode)
	{
		final RestrictionDataToModelConverter dataToModelConverter = getRestrictionDataToModelConverterRegistry()
				.getRestrictionDataToModelConverter(typecode) //
				.orElseThrow(
						() -> new ConversionException(String.format("Converter not found for CMS Restriction Data [%s]", typecode)));
		return dataToModelConverter.getConverter();
	}

	protected void generateRestrictionUid(final AbstractRestrictionData restrictionData)
	{
		if (StringUtils.isBlank(restrictionData.getUid()))
		{
			final String generatedUid = getCmsRestrictionModelUidGenerator().generate().toString();
			final String uid = StringUtils.isNotBlank(getUidPrefix()) ? String.format("%s-%s", getUidPrefix(), generatedUid)
					: generatedUid;
			restrictionData.setUid(uid);
		}
	}

	@Override
	public SearchResult<AbstractRestrictionData> findRestrictionsByMask(final String mask, final NamedQueryData namedQueryData)
	{
		namedQueryData.setNamedQuery(NAMED_QUERY_RESTRICTION_SEARCH_BY_NAME);
		namedQueryData.setQueryType(AbstractRestrictionData.class);
		augmentNamedQueryParams(mask, namedQueryData);
		return findRestrictionsByNamedQuery(namedQueryData);
	}

	/**
	 * Add mask for 'name' to {@link NamedQueryData} <code>params</code>.
	 * <p>
	 * When {@link NamedQueryData#getParams()} defines a value for 'name' (e.g. params=name:hello) and a
	 * <code>mask</code> is also defined (e.g. mask=bye), because the filter is on the name field, the named query data
	 * will ignore the <code>mask</code> value and perform a search for all restrictions with name 'hello'.
	 *
	 * @param mask
	 *           the string value on which restrictions will be filtered, implementations may choose to filter on the
	 *           restriction name. The mask value will be appended to the list of {@link NamedQueryData#getParams()}
	 * @param namedQueryData
	 *           the {@link NamedQueryData} to be augmented with some default values
	 */
	protected void augmentNamedQueryParams(final String mask, final NamedQueryData namedQueryData)
	{
		String params = Objects.isNull(namedQueryData.getParams()) ? "" : namedQueryData.getParams();
		if (!params.contains(NAME))
		{
			if (Strings.isNotBlank(namedQueryData.getParams())) {
				params += ",";
			}
			params += NAME + ":" + (Objects.nonNull(mask) ? mask : "");

		}
		namedQueryData.setParams(params);
	}

	@Override
	public SearchResult<AbstractRestrictionData> findRestrictionsByNamedQuery(final NamedQueryData namedQueryData)
	{
		getFacadeValidationService().validate(getNamedQueryDataValidator(), namedQueryData);

		final Supplier<SearchResult<AbstractRestrictionData>> supplier = () ->
		{
			SearchResult<AbstractRestrictionModel> modelSearchResult = null;
			try
			{
				final NamedQuery namedQuery = getRestrictionNamedQueryConverter().convert(namedQueryData);
				modelSearchResult = getNamedQueryService().getSearchResult(namedQuery);
			}
			catch (ConversionException | InvalidNamedQueryException | SearchExecutionNamedQueryException e)
			{
				LOG.info("Unable to apply named query.", e);
			}
			final Function<AbstractRestrictionModel, AbstractRestrictionData> convertFunction = //
					model -> getModelToDataConverter(model.getItemtype()).convert(model);
					return getCmsSearchResultConverter().convert(modelSearchResult, convertFunction);
		};
		return getSessionSearchRestrictionsDisabler().execute(supplier);
	}

	@Override
	public AbstractRestrictionData findRestrictionById(final String id) throws CMSItemNotFoundException
	{
		final Supplier<Optional<AbstractRestrictionData>> supplier = () ->
		{
			try
			{
				final AbstractRestrictionModel restrictionModel = getCmsAdminRestrictionService().getRestriction(id);
				return Optional.ofNullable(getModelToDataConverter(restrictionModel.getItemtype()).convert(restrictionModel));
			}
			catch (final CMSItemNotFoundException e)
			{
				return Optional.empty();
			}
		};
		return getSessionSearchRestrictionsDisabler().execute(supplier).orElseThrow(
				() -> new CMSItemNotFoundException("Could not find restriction with id [" + id + "] in active catalog version"));
	}

	/*
	 * Suppress sonar warning (squid:S1166 | Exception handlers should preserve the original exceptions) : It is
	 * perfectly acceptable not to handle "e" here
	 */
	@SuppressWarnings("squid:S1166")
	@Override
	public AbstractRestrictionData updateRestriction(final String id, final AbstractRestrictionData restrictionData) throws CMSItemNotFoundException
	{
		final Validator validator = Optional.ofNullable(getCmsUpdateRestrictionValidatorFactory().get(restrictionData.getClass()))
				.orElseThrow(() -> new IllegalArgumentException("The validator is required and must not be null."));

		getFacadeValidationService().validate(validator, buildRestrictionValidationDto(id, restrictionData), restrictionData);

		final Supplier<Optional<String>> supplier = () ->
		{
			AbstractRestrictionModel restrictionModel;
			try
			{
				restrictionModel = getCmsAdminRestrictionService().getRestriction(id);
			}
			catch (final CMSItemNotFoundException e)
			{
				return Optional.empty();
			}
			getDataToModelConverter(restrictionData.getClass().getSimpleName()).populate(restrictionData, restrictionModel);
			getModelService().save(restrictionModel);
			return Optional.of(restrictionModel.getUid());
		};
		final String uid = getSessionSearchRestrictionsDisabler().execute(supplier)
				.orElseThrow(() -> new CMSItemNotFoundException("Restriction with id [" + id + "] not found."));

		// find the updated object
		return findRestrictionById(uid);
	}

	/**
	 * Build a DTO for validating a restriction to be updated.
	 * @param originalUid
	 * @param restriction
	 * @return the validation DTO
	 */
	protected UpdateRestrictionValidationDto buildRestrictionValidationDto(final String originalUid,
			final AbstractRestrictionData restriction)
	{
		final UpdateRestrictionValidationDto dto = new UpdateRestrictionValidationDto();
		dto.setOriginalUid(originalUid);
		dto.setRestriction(restriction);
		return dto;
	}


	protected CMSAdminRestrictionService getCmsAdminRestrictionService()
	{
		return cmsAdminRestrictionService;
	}

	@Required
	public void setCmsAdminRestrictionService(final CMSAdminRestrictionService cmsAdminRestrictionService)
	{
		this.cmsAdminRestrictionService = cmsAdminRestrictionService;
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

	protected Converter<RestrictionTypeModel, RestrictionTypeData> getRestrictionTypeModelConverter()
	{
		return restrictionTypeModelConverter;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setRestrictionTypeModelConverter(
			final Converter<RestrictionTypeModel, RestrictionTypeData> restrictionTypeModelConverter)
	{
		this.restrictionTypeModelConverter = restrictionTypeModelConverter;
	}

	protected Comparator<AbstractRestrictionData> getRestrictionComparator()
	{
		return restrictionComparator;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setRestrictionComparator(final Comparator<AbstractRestrictionData> restrictionComparator)
	{
		this.restrictionComparator = restrictionComparator;
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

	protected Map<Class<?>, CompositeValidator> getCmsCreateRestrictionValidatorFactory()
	{
		return cmsCreateRestrictionValidatorFactory;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setCmsCreateRestrictionValidatorFactory(
			final Map<Class<?>, CompositeValidator> cmsCreateRestrictionValidatorFactory)
	{
		this.cmsCreateRestrictionValidatorFactory = cmsCreateRestrictionValidatorFactory;
	}

	protected PersistentKeyGenerator getCmsRestrictionModelUidGenerator()
	{
		return cmsRestrictionModelUidGenerator;
	}

	@Required
	public void setCmsRestrictionModelUidGenerator(final PersistentKeyGenerator cmsRestrictionModelUidGenerator)
	{
		this.cmsRestrictionModelUidGenerator = cmsRestrictionModelUidGenerator;
	}

	protected String getUidPrefix()
	{
		return uidPrefix;
	}

	public void setUidPrefix(final String uidPrefix)
	{
		this.uidPrefix = uidPrefix;
	}

	protected RestrictionDataToModelConverterRegistry getRestrictionDataToModelConverterRegistry()
	{
		return restrictionDataToModelConverterRegistry;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setRestrictionDataToModelConverterRegistry(
			final RestrictionDataToModelConverterRegistry restrictionDataToModelConverterRegistry)
	{
		this.restrictionDataToModelConverterRegistry = restrictionDataToModelConverterRegistry;
	}

	protected RestrictionModelToDataConverterRegistry getRestrictionModelToDataConverterRegistry()
	{
		return restrictionModelToDataConverterRegistry;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setRestrictionModelToDataConverterRegistry(
			final RestrictionModelToDataConverterRegistry restrictionModelToDataConverterRegistry)
	{
		this.restrictionModelToDataConverterRegistry = restrictionModelToDataConverterRegistry;
	}

	protected NamedQueryService getNamedQueryService()
	{
		return namedQueryService;
	}

	@Required
	public void setNamedQueryService(final NamedQueryService namedQueryService)
	{
		this.namedQueryService = namedQueryService;
	}

	protected Validator getNamedQueryDataValidator()
	{
		return namedQueryDataValidator;
	}

	@Required
	public void setNamedQueryDataValidator(final Validator namedQueryDataValidator)
	{
		this.namedQueryDataValidator = namedQueryDataValidator;
	}

	protected Converter<NamedQueryData, NamedQuery> getRestrictionNamedQueryConverter()
	{
		return restrictionNamedQueryConverter;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setRestrictionNamedQueryConverter(final Converter<NamedQueryData, NamedQuery> restrictionNamedQueryConverter)
	{
		this.restrictionNamedQueryConverter = restrictionNamedQueryConverter;
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

	protected Map<Class<?>, CompositeValidator> getCmsUpdateRestrictionValidatorFactory()
	{
		return cmsUpdateRestrictionValidatorFactory;
	}

	/**
	 * @deprecated since 6.6
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.6")
	@Required
	public void setCmsUpdateRestrictionValidatorFactory(
			final Map<Class<?>, CompositeValidator> cmsUpdateRestrictionValidatorFactory)
	{
		this.cmsUpdateRestrictionValidatorFactory = cmsUpdateRestrictionValidatorFactory;
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
