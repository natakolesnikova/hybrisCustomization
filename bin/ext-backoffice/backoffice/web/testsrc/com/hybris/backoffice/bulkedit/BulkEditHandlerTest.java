/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.hybris.backoffice.bulkedit;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;
import com.hybris.backoffice.attributechooser.Attribute;
import com.hybris.backoffice.attributechooser.AttributeChooserForm;
import com.hybris.backoffice.bulkedit.renderer.BulkEditAttributesSelectorRenderer;
import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEventTypes;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeOperationResult;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectAccessException;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


@RunWith(MockitoJUnitRunner.class)
public class BulkEditHandlerTest
{
	@Mock
	private ObjectValueService objectValueService;
	@Mock
	private ObjectFacade objectFacade;
	@Mock
	private TypeFacade typeFacade;
	@Mock
	private NotificationService notificationService;
	@Mock
	private ModelService modelService;
	@Mock
	private FlowActionHandlerAdapter adapter;
	@Mock
	private CustomType customType;
	@Mock
	private SessionService sessionService;
	@Mock
	private BulkEditValidationHelper bulkEditValidationHelper;

	@InjectMocks
	@Spy
	private BulkEditHandler handler;

	private WidgetInstanceManager wim;
	private Context context;

	@Before
	public void setUp()
	{
		doNothing().when(handler).beginTransaction();
		doNothing().when(handler).rollbackTransaction();
		doNothing().when(handler).commitTransaction();
		wim = CockpitTestUtil.mockWidgetInstanceManager();
		when(adapter.getWidgetInstanceManager()).thenReturn(wim);
		context = new DefaultContext();
		doAnswer(inv -> ((SessionExecutionBody) inv.getArguments()[1]).execute()).when(sessionService)
				.executeInLocalViewWithParams(any(), any());
	}

	@Test
	public void notifyWhenValidationFails() throws TypeNotFoundException
	{
		//given
		final HashMap<String, String> params = new HashMap<>();
		final String qualifier = ProductModel.SUPERCATEGORIES;
		final ItemModel product = prepareBulkEditData(1, 2, params, qualifier, DataAttribute.AttributeType.MAP, true);

		final Map<Object, List<ValidationInfo>> validationResult = new HashMap<>();
		final ValidationInfo info = mock(ValidationInfo.class);
		final List<ValidationInfo> infos = Lists.newArrayList(info);
		validationResult.put(product, infos);

		when(bulkEditValidationHelper.validateModifiedItems(any(), eq(ValidationSeverity.WARN))).thenReturn(validationResult);
		//when
		handler.perform(customType, adapter, params);

		//then
		verify(notificationService).notifyUser(BulkEditConstants.NOTIFICATION_SOURCE_BULK_EDIT,
				BulkEditConstants.NOTIFICATION_EVENT_TYPE_VALIDATION_ERROR, NotificationEvent.Level.FAILURE, product, infos);
	}

	@Test
	public void rollbackTransactionOnSaveFailure() throws TypeNotFoundException
	{
		//given
		final DataAttribute categories = mockDataAttribute(ProductModel.SUPERCATEGORIES, DataAttribute.AttributeType.COLLECTION);

		final Attribute attributeCategories = mockAttribute(categories);

		final ItemModel product = mock(ProductModel.class);
		final DataType dataType = mockDataTypeWithAttributes(ProductModel._TYPECODE, Lists.newArrayList(categories));
		when(typeFacade.getType(product)).thenReturn(ProductModel._TYPECODE);
		when(typeFacade.load(ProductModel._TYPECODE)).thenReturn(dataType);

		final AttributeChooserForm attributesForm = new AttributeChooserForm();
		attributesForm.setChosenAttributes(Sets.newHashSet(attributeCategories));

		final BulkEditForm bulkEditForm = new BulkEditForm();
		bulkEditForm.setItemsToEdit(Lists.newArrayList(product));
		bulkEditForm.setAttributesForm(attributesForm);


		final HashMap<String, String> params = new HashMap<>();
		params.put(BulkEditAttributesSelectorRenderer.PARAM_BULK_EDIT_FORM_MODEL_KEY, "bulkEditForm");
		wim.getModel().setValue("bulkEditForm", bulkEditForm);


		final ObjectFacadeOperationResult saveFailure = new ObjectFacadeOperationResult();
		final ObjectAccessException cause = mock(ObjectAccessException.class);
		saveFailure.addFailedObject(product, cause);
		when(objectFacade.save(eq(bulkEditForm.getItemsToEdit()), any(Context.class))).thenReturn(saveFailure);
		//when
		handler.perform(customType, adapter, params);

		//then
		verify(handler).beginTransaction();
		verify(handler).rollbackTransaction();
		verify(modelService).refresh(product);
		verify(notificationService).notifyUser(anyString(), eq(NotificationEventTypes.EVENT_TYPE_OBJECT_UPDATE),
				eq(NotificationEvent.Level.FAILURE), any());
	}

	@Test
	public void commitTransactionOnSaveSuccess() throws TypeNotFoundException
	{
		//given
		final DataAttribute categories = mockDataAttribute(ProductModel.SUPERCATEGORIES, DataAttribute.AttributeType.COLLECTION);

		final Attribute attributeCategories = mockAttribute(categories);

		final ItemModel product = mock(ProductModel.class);
		final DataType dataType = mockDataTypeWithAttributes(ProductModel._TYPECODE, Lists.newArrayList(categories));
		when(typeFacade.getType(product)).thenReturn(ProductModel._TYPECODE);
		when(typeFacade.load(ProductModel._TYPECODE)).thenReturn(dataType);

		final AttributeChooserForm attributesForm = new AttributeChooserForm();
		attributesForm.setChosenAttributes(Sets.newHashSet(attributeCategories));

		final BulkEditForm bulkEditForm = new BulkEditForm();
		bulkEditForm.setItemsToEdit(Lists.newArrayList(product));
		bulkEditForm.setAttributesForm(attributesForm);


		final HashMap<String, String> params = new HashMap<>();
		params.put(BulkEditAttributesSelectorRenderer.PARAM_BULK_EDIT_FORM_MODEL_KEY, "bulkEditForm");
		wim.getModel().setValue("bulkEditForm", bulkEditForm);


		final ObjectFacadeOperationResult saveFailure = new ObjectFacadeOperationResult();
		when(objectFacade.save(eq(bulkEditForm.getItemsToEdit()), any(Context.class))).thenReturn(saveFailure);
		//when
		handler.perform(customType, adapter, params);

		//then
		verify(handler).beginTransaction();
		verify(handler).commitTransaction();
		verify(notificationService).notifyUser(anyString(), eq(NotificationEventTypes.EVENT_TYPE_OBJECT_UPDATE),
				eq(NotificationEvent.Level.SUCCESS), any());
	}

	@Test
	public void changeCollectionValue() throws TypeNotFoundException
	{
		//given
		final ArrayList<String> newValue = Lists.newArrayList("a", "b");
		final ArrayList<String> oldValue = Lists.newArrayList("c");
		final HashMap<String, String> params = new HashMap<>();
		final ItemModel product = prepareBulkEditData(newValue, oldValue, params, ProductModel.SUPERCATEGORIES,
				DataAttribute.AttributeType.COLLECTION, false);
		//when
		handler.perform(customType, adapter, params);

		//then
		verify(objectValueService).setValue(eq(ProductModel.SUPERCATEGORIES), same(product),
				argThat(new ArgumentMatcher<Collection<String>>()
				{
					@Override
					public boolean matches(final Object o)
					{
						return ((Collection) o).containsAll(Lists.newArrayList("a", "b", "c"));
					}
				}));
	}

	@Test
	public void changeCollectionValueWithOverride() throws TypeNotFoundException
	{
		//given
		final ArrayList<String> newValue = Lists.newArrayList("a", "b");
		final ArrayList<String> oldValue = Lists.newArrayList("c");
		final HashMap<String, String> params = new HashMap<>();
		final ItemModel product = prepareBulkEditData(newValue, oldValue, params, ProductModel.SUPERCATEGORIES,
				DataAttribute.AttributeType.COLLECTION, true);
		//when
		handler.perform(customType, adapter, params);

		//then
		verify(objectValueService).setValue(eq(ProductModel.SUPERCATEGORIES), same(product),
				argThat(new ArgumentMatcher<Collection<String>>()
				{
					@Override
					public boolean matches(final Object o)
					{
						return ((Collection) o).containsAll(newValue) && !((Collection) o).containsAll(oldValue);
					}
				}));
	}

	@Test
	public void changeMapValue() throws TypeNotFoundException
	{
		//given
		final Map<String, String> newValue = new HashMap<>();
		newValue.put("a", "valA");
		newValue.put("b", "valB");

		final Map<String, String> oldValue = new HashMap<>();
		oldValue.put("c", "valC");
		final HashMap<String, String> params = new HashMap<>();
		final String qualifier = ProductModel.SUPERCATEGORIES;
		final ItemModel product = prepareBulkEditData(newValue, oldValue, params, qualifier, DataAttribute.AttributeType.MAP,
				false);
		//when
		handler.perform(customType, adapter, params);

		//then
		verify(objectValueService).setValue(eq(qualifier), same(product), argThat(new ArgumentMatcher<Map<String, String>>()
		{
			@Override
			public boolean matches(final Object o)
			{
				return ((Map<String, String>) o).entrySet()
						.containsAll(CollectionUtils.union(newValue.entrySet(), oldValue.entrySet()));
			}
		}));
	}

	@Test
	public void changeMapValueWithOverride() throws TypeNotFoundException
	{
		//given
		final Map<String, String> newValue = new HashMap<>();
		newValue.put("a", "valA");
		newValue.put("b", "valB");

		final Map<String, String> oldValue = new HashMap<>();
		oldValue.put("c", "valC");
		final HashMap<String, String> params = new HashMap<>();
		final String qualifier = ProductModel.SUPERCATEGORIES;
		final ItemModel product = prepareBulkEditData(newValue, oldValue, params, qualifier, DataAttribute.AttributeType.MAP, true);
		//when
		handler.perform(customType, adapter, params);

		//then
		verify(objectValueService).setValue(eq(qualifier), same(product), argThat(new ArgumentMatcher<Map<String, String>>()
		{
			@Override
			public boolean matches(final Object o)
			{
				return ((Map<String, String>) o).entrySet().containsAll(newValue.entrySet())
						&& !((Map<String, String>) o).entrySet().containsAll(oldValue.entrySet());
			}
		}));
	}

	protected ItemModel prepareBulkEditData(final Object newValue, final Object oldValue, final HashMap<String, String> params,
			final String qualifier, final DataAttribute.AttributeType type, final boolean addQualifierToMerge)
			throws TypeNotFoundException
	{
		final DataAttribute categories = mockDataAttribute(qualifier, type);

		final Attribute attributeCategories = mockAttribute(categories);

		final ItemModel product = mock(ProductModel.class);
		final DataType dataType = mockDataTypeWithAttributes(ProductModel._TYPECODE, Lists.newArrayList(categories));
		when(typeFacade.getType(product)).thenReturn(ProductModel._TYPECODE);
		when(typeFacade.load(ProductModel._TYPECODE)).thenReturn(dataType);

		final AttributeChooserForm attributesForm = new AttributeChooserForm();
		attributesForm.setChosenAttributes(Sets.newHashSet(attributeCategories));

		final ItemModel templateObject = mock(ProductModel.class);
		final BulkEditForm bulkEditForm = new BulkEditForm();
		bulkEditForm.setItemsToEdit(Lists.newArrayList(product));
		bulkEditForm.setAttributesForm(attributesForm);
		bulkEditForm.setTemplateObject(templateObject);
		if (!addQualifierToMerge)
		{
			bulkEditForm.addQualifierToMerge(categories.getQualifier());
		}

		params.put(BulkEditAttributesSelectorRenderer.PARAM_BULK_EDIT_FORM_MODEL_KEY, "bulkEditForm");
		wim.getModel().setValue("bulkEditForm", bulkEditForm);


		final ObjectFacadeOperationResult saveFailure = new ObjectFacadeOperationResult();
		when(objectFacade.save(eq(bulkEditForm.getItemsToEdit()), any(Context.class))).thenReturn(saveFailure);


		when(objectValueService.getValue(attributeCategories.getQualifier(), templateObject)).thenReturn(newValue);
		when(objectValueService.getValue(attributeCategories.getQualifier(), product)).thenReturn(oldValue);
		return product;
	}

	@Test
	public void changeLocalizedValue() throws TypeNotFoundException
	{
		//given
		final DataAttribute categories = mockDataAttribute(ProductModel.SUPERCATEGORIES, DataAttribute.AttributeType.SINGLE);
		when(categories.isLocalized()).thenReturn(true);

		final Attribute attributeCategories = mockAttribute(categories);
		attributeCategories.setSubAttributes(Sets.newHashSet(new Attribute(attributeCategories, Locale.ENGLISH.toLanguageTag()),
				new Attribute(attributeCategories, Locale.TRADITIONAL_CHINESE.toLanguageTag())));

		final ItemModel product = mock(ProductModel.class);
		final DataType dataType = mockDataTypeWithAttributes(ProductModel._TYPECODE, Lists.newArrayList(categories));
		when(typeFacade.getType(product)).thenReturn(ProductModel._TYPECODE);
		when(typeFacade.load(ProductModel._TYPECODE)).thenReturn(dataType);

		final AttributeChooserForm attributesForm = new AttributeChooserForm();
		attributesForm.setChosenAttributes(Sets.newHashSet(attributeCategories));

		final ItemModel templateObject = mock(ProductModel.class);
		final BulkEditForm bulkEditForm = new BulkEditForm();
		bulkEditForm.setItemsToEdit(Lists.newArrayList(product));
		bulkEditForm.setAttributesForm(attributesForm);
		bulkEditForm.setTemplateObject(templateObject);


		final HashMap<String, String> params = new HashMap<>();
		params.put(BulkEditAttributesSelectorRenderer.PARAM_BULK_EDIT_FORM_MODEL_KEY, "bulkEditForm");
		wim.getModel().setValue("bulkEditForm", bulkEditForm);


		final ObjectFacadeOperationResult saveFailure = new ObjectFacadeOperationResult();
		when(objectFacade.save(eq(bulkEditForm.getItemsToEdit()), any(Context.class))).thenReturn(saveFailure);

		final Map<Locale, String> newValue = new HashMap<>();
		newValue.put(Locale.TRADITIONAL_CHINESE, "newValue");
		newValue.put(Locale.ENGLISH, null);

		final Map<Locale, String> oldValue = new HashMap<>();
		oldValue.put(Locale.ENGLISH, "oldValueEnglish");

		when(objectValueService.getValue(attributeCategories.getQualifier(), templateObject)).thenReturn(newValue);
		when(objectValueService.getValue(attributeCategories.getQualifier(), product)).thenReturn(oldValue);
		//when
		handler.perform(customType, adapter, params);

		//then
		verify(objectValueService).setValue(eq(categories.getQualifier()), same(product),
				argThat(new ArgumentMatcher<Collection<String>>()
				{
					@Override
					public boolean matches(final Object o)
					{
						return ((Map) o).size() == 2 && "newValue".equals(((Map) o).get(Locale.TRADITIONAL_CHINESE))
								&& "oldValueEnglish".equals(((Map) o).get(Locale.ENGLISH));
					}
				}));
	}

	@Test
	public void clearLocalizedValue() throws TypeNotFoundException
	{
		//given
		final DataAttribute categories = mockDataAttribute(ProductModel.SUPERCATEGORIES, DataAttribute.AttributeType.SINGLE);
		when(categories.isLocalized()).thenReturn(true);

		final Attribute attributeCategories = mockAttribute(categories);
		attributeCategories.setSubAttributes(Sets.newHashSet(new Attribute(attributeCategories, Locale.ENGLISH.toLanguageTag()),
				new Attribute(attributeCategories, Locale.TRADITIONAL_CHINESE.toLanguageTag())));

		final ItemModel product = mock(ProductModel.class);
		final DataType dataType = mockDataTypeWithAttributes(ProductModel._TYPECODE, Lists.newArrayList(categories));
		when(typeFacade.getType(product)).thenReturn(ProductModel._TYPECODE);
		when(typeFacade.load(ProductModel._TYPECODE)).thenReturn(dataType);

		final AttributeChooserForm attributesForm = new AttributeChooserForm();
		attributesForm.setChosenAttributes(Sets.newHashSet(attributeCategories));

		final ItemModel templateObject = mock(ProductModel.class);
		final BulkEditForm bulkEditForm = new BulkEditForm();
		bulkEditForm.setItemsToEdit(Lists.newArrayList(product));
		bulkEditForm.setAttributesForm(attributesForm);
		bulkEditForm.setTemplateObject(templateObject);
		bulkEditForm.addQualifierToClear(ProductModel.SUPERCATEGORIES);


		final HashMap<String, String> params = new HashMap<>();
		params.put(BulkEditAttributesSelectorRenderer.PARAM_BULK_EDIT_FORM_MODEL_KEY, "bulkEditForm");
		wim.getModel().setValue("bulkEditForm", bulkEditForm);


		final ObjectFacadeOperationResult<Object> saveFailure = new ObjectFacadeOperationResult();
		when(objectFacade.save(eq(bulkEditForm.getItemsToEdit()), any(Context.class))).thenReturn(saveFailure);

		final Map<Locale, String> newValue = new HashMap<>();
		newValue.put(Locale.TRADITIONAL_CHINESE, "newValue");
		newValue.put(Locale.ENGLISH, null);

		final Map<Locale, String> oldValue = new HashMap<>();
		oldValue.put(Locale.ENGLISH, "oldValueEnglish");

		when(objectValueService.getValue(attributeCategories.getQualifier(), templateObject)).thenReturn(newValue);
		when(objectValueService.getValue(attributeCategories.getQualifier(), product)).thenReturn(oldValue);
		//when
		handler.perform(customType, adapter, params);

		//then
		verify(objectValueService).setValue(eq(categories.getQualifier()), same(product),
				argThat(new ArgumentMatcher<Collection<String>>()
				{
					@Override
					public boolean matches(final Object o)
					{
						return ((Map) o).size() == 2 && ((Map) o).get(Locale.TRADITIONAL_CHINESE) == null
								&& ((Map) o).get(Locale.ENGLISH) == null;
					}
				}));
	}

	@Test
	public void clearValue() throws TypeNotFoundException
	{
		//given
		final DataAttribute categories = mockDataAttribute(ProductModel.SUPERCATEGORIES, DataAttribute.AttributeType.SINGLE);

		final Attribute attributeCategories = mockAttribute(categories);

		final ItemModel product = mock(ProductModel.class);
		final DataType dataType = mockDataTypeWithAttributes(ProductModel._TYPECODE, Lists.newArrayList(categories));
		when(typeFacade.getType(product)).thenReturn(ProductModel._TYPECODE);
		when(typeFacade.load(ProductModel._TYPECODE)).thenReturn(dataType);

		final AttributeChooserForm attributesForm = new AttributeChooserForm();
		attributesForm.setChosenAttributes(Sets.newHashSet(attributeCategories));

		final ItemModel templateObject = mock(ProductModel.class);
		final BulkEditForm bulkEditForm = new BulkEditForm();
		bulkEditForm.setItemsToEdit(Lists.newArrayList(product));
		bulkEditForm.setAttributesForm(attributesForm);
		bulkEditForm.setTemplateObject(templateObject);
		bulkEditForm.addQualifierToClear(ProductModel.SUPERCATEGORIES);


		final HashMap<String, String> params = new HashMap<>();
		params.put(BulkEditAttributesSelectorRenderer.PARAM_BULK_EDIT_FORM_MODEL_KEY, "bulkEditForm");
		wim.getModel().setValue("bulkEditForm", bulkEditForm);


		final ObjectFacadeOperationResult saveFailure = new ObjectFacadeOperationResult();
		when(objectFacade.save(eq(bulkEditForm.getItemsToEdit()), any(Context.class))).thenReturn(saveFailure);

		when(objectValueService.getValue(attributeCategories.getQualifier(), templateObject)).thenReturn("a");
		when(objectValueService.getValue(attributeCategories.getQualifier(), product)).thenReturn("b");
		//when
		handler.perform(customType, adapter, params);

		//then
		verify(objectValueService).setValue(eq(categories.getQualifier()), same(product),
				argThat(new ArgumentMatcher<Collection<String>>()
				{
					@Override
					public boolean matches(final Object o)
					{
						return o == null;
					}
				}));
	}


	@Test
	public void doNothingOnMissingForm()
	{
		final Map<String, String> params = new HashMap<>();

		handler.perform(customType, adapter, params);

		verify(adapter, never()).done();
		verify(adapter, never()).cancel();
		verify(adapter, never()).next();
		verify(adapter, never()).back();
		verify(adapter, never()).custom();
	}


	private DataType mockDataTypeWithAttributes(final String typeCode, final Collection<DataAttribute> attributes)
	{
		final DataType dataType = mock(DataType.class);
		when(dataType.getCode()).thenReturn(typeCode);
		when(dataType.getAttributes()).thenReturn(attributes);
		attributes.forEach(attr -> when(dataType.getAttribute(attr.getQualifier())).thenReturn(attr));
		return dataType;
	}

	private DataAttribute mockDataAttribute(final String attribute, final DataAttribute.AttributeType attributeType)
	{
		final DataAttribute da = mock(DataAttribute.class);
		when(da.getQualifier()).thenReturn(attribute);
		when(da.getDefinedType()).thenReturn(DataType.STRING);
		when(da.getValueType()).thenReturn(DataType.STRING);
		when(da.getAttributeType()).thenReturn(attributeType);
		return da;
	}

	protected Attribute mockAttribute(final DataAttribute approval)
	{
		return new Attribute(approval.getQualifier(), approval.getQualifier(), approval.isMandatory());
	}

}
