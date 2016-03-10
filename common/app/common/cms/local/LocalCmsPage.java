package common.cms.local;

import common.cms.CmsPage;
import common.i18n.I18nResolver;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class LocalCmsPage implements CmsPage {
    private final I18nResolver i18nResolver;
    private final String pageKey;
    private final List<Locale> locales;

    LocalCmsPage(final I18nResolver i18nResolver, final List<Locale> locales, final String pageKey) {
        this.i18nResolver = i18nResolver;
        this.pageKey = pageKey;
        this.locales = locales;
    }

    @Override
    public Optional<String> get(final String messageKey, final Map<String, Object> hashArgs) {
        return i18nResolver.get(locales, pageKey, messageKey, hashArgs);
    }
}
