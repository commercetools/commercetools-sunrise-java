package common.cms;

import common.cms.local.LocalCmsService;
import common.i18n.I18nResolver;
import common.i18n.YamlI18nResolver;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class LocalCmsPageTest {
    private static final Locale DE = Locale.forLanguageTag("de");
    private static final Locale DE_AT = Locale.forLanguageTag("de-AT");
    private static final List<Locale> SUPPORTED_LOCALES = asList(DE, DE_AT);
    private static final List<String> AVAILABLE_BUNDLES = singletonList("home");

    @Test
    public void resolvesMessage() throws Exception {
        assertThat(cms(DE, "home").get("header.title")).contains("foo");
    }

    @Test
    public void resolvesWithRegion() throws Exception {
        assertThat(cms(DE_AT, "home").get("header.title")).contains("bar");
    }

    @Test
    public void emptyWhenPageKeyNotFound() throws Exception {
        assertThat(cms(DE, "unknown").get("header.title")).isEmpty();
    }

    @Test
    public void emptyWhenMessageKeyNotFound() throws Exception {
        assertThat(cms(DE, "home").get("wrong.message")).isEmpty();
    }

    @Test
    public void resolvesWithParameters() throws Exception {
        final Map<String, Object> hashArgs = new LinkedHashMap<>();
        hashArgs.put("today", "Monday");
        hashArgs.put("tomorrow", "Tuesday");
        assertThat(cms(DE, "home").get("day", hashArgs)).contains("Today is Monday, tomorrow is Tuesday");
    }

    private CmsPage cms(final Locale locale) {
        return cms(locale, null);
    }

    private CmsPage cms(final Locale locale, final String pageKey) {
        final I18nResolver i18nResolver = YamlI18nResolver.of("cms", SUPPORTED_LOCALES, AVAILABLE_BUNDLES);
        return LocalCmsService.of(i18nResolver).getPage(singletonList(locale), pageKey).get(0);
    }
}
