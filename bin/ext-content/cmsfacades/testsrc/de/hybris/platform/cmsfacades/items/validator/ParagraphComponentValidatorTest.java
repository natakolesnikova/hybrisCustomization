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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.data.CMSParagraphComponentData;

import java.util.Locale;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
public class ParagraphComponentValidatorTest
{
	private final ParagraphComponentValidator validator = new ParagraphComponentValidator();
	private CMSParagraphComponentData data;
	private Errors errors;

	@Before
	public void setUp()
	{
		data = new CMSParagraphComponentData();
		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
	}

	@Test
	public void shouldValidateParagraph_Valid_Content()
	{
		validator.validateContent(Locale.ENGLISH.getLanguage(), "Non empty", errors);
		assertThat(errors.getAllErrors().isEmpty(), Matchers.is(Boolean.TRUE));
	}

	@Test
	public void shouldValidateParagraph_Content_Null()
	{
		validator.validateContent(Locale.ENGLISH.getLanguage(), null, errors);
		assertThat(errors.getAllErrors().size(), equalTo(1));
	}

}
