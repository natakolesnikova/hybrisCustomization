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
package de.hybris.platform.solrfacetsearch.converters.populator;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.solrfacetsearch.config.EndpointURL;
import de.hybris.platform.solrfacetsearch.config.SolrServerMode;
import de.hybris.platform.solrfacetsearch.enums.SolrServerModes;
import de.hybris.platform.solrfacetsearch.model.config.SolrEndpointUrlModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;
import de.hybris.platform.util.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class DefaultSolrServerConfigPopulatorIntegrationTest extends ServicelayerBaseTest
{
	private static final String DB_MASTER_URL = "http://dbmaster:123";
	private static final String DB_SLAVE_URL = "http://dbslave:123";
	private static final String PROPERTIES_MASTER_URL = "http://propertiesmaster:123";
	private static final String PROPERTIES_SLAVE_URL = "http://propertiesslave:123";
	private static final String DB_MODE = "STANDALONE";
	private static final String PROPERTIES_MODE = "CLOUD";

	@Resource
	private DefaultSolrServerConfigPopulator defaultSolrServerConfigPopulator;

	@Test
	public void shouldReturnEmptyListWhenThereAreNoURLsInDBAndPropertyIsNotSet()
	{
		final SolrServerConfigModel solrConfig = givenSolrConfigWithoutURLs();

		final List<EndpointURL> urls = defaultSolrServerConfigPopulator.populateEndpointUrls(solrConfig);

		assertThat(urls).isNotNull().isEmpty();
	}

	@Test
	public void shouldReturnURLsFromPropertiesWhenThereAreNoURLsInDBAndPropertyIsSet()
	{
		final SolrServerConfigModel solrConfig = givenSolrConfig();

		setUrlEndpointPropertyFor(solrConfig);
		final List<EndpointURL> urls = defaultSolrServerConfigPopulator.populateEndpointUrls(solrConfig);

		assertThat(urls).isNotNull().hasSize(2).doesNotContainNull().extracting(url -> url.getUrl()).contains(PROPERTIES_MASTER_URL,
				PROPERTIES_SLAVE_URL);
	}

	@Test
	public void shouldReturnURLsFromDBWhenThereAreURLsInDBAndPropertyIsNotSet()
	{
		final SolrServerConfigModel solrConfig = givenSolrConfig();

		final List<EndpointURL> urls = defaultSolrServerConfigPopulator.populateEndpointUrls(solrConfig);

		assertThat(urls).isNotNull().hasSize(2).doesNotContainNull().extracting(url -> url.getUrl()).contains(DB_MASTER_URL,
				DB_SLAVE_URL);
	}

	@Test
	public void shouldIgnoreURLsFromDBWhenPropertyIsSet()
	{
		final SolrServerConfigModel solrConfig = givenSolrConfig();

		setUrlEndpointPropertyFor(solrConfig);
		final List<EndpointURL> urls = defaultSolrServerConfigPopulator.populateEndpointUrls(solrConfig);

		assertThat(urls).isNotNull().hasSize(2).doesNotContainNull().extracting(url -> url.getUrl()).contains(PROPERTIES_MASTER_URL,
				PROPERTIES_SLAVE_URL);
	}

	@Test
	public void shouldReturnModeFromPropertiesWhenThereAreNoModeInDBAndPropertyIsSet()
	{
		final SolrServerConfigModel solrConfig = givenSolrConfig();

		setModePropertyFor(solrConfig);
		final SolrServerMode mode = defaultSolrServerConfigPopulator.populateConfigServerMode(solrConfig);

		assertThat(mode).isNotNull().isEqualTo(SolrServerMode.valueOf(PROPERTIES_MODE));
	}

	@Test
	public void shouldReturnModeFromDBWhenThereIsModeInDBAndPropertyIsNotSet()
	{
		final SolrServerConfigModel solrConfig = givenSolrConfig();

		final SolrServerMode mode = defaultSolrServerConfigPopulator.populateConfigServerMode(solrConfig);

		assertThat(mode).isNotNull().isEqualTo(SolrServerMode.valueOf(DB_MODE));
	}

	@Test
	public void shouldIgnoreModeFromDBWhenPropertyIsSet()
	{
		final SolrServerConfigModel solrConfig = givenSolrConfig();

		setModePropertyFor(solrConfig);
		final SolrServerMode mode = defaultSolrServerConfigPopulator.populateConfigServerMode(solrConfig);

		assertThat(mode).isNotNull().isEqualTo(SolrServerMode.valueOf(PROPERTIES_MODE));
	}

	@Test
	public void shouldReturnEmptyURLListForBlankConfigName()
	{
		// given
		final SolrServerConfigModel serverConfig = givenSolrConfigWithoutURLs();
		setConfigUrls(serverConfig, "");

		// when
		final List<EndpointURL> urls = defaultSolrServerConfigPopulator.populateEndpointUrls(serverConfig);

		// then
		assertThat(urls).isNotNull().isEmpty();
	}

	@Test
	public void shouldReturnSingleMasterURLForSingleValue()
	{
		// given
		final SolrServerConfigModel serverConfig = givenSolrConfigWithoutURLs();
		setConfigUrls(serverConfig, " http://master:12345\t");

		// when
		final List<EndpointURL> urls = defaultSolrServerConfigPopulator.populateEndpointUrls(serverConfig);

		// then
		assertThat(urls).isNotNull().hasSize(1).doesNotContainNull();

		final EndpointURL master = urls.get(0);
		assertThat(master.isMaster()).isTrue();
		assertThat(master.getUrl()).isEqualTo("http://master:12345");
	}

	@Test
	public void shouldReturnMultipleURLsAndFirstMustBeMarkedAsMaster1()
	{
		// given
		final SolrServerConfigModel serverConfig = givenSolrConfigWithoutURLs();
		setConfigUrls(serverConfig, " http://master:12345\t http://slave:1234      ");

		// when
		final List<EndpointURL> urls = defaultSolrServerConfigPopulator.populateEndpointUrls(serverConfig);

		// then
		assertThat(urls).isNotNull().hasSize(2).doesNotContainNull();

		final EndpointURL master = urls.get(0);
		assertThat(master.isMaster()).isTrue();
		assertThat(master.getUrl()).isEqualTo("http://master:12345");
		assertThat(master.getModifiedTime()).isNotNull();

		final EndpointURL slave = urls.get(1);
		assertThat(slave.isMaster()).isFalse();
		assertThat(slave.getUrl()).isEqualTo("http://slave:1234");
		assertThat(slave.getModifiedTime()).isNotNull();

		assertThat(master.getModifiedTime()).isEqualTo(slave.getModifiedTime());
	}

	@Test
	public void shouldReturnMultipleURLsAndFirstMustBeMarkedAsMaster2()
	{
		// given
		final SolrServerConfigModel serverConfig = givenSolrConfigWithoutURLs();
		setConfigUrls(serverConfig, " host1:12345 , 	host2:3872 ");

		// when
		final List<EndpointURL> urls = defaultSolrServerConfigPopulator.populateEndpointUrls(serverConfig);

		// then
		assertThat(urls).isNotNull().hasSize(2).doesNotContainNull();

		final EndpointURL master = urls.get(0);
		assertThat(master.isMaster()).isTrue();
		assertThat(master.getUrl()).isEqualTo("host1:12345");
		assertThat(master.getModifiedTime()).isNotNull();

		final EndpointURL slave = urls.get(1);
		assertThat(slave.isMaster()).isFalse();
		assertThat(slave.getUrl()).isEqualTo("host2:3872");
		assertThat(slave.getModifiedTime()).isNotNull();

		assertThat(master.getModifiedTime()).isEqualTo(slave.getModifiedTime());
	}

	private void setUrlEndpointPropertyFor(final SolrServerConfigModel config)
	{
		Config.setParameter("solr.config." + config.getName() + ".urls", PROPERTIES_MASTER_URL + " " + PROPERTIES_SLAVE_URL);
	}

	private void setModePropertyFor(final SolrServerConfigModel config)
	{
		Config.setParameter("solr.config." + config.getName() + ".mode", PROPERTIES_MODE);
	}

	private SolrServerConfigModel givenSolrConfig()
	{
		final SolrServerConfigModel config = givenSolrConfigWithoutURLs();

		final SolrEndpointUrlModel master = new SolrEndpointUrlModel();
		master.setMaster(true);
		master.setUrl(DB_MASTER_URL);

		final SolrEndpointUrlModel slave = new SolrEndpointUrlModel();
		slave.setMaster(false);
		slave.setUrl(DB_SLAVE_URL);

		config.setMode(SolrServerModes.valueOf(DB_MODE));

		config.setSolrEndpointUrls(Arrays.asList(master, slave));
		return config;
	}

	private SolrServerConfigModel givenSolrConfigWithoutURLs()
	{
		final String configName = "TEST_CONFIG" + UUID.randomUUID();
		final SolrServerConfigModel config = new SolrServerConfigModel();
		config.setName(configName);
		config.setSolrEndpointUrls(Collections.emptyList());

		return config;
	}

	private void setConfigUrls(final SolrServerConfigModel serverConfig, final String value)
	{
		Config.setParameter("solr.config." + serverConfig.getName() + ".urls", value);
	}
}
