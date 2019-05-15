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
package de.hybris.platform.cmsfacades.items.validator.consumer;


import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;

import java.util.Locale;
import java.util.Map;


/**
 * Java bean that contains everything related to Localized validations
 *
 * @deprecated since 6.6
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.6")
public class LocalizedValidationData
{

	private String fieldName;

	private String value;

	private Locale locale;

	private boolean requiredLanguage;

	private Map<String, String> optionals;

	public LocalizedValidationData(final String fieldName, final String value, final Locale locale,
			final boolean requiredLanguage)
	{
		this(fieldName, value, locale, requiredLanguage, null);
	}

	public LocalizedValidationData(final String fieldName, final String value, final Locale locale,
			final boolean requiredLanguage, final Map<String, String> optionals)
	{
		this.fieldName = fieldName;
		this.value = value;
		this.locale = locale;
		this.requiredLanguage = requiredLanguage;
		this.optionals = optionals;
	}

	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldName(final String fieldName)
	{
		this.fieldName = fieldName;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(final String value)
	{
		this.value = value;
	}

	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(final Locale locale)
	{
		this.locale = locale;
	}

	public boolean isRequiredLanguage()
	{
		return requiredLanguage;
	}

	public void setRequiredLanguage(final boolean requiredLanguage)
	{
		this.requiredLanguage = requiredLanguage;
	}

	public Map<String, String> getOptionals()
	{
		return optionals;
	}

	public void setOptionals(final Map<String, String> optionals)
	{
		this.optionals = optionals;
	}

	public static class Builder
	{
		private String fieldName;
		private String value;
		private Locale locale;
		private boolean requiredLanguage;
		private Map<String, String> optionals;

		public Builder setFieldName(final String fieldName)
		{
			this.fieldName = fieldName;
			return this;
		}

		public Builder setValue(final String value)
		{
			this.value = value;
			return this;
		}

		public Builder setLocale(final Locale locale)
		{
			this.locale = locale;
			return this;
		}

		public Builder setRequiredLanguage(final boolean requiredLanguage)
		{
			this.requiredLanguage = requiredLanguage;
			return this;
		}

		public Builder setOptionals(final Map<String, String> optionals)
		{
			this.optionals = optionals;
			return this;
		}

		public LocalizedValidationData build()
		{
			return new LocalizedValidationData(fieldName, value, locale, requiredLanguage, optionals);
		}
	}

}
