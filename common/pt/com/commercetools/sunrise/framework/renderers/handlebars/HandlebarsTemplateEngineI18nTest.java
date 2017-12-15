package com.commercetools.sunrise.framework.renderers.handlebars;

import com.commercetools.sunrise.framework.renderers.TemplateContext;
import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.test.WithApplication;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Locale.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static play.inject.Bindings.bind;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.invokeWithContext;

public class HandlebarsTemplateEngineI18nTest extends WithApplication {

    private static final String FILES_PATH = "handlebars/template/i18n";
    private static final List<TemplateLoader> TEMPLATE_LOADERS = singletonList(new ClassPathTemplateLoader("/" + FILES_PATH));

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .bindings(bind(HandlebarsSettings.class).toInstance(() -> TEMPLATE_LOADERS))
                .configure("play.i18n.langs", asList("en", "de"))
                .configure("play.i18n.path", FILES_PATH)
                .build();
    }

    @Test
    public void resolvesMessage() throws Exception {
        testTemplate(ENGLISH, "simple", html -> assertThat(html).contains("something"));
    }

    @Test
    public void resolvesMessageInSpecificLanguage() throws Exception {
        testTemplate(GERMAN, "simple", html -> assertThat(html).contains("etwas"));
    }

    @Test
    public void resolvesMessageWithBundle() throws Exception {
        testTemplate(ENGLISH, "bundle", html -> assertThat(html).contains("something"));
    }

    @Test
    public void resolvesMessageWithParameters() throws Exception {
        testTemplate(ENGLISH, "parameter", html -> assertThat(html).contains("Hello John Doe!"));
    }

    @Test
    public void keyNotFound() throws Exception {
        testTemplate(ENGLISH, "missingKey", html -> assertThat(html).isEmpty());
    }

    @Test
    public void bundleNotFound() throws Exception {
        testTemplate(ENGLISH, "missingBundle", html -> assertThat(html).isEmpty());
    }

    @Test
    public void languageNotSupportedFallbacksToDefault() throws Exception {
        testTemplate(ITALIAN, "simple", html -> assertThat(html).contains("something"));
    }

    private void testTemplate(final Locale locale, final String templateName, final Consumer<String> test) {
        final String html = invokeWithContext(fakeRequest(), () -> {
            Http.Context.current().changeLang(locale.toLanguageTag());
            final TemplateContext templateContext = new TemplateContext(new PageData(), null);
            final HandlebarsTemplateEngine templateEngine = app.injector().instanceOf(HandlebarsTemplateEngine.class);
            return templateEngine.render(templateName, templateContext);
        });
        test.accept(html);
    }
}
