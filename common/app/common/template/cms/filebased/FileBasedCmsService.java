package common.template.cms.filebased;

import common.template.cms.CmsIdentifier;
import common.template.cms.CmsService;
import common.template.i18n.I18nIdentifier;
import common.template.i18n.I18nResolver;

import javax.inject.Inject;
import javax.inject.Singleton;
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
 * - {@code messageKey} = {@code entryKey.field} (e.g. homeTopLeft.subtitle.text)
 */
@Singleton
public final class FileBasedCmsService implements CmsService {
    private final I18nResolver i18nResolver;

    @Inject
    private FileBasedCmsService(final I18nResolver i18nResolver) {
        this.i18nResolver = i18nResolver;
    }

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

    public static FileBasedCmsService of(final I18nResolver i18nResolver) {
        return new FileBasedCmsService(i18nResolver);
    }
}