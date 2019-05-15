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
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import de.hybris.platform.store.BaseStoreModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * This ValueProvider will provide a boolean flag to indicate if this product is in stock. This can be used to sort the
 * search results by in stock status.
 */
public class ProductInStockFlagValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
	private FieldNameProvider fieldNameProvider;
	private CommerceStockService commerceStockService;

	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	protected CommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	@Required
	public void setCommerceStockService(final CommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof ProductModel)
		{
			final ProductModel product = (ProductModel) model;

			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			final BaseSiteModel baseSiteModel = indexConfig.getBaseSite();

			if (baseSiteModel != null && baseSiteModel.getStores() != null && !baseSiteModel.getStores().isEmpty()
					&& getCommerceStockService().isStockSystemEnabled(baseSiteModel.getStores().get(0)))
			{
				fieldValues.addAll(createFieldValue(product, indexConfig.getBaseSite().getStores().get(0), indexedProperty));
			}
			else
			{
				fieldValues.addAll(createFieldValue(product, null, indexedProperty));
			}
			return fieldValues;
		}
		else
		{
			throw new FieldValueProviderException("Cannot get stock of non-product item");
		}
	}

	protected List<FieldValue> createFieldValue(final ProductModel product, final BaseStoreModel baseStore,
			final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		if (baseStore != null)
		{
			addFieldValues(fieldValues, indexedProperty, Boolean.valueOf(isInStock(product, baseStore)));
		}
		else
		{
			addFieldValues(fieldValues, indexedProperty, Boolean.TRUE);
		}

		return fieldValues;
	}

	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty, final Object value)
	{
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}

	protected StockLevelStatus getProductStockLevelStatus(final ProductModel product, final BaseStoreModel baseStore)
	{
		return getCommerceStockService().getStockLevelStatusForProductAndBaseStore(product, baseStore);
	}

	protected boolean isInStock(final ProductModel product, final BaseStoreModel baseStore)
	{
		return isInStock(getProductStockLevelStatus(product, baseStore));
	}

	protected boolean isInStock(final StockLevelStatus stockLevelStatus)
	{
		return stockLevelStatus != null && !StockLevelStatus.OUTOFSTOCK.equals(stockLevelStatus);
	}
}
