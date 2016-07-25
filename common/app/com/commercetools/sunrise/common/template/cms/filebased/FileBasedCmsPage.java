package com.commercetools.sunrise.common.template.cms.filebased;

import com.commercetools.sunrise.common.template.cms.CmsPage;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @see FileBasedCmsService
 */
public final class FileBasedCmsPage implements CmsPage {

    private final I18nResolver i18nResolver;
    private final String pageKey;
    private final List<Locale> locales;

    FileBasedCmsPage(final I18nResolver i18nResolver, final String pageKey, final List<Locale> locales) {
        this.i18nResolver = i18nResolver;
        this.pageKey = pageKey;
        this.locales = locales;
    }

    @Override
    public Optional<String> get(final String fieldName) {
        final I18nIdentifier i18nIdentifier = I18nIdentifier.of(pageKey, fieldName);
        return i18nResolver.get(locales, i18nIdentifier);
    }
}