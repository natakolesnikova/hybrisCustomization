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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cmsfacades.common.validator.CompositeValidator;
import de.hybris.platform.cmsfacades.common.validator.ValidationDtoFactory;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.cmsfacades.dto.ComponentTypeAndContentSlotValidationDto;

import java.util.Map;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CreateComponentValidatorTest
{
	private static final String NAME = "name";
	private static final String TYPE_CODE = "type-code";
	private static final String SLOT_ID = "slot-id";
	private static final String INVALID_SLOT_ID = "invalid-slot-id";
	private static final String PAGE_ID = "homepage";
	private static final Integer POSITION = 0;
	private static final String COMPONENT_ID = "component-id";
	private static final String INVALID_COMPONENT_ID = "invalid-component-id";


	@InjectMocks
	private final Validator validator = new CreateComponentValidator();

	@Mock
	private Map<Class<?>, Validator> cmsComponentValidatorFactory;

	@Mock
	private CMSAdminContentSlotService cmsAdminContentSlotService;
	@Mock
	private Predicate<String> contentSlotExistsPredicate;
	@Mock
	private Predicate<ComponentTypeAndContentSlotValidationDto> componentTypeAllowedForContentSlotPredicate;
	@Mock
	private Predicate<String> validStringLengthPredicate;
	@Mock
	private ValidationDtoFactory validationDtoFactory;
	@Mock
	private Predicate<String> pageExistsPredicate;
	@Mock
	private ContentSlotModel contentSlot;
	@Mock
	private AbstractCMSComponentModel component;
	@Mock
	private ComponentTypeAndContentSlotValidationDto componentTypeAndContentSlotValidationDto;
	@Mock
	private CompositeValidator compositeValidator;
	@Mock
	private Predicate<String> componentExistsPredicate;

	private AbstractCMSComponentData target;
	private Errors errors;

	@Before
	public void setUp()
	{
		target = new AbstractCMSComponentData();
		target.setUid(COMPONENT_ID);
		target.setSlotId(SLOT_ID);
		target.setName(NAME);
		target.setTypeCode(TYPE_CODE);
		target.setPageId(PAGE_ID);
		target.setPosition(POSITION);

		errors = new BeanPropertyBindingResult(target, target.getClass().getSimpleName());

		when(pageExistsPredicate.test(Mockito.anyString())).thenReturn(Boolean.TRUE);

		when(contentSlotExistsPredicate.test(SLOT_ID)).thenReturn(Boolean.TRUE);
		when(contentSlotExistsPredicate.test(INVALID_SLOT_ID)).thenReturn(Boolean.FALSE);

		when(cmsAdminContentSlotService.getContentSlotForId(SLOT_ID)).thenReturn(contentSlot);

		when(componentTypeAllowedForContentSlotPredicate.test(Mockito.any(ComponentTypeAndContentSlotValidationDto.class)))
		.thenReturn(Boolean.TRUE);

		when(validStringLengthPredicate.test(NAME)).thenReturn(Boolean.TRUE);
		when(validStringLengthPredicate.test(COMPONENT_ID)).thenReturn(Boolean.TRUE);


		when(validationDtoFactory.buildComponentTypeAndContentSlotValidationDto(Mockito.any(), Mockito.any(), Mockito.any()))
		.thenReturn(componentTypeAndContentSlotValidationDto);

		when(cmsComponentValidatorFactory.get(target.getClass())).thenReturn(compositeValidator);

		when(componentExistsPredicate.test(COMPONENT_ID)).thenReturn(Boolean.FALSE);
	}

	@Test
	public void shouldHaveNoFailures_ComponentUidDoesNotExist()
	{
		validator.validate(target, errors);
		assertFalse(errors.hasErrors());
	}

	@Test
	public void shouldFail_PageIdNull()
	{
		target.setPageId(null);
		validator.validate(target, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}

	@Test
	public void shouldFail_PositionNull()
	{
		target.setPosition(null);

		validator.validate(target, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}

	@Test
	public void shouldFail_TypeNotAllowedInSlot()
	{
		when(componentTypeAllowedForContentSlotPredicate.test(Mockito.any(ComponentTypeAndContentSlotValidationDto.class)))
		.thenReturn(Boolean.FALSE);

		validator.validate(target, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}

	@Test
	public void shouldFail_SlotDoesNotExist()
	{
		target.setSlotId(INVALID_SLOT_ID);

		validator.validate(target, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}

	@Test
	public void shouldPass_SlotNull()
	{
		target.setSlotId(null);

		validator.validate(target, errors);
		assertFalse(errors.hasErrors());
	}

	@Test
	public void shouldPass_SlotEmpty()
	{
		target.setSlotId("");

		validator.validate(target, errors);
		assertFalse(errors.hasErrors());
	}

	@Test
	public void shouldPass_SlotAndPositionNull()
	{
		target.setSlotId(null);
		target.setPosition(null);

		validator.validate(target, errors);
		assertFalse(errors.hasErrors());
	}

	@Test
	public void shouldFail_ComponentExist()
	{
		when(componentExistsPredicate.test(INVALID_COMPONENT_ID)).thenReturn(Boolean.TRUE);
		target.setUid(INVALID_COMPONENT_ID);

		validator.validate(target, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}

}
