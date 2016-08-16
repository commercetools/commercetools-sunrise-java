package com.commercetools.sunrise.common.template.cms.filebased;

import com.commercetools.sunrise.cms.CmsPage;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.common.template.i18n.yaml.YamlI18nResolver;
import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class FileBasedCmsPageTest {

    private static final Locale DE = Locale.forLanguageTag("de");
    private static final Locale DE_AT = Locale.forLanguageTag("de-AT");
    private static final List<Locale> SUPPORTED_LOCALES = asList(DE, DE_AT);
    private static final List<String> AVAILABLE_BUNDLES = singletonList("home");

    @Test
    public void resolvesMessage() throws Exception {
        testCms(DE, "home", "header.title", content -> assertThat(content).contains("foo"));
    }

    @Test
    public void resolvesWithRegion() throws Exception {
        testCms(DE_AT, "home", "header.title", content -> assertThat(content).contains("bar"));
    }

    @Test
    public void emptyWhenPageKeyNotFound() throws Exception {
        testCms(DE, "unknown", "header.title", content -> assertThat(content).isEmpty());
    }

    @Test
    public void emptyWhenFieldNameNotFound() throws Exception {
        testCms(DE, "home", "wrong.message", content -> assertThat(content).isEmpty());
    }

    @Test
    public void doesNotFailWithEmptyPageKey() throws Exception {
        testCms(DE, "", "header.title", content -> assertThat(content).isEmpty());
    }

    @Test
    public void doesNotFailWithEmptyFieldName() throws Exception {
        testCms(DE, "home", "", content -> assertThat(content).isEmpty());
    }

    private void testCms(final Locale locale, final String pageKey, final String fieldName, final Consumer<Optional<String>> test) throws Exception {
        final I18nResolver i18nResolver = YamlI18nResolver.of("cms", SUPPORTED_LOCALES, AVAILABLE_BUNDLES);
        final FileBasedCmsService cmsService = FileBasedCmsService.of(i18nResolver);
        final Optional<CmsPage> page = cmsService.page(pageKey, singletonList(locale)).toCompletableFuture().join();
        test.accept(page.flatMap(p -> p.field(fieldName)));
    }
}
