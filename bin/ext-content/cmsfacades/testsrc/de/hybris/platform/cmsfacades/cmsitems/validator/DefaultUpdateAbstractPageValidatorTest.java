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
package de.hybris.platform.cmsfacades.cmsitems.validator;


import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_NOT_ALLOWED;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrors;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrorsProvider;
import de.hybris.platform.cmsfacades.common.validator.impl.DefaultValidationErrors;
import de.hybris.platform.cmsfacades.validator.data.ValidationError;

import java.util.List;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultUpdateAbstractPageValidatorTest
{
	private static final String TEST_UID = "test-uid";

	@InjectMocks
	private DefaultUpdateAbstractPageValidator validator;

	@Mock
	private CMSAdminPageService cmsAdminPageService;
	@Mock
	private Predicate<String> pageExistsPredicate;
	@Mock
	private ValidationErrorsProvider validationErrorsProvider;

	private AbstractPageModel pageModel = new AbstractPageModel();
	private ValidationErrors validationErrors = new DefaultValidationErrors();

	@Before
	public void setup()
	{
		pageModel.setDefaultPage(false);
		
		when(validationErrorsProvider.getCurrentValidationErrors()).thenReturn(validationErrors);
		when(pageExistsPredicate.test(TEST_UID)).thenReturn(true);
		when(cmsAdminPageService.getPageForIdFromActiveCatalogVersion(TEST_UID)).thenReturn(pageModel);
	}

	@Test
	public void testValidateDefaultPageModified()
	{
		final AbstractPageModel pageModel = new AbstractPageModel();
		pageModel.setUid(TEST_UID);
		pageModel.setDefaultPage(true);
		
		validator.validate(pageModel);

		final List<ValidationError> errors = validationErrorsProvider.getCurrentValidationErrors().getValidationErrors();

		assertEquals(1, errors.size());
		assertThat(errors.get(0).getField(), is(AbstractPageModel.DEFAULTPAGE));
		assertThat(errors.get(0).getErrorCode(), is(FIELD_NOT_ALLOWED));
	}
}
