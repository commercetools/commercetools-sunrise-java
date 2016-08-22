package com.commercetools.sunrise.common.template.engine.handlebars;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.common.template.engine.TemplateContext;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.common.template.engine.TestablePageData;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.common.template.i18n.TestableI18nResolver;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static org.assertj.core.api.Assertions.assertThat;

public class HandlebarsI18nHelperTest {

    private static final TemplateLoader DEFAULT_LOADER = new ClassPathTemplateLoader("/templates/i18nHelper");
    private static final PageData SOME_PAGE_DATA = new TestablePageData();

    @Test
    public void resolvesMessage() throws Exception {
        final Map<String, String> i18nMap = new HashMap<>();
        i18nMap.put("en/main:foo", "bar");
        testTemplate("simple", ENGLISH, i18nMap, html -> assertThat(html).contains("bar"));
    }

    @Test
    public void resolvesMessageWithBundle() throws Exception {
        testTemplate("bundle", ENGLISH, defaultI18nMap(), html -> assertThat(html).contains("something"));
    }

    @Test
    public void resolvesMessageWithParameters() throws Exception {
        testTemplate("parameter", ENGLISH, defaultI18nMap(), html -> assertThat(html).contains("something firstName=John,lastName=Doe"));
    }

    @Test
    public void languageNotFound() throws Exception {
        testTemplate("bundle", GERMAN, defaultI18nMap(), html -> assertThat(html).isEmpty());
    }

    @Test
    public void keyNotFound() throws Exception {
        testTemplate("missingKey", ENGLISH, defaultI18nMap(), html -> assertThat(html).isEmpty());
    }

    @Test
    public void bundleNotFound() throws Exception {
        testTemplate("missingBundle", ENGLISH, defaultI18nMap(), html -> assertThat(html).isEmpty());
    }

    private static void testTemplate(final String templateName, final Locale locale, final Map<String, String> i18nMap,
                                     final Consumer<String> test) {
        final Handlebars handlebars = HandlebarsFactory.create(singletonList(DEFAULT_LOADER), i18nResolver(i18nMap), new I18nIdentifierFactory());
        final TemplateEngine templateEngine = HandlebarsTemplateEngine.of(handlebars, new HandlebarsContextFactory());
        final String html = renderTemplate(templateName, templateEngine, locale);
        test.accept(html);
    }

    private static String renderTemplate(final String templateName, final TemplateEngine templateEngine, final Locale locale) {
        final TemplateContext templateContext = new TemplateContext(SOME_PAGE_DATA, singletonList(locale), null);
        return templateEngine.render(templateName, templateContext);
    }

    private static I18nResolver i18nResolver(final Map<String, String> i18nMap) {
        return new TestableI18nResolver(i18nMap);
    }

    private static Map<String, String> defaultI18nMap() {
        final Map<String, String> i18nMap = new HashMap<>();
        i18nMap.put("en/somebundle:somekey", "something");
        i18nMap.put("es/somebundle:somekey", "algo");
        return i18nMap;
    }
}
