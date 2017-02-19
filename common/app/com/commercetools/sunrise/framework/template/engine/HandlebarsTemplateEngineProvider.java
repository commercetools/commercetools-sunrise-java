package com.commercetools.sunrise.framework.template.engine;

import com.commercetools.sunrise.framework.SunriseConfigurationException;
import com.commercetools.sunrise.framework.template.engine.handlebars.HandlebarsContextFactory;
import com.commercetools.sunrise.framework.template.engine.handlebars.HandlebarsFactory;
import com.commercetools.sunrise.framework.template.engine.handlebars.HandlebarsTemplateEngine;
import com.github.jknack.handlebars.Handlebars;
import com.google.inject.Provider;
import play.Configuration;

import javax.inject.Inject;

public final class HandlebarsTemplateEngineProvider implements Provider<TemplateEngine> {

    private static final String CONFIG_HANDLEBARS = "handlebars";

    private final Configuration handlebarsConfiguration;
    private final HandlebarsFactory handlebarsFactory;
    private final HandlebarsContextFactory handlebarsContextFactory;

    @Inject
    public HandlebarsTemplateEngineProvider(final Configuration configuration, final HandlebarsFactory handlebarsFactory,
                                            final HandlebarsContextFactory handlebarsContextFactory) {
        this.handlebarsConfiguration = configuration.getConfig(CONFIG_HANDLEBARS);
        if (handlebarsConfiguration == null) {
            throw new SunriseConfigurationException("Could not initialize HandlebarsTemplateEngine due to missing configuration", CONFIG_HANDLEBARS);
        }
        this.handlebarsFactory = handlebarsFactory;
        this.handlebarsContextFactory = handlebarsContextFactory;
    }

    @Override
    public TemplateEngine get() {
        final Handlebars handlebars = handlebarsFactory.create(handlebarsConfiguration);
        return HandlebarsTemplateEngine.of(handlebars, handlebarsContextFactory);
    }
}
