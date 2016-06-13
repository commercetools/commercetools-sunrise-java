package com.commercetools.sunrise.common.template.cms.filebased;

import com.commercetools.sunrise.common.template.cms.CmsIdentifier;
import com.commercetools.sunrise.common.template.cms.CmsService;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Service that provides content data from i18n files that can be found in a local file.
 * Internally it uses a I18nResolver to resolve the requested messages.
 *
 * The mapping of the {@link CmsService} parameters to {@link I18nResolver} parameters goes as follows:
 *
 * - {@code bundle} = {@code entryType} (e.g. banner)
 * - {@code messageKey} = {@code entryKey.fieldName} (e.g. homeTopLeft.subtitle.text)
 */
public final class FileBasedCmsService implements CmsService {

    @Inject
    private I18nResolver i18nResolver;

    @Override
    public CompletionStage<Optional<String>> get(final List<Locale> locales, final CmsIdentifier cmsIdentifier) {
        final I18nIdentifier i18nIdentifier = I18nIdentifier.ofBundleAndKey(cmsIdentifier.getEntryType(), messageKey(cmsIdentifier));
        return completedFuture(i18nResolver.get(locales, i18nIdentifier));
    }

    /**
     * Generates the message key as {@code entryKey.field} (e.g. homeTopLeft.subtitle.text).
     * @param cmsIdentifier identifier of the CMS content
     * @return the message key mapped to the entry key and field
     */
    private String messageKey(final CmsIdentifier cmsIdentifier) {
        return cmsIdentifier.getEntryKey() + "." + cmsIdentifier.getFieldName();
    }
}