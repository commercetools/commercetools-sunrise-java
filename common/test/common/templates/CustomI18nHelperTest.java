package common.templates;

import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import common.controllers.PageData;
import common.i18n.I18nResolver;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomI18nHelperTest {
    private static final TemplateLoader DEFAULT_LOADER = new ClassPathTemplateLoader("/templates");
    private static final PageData SOME_PAGE_DATA = new TestablePageData();

    @Test
    public void resolvesMessage() throws Exception {
        final Map<String, String> i18nMap = new HashMap<>();
        i18nMap.put("en/translations:foo", "bar");
        testTemplate("translations/simple", ENGLISH, i18nMap, html -> assertThat(html).contains("bar"));
    }

    @Test
    public void resolvesMessageWithBundle() throws Exception {
        testTemplate("translations/bundle", ENGLISH, html -> assertThat(html).contains("something"));
    }

    @Test
    public void languageNotFound() throws Exception {
        testTemplate("translations/bundle", GERMAN, html -> assertThat(html).isEmpty());
    }

    @Test
    public void keyNotFound() throws Exception {
        testTemplate("translations/missingKey", ENGLISH, html -> assertThat(html).isEmpty());
    }

    @Test
    public void bundleNotFound() throws Exception {
        testTemplate("translations/missingBundle", ENGLISH, html -> assertThat(html).isEmpty());
    }

    @Test
    public void translatesWithParameter() throws Exception {
        final Map<String, String> i18nMap = new HashMap<>();
        i18nMap.put("en/default:greetings", "Hello __name__!");
        testTemplate("translations/parameter", ENGLISH, i18nMap, html ->
                assertThat(html).contains("Hello John Doe!"));
    }

    @Test
    public void translatesWithPlural() throws Exception {
        final Map<String, String> i18nMap = new HashMap<>();
        i18nMap.put("en/default:items", "__count__ item");
        i18nMap.put("en/default:items_plural", "__count__ items");
        testTemplate("translations/plural", ENGLISH, i18nMap, html ->
                assertThat(html).isEqualTo("0 items\n" +
                        "1 item\n" +
                        "2 items\n" +
                        "10 items"));
    }

    private static void testTemplate(final String templateName, final Locale locale, final Consumer<String> test) {
        testTemplate(templateName, locale, defaultI18nMap(), test);
    }

    private static void testTemplate(final String templateName, final Locale locale, final Map<String, String> i18nMap,
                                     final Consumer<String> test) {
        final TemplateService templateService = HandlebarsTemplateService.of(singletonList(DEFAULT_LOADER), i18nResolver(i18nMap));
        final String html = renderTemplate(templateName, templateService, locale);
        test.accept(html);
    }

    private static String renderTemplate(final String templateName, final TemplateService templateService, final Locale locale) {
        return templateService.render(templateName, SOME_PAGE_DATA, singletonList(locale));
    }

    private static I18nResolver i18nResolver(final Map<String, String> i18nMap) {
        return (locale, bundle, key, args) -> {
            final String mapKey = String.format("%s/%s:%s", locale.toLanguageTag(), bundle, key);
            final String message = i18nMap.get(mapKey);
            return Optional.ofNullable(message);
        };
    }

    private static Map<String, String> defaultI18nMap() {
        final Map<String, String> i18nMap = new HashMap<>();
        i18nMap.put("en/somebundle:somekey", "something");
        i18nMap.put("es/somebundle:somekey", "algo");
        return i18nMap;
    }
}
