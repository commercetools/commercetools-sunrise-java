package com.commercetools.sunrise.core.renderers.handlebars;

import com.commercetools.sunrise.core.i18n.I18nResolver;
import com.commercetools.sunrise.core.renderers.TemplateContext;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.TemplateLoader;

import java.util.List;

import static java.util.Arrays.asList;

final class TestableHandlebarsTemplateEngine implements TemplateEngine {

    private final TemplateEngine delegate;

    TestableHandlebarsTemplateEngine(final List<TemplateLoader> templateLoaders, final I18nResolver i18nResolver) {
        this.delegate = handlebarsTemplateEngine(templateLoaders, i18nResolver);
    }

    @Override
    public String render(final String templateName, final TemplateContext templateContext) {
        return delegate.render(templateName, templateContext);
    }

    private TemplateEngine handlebarsTemplateEngine(final List<TemplateLoader> templateLoaders, final I18nResolver i18nResolver) {
        final Handlebars handlebars = handlebars(templateLoaders, i18nResolver);
        final HandlebarsValueResolvers handlebarsValueResolvers = valueResolvers(i18nResolver);
        return new HandlebarsTemplateEngine(handlebars, handlebarsValueResolvers);
    }

    private HandlebarsValueResolvers valueResolvers(final I18nResolver i18nResolver) {
        final PlayJavaFormResolver playJavaFormResolver = new PlayJavaFormResolver(msg -> msg);
        final SunriseJavaBeanValueResolver sunriseJavaBeanValueResolver = new SunriseJavaBeanValueResolver(i18nResolver);
        return () -> asList(playJavaFormResolver, sunriseJavaBeanValueResolver);
    }

    private static Handlebars handlebars(final List<TemplateLoader> templateLoaders, final I18nResolver i18nResolver) {
        final DefaultHandlebarsHelperSource helperSource = new DefaultHandlebarsHelperSource(i18nResolver);
        final HandlebarsSettings settings = () -> templateLoaders;
        return new HandlebarsProvider(settings, helperSource).get();
    }
}
