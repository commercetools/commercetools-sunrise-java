package common.cms;

import common.cms.local.LocalCmsService;
import common.i18n.I18nResolver;
import common.i18n.YamlI18nResolver;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        assertThat(cms(DE).get("home", "header").get().get("title")).contains("foo");
    }

    @Test
    public void resolvesWithRegion() throws Exception {
        assertThat(cms(DE_AT).get("home", "header").get().get("title")).contains("bar");
    }

    @Test
    public void emptyWhenPageKeyNotFound() throws Exception {
        assertThat(cms(DE).get("unknown", "header").get().get("title")).isEmpty();
    }

    @Test
    public void emptyWhenMessageKeyNotFound() throws Exception {
        assertThat(cms(DE).get("home", "wrong").get().get("message")).isEmpty();
    }

    @Test
    public void resolvesWithParameters() throws Exception {
        final Map<String, Object> hashArgs = new LinkedHashMap<>();
        hashArgs.put("today", "Monday");
        hashArgs.put("tomorrow", "Tuesday");
        assertThat(cms(DE).get("home", "content").get().get("day", hashArgs))
                .contains("Today is Monday, tomorrow is Tuesday");
    }

    private CmsPage cms(final Locale locale) throws Exception {
        final I18nResolver i18nResolver = YamlI18nResolver.of("cms", SUPPORTED_LOCALES, AVAILABLE_BUNDLES);
        return LocalCmsService.of(i18nResolver).getPage(singletonList(locale), "cms").toCompletableFuture().get(0, TimeUnit.SECONDS);
    }
}
