package com.commercetools.sunrise.common.template.engine.handlebars;

import com.commercetools.sunrise.cms.CmsPage;
import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.common.template.engine.TemplateContext;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.common.template.engine.TestablePageData;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.TestableI18nResolver;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;

public class HandlebarsCmsHelperTest {

    private static final PageData SOME_PAGE_DATA = new TestablePageData();

    @Test
    public void resolvesMessage() throws Exception {
        testTemplate("simple", html -> assertThat(html).contains("something"));
    }

    @Test
    public void fieldNotFound() throws Exception {
        testTemplate("missingField", html -> assertThat(html).isEmpty());
    }

    private static void testTemplate(final String templateName, final Consumer<String> test) throws Exception {
        final String html = renderTemplate(templateName, handlebarsTemplateEngine());
        test.accept(html);
    }

    private static String renderTemplate(final String templateName, final TemplateEngine templateEngine) throws Exception {
        final TemplateContext templateContext = new TemplateContext(SOME_PAGE_DATA, emptyList(), cmsPage());
        return templateEngine.render(templateName, templateContext);
    }

    private static CmsPage cmsPage() {
        return fieldName -> {
            if (fieldName.equals("someid.somefield")) return Optional.of("something");
            else return Optional.empty();
        };
    }

    private static TemplateEngine handlebarsTemplateEngine() {
        final TestableI18nResolver i18nResolver = new TestableI18nResolver(emptyMap());
        final List<TemplateLoader> templateLoaders = singletonList(new ClassPathTemplateLoader("/templates/cmsHelper"));
        final Handlebars handlebars = HandlebarsFactory.create(templateLoaders, i18nResolver, new I18nIdentifierFactory());
        return HandlebarsTemplateEngine.of(handlebars, new HandlebarsContextFactory());
    }
}
