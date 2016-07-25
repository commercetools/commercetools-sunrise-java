package com.commercetools.sunrise.common.template.cms.filebased;

import com.commercetools.sunrise.common.template.cms.CmsPage;
import com.commercetools.sunrise.common.template.cms.CmsService;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;

import javax.inject.Inject;
import javax.inject.Named;
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
    @Named("cms")
    private I18nResolver i18nResolver;

    @Override
    public CompletionStage<Optional<CmsPage>> get(final String pageKey, final List<Locale> locales) {
        final CmsPage cmsPage = new FileBasedCmsPage(i18nResolver, pageKey, locales);
        return completedFuture(Optional.of(cmsPage));
    }
}