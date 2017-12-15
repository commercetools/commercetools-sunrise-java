package com.commercetools.sunrise.cms.filebased;

import com.commercetools.sunrise.cms.CmsPage;
import play.i18n.Lang;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @see FileBasedCmsService
 */
public final class FileBasedCmsPage implements CmsPage {

    private final CmsMessagesApi cmsMessagesApi;
    private final String pageKey;
    private final List<Locale> locales;

    FileBasedCmsPage(final CmsMessagesApi cmsMessagesApi, final String pageKey, final List<Locale> locales) {
        this.cmsMessagesApi = cmsMessagesApi;
        this.pageKey = pageKey;
        this.locales = locales;
    }

    @Override
    public Optional<String> field(final String fieldName) {
        final String messageKey = pageKey + "." + fieldName;
        return locales.stream()
                .map(locale -> translate(locale, messageKey))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<String> translate(final Locale locale, final String messageKey) {
        final Lang lang = new Lang(locale);
        if (cmsMessagesApi.isDefinedAt(lang, messageKey)) {
            return Optional.of(cmsMessagesApi.get(lang, messageKey));
        }
        return Optional.empty();
    }
}