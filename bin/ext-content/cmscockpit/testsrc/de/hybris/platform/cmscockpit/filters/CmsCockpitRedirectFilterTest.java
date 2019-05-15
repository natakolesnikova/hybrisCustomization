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
package de.hybris.platform.cmscockpit.filters;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CmsCockpitRedirectFilterTest
{

	private static final int PORT = 9001;
	private static final String SSL_PORT = "9002";

	private static final String HTTPS_ADDRESS = "https://localhost:9002/";

	private static final String HTTP_ADDRESS = "http://localhost:9001/";

	@Spy
	CmsCockpitRedirectFilter filter;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	@Test
	public void should_Redirect_Unsecured_Request() throws Exception
	{
		// Arrange
		when(request.isSecure()).thenReturn(Boolean.FALSE);
		when(request.getRequestURL()).thenReturn(new StringBuffer(HTTP_ADDRESS));
		when(request.getLocalPort()).thenReturn(PORT);
		doReturn(SSL_PORT).when(filter).getPort();

		// Act
		filter.doFilter(request, response, filterChain);

		// Assert
		verify(response).sendRedirect(HTTPS_ADDRESS);
		verify(filterChain, never()).doFilter(request, response);
	}

	@Test
	public void should_Not_Redirect_Secured_Request() throws IOException, ServletException
	{
		// Arrange
		when(request.isSecure()).thenReturn(Boolean.TRUE);

		// Act
		filter.doFilter(request, response, filterChain);

		// Assert
		verify(response, never()).sendRedirect(Mockito.any());
		verify(filterChain).doFilter(request, response);
	}

}
