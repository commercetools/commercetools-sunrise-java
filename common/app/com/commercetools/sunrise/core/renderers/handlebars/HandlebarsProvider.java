package com.commercetools.sunrise.core.renderers.handlebars;

import com.commercetools.sunrise.models.attributes.AttributeHelperSource;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.cache.HighConcurrencyTemplateCache;
import com.github.jknack.handlebars.helper.StringHelpers;
import com.github.jknack.handlebars.io.TemplateLoader;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public final class HandlebarsProvider implements Provider<Handlebars> {

    private final HandlebarsSettings handlebarsSettings;
    private final HandlebarsHelperSource handlebarsHelperSource;
    private final AttributeHelperSource attributeHelperSource;

    @Inject
    HandlebarsProvider(final HandlebarsSettings handlebarsSettings, final HandlebarsHelperSource handlebarsHelperSource,
                       final AttributeHelperSource attributeHelperSource) {
        this.handlebarsSettings = handlebarsSettings;
        this.handlebarsHelperSource = handlebarsHelperSource;
        this.attributeHelperSource = attributeHelperSource;
    }

    @Override
    public Handlebars get() {
        final List<TemplateLoader> templateLoaders = handlebarsSettings.templateLoaders();
        final TemplateLoader[] loaders = templateLoaders.toArray(new TemplateLoader[templateLoaders.size()]);
        return new Handlebars()
                .with(loaders)
                .with(new HighConcurrencyTemplateCache())
                .infiniteLoops(true)
                .registerHelpers(StringHelpers.class)
                .registerHelpers(handlebarsHelperSource)
                .registerHelpers(attributeHelperSource);
    }
}
