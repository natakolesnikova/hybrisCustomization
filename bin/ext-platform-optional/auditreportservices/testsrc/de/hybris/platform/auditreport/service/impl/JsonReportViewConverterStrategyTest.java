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
package de.hybris.platform.auditreport.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyZeroInteractions;

import de.hybris.platform.audit.view.impl.ReportView;
import de.hybris.platform.auditreport.service.ReportConversionData;
import de.hybris.platform.auditreport.service.ReportGenerationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class JsonReportViewConverterStrategyTest
{
	@Spy
	private JsonReportViewConverterStrategy converterStrategy;

	@Test(expected = ReportGenerationException.class)
	public void testConvertWhenConvertToJsonThrowsException() throws IOException
	{
		// given
		final List<ReportView> reports = new ArrayList<>();
		final Map<String, Object> context = new HashMap<>();

		doThrow(IOException.class).when(converterStrategy).convertToJson(reports, context);

		// when
		converterStrategy.convert(reports, context);

		// then exception
	}

	@Test
	public void testConvertWhenConvertToJsonReturnsBytesAndUseDefaultReportName() throws IOException
	{
		// given
		final List<ReportView> reports = new ArrayList<>();
		final Map<String, Object> context = new HashMap<>();

		final byte[] jsonBytes =
		{ 'j', 's', 'o', 'n' };
		doReturn(jsonBytes).when(converterStrategy).convertToJson(reports, context);

		// when
		final List<ReportConversionData> result = converterStrategy.convert(reports, context);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		final ReportConversionData firstItem = result.get(0);
		assertThat(firstItem.getName()).isSameAs(JsonReportViewConverterStrategy.DEFAULT_FILE_NAME);
		assertThat(firstItem.getData()).isSameAs(jsonBytes);
	}

	@Test
	public void testConvertWhenConvertToJsonReturnsBytesAndUseCustomReportName() throws IOException
	{
		// given
		final List<ReportView> reports = new ArrayList<>();
		final Map<String, Object> context = new HashMap<>();

		final byte[] jsonBytes =
		{ 'n', 'o', 's', 'j' };
		doReturn(jsonBytes).when(converterStrategy).convertToJson(reports, context);

		final String reportName = "Custom" + JsonReportViewConverterStrategy.DEFAULT_FILE_NAME;
		converterStrategy.setReportName(reportName);

		// when
		final List<ReportConversionData> result = converterStrategy.convert(reports, context);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		final ReportConversionData firstItem = result.get(0);
		assertThat(firstItem.getName()).isSameAs(reportName);
		assertThat(firstItem.getData()).isSameAs(jsonBytes);
	}

	@Test
	public void testConvertToJson() throws IOException
	{
		// given
		final Map<String, Object> payload = new LinkedHashMap<>();
		payload.put("text", "ac\"ac");
		payload.put("amp", "'");
		payload.put("quot", "\"");
		payload.put("empty", Collections.emptyList());
		final Date creationTime = new Date();
		final ReportView reportView = ReportView.builder(payload, creationTime, "admin")
				.withContext(Collections.singletonMap("actingUser", "userName")).build();
		final Map<String, Object> context = spy(new HashMap<>());

		// when
		final byte[] result = converterStrategy.convertToJson(Collections.singletonList(reportView), context);

		// then
		verifyZeroInteractions(context);
		assertThat(result).isNotNull();

		final String expected = "[ {" + //
				"  \"payload\" : {" + //
				"    \"text\" : \"ac\\\"ac\"," + //
				"    \"amp\" : \"'\"," + //
				"    \"quot\" : \"\\\"\"," + //
				"    \"empty\" : [ ]" + //
				"  }," + //
				"  \"changingUser\" : \"admin\"," + //
				"  \"context\" : {" + //
				"    \"actingUser\" : \"userName\"" + //
				"  }," + //
				"  \"timestamp\" : " + creationTime.getTime() + //
				"} ]";
		final String jsonText = new String(result).replace("\n", "").replace("\r", "");
		assertThat(jsonText).isEqualTo(expected);
	}
}
