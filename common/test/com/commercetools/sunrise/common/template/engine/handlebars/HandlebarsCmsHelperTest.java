package com.commercetools.sunrise.common.template.engine.handlebars;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.common.template.cms.CmsService;
import com.commercetools.sunrise.common.template.cms.filebased.FileBasedCmsService;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.common.template.engine.TemplateEngineProvider;
import com.commercetools.sunrise.common.template.engine.TestablePageData;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.common.template.i18n.TestableI18nResolver;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.junit.Test;
import play.Configuration;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static org.assertj.core.api.Assertions.assertThat;

public class HandlebarsCmsHelperTest {

    private static final PageData SOME_PAGE_DATA = new TestablePageData();
    public static final I18nResolver I18N_RESOLVER = i18nResolver(defaultI18nMap());

    @Test
    public void resolvesMessage() throws Exception {
        testTemplate("simple", ENGLISH, html -> assertThat(html).contains("something"));
    }

    @Test
    public void languageNotFound() throws Exception {
        testTemplate("simple", GERMAN, html -> assertThat(html).isEmpty());
    }

    @Test
    public void entryTypeNotFound() throws Exception {
        testTemplate("missingEntryType", ENGLISH, html -> assertThat(html).isEmpty());
    }

    @Test
    public void entryKeyNotFound() throws Exception {
        testTemplate("missingEntryKey", ENGLISH, html -> assertThat(html).isEmpty());
    }

    @Test
    public void contentFieldNotFound() throws Exception {
        testTemplate("missingField", ENGLISH, html -> assertThat(html).isEmpty());
    }

    private static void testTemplate(final String templateName, final Locale locale, final Consumer<String> test) throws Exception {
        final Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(final Binder binder) {
                binder.bind(TemplateEngine.class).toProvider(TemplateEngineProvider.class);
                binder.bind(I18nResolver.class).toInstance(I18N_RESOLVER);
                binder.bind(CmsService.class).to(FileBasedCmsService.class);
                binder.bind(Configuration.class).toInstance(new Configuration("handlebars.templateLoaders = [\n" +
                        "  {\n" +
                        "    \"type\":\"classpath\",\n" +
                        "    \"path\":\"/templates/cmsHelper\"\n" +
                        "  }\n" +
                        "]"));
            }
        });
        final TemplateEngine templateEngine = injector.getInstance(TemplateEngine.class);
        final String html = renderTemplate(templateName, templateEngine, locale);
        test.accept(html);
    }

    private static String renderTemplate(final String templateName, final TemplateEngine templateEngine, final Locale locale) throws Exception {
        return templateEngine.render(templateName, SOME_PAGE_DATA, singletonList(locale));
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
