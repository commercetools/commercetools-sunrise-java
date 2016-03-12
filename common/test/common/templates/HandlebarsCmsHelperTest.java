package common.templates;

import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import common.cms.CmsPage;
import common.cms.CmsService;
import common.cms.local.LocalCmsService;
import common.controllers.PageData;
import common.i18n.I18nResolver;
import common.i18n.TestableI18nResolver;
import common.templates.handlebars.HandlebarsTemplateService;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static org.assertj.core.api.Assertions.assertThat;

public class HandlebarsCmsHelperTest {
    private static final TemplateLoader DEFAULT_LOADER = new ClassPathTemplateLoader("/templates/cmsHelper");
    private static final PageData SOME_PAGE_DATA = new TestablePageData();

    @Test
    public void resolvesMessage() throws Exception {
        testTemplate("simple", ENGLISH, defaultI18nMap(), html -> assertThat(html).contains("something"));
    }

    @Test
    public void resolvesMessageWithParameters() throws Exception {
        testTemplate("parameter", ENGLISH, defaultI18nMap(), html -> assertThat(html).contains("something firstName=John,lastName=Doe"));
    }

    @Test
    public void languageNotFound() throws Exception {
        testTemplate("simple", GERMAN, defaultI18nMap(), html -> assertThat(html).isEmpty());
    }

    @Test
    public void contentTypeNotFound() throws Exception {
        testTemplate("missingType", ENGLISH, defaultI18nMap(), html -> assertThat(html).isEmpty());
    }

    @Test
    public void contentIdNotFound() throws Exception {
        testTemplate("missingId", ENGLISH, defaultI18nMap(), html -> assertThat(html).isEmpty());
    }

    @Test
    public void contentFieldNotFound() throws Exception {
        testTemplate("missingField", ENGLISH, defaultI18nMap(), html -> assertThat(html).isEmpty());
    }

    private static void testTemplate(final String templateName, final Locale locale, final Map<String, String> i18nMap,
                                     final Consumer<String> test) {
        testTemplate(templateName, locale, emptyMap(), i18nMap, test);
    }

    private static void testTemplate(final String templateName, final Locale locale, final Map<String, Object> parameters,
                                     final Map<String, String> i18nMap, final Consumer<String> test) {
        final TemplateService templateService = HandlebarsTemplateService.of(singletonList(DEFAULT_LOADER), i18nResolver(i18nMap));
        final String html = renderTemplate(templateName, templateService, locale);
        test.accept(html);
    }

    private static String renderTemplate(final String templateName, final TemplateService templateService, final Locale locale) {
        final CmsService cmsService =  LocalCmsService.of(i18nResolver(defaultI18nMap()));
        final CmsPage cmsPage = cmsService.getPage(singletonList(locale), "cms").get(0);
        return templateService.render(templateName, SOME_PAGE_DATA, singletonList(locale), cmsPage);
    }

    private static I18nResolver i18nResolver(final Map<String, String> i18nMap) {
        return new TestableI18nResolver(i18nMap);
    }

    private static Map<String, String> defaultI18nMap() {
        final Map<String, String> i18nMap = new HashMap<>();
        i18nMap.put("en/sometype:someid.somefield", "something");
        i18nMap.put("es/sometype:someid.somefield", "algo");
        return i18nMap;
    }
}
