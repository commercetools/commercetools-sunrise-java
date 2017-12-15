package com.commercetools.sunrise.framework.renderers.handlebars;

import com.commercetools.sunrise.framework.i18n.I18nResolver;
import com.commercetools.sunrise.framework.renderers.TemplateContext;
import com.commercetools.sunrise.framework.renderers.TemplateEngine;
import com.commercetools.sunrise.framework.renderers.TemplateNotFoundException;
import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(MockitoJUnitRunner.class)
public class HandlebarsTemplateEngineLoadersTest {

    private static final TemplateLoader DEFAULT_LOADER = new ClassPathTemplateLoader("/templates/loaders");
    private static final TemplateLoader OVERRIDE_LOADER = new ClassPathTemplateLoader("/templates/loaders/override");

    @Mock
    private I18nResolver dummyI18nResolver;

    @Test
    public void rendersTemplateWithPartial() throws Exception {
        testTemplate("template", defaultHandlebars(), html ->
                assertThat(html)
                        .contains("<title>foo</title>")
                        .contains("<h1>bar</h1>")
                        .contains("<h2></h2>")
                        .contains("<p>default partial</p>")
                        .contains("<ul></ul>"));
    }

    @Test
    public void rendersOverriddenTemplateUsingOverriddenAndDefaultPartials() throws Exception {
        testTemplate("template", handlebarsWithOverride(), html ->
                assertThat(html)
                        .contains("overridden template")
                        .contains("overridden partial")
                        .contains("another default partial"));
    }

    @Test
    public void rendersDefaultTemplateUsingOverriddenAndDefaultPartials() throws Exception {
        testTemplate("anotherTemplate", handlebarsWithOverride(), html ->
                assertThat(html)
                        .contains("default template")
                        .contains("overridden partial")
                        .contains("another default partial"));
    }

    @Test
    public void throwsExceptionWhenTemplateNotFound() throws Exception {
        assertThatThrownBy(() -> defaultHandlebars().render("unknown", templateContext()))
                .isInstanceOf(TemplateNotFoundException.class);
    }

    private TemplateEngine defaultHandlebars() {
        return new TestableHandlebarsTemplateEngine(singletonList(DEFAULT_LOADER), dummyI18nResolver);
    }

    private TemplateEngine handlebarsWithOverride() {
        return new TestableHandlebarsTemplateEngine(asList(OVERRIDE_LOADER, DEFAULT_LOADER), dummyI18nResolver);
    }

    private static void testTemplate(final String templateName, final TemplateEngine templateEngine, final Consumer<String> test) {
        final String html = templateEngine.render(templateName, templateContext());
        test.accept(html);
    }

    private static TemplateContext templateContext() {
        final PageData pageData = new PageData();
        pageData.put("title", "foo");
        pageData.put("message", "bar");
        return new TemplateContext(pageData, f -> Optional.empty());
    }
}
