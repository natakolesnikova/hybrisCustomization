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
package de.hybris.platform.acceleratorcms.jalo.restrictions;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.util.localization.Localization;


public class CMSUiExperienceRestriction extends GeneratedCMSUiExperienceRestriction
{
	/**
	 * @deprecated Since 4.6. Use
	 *             {@link de.hybris.platform.acceleratorcms.model.restrictions.CMSUiExperienceRestrictionModel#getDescription()}
	 *             instead.
	 */
	@Deprecated
	@Override
	public String getDescription(final SessionContext ctx)
	{
		final EnumerationValue uiExperience = getUiExperience(ctx);

		final StringBuilder result = new StringBuilder();
		if (uiExperience != null)
		{
			final String localizedString = Localization.getLocalizedString("type.CMSUiExperienceRestriction.description.text");
			result.append(localizedString == null ? "Page only applies on experience level:" : localizedString);
			result.append(' ').append(uiExperience.getName(ctx));
		}
		return result.toString();
	}
}
