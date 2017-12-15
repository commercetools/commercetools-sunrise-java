package com.commercetools.sunrise.core.renderers.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.cache.HighConcurrencyTemplateCache;
import com.github.jknack.handlebars.io.TemplateLoader;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public final class HandlebarsProvider implements Provider<Handlebars> {

    private final HandlebarsSettings handlebarsSettings;
    private final HandlebarsHelperSource handlebarsHelperSource;

    @Inject
    HandlebarsProvider(final HandlebarsSettings handlebarsSettings, final HandlebarsHelperSource handlebarsHelperSource) {
        this.handlebarsSettings = handlebarsSettings;
        this.handlebarsHelperSource = handlebarsHelperSource;
    }

    @Override
    public Handlebars get() {
        final List<TemplateLoader> templateLoaders = handlebarsSettings.templateLoaders();
        final TemplateLoader[] loaders = templateLoaders.toArray(new TemplateLoader[templateLoaders.size()]);
        return new Handlebars()
                .with(loaders)
                .with(new HighConcurrencyTemplateCache())
                .infiniteLoops(true)
                .registerHelpers(handlebarsHelperSource);
    }
}
