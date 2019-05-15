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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.namedquery.NamedQuery;
import de.hybris.platform.cms2.namedquery.service.NamedQueryService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminRestrictionService;
import de.hybris.platform.cmsfacades.common.service.SearchResultConverter;
import de.hybris.platform.cmsfacades.common.validator.CompositeValidator;
import de.hybris.platform.cmsfacades.common.validator.FacadeValidationService;
import de.hybris.platform.cmsfacades.data.AbstractRestrictionData;
import de.hybris.platform.cmsfacades.data.NamedQueryData;
import de.hybris.platform.cmsfacades.exception.ValidationError;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.namedquery.validator.NamedQueryDataValidator;
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

import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Validator;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultRestrictionFacadeTest
{
	@InjectMocks
	private DefaultRestrictionFacade restrictionFacade;

	@Mock
	private ModelService modelService;
	@Mock
	private FacadeValidationService facadeValidationService;
	@Mock
	private PersistentKeyGenerator cmsRestrictionModelUidGenerator;
	@Mock
	private CompositeValidator validator;
	@Mock
	private RestrictionDataToModelConverter dataToModelConverter;
	@Mock
	private RestrictionModelToDataConverter modelToDataConverter;
	@Mock
	private AbstractPopulatingConverter<AbstractRestrictionData, AbstractRestrictionModel> dataConverter;
	@Mock
	private AbstractPopulatingConverter<AbstractRestrictionModel, AbstractRestrictionData> modelConverter;
	@Mock
	private Map<Class<?>, Validator> cmsCreateRestrictionValidatorFactory;
	@Mock
	private Map<Class<?>, Validator> cmsUpdateRestrictionValidatorFactory;
	@Mock
	private RestrictionDataToModelConverterRegistry restrictionDataToModelConverterRegistry;
	@Mock
	private RestrictionModelToDataConverterRegistry restrictionModelToDataConverterRegistry;
	@Mock
	private NamedQueryService namedQueryService;
	@Mock
	private Validator namedQueryDataValidator;
	@Mock
	private Converter<NamedQueryData, NamedQuery> restrictionNamedQueryConverter;

	@Mock
	private NamedQuery namedQuery;
	@Mock
	private SearchResult<AbstractRestrictionModel> modelSearchResult;
	@Mock
	private SearchResult<AbstractRestrictionData> dataSearchResult;
	@Mock
	private SearchResultConverter searchResultConverter;
	@Mock
	private CMSAdminRestrictionService cmsAdminRestrictionService;
	@Mock
	private SessionSearchRestrictionsDisabler sessionSearchRestrictionsDisabler;

	private AbstractRestrictionData restrictionData;
	private AbstractRestrictionModel restrictionModel;

	@Before
	public void setUp()
	{
		restrictionData = new AbstractRestrictionData();
		restrictionModel = new AbstractRestrictionModel();

		when(cmsRestrictionModelUidGenerator.generate()).thenReturn(12345678);
	}

	@Test
	public void shouldGenerateRestrictionUid()
	{
		restrictionFacade.generateRestrictionUid(restrictionData);

		assertThat(restrictionData.getUid(), equalTo(DefaultRestrictionFacade.DEFAULT_UID_PREFIX + "-12345678"));
	}

	@Test
	public void shouldGenerateRestrictionUidWithoutPrefix()
	{
		restrictionFacade.setUidPrefix("");

		restrictionFacade.generateRestrictionUid(restrictionData);

		assertThat(restrictionData.getUid(), equalTo("12345678"));
	}

	@Test
	public void shouldNotGenerateRestrictionUid()
	{
		restrictionData.setUid("test-restriction-uid");

		restrictionFacade.generateRestrictionUid(restrictionData);

		verifyZeroInteractions(cmsRestrictionModelUidGenerator);
	}

	@Test
	public void shouldCreateRestriction()
	{
		when(cmsCreateRestrictionValidatorFactory.get(any())).thenReturn(validator);
		doNothing().when(facadeValidationService).validate(validator, restrictionData);
		when(sessionSearchRestrictionsDisabler.execute(any())).thenReturn(restrictionData);

		restrictionFacade.createRestriction(restrictionData);

		verify(cmsRestrictionModelUidGenerator).generate();
		verify(cmsCreateRestrictionValidatorFactory).get(any());
		verify(facadeValidationService).validate(any(), any());
		verify(sessionSearchRestrictionsDisabler).execute(any());
	}

	@Test
	public void shouldUpdateRestriction() throws CMSItemNotFoundException
	{
		when(cmsUpdateRestrictionValidatorFactory.get(any())).thenReturn(validator);
		doNothing().when(facadeValidationService).validate(any(), any(), any());
		when(sessionSearchRestrictionsDisabler.execute(any())).thenReturn(Optional.of("testId"))
		.thenReturn(Optional.of(restrictionData));

		restrictionFacade.updateRestriction("testId", restrictionData);

		verify(cmsUpdateRestrictionValidatorFactory).get(any());
		verify(facadeValidationService).validate(any(), any(), any());
		verify(sessionSearchRestrictionsDisabler, times(2)).execute(any());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailCreateRestrictionValidatorNotFound()
	{
		when(cmsCreateRestrictionValidatorFactory.get(any())).thenReturn(null);

		restrictionFacade.createRestriction(restrictionData);
	}

	@Test(expected = ValidationException.class)
	public void shouldFailCreateRestrictionValidationError()
	{
		when(cmsCreateRestrictionValidatorFactory.get(any())).thenReturn(validator);
		doThrow(new ValidationException(new ValidationError("exception"))) //
		.when(facadeValidationService).validate(validator, restrictionData);

		restrictionFacade.createRestriction(restrictionData);
	}

	@Test(expected = ConversionException.class)
	public void shouldFailCreateRestrictionConverterNotFound()
	{
		when(cmsCreateRestrictionValidatorFactory.get(any())).thenReturn(validator);
		doNothing().when(facadeValidationService).validate(validator, restrictionData);
		doThrow(new ConversionException("exception")).when(sessionSearchRestrictionsDisabler).execute(any());

		restrictionFacade.createRestriction(restrictionData);
	}

	@Test
	public void shouldFindRestrictionsByMask()
	{
		final NamedQueryData namedQueryData = new NamedQueryData();
		namedQueryData.setCurrentPage("0");
		namedQueryData.setPageSize("5");
		namedQueryData.setSort("name:ASC");

		doNothing().when(facadeValidationService).validate(any(NamedQueryDataValidator.class), any(NamedQueryData.class));
		when(sessionSearchRestrictionsDisabler.execute(any())).thenReturn(dataSearchResult);

		final SearchResult<AbstractRestrictionData> result = restrictionFacade.findRestrictionsByMask("help", namedQueryData);

		assertThat(result, equalTo(dataSearchResult));
	}

	@Test
	public void shouldFindRestrictionById() throws CMSItemNotFoundException
	{
		when(sessionSearchRestrictionsDisabler.execute(any())).thenReturn(Optional.of(restrictionData));

		restrictionFacade.findRestrictionById("testId");

		verify(sessionSearchRestrictionsDisabler).execute(any());
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldNotFindNonExistantRestrictionById() throws CMSItemNotFoundException
	{
		when(sessionSearchRestrictionsDisabler.execute(any())).thenReturn(Optional.empty());

		restrictionFacade.findRestrictionById("testId");
	}

	@Test
	public void shouldAugmentNamedQueryDataBySettingDefaultValues()
	{
		final NamedQueryData data = new NamedQueryData();
		restrictionFacade.augmentNamedQueryParams(null, data);

		assertThat(data.getParams(), equalTo("name:"));
	}

	@Test
	public void shouldAugmentNamedQueryDataByAddingMaskToParams()
	{
		final NamedQueryData data = new NamedQueryData();
		restrictionFacade.augmentNamedQueryParams("test", data);

		assertThat(data.getParams(), equalTo("name:test"));
	}

	@Test
	public void shouldAugmentNamedQueryDataByAppendingMaskToParams()
	{
		final String params = "field:blabla";
		final NamedQueryData data = new NamedQueryData();
		data.setParams(params);
		restrictionFacade.augmentNamedQueryParams("test", data);

		assertThat(data.getParams(), equalTo(params + ",name:test"));
	}

}
