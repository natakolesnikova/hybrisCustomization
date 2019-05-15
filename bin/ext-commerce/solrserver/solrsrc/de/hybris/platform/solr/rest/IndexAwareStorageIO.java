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
package de.hybris.platform.solr.rest;

import static org.apache.solr.rest.ManagedResourceStorage.STORAGE_DIR_INIT_ARG;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.solr.cloud.ZkSolrResourceLoader;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.rest.ManagedResourceStorage.FileStorageIO;
import org.apache.solr.rest.ManagedResourceStorage.StorageIO;
import org.apache.solr.rest.ManagedResourceStorage.ZooKeeperStorageIO;


/**
 * {@link StorageIO} implementation that keeps resources in an index (core, collection) specific location.
 */
public class IndexAwareStorageIO implements StorageIO
{
	public static final String MANAGED_RESOURCES_DIR = "managed";

	public static final String COLLECTION_PROPERTY = "solr.core.collection";
	public static final String INSTANCE_DIR_PROPERTY = "solr.core.instanceDir";

	private volatile StorageIO delegate;

	protected StorageIO getDelegate()
	{
		return delegate;
	}

	@Override
	public void configure(final SolrResourceLoader resourceLoader, final NamedList<String> initArgs) throws SolrException
	{
		if (resourceLoader instanceof ZkSolrResourceLoader)
		{
			final String collection = resourceLoader.getCoreProperties().getProperty(COLLECTION_PROPERTY);
			if (collection == null || collection.isEmpty())
			{
				throw new SolrException(ErrorCode.SERVER_ERROR, "Failed to detect collection");
			}

			final SolrZkClient zkClient = ((ZkSolrResourceLoader) resourceLoader).getZkController().getZkClient();
			final String znodeBase = "/collections/" + collection + "/" + MANAGED_RESOURCES_DIR;

			delegate = new ZooKeeperStorageIO(zkClient, znodeBase);
		}
		else
		{
			initArgs.remove(STORAGE_DIR_INIT_ARG);

			final String instanceDir = resourceLoader.getCoreProperties().getProperty(INSTANCE_DIR_PROPERTY);
			if (instanceDir == null || instanceDir.isEmpty())
			{
				throw new SolrException(ErrorCode.SERVER_ERROR, "Failed to detect instance dir");
			}

			final File storageDir = new File(instanceDir, MANAGED_RESOURCES_DIR);

			initArgs.add(STORAGE_DIR_INIT_ARG, storageDir.getAbsolutePath());

			delegate = new FileStorageIO();
		}

		getDelegate().configure(resourceLoader, initArgs);
	}

	@Override
	public boolean delete(final String storedResourceId) throws IOException
	{
		return getDelegate().delete(storedResourceId);
	}

	@Override
	public boolean exists(final String storedResourceId) throws IOException
	{
		return getDelegate().exists(storedResourceId);
	}

	@Override
	public String getInfo()
	{
		return getDelegate().getInfo();
	}

	@Override
	public InputStream openInputStream(final String storedResourceId) throws IOException
	{
		return getDelegate().openInputStream(storedResourceId);
	}

	@Override
	public OutputStream openOutputStream(final String storedResourceId) throws IOException
	{
		return getDelegate().openOutputStream(storedResourceId);
	}
}
