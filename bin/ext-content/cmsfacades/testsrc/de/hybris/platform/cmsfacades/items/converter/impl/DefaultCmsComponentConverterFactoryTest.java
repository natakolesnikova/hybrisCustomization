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
package de.hybris.platform.cmsfacades.items.converter.impl;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCmsComponentConverterFactoryTest
{

	private final Map<Class<? extends AbstractCMSComponentModel>, AbstractPopulatingConverter<AbstractCMSComponentModel, AbstractCMSComponentData>> converters = new HashMap<>();

	private DefaultCmsComponentConverterFactory cmsComponentConverterFactory;

	@Mock
	private AbstractPopulatingConverter<AbstractCMSComponentModel, AbstractCMSComponentData> converter;

	@Before
	public void setUp()
	{
		converters.put(CMSParagraphComponentModel.class, converter);
		converters.put(AbstractCMSComponentModel.class, converter);
		cmsComponentConverterFactory = new DefaultCmsComponentConverterFactory(converters);
	}

	@Test
	public void shouldGetConverterWithParagraphModel()
	{
		assertThat(cmsComponentConverterFactory.getConverter(CMSParagraphComponentModel.class).isPresent(), is(Boolean.TRUE));
	}

	@Test
	public void shouldGetDefaultConverter()
	{
		assertThat(cmsComponentConverterFactory.getConverter(CMSLinkComponentModel.class).isPresent(), is(Boolean.TRUE));
	}
}
