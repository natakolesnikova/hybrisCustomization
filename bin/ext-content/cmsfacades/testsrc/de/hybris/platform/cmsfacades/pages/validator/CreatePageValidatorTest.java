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
package de.hybris.platform.cmsfacades.pages.validator;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolver;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolverType;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolverTypeRegistry;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CreatePageValidatorTest
{
	private static final String TYPE_CODE = "typeCode";

	@InjectMocks
	private CreatePageValidator validator;

	@Mock
	private PageVariationResolverTypeRegistry registry;
	@Mock
	private PageVariationResolverType resolverType;
	@Mock
	private PageVariationResolver<AbstractPageModel> resolver;

	private Errors errors;
	private AbstractPageData pageData;
	private AbstractPageModel defaultPage;

	@Before
	public void setUp()
	{
		pageData = new AbstractPageData();
		pageData.setTypeCode(AbstractPageModel._TYPECODE);
		pageData.setDefaultPage(Boolean.FALSE);

		errors = new BeanPropertyBindingResult(pageData, pageData.getClass().getSimpleName());

		when(registry.getPageVariationResolverType(AbstractPageModel._TYPECODE)).thenReturn(Optional.of(resolverType));
		when(resolverType.getResolver()).thenReturn(resolver);
	}

	@Test
	public void shouldPassCreatePrimaryPage()
	{
		pageData.setDefaultPage(Boolean.TRUE);
		when(resolver.findPagesByType(AbstractPageModel._TYPECODE, Boolean.TRUE)).thenReturn(Collections.emptyList());

		validator.validate(pageData, errors);

		assertThat(errors.getAllErrors(), empty());
	}

	@Test
	public void shouldPassCreateVariationPage()
	{
		pageData.setDefaultPage(Boolean.FALSE);
		when(resolver.findPagesByType(AbstractPageModel._TYPECODE, Boolean.TRUE)).thenReturn(Arrays.asList(defaultPage));

		validator.validate(pageData, errors);

		assertThat(errors.getAllErrors(), empty());
	}

	@Test
	public void shouldFailCreatePrimaryPage_PrimaryAlreadyExists()
	{
		pageData.setDefaultPage(Boolean.TRUE);
		when(resolver.findPagesByType(AbstractPageModel._TYPECODE, Boolean.TRUE)).thenReturn(Arrays.asList(defaultPage));

		validator.validate(pageData, errors);

		assertThat(errors.getFieldErrorCount(), is(1));
		assertThat(errors.getFieldError().getField(), is(TYPE_CODE));
		assertThat(errors.getFieldError().getCode(), is(CmsfacadesConstants.DEFAULT_PAGE_ALREADY_EXIST));
	}

	@Test
	public void shouldFailCreateVariationPage_PrimaryNotExist()
	{
		pageData.setDefaultPage(Boolean.FALSE);
		when(resolver.findPagesByType(AbstractPageModel._TYPECODE, Boolean.TRUE)).thenReturn(Collections.emptyList());

		validator.validate(pageData, errors);

		assertThat(errors.getFieldErrorCount(), is(1));
		assertThat(errors.getFieldError().getField(), is(TYPE_CODE));
		assertThat(errors.getFieldError().getCode(), is(CmsfacadesConstants.DEFAULT_PAGE_DOES_NOT_EXIST));
	}

}
