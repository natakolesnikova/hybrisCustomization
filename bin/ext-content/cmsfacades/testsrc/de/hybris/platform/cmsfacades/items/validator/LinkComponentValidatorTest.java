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
package de.hybris.platform.cmsfacades.items.validator;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.common.predicate.ItemModelExistsPredicate;
import de.hybris.platform.cmsfacades.common.validator.LocalizedTypeValidator;
import de.hybris.platform.cmsfacades.common.validator.LocalizedValidator;
import de.hybris.platform.cmsfacades.data.CMSLinkComponentData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LinkComponentValidatorTest
{
	private static final String ERROR_FIELD = "field";
	private static final String ERROR_CODE = "code";
	private static final String ERROR_LINK_ITEMS_EXCEEDED = "link.items.exceeded";
	private static final String LINK_MISSING_ITEMS = "link.items.missing";
	private static final String ERROR_URL_FORMAT_INVALID = "url.format.invalid";
	private static final String ERROR_FIELD_DOESNOT_EXIST = "field.doesnot.exist";

	private static final String CATEGORY = "category";
	private static final String CONTENT_PAGE = "contentPage";
	private static final String PRODUCT = "product";
	private static final String URL = "url";

	private static final String URL_VALUE = "https://dummy-url.com";
	private static final String CATEGORY_UUID = "category-uuid";
	private static final String CONTENT_PAGE_UUID = "contentPage-uuid";
	private static final String PRODUCT_UUID = "product-uuid";

	@Mock
	private LocalizedValidator localizedValidator;
	@Mock
	private LocalizedTypeValidator localizedStringValidator;
	@Mock
	private ItemModelExistsPredicate itemModelExistsPredicate;

	@InjectMocks
	private LinkComponentValidator validator;

	@Before
	public void setUp()
	{
		final LanguageData languageEN = new LanguageData();
		languageEN.setIsocode("EN");

		doNothing().when(localizedValidator).validateRequiredLanguages(any(), any(), any());
		doNothing().when(localizedStringValidator).validate(any(), any(), any(), any());
		when(itemModelExistsPredicate.test(any(), any())).thenReturn(true);
	}

	@Test
	public void shouldValidateCategoryLink()
	{
		final CMSLinkComponentData data = new CMSLinkComponentData();
		data.setCategory(CATEGORY_UUID);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertThat(errors.getAllErrors(), is(empty()));
	}

	@Test
	public void shouldFailCategoryNotFound()
	{
		when(itemModelExistsPredicate.test(any(), any())).thenReturn(false);
		final CMSLinkComponentData data = new CMSLinkComponentData();
		data.setCategory(CATEGORY_UUID);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertThat(errors.getErrorCount(), is(1));
		assertThat(errors.getFieldError(),
				allOf( //
						hasProperty(ERROR_CODE, equalTo(ERROR_FIELD_DOESNOT_EXIST)), //
						hasProperty(ERROR_FIELD, equalTo(CATEGORY))));
	}

	@Test
	public void shouldValidateContentPageLink()
	{
		final CMSLinkComponentData data = new CMSLinkComponentData();
		data.setContentPage(CONTENT_PAGE_UUID);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertThat(errors.getAllErrors(), is(empty()));
	}

	@Test
	public void shouldFailContentPageNotFound()
	{
		when(itemModelExistsPredicate.test(any(), any())).thenReturn(false);
		final CMSLinkComponentData data = new CMSLinkComponentData();
		data.setContentPage(CONTENT_PAGE_UUID);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertThat(errors.getErrorCount(), is(1));
		assertThat(errors.getFieldError(),
				allOf( //
						hasProperty(ERROR_CODE, equalTo(ERROR_FIELD_DOESNOT_EXIST)), //
						hasProperty(ERROR_FIELD, equalTo(CONTENT_PAGE))));
	}

	@Test
	public void shouldValidateProductLink()
	{
		final CMSLinkComponentData data = new CMSLinkComponentData();
		data.setProduct(PRODUCT_UUID);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertThat(errors.getAllErrors(), is(empty()));
	}

	@Test
	public void shouldFailProductNotFound()
	{
		when(itemModelExistsPredicate.test(any(), any())).thenReturn(false);
		final CMSLinkComponentData data = new CMSLinkComponentData();
		data.setProduct(PRODUCT_UUID);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertThat(errors.getErrorCount(), is(1));
		assertThat(errors.getFieldError(),
				allOf( //
						hasProperty(ERROR_CODE, equalTo(ERROR_FIELD_DOESNOT_EXIST)), //
						hasProperty(ERROR_FIELD, equalTo(PRODUCT))));
	}

	@Test
	public void shouldValidateExternalLink()
	{
		final CMSLinkComponentData data = new CMSLinkComponentData();
		data.setUrl(URL_VALUE);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertThat(errors.getAllErrors(), is(empty()));
	}

	@Test
	public void shouldFailInvalidUrlFormat()
	{
		final CMSLinkComponentData data = new CMSLinkComponentData();
		data.setUrl("dummy-invalid-url");
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertThat(errors.getErrorCount(), is(1));
		assertThat(errors.getFieldError(),
				allOf( //
						hasProperty(ERROR_CODE, equalTo(ERROR_URL_FORMAT_INVALID)), //
						hasProperty(ERROR_FIELD, equalTo(URL))));
	}

	@Test
	public void shouldFailValidateMultipleTypesProvided()
	{
		final CMSLinkComponentData data = new CMSLinkComponentData();
		data.setCategory(CATEGORY_UUID);
		data.setContentPage(CONTENT_PAGE_UUID);
		data.setProduct(PRODUCT_UUID);
		data.setUrl(URL_VALUE);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertThat(errors.getErrorCount(), is(4));
		assertAllFieldsHaveErrors(errors, ERROR_LINK_ITEMS_EXCEEDED);
	}

	@Test
	public void shouldFailValidateIfNoTypeProvided()
	{
		final Map<String, String> names = new HashMap<>();
		names.put("en", "Contact Us");
		names.put("fr", "Contactez Nous");

		final CMSLinkComponentData data = new CMSLinkComponentData();
		data.setLinkName(names); // Name available, but no type provided.
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);

		assertThat(errors.getErrorCount(), is(4));
		assertAllFieldsHaveErrors(errors, LINK_MISSING_ITEMS);
	}

	@Test
	public void shouldValidateLinkName_ValidLocalized()
	{
		final Map<String, String> names = new HashMap<>();
		names.put("en", "Contact Us");
		names.put("fr", "Contactez Nous");

		final CMSLinkComponentData data = new CMSLinkComponentData();
		data.setLinkName(names);
		data.setUrl(URL_VALUE);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertThat(errors.getAllErrors(), is(empty()));
	}

	@Test
	public void shouldValidateLinkName_InvalidLocalizedNull()
	{
		final Map<String, String> names = new HashMap<>();
		final CMSLinkComponentData data = new CMSLinkComponentData();
		data.setLinkName(names);
		data.setUrl(URL_VALUE);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		doAnswer((Answer<Void>) invocationOnMock -> {
			errors.rejectValue("linkName", "missing required field", new Object[]
					{ Locale.ENGLISH }, null);
			return null;
		}).when(localizedValidator).validateRequiredLanguages(any(), any(), any());

		validator.validate(data, errors);

		// only english is required
		assertThat(errors.getErrorCount(), is(1));
	}

	protected void assertAllFieldsHaveErrors(Errors errors, String expectedError){
		assertThat(errors.getFieldError(CONTENT_PAGE),
				allOf(
						hasProperty(ERROR_CODE, equalTo(expectedError)),
						hasProperty(ERROR_FIELD, equalTo(CONTENT_PAGE))));
		assertThat(errors.getFieldError(PRODUCT),
				allOf(
						hasProperty(ERROR_CODE, equalTo(expectedError)),
						hasProperty(ERROR_FIELD, equalTo(PRODUCT))));
		assertThat(errors.getFieldError(CATEGORY),
				allOf(
						hasProperty(ERROR_CODE, equalTo(expectedError)),
						hasProperty(ERROR_FIELD, equalTo(CATEGORY))));
		assertThat(errors.getFieldError(URL),
				allOf(
						hasProperty(ERROR_CODE, equalTo(expectedError)),
						hasProperty(ERROR_FIELD, equalTo(URL))));
	}
}
