package common.template.cms.filebased;

import common.template.cms.CmsPage;
import common.template.cms.CmsService;
import common.template.i18n.I18nResolver;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Service that provides content data from i18n files that can be found in a local file.
 * It uses a I18nResolver internally to resolve the requested messages.
 *
 * The mapping of the {@link CmsService} parameters to {@link I18nResolver} parameters goes as follows:
 *
 * - {@code bundle} = {@code contentType} (e.g. banner)
 * - {@code key} = {@code contentId.field} (e.g. homeTopLeft.subtitle)
 *
 * The {@code pageKey} is then ignored as its only purpose is for remote access.
 */
@Singleton
public final class FileBasedCmsService implements CmsService {
    private final I18nResolver i18nResolver;

    @Inject
    private FileBasedCmsService(final I18nResolver i18nResolver) {
        this.i18nResolver = i18nResolver;
    }

    @Override
    public CompletionStage<CmsPage> getPage(final List<Locale> locales, final String pageKey) {
        final CmsPage cmsPage = new FileBasedCmsPage(i18nResolver, locales);
        return CompletableFuture.completedFuture(cmsPage);
    }

    public static FileBasedCmsService of(final I18nResolver i18nResolver) {
        return new FileBasedCmsService(i18nResolver);
    }
}