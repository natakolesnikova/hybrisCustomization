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
package de.hybris.platform.cmscockpit.resolvers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LiveUrlPageResolverTest
{

	private static final String MOCK_STRING = "This is a mock string";

	@Mock
	private HttpURLConnection httpURLConnection;

	private final InputStream inputStream = new ByteArrayInputStream(MOCK_STRING.getBytes());

	private final LiveUrlPageResolver liveUrlPageResolver = new LiveUrlPageResolver();

	@Test
	public void resolveByWebService_will_return_string() throws Exception
	{
		when(httpURLConnection.getInputStream()).thenReturn(inputStream);
		when(httpURLConnection.getResponseCode()).thenReturn(Status.OK.getStatusCode());
		final String result = liveUrlPageResolver.resolveByWebService(httpURLConnection);
		assertThat(result, is(MOCK_STRING));
	}

	@Test(expected = Exception.class)
	public void resolveByWebService_will_throw_exception() throws Exception
	{
		when(httpURLConnection.getInputStream()).thenReturn(inputStream);
		when(httpURLConnection.getResponseCode()).thenReturn(Status.BAD_REQUEST.getStatusCode());
		liveUrlPageResolver.resolveByWebService(httpURLConnection);
	}

}
