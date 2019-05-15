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

import org.apache.lucene.search.similarities.ClassicSimilarity;


/**
 * Similarity implementation that uses a fixed TF and IDF of 1.0 for scoring. This is intended to be used for boosted
 * fields where standard full-text scoring metrics are not relevant.
 * 
 * @deprecated Since 6.6, it is no longer used
 */
@Deprecated
public class FixedTFIDFSimilarity extends ClassicSimilarity
{
	/**
	 * Always returns 1.0 as inverse document frequency
	 */
	@Override
	public float idf(final long docFreq, final long numDocs)
	{
		return 1.0F;
	}

	/**
	 * Always returns 1.0 as term frequency
	 */
	@Override
	public float tf(final float freq)
	{
		return 1.0F;
	}
}
