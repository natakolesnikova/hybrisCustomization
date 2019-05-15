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
package de.hybris.platform.cmsfacades.items.validator;


import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmsfacades.common.validator.LocalizedTypeValidator;
import de.hybris.platform.cmsfacades.common.validator.LocalizedValidator;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.CMSLinkComponentData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.google.common.base.Strings;


/**
 * Validates fields of {@link CMSLinkComponentData}
 */
public class LinkComponentValidator implements Validator
{
	protected static final String LINK_NAME = "linkName";
	protected static final String CATEGORY = "category";
	protected static final String CONTENT_PAGE = "contentPage";
	protected static final String PRODUCT = "product";
	protected static final String URL = "url";

	private LocalizedValidator localizedValidator;
	private LocalizedTypeValidator localizedStringValidator;
	private BiPredicate<String, Class<?>> itemModelExistsPredicate;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return CMSLinkComponentData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final CMSLinkComponentData target = (CMSLinkComponentData) obj;

		final Function<String, String> linkNameGetter = (language) -> Optional.ofNullable(target.getLinkName())
				.orElse(Collections.emptyMap()).get(language);
		getLocalizedValidator().validateRequiredLanguages(//
				(language, value) -> getLocalizedStringValidator().validate(language, LINK_NAME, value, errors), //
				linkNameGetter, errors);

		verifyOnlyOneTypeProvided(target, errors);

		if (Objects.nonNull(target.getCategory())
				&& !getItemModelExistsPredicate().test(target.getCategory(), CategoryModel.class))
		{
			errors.rejectValue(CATEGORY, CmsfacadesConstants.FIELD_DOES_NOT_EXIST);
		}
		else if (Objects.nonNull(target.getContentPage())
				&& !getItemModelExistsPredicate().test(target.getContentPage(), ContentPageModel.class))
		{
			errors.rejectValue(CONTENT_PAGE, CmsfacadesConstants.FIELD_DOES_NOT_EXIST);
		}
		else if (Objects.nonNull(target.getProduct())
				&& !getItemModelExistsPredicate().test(target.getProduct(), ProductModel.class))
		{
			errors.rejectValue(PRODUCT, CmsfacadesConstants.FIELD_DOES_NOT_EXIST);
		}
		else if (Objects.nonNull(target.getUrl()) && !UrlValidator.getInstance().isValid(target.getUrl()))
		{
			errors.rejectValue(URL, CmsfacadesConstants.INVALID_URL_FORMAT);
		}
	}

	/**
	 * Verifies that one of the following is specified: category, content page, product or url
	 *
	 * @param target - the link component dto
	 * @param errors - the object collecting the validation errors
	 */
	protected void verifyOnlyOneTypeProvided(final CMSLinkComponentData target, final Errors errors)
	{
		final long count = Stream
				.of(target.getCategory(), target.getContentPage(), target.getProduct(), target.getUrl())
				.filter(item -> !Strings.isNullOrEmpty(item)).count();
		if (count > 1)
		{
			errors.rejectValue(CONTENT_PAGE, CmsfacadesConstants.LINK_ITEMS_EXCEEDED);
			errors.rejectValue(PRODUCT, CmsfacadesConstants.LINK_ITEMS_EXCEEDED);
			errors.rejectValue(CATEGORY, CmsfacadesConstants.LINK_ITEMS_EXCEEDED);
			errors.rejectValue(URL, CmsfacadesConstants.LINK_ITEMS_EXCEEDED);
		}
		else if (count < 1)
		{
			errors.rejectValue(CONTENT_PAGE, CmsfacadesConstants.LINK_MISSING_ITEMS);
			errors.rejectValue(PRODUCT, CmsfacadesConstants.LINK_MISSING_ITEMS);
			errors.rejectValue(CATEGORY, CmsfacadesConstants.LINK_MISSING_ITEMS);
			errors.rejectValue(URL, CmsfacadesConstants.LINK_MISSING_ITEMS);
		}
	}

	protected LocalizedTypeValidator getLocalizedStringValidator()
	{
		return localizedStringValidator;
	}

	@Required
	public void setLocalizedStringValidator(final LocalizedTypeValidator localizedStringValidator)
	{
		this.localizedStringValidator = localizedStringValidator;
	}

	protected LocalizedValidator getLocalizedValidator()
	{
		return localizedValidator;
	}

	@Required
	public void setLocalizedValidator(final LocalizedValidator localizedValidator)
	{
		this.localizedValidator = localizedValidator;
	}

	protected BiPredicate<String, Class<?>> getItemModelExistsPredicate()
	{
		return itemModelExistsPredicate;
	}

	@Required
	public void setItemModelExistsPredicate(final BiPredicate<String, Class<?>> itemModelExistsPredicate)
	{
		this.itemModelExistsPredicate = itemModelExistsPredicate;
	}

}
