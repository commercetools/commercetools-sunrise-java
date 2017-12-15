package com.commercetools.sunrise.cms.filebased;

import com.commercetools.sunrise.cms.CmsPage;
import com.commercetools.sunrise.cms.CmsService;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Service that provides content data from i18n files that can be found in a local file.
 * Internally it uses the {@link CmsMessagesApi} to resolve the requested messages.
 *
 * The message key for the {@link CmsMessagesApi} is built as follows: {@code pageKey.fieldName}
 * For example, for a page key {@code home} and a field name {@code banners[0].title}, the message key
 * obtained would be {@code home.banners[0].title}
 */
public final class FileBasedCmsService implements CmsService {

    private CmsMessagesApi cmsMessagesApi;

    @Inject
    FileBasedCmsService(final CmsMessagesApi cmsMessagesApi) {
        this.cmsMessagesApi = cmsMessagesApi;
    }

    @Override
    public CompletionStage<Optional<CmsPage>> page(final String pageKey, final List<Locale> locales) {
        final CmsPage cmsPage = new FileBasedCmsPage(cmsMessagesApi, pageKey, locales);
        return completedFuture(Optional.of(cmsPage));
    }
}