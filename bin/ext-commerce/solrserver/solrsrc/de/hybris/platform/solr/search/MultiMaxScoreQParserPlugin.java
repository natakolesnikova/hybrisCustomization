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
package de.hybris.platform.solr.search;

import de.hybris.platform.solr.search.MultiMaxScoreQParser;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;

public class MultiMaxScoreQParserPlugin extends QParserPlugin
{
	@Override
	public QParser createParser(final String qstr, final SolrParams localParams, final SolrParams params,
			final SolrQueryRequest req)
	{
		return new MultiMaxScoreQParser(qstr, localParams, params, req);
	}
}
