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
package de.hybris.platform.voucher.jalo;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloImplementationManager;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.price.PriceFactory;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.order.TestPriceFactory;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.voucher.model.PromotionVoucherModel;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;


@Ignore
public abstract class AbstractVoucherTest extends ServicelayerTest
{

	private ProductModel product;
	private UnitModel unit;
	private PromotionVoucherModel promotionVoucher;
	private DeliveryModeModel deliveryMode;
	private PriceFactory prevPriceFactory;
	@Resource
	private ModelService modelService;


	private double discountAmount;
	protected static final double DELIVERYCOST = TestDeliveryMode.deliveryCost;

	@Before
	public void setUp() throws ConsistencyCheckException
	{
		// mess with delivery mode - must be first
		JaloImplementationManager.replaceCoreJaloClass(DeliveryMode.class, TestDeliveryMode.class);

		setDeliveryMode(new DeliveryModeModel());
		getDeliveryMode().setCode("testDeliveryModel");
		getDeliveryMode().setActive(Boolean.TRUE);
		getModelService().save(getDeliveryMode());

		setDiscountAmount(10d);

		setUnit(getModelService().create(UnitModel.class));
		getUnit().setCode("myUnit");
		getUnit().setName("myUnit");
		getUnit().setUnitType("test");

		final CatalogModel catalog = getModelService().create(CatalogModel.class);
		catalog.setId("myCatalog");
		catalog.setDefaultCatalog(Boolean.TRUE);

		final CatalogVersionModel catalogVersion = getModelService().create(CatalogVersionModel.class);
		catalogVersion.setCatalog(catalog);
		catalogVersion.setVersion("Online");
		catalogVersion.setActive(Boolean.TRUE);

		setProduct(getModelService().create(ProductModel.class));
		getProduct().setApprovalStatus(ArticleApprovalStatus.APPROVED);
		getProduct().setCatalogVersion(catalogVersion);
		getProduct().setCode("myProduct");
		getProduct().setUnit(getUnit());

		final CurrencyModel curr = getModelService().create(CurrencyModel.class);
		curr.setIsocode("EUR");
		curr.setBase(Boolean.TRUE);
		curr.setSymbol("E");

		setPromotionVoucher(getModelService().create(PromotionVoucherModel.class));

		getPromotionVoucher().setCode("testFreeSHippingVoucher");
		getPromotionVoucher().setFreeShipping(Boolean.TRUE);
		getPromotionVoucher().setVoucherCode("123");
		getPromotionVoucher().setValue(Double.valueOf(getDiscountAmount()));
		getPromotionVoucher().setCurrency(curr);
		getPromotionVoucher().setRedemptionQuantityLimitPerUser(Integer.valueOf(1));
		getPromotionVoucher().setRedemptionQuantityLimit(Integer.valueOf(10));

		getModelService().saveAll();

		//Setup items
		prevPriceFactory = jaloSession.getSessionContext().getPriceFactory();
		final TestPriceFactory priceFactory = new TestPriceFactory();
		jaloSession.getSessionContext().setPriceFactory(priceFactory);

		//make the test price factory return 15EUR for the test product
		priceFactory.setBasePrice((Product) getModelService().getSource(getProduct()),
				new PriceValue(curr.getIsocode(), 15, false));

	}

	@After
	public void tearDown()
	{
		jaloSession.getSessionContext().setPriceFactory(prevPriceFactory);
		// switch back jalo class for delivery mode
		JaloImplementationManager.replaceCoreJaloClass(DeliveryMode.class, DeliveryMode.class);
	}

	/**
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	protected void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the deliveryMode
	 */
	protected DeliveryModeModel getDeliveryMode()
	{
		return deliveryMode;
	}

	/**
	 * @param deliveryMode
	 *           the deliveryMode to set
	 */
	protected void setDeliveryMode(final DeliveryModeModel deliveryMode)
	{
		this.deliveryMode = deliveryMode;
	}

	/**
	 * @return the product
	 */
	protected ProductModel getProduct()
	{
		return product;
	}

	/**
	 * @param product
	 *           the product to set
	 */
	protected void setProduct(final ProductModel product)
	{
		this.product = product;
	}

	/**
	 * @return the promotionVoucher
	 */
	protected PromotionVoucherModel getPromotionVoucher()
	{
		return promotionVoucher;
	}

	/**
	 * @param promotionVoucher
	 *           the promotionVoucher to set
	 */
	protected void setPromotionVoucher(final PromotionVoucherModel promotionVoucher)
	{
		this.promotionVoucher = promotionVoucher;
	}

	/**
	 * @return the unit
	 */
	protected UnitModel getUnit()
	{
		return unit;
	}

	/**
	 * @param unit
	 *           the unit to set
	 */
	protected void setUnit(final UnitModel unit)
	{
		this.unit = unit;
	}

	/**
	 * @return the discountAmount
	 */
	protected double getDiscountAmount()
	{
		return discountAmount;
	}

	/**
	 * @param discountAmount
	 *           the discountAmount to set
	 */
	protected void setDiscountAmount(final double discountAmount)
	{
		this.discountAmount = discountAmount;
	}

}
