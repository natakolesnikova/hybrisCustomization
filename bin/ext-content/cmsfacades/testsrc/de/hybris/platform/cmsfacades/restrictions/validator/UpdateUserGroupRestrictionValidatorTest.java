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
package de.hybris.platform.cmsfacades.restrictions.validator;

import static de.hybris.platform.cms2.model.restrictions.CMSUserGroupRestrictionModel.USERGROUPS;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_DOES_NOT_EXIST;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_MIN_VIOLATED;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_REQUIRED;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.data.CategoryRestrictionData;
import de.hybris.platform.cmsfacades.data.UserGroupRestrictionData;
import de.hybris.platform.cmsfacades.dto.UpdateRestrictionValidationDto;
import de.hybris.platform.core.model.user.UserGroupModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class UpdateUserGroupRestrictionValidatorTest
{
	@InjectMocks
	private UpdateUserGroupRestrictionValidator validator;

	@Mock
	private BiPredicate<String, Class<?>> itemModelExistsPredicate;

	@Mock
	private Errors errors;

	@Captor
	private ArgumentCaptor<String> userGroupsCaptor;

	@Captor
	private ArgumentCaptor<String> fieldDoesNotExistsCaptor;

	@Captor
	private ArgumentCaptor<Object[]> userGroupErrorResultCaptor;


	private UpdateRestrictionValidationDto validationDto;
	private UserGroupRestrictionData data;
	private final String notExistentUserGroupKey01 = "UserGroup-not-exists-01";
	private final String notExistentUserGroupKey02 = "UserGroup-not-exists-02";
	private final String existentUserGroup01 = "UserGroup-exists-01";
	private final String existentUserGroup02 = "UserGroup-exists-02";
	private final String existentUserGroup03 = "UserGroup-exists-03";

	private final List<String> MIXED_USERGROUP_KEYS = Arrays.asList(
			existentUserGroup01, notExistentUserGroupKey01, existentUserGroup02,
			notExistentUserGroupKey02, existentUserGroup03);

	private final List<String> EXISTENT_USERGROUP_KEYS = Arrays.asList(
			existentUserGroup01, existentUserGroup02, existentUserGroup03);

	class UpdateRestrictionValidationDtoSubclass extends UpdateRestrictionValidationDto
	{
		// Intentionally left empty.
	}

	@Before
	public void setUp()
	{
		data = new UserGroupRestrictionData();
		data.setIncludeSubgroups(false);

		validationDto = new UpdateRestrictionValidationDto();
		validationDto.setRestriction(data);
	}

	@Test
	public void unKnownClassIsNotSupported()
	{
		Assert.assertThat(
				"UpdateUserGroupRestrictionValidatorTest should support UpdateRestrictionValidationDto class or its subclasses",
				validator.supports(CategoryRestrictionData.class), Matchers.is(false));
	}

	@Test
	public void knownClassIsSupported()
	{
		Assert.assertThat("UpdateUserGroupRestrictionValidatorTest should support UpdateRestrictionValidationDto class",
				validator.supports(UpdateRestrictionValidationDto.class), Matchers.is(true));
	}

	@Test
	public void knownSubclassIsSupported()
	{
		Assert.assertThat("UpdateUserGroupRestrictionValidatorTest should support UpdateRestrictionValidationDto subclass",
				validator.supports(UpdateRestrictionValidationDtoSubclass.class), Matchers.is(true));
	}

	@Test
	public void shouldFailMissingRequiredField()
	{
		//prepare
		data.setUserGroups(null);

		//execute
		validator.validate(validationDto, errors);

		//assert
		verify(errors, times(1)).rejectValue(USERGROUPS, FIELD_REQUIRED, null, null);
	}

	@Test
	public void shouldFailEmptyUserGroupKeys()
	{
		//prepare
		data.setUserGroups(new ArrayList<>());
		when(errors.getFieldValue(USERGROUPS)).thenReturn("fake-data");

		//execute
		validator.validate(validationDto, errors);

		//assert
		verify(errors, times(1)).rejectValue(USERGROUPS, FIELD_MIN_VIOLATED);
	}

	@Test
	public void shouldPartiallyFailNotExistentUserGroupKeys()
	{
		//prepare
		MIXED_USERGROUP_KEYS.forEach(userGroupKey ->
		when(itemModelExistsPredicate.test(userGroupKey, UserGroupModel.class)).thenReturn(!userGroupKey.contains("-not-")));

		data.setUserGroups(MIXED_USERGROUP_KEYS);
		when(errors.getFieldValue(USERGROUPS)).thenReturn("fake-data");

		final List<Object[]> errorValuesResult = Arrays.asList(
				new Object[] { "userGroupUid", notExistentUserGroupKey01 },
				new Object[] { "userGroupUid", notExistentUserGroupKey02 });

		//execute
		validator.validate(validationDto, errors);

		//assert
		verify(errors, times(2)).rejectValue(
				userGroupsCaptor.capture(),
				fieldDoesNotExistsCaptor.capture(),
				userGroupErrorResultCaptor.capture(),
				Mockito.eq(null)
				);

		final List<String> fieldNames = userGroupsCaptor.getAllValues();
		final List<String> errorCodes = fieldDoesNotExistsCaptor.getAllValues();
		final List<Object[]> errorValues = userGroupErrorResultCaptor.getAllValues();

		assertThat("UpdateUserGroupRestrictionValidatorTest errors object should contain 2 USERGROUPS fields",
				fieldNames, contains(Arrays.asList(USERGROUPS, USERGROUPS).toArray()));

		assertThat("UpdateUserGroupRestrictionValidatorTest errors object should contain 2 FIELD_REQUIRED codes",
				errorCodes, contains(Arrays.asList(FIELD_DOES_NOT_EXIST, FIELD_DOES_NOT_EXIST).toArray()));

		assertThat("UpdateUserGroupRestrictionValidatorTest errors object should contain 2 VALUE objects",
				errorValues, contains(errorValuesResult.toArray()));
	}

	@Test
	public void shouldPassWithExistentUserGroupKeys()
	{
		//prepare
		EXISTENT_USERGROUP_KEYS.forEach(userGroupKey ->
		when(itemModelExistsPredicate.test(userGroupKey, UserGroupModel.class)).thenReturn(true));
		data.setUserGroups(EXISTENT_USERGROUP_KEYS);
		when(errors.getFieldValue(USERGROUPS)).thenReturn("fake-data");

		//execute
		validator.validate(validationDto, errors);

		//assert
		verify(errors, times(0)).rejectValue(
				userGroupsCaptor.capture(),
				fieldDoesNotExistsCaptor.capture(),
				userGroupErrorResultCaptor.capture(),
				Mockito.eq(null)
				);
	}
}
