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
package de.hybris.platform.lucene.search.similarities;

import org.apache.lucene.search.similarities.Similarity;
import org.apache.solr.schema.SimilarityFactory;


/**
 * Similarity factory that returns the FixedTFIDFSimilarity
 * 
 * @deprecated Since 6.6, it is no longer used
 */
@Deprecated
public class FixedTFIDFSimilarityFactory extends SimilarityFactory
{

	@Override
	public Similarity getSimilarity()
	{
		return new FixedTFIDFSimilarity();
	}

}
