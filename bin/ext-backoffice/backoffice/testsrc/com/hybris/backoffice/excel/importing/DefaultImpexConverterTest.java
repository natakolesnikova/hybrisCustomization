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
package com.hybris.backoffice.excel.importing;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;


public class DefaultImpexConverterTest
{

	private final DefaultImpexConverter defaultImpexConverter = new DefaultImpexConverter();

	@Test
	public void shouldEscapeValueWhenItContainsNewLineN()
	{
		// given
		final String value = "First line\nSecond line";

		// when
		final String escapedValue = defaultImpexConverter.getValue(value);

		// then
		assertThat(escapedValue).isEqualTo("\"First line\nSecond line\"");
	}

	@Test
	public void shouldEscapeValueWhenItContainsNewLineRN()
	{
		// given
		final String value = "First line\r\nSecond line";

		// when
		final String escapedValue = defaultImpexConverter.getValue(value);

		// then
		assertThat(escapedValue).isEqualTo("\"First line\r\nSecond line\"");
	}

	@Test
	public void shouldEscapeValueWhenItContainsSemicolon()
	{
		// given
		final String value = "First line;Second line";

		// when
		final String escapedValue = defaultImpexConverter.getValue(value);

		// then
		assertThat(escapedValue).isEqualTo("\"First line;Second line\"");
	}

	@Test
	public void shouldNotEscapeValueWhenItContainsBoolean()
	{
		// given
		final Boolean value = Boolean.TRUE;

		// when
		final String escapedValue = defaultImpexConverter.getValue(value);

		// then
		assertThat(escapedValue).isEqualTo(value.toString());
	}

	@Test
	public void shouldNotEscapeValueWhenItContainsNumber()
	{
		// given
		final Double value = 3.14;

		// when
		final String escapedValue = defaultImpexConverter.getValue(value);

		// then
		assertThat(escapedValue).isEqualTo(value.toString());
	}

	@Test
	public void shouldNotEscapeValueWhenItContainsStringWithoutSpecialCharacters()
	{
		// given
		final String value = "First line and second line";

		// when
		final String escapedValue = defaultImpexConverter.getValue(value);

		// then
		assertThat(escapedValue).isEqualTo(value);
	}

	@Test
	public void shouldPrepareHeaderWithoutSpecialAttributes()
	{
		// given
		final ImpexHeaderValue impexHeaderValue = new ImpexHeaderValue(ProductModel.CODE, false);

		// when
		final String headerAttribute = defaultImpexConverter.prepareHeaderAttribute(impexHeaderValue);

		// then
		assertThat(headerAttribute).isEqualTo(String.format("%s", ProductModel.CODE));
	}

	@Test
	public void shouldPrepareHeaderWithUniqueAttribute()
	{
		// given
		final ImpexHeaderValue impexHeaderValue = new ImpexHeaderValue(ProductModel.CODE, true);

		// when
		final String headerAttribute = defaultImpexConverter.prepareHeaderAttribute(impexHeaderValue);

		// then
		assertThat(headerAttribute).isEqualTo(String.format("%s[unique=true]", ProductModel.CODE));
	}

	@Test
	public void shouldPrepareHeaderWithLangAttribute()
	{
		// given
		final ImpexHeaderValue impexHeaderValue = new ImpexHeaderValue(ProductModel.CODE, false, "en");

		// when
		final String headerAttribute = defaultImpexConverter.prepareHeaderAttribute(impexHeaderValue);

		// then
		assertThat(headerAttribute).isEqualTo(String.format("%s[lang=en]", ProductModel.CODE));
	}

	@Test
	public void shouldPrepareHeaderWithUniqueAndLangAttributes()
	{
		// given
		final ImpexHeaderValue impexHeaderValue = new ImpexHeaderValue(ProductModel.CODE, true, "en");

		// when
		final String headerAttribute = defaultImpexConverter.prepareHeaderAttribute(impexHeaderValue);

		// then
		assertThat(headerAttribute).isEqualTo(String.format("%s[unique=true,lang=en]", ProductModel.CODE));
	}

	@Test
	public void shouldPrepareHeaderWithDateAttribute()
	{
		// given
		final ImpexHeaderValue impexHeaderValue = new ImpexHeaderValue(ProductModel.CODE, false, "", "M/d/yy h:mm a");

		// when
		final String headerAttribute = defaultImpexConverter.prepareHeaderAttribute(impexHeaderValue);

		// then
		assertThat(headerAttribute).isEqualTo(String.format("%s[dateformat=M/d/yy h:mm a]", ProductModel.CODE));
	}

	@Test
	public void shouldPrepareImpexHeader()
	{
		// given
		final ImpexForType impexForType = new ImpexForType(ProductModel._TYPECODE);
		impexForType.putValue(0, new ImpexHeaderValue(ProductModel.CODE, true), "");
		impexForType.putValue(0, new ImpexHeaderValue(ProductModel.NAME, false, "en"), "");

		// when
		final String impexHeader = defaultImpexConverter.prepareImpexHeader(impexForType);

		// then
		assertThat(impexHeader).isEqualTo("INSERT_UPDATE Product;code[unique=true];name[lang=en];\n");
	}

	@Test
	public void shouldReturnTrueWhenAllUniqueValuesAreNotEmpty()
	{
		// given
		final Map<ImpexHeaderValue, Object> row = new HashMap<>();
		row.put(new ImpexHeaderValue(ProductModel.CODE, true), "notEmptyValue");
		row.put(new ImpexHeaderValue(ProductModel.NAME, false), "notEmptyValue");
		row.put(new ImpexHeaderValue(ProductModel.DELIVERYTIME, false), null);
		row.put(new ImpexHeaderValue(ProductModel.DETAIL, false), "");
		row.put(new ImpexHeaderValue(ProductModel.CATALOGVERSION, true), "notEmptyValue");

		// when
		final boolean result = defaultImpexConverter.areUniqueAttributesPopulated(row);

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void shouldReturnFalseWhenNotAllUniqueValuesAreNotEmpty()
	{
		// given
		final Map<ImpexHeaderValue, Object> row = new HashMap<>();
		row.put(new ImpexHeaderValue(ProductModel.CODE, true), "notEmptyValue");
		row.put(new ImpexHeaderValue(ProductModel.NAME, false), "notEmptyValue");
		row.put(new ImpexHeaderValue(ProductModel.DELIVERYTIME, false), null);
		row.put(new ImpexHeaderValue(ProductModel.DETAIL, false), "");
		row.put(new ImpexHeaderValue(ProductModel.CATALOGVERSION, true), "");

		// when
		final boolean result = defaultImpexConverter.areUniqueAttributesPopulated(row);

		// then
		assertThat(result).isFalse();
	}

	@Test
	public void shouldGeneratedSingleLineForImpexScript()
	{
		// given
		final ImpexForType impexForType = new ImpexForType(ProductModel._TYPECODE);
		impexForType.putValue(0, new ImpexHeaderValue(ProductModel.CODE, true), "ProductCode");
		impexForType.putValue(0, new ImpexHeaderValue(ProductModel.NAME, false, "en"), "ProductName");

		// when
		final String singleRow = defaultImpexConverter.prepareImpexRows(impexForType);

		// then
		assertThat(singleRow).isEqualTo(";ProductCode;ProductName;");
	}

	@Test
	public void shouldGeneratedMultiLineForImpexScript()
	{
		// given
		final ImpexForType impexForType = new ImpexForType(ProductModel._TYPECODE);
		impexForType.putValue(0, new ImpexHeaderValue(ProductModel.CODE, true), "First product code");
		impexForType.putValue(0, new ImpexHeaderValue(ProductModel.NAME, false, "en"), "First product name");

		impexForType.putValue(1, new ImpexHeaderValue(ProductModel.CODE, true), "Second product code");
		impexForType.putValue(1, new ImpexHeaderValue(ProductModel.NAME, false, "en"), "Second product name");

		// when
		final String singleRow = defaultImpexConverter.prepareImpexRows(impexForType);

		// then
		assertThat(singleRow).isEqualTo(";First product code;First product name;\n;Second product code;Second product name;");
	}

	@Test
	public void shouldGeneratedMultiLineForImpexScriptWithEmptyNotUniqueValues()
	{
		// given
		final ImpexForType impexForType = new ImpexForType(ProductModel._TYPECODE);
		impexForType.putValue(0, new ImpexHeaderValue(ProductModel.CODE, true), "First product code");
		impexForType.putValue(0, new ImpexHeaderValue(ProductModel.NAME, false, "en"), "First product name");

		impexForType.putValue(1, new ImpexHeaderValue(ProductModel.CODE, true), "Second product code");
		impexForType.putValue(1, new ImpexHeaderValue(ProductModel.NAME, false, "en"), "");

		// when
		final String singleRow = defaultImpexConverter.prepareImpexRows(impexForType);

		// then
		assertThat(singleRow).isEqualTo(";First product code;First product name;\n;Second product code;;");
	}

	@Test
	public void shouldGeneratedMultiLineForImpexScriptOnlyWithLinesWhichHaveAllUniqueValues()
	{
		// given
		final ImpexForType impexForType = new ImpexForType(ProductModel._TYPECODE);
		impexForType.putValue(0, new ImpexHeaderValue(ProductModel.CODE, true), "First product code");
		impexForType.putValue(0, new ImpexHeaderValue(ProductModel.NAME, false, "en"), "First product name");

		impexForType.putValue(1, new ImpexHeaderValue(ProductModel.CODE, true), "");
		impexForType.putValue(1, new ImpexHeaderValue(ProductModel.NAME, false, "en"), "");

		impexForType.putValue(1, new ImpexHeaderValue(ProductModel.CODE, true), "Third product code");
		impexForType.putValue(1, new ImpexHeaderValue(ProductModel.NAME, false, "en"), "Third product name");

		// when
		final String singleRow = defaultImpexConverter.prepareImpexRows(impexForType);

		// then
		assertThat(singleRow).isEqualTo(";First product code;First product name;\n;Third product code;Third product name;");
	}

	@Test
	public void shouldGenerateWholeImpexScriptForSingleTypeCode()
	{
		// given
		final Impex impex = new Impex();
		final ImpexForType impexForProduct = impex.findUpdates(ProductModel._TYPECODE);
		impexForProduct.putValue(0, new ImpexHeaderValue(ProductModel.CODE, true), "First product code");
		impexForProduct.putValue(0, new ImpexHeaderValue(ProductModel.NAME, false, "en"), "First product name");
		impexForProduct.putValue(1, new ImpexHeaderValue(ProductModel.CODE, true), "Second product code");
		impexForProduct.putValue(1, new ImpexHeaderValue(ProductModel.NAME, false, "en"), "Second product name");

		// when
		final String generatedImpex = defaultImpexConverter.convert(impex);

		// then
		assertThat(generatedImpex)
				.isEqualTo(
						"INSERT_UPDATE Product;code[unique=true];name[lang=en];\n;First product code;First product name;\n;Second product code;Second product name;\n\n");
	}

	@Test
	public void shouldGenerateWholeImpexScriptForManyTypeCodes()
	{
		// given
		final Impex impex = new Impex();
		final ImpexForType impexForProduct = impex.findUpdates(ProductModel._TYPECODE);
		impexForProduct.putValue(0, new ImpexHeaderValue(ProductModel.CODE, true), "First product code");
		impexForProduct.putValue(0, new ImpexHeaderValue(ProductModel.NAME, false, "en"), "First product name");
		impexForProduct.putValue(1, new ImpexHeaderValue(ProductModel.CODE, true), "Second product code");
		impexForProduct.putValue(1, new ImpexHeaderValue(ProductModel.NAME, false, "en"), "Second product name");

		final ImpexForType impexForCatalog = impex.findUpdates(CatalogModel._TYPECODE);
		impexForCatalog.putValue(0, new ImpexHeaderValue(CatalogModel.ID, true), "Clothing");

		// when
		final String generatedImpex = defaultImpexConverter.convert(impex);

		// then
		assertThat(generatedImpex)
				.isEqualTo(
						"INSERT_UPDATE Catalog;id[unique=true];\n;Clothing;\n\nINSERT_UPDATE Product;code[unique=true];name[lang=en];\n;First product code;First product name;\n;Second product code;Second product name;\n\n");
	}
}
