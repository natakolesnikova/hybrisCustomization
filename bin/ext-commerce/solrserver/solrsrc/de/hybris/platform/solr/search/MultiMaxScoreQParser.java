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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.LuceneQParser;
import org.apache.solr.search.SyntaxError;


public class MultiMaxScoreQParser extends LuceneQParser
{
	float tie = 0.0f;

	/**
	 * Constructor for the QParser
	 *
	 * @param qstr
	 * 		The part of the query string specific to this parser
	 * @param localParams
	 * 		The set of parameters that are specific to this QParser.  See http://wiki.apache.org/solr/LocalParams
	 * @param params
	 * 		The rest of the {@link SolrParams}
	 * @param req
	 * 		The original {@link SolrQueryRequest}.
	 */
	public MultiMaxScoreQParser(final String qstr, final SolrParams localParams, final SolrParams params,
			final SolrQueryRequest req)
	{
		super(qstr, localParams, params, req);

		if (getParam("tie") != null)
		{
			tie = Float.parseFloat(getParam("tie"));
		}
	}

	@Override
	public Query parse() throws SyntaxError
	{
		final Query q = super.parse();

		if (!(q instanceof BooleanQuery))
		{
			return q;
		}

		final BooleanQuery oldQuery = (BooleanQuery) q;
		final BooleanQuery.Builder newQuery = new BooleanQuery.Builder();

		List<Query> disjuncts = null;

		for (final BooleanClause clause : oldQuery.clauses())
		{
			if (clause.isProhibited() || clause.isRequired())
			{
				newQuery.add(clause);
			}
			else
			{
				final Query subQuery = clause.getQuery();
				if (!(subQuery instanceof BooleanQuery))
				{
					if (disjuncts == null)
					{
						disjuncts = new ArrayList<>();
					}

					disjuncts.add(clause.getQuery());
				}
				else
				{
					final List<Query> subQueriesList = new ArrayList<>();
					for (final BooleanClause subQueryClause : ((BooleanQuery) subQuery).clauses())
					{
						subQueriesList.add(subQueryClause.getQuery());
					}
					final DisjunctionMaxQuery subDmq = new DisjunctionMaxQuery(subQueriesList, tie);
					newQuery.add(subDmq, BooleanClause.Occur.SHOULD);
				}
			}
		}

		if (CollectionUtils.isNotEmpty(disjuncts))
		{
			final DisjunctionMaxQuery disjunctionQuery = new DisjunctionMaxQuery(disjuncts, tie);
			newQuery.add(disjunctionQuery, BooleanClause.Occur.SHOULD);
		}

		return newQuery.build();
	}
}
