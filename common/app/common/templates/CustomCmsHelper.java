package common.templates;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import common.cms.CmsService;
import io.sphere.sdk.models.Base;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

final class CustomCmsHelper extends Base implements Helper<String> {
    private static final int ALLOWED_TIMEOUT = 5000;
    private final CmsService cmsService;

    public CustomCmsHelper(final CmsService cmsService) {
        this.cmsService = cmsService;
    }

    @Override
    public CharSequence apply(final String context, final Options options) throws IOException {
        final List<Locale> locales = getLocales(options);
        final CmsIdentifier cmsIdentifier = new CmsIdentifier(context);
        return cmsService.getPage(locales, cmsIdentifier.bundle) // TODO avoid doing this for every cms occurrence
                .map(page -> page.getOrEmpty(cmsIdentifier.key, options.hash))
                .get(ALLOWED_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @SuppressWarnings("unchecked")
    private static List<Locale> getLocales(final Options options) {
        final List<String> languageTags = (List<String>) options.context.get("locales");
        return languageTags.stream()
                .map(Locale::forLanguageTag)
                .collect(toList());
    }

    private static class CmsIdentifier extends Base {
        private final String bundle;
        private final String key;

        public CmsIdentifier(final String context) {
            final String[] parts = StringUtils.split(context, ':');
            final boolean usingDefaultBundle = parts.length == 1;
            this.bundle = usingDefaultBundle ? "main" : parts[0];
            this.key = usingDefaultBundle ? context : parts[1];
        }
    }
}
