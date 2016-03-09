package common.templates;

import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import common.cms.CmsService;
import common.controllers.PageData;
import common.i18n.I18nResolver;
import org.junit.Test;
import play.libs.F;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomI18nHelperTest {
    private static final CmsService CMS_SERVICE = (locales, pageKey) -> F.Promise.pure((message, args) -> Optional.empty());
    private static final TemplateLoader DEFAULT_LOADER = new ClassPathTemplateLoader("/templates");
    private static final PageData SOME_PAGE_DATA = new TestablePageData();

    @Test
    public void resolvesMessage() throws Exception {
        final Map<String, String> i18nMap = new HashMap<>();
        i18nMap.put("en/main:foo", "bar");
        testTemplate("translations/simple", ENGLISH, i18nMap, html -> assertThat(html).contains("bar"));
    }

    @Test
    public void resolvesMessageWithBundle() throws Exception {
        testTemplate("translations/bundle", ENGLISH, defaultI18nMap(), html -> assertThat(html).contains("something"));
    }

    @Test
    public void resolvesMessageWithParameters() throws Exception {
        testTemplate("translations/parameter", ENGLISH, defaultI18nMap(), html -> assertThat(html).contains("something firstName=John,lastName=Doe"));
    }

    @Test
    public void languageNotFound() throws Exception {
        testTemplate("translations/bundle", GERMAN, defaultI18nMap(), html -> assertThat(html).isEmpty());
    }

    @Test
    public void keyNotFound() throws Exception {
        testTemplate("translations/missingKey", ENGLISH, defaultI18nMap(), html -> assertThat(html).isEmpty());
    }

    @Test
    public void bundleNotFound() throws Exception {
        testTemplate("translations/missingBundle", ENGLISH, defaultI18nMap(), html -> assertThat(html).isEmpty());
    }

    private static void testTemplate(final String templateName, final Locale locale, final Map<String, String> i18nMap,
                                     final Consumer<String> test) {
        testTemplate(templateName, locale, emptyMap(), i18nMap, test);
    }

    private static void testTemplate(final String templateName, final Locale locale, final Map<String, Object> parameters,
                                     final Map<String, String> i18nMap, final Consumer<String> test) {
        final TemplateService templateService = HandlebarsTemplateService.of(singletonList(DEFAULT_LOADER), i18nResolver(i18nMap), CMS_SERVICE);
        final String html = renderTemplate(templateName, templateService, locale);
        test.accept(html);
    }

    private static String renderTemplate(final String templateName, final TemplateService templateService, final Locale locale) {
        return templateService.render(templateName, SOME_PAGE_DATA, singletonList(locale));
    }

    private static I18nResolver i18nResolver(final Map<String, String> i18nMap) {
        return (locales, bundle, key, hashArgs) -> {
            final String mapKey = String.format("%s/%s:%s", locales.get(0), bundle, key);
            final String message = i18nMap.get(mapKey);
            final String parameters = hashArgs.entrySet().stream()
                    .map(hashPair -> hashPair.getKey() + "=" + hashPair.getValue())
                    .collect(Collectors.joining(","));
            if (parameters.isEmpty()) {
                return Optional.ofNullable(message);
            } else {
                return Optional.of(message + " " + parameters);
            }
        };
    }

    private static Map<String, String> defaultI18nMap() {
        final Map<String, String> i18nMap = new HashMap<>();
        i18nMap.put("en/somebundle:somekey", "something");
        i18nMap.put("es/somebundle:somekey", "algo");
        return i18nMap;
    }
}
