package com.commercetools.sunrise.common.template.engine;

import com.commercetools.sunrise.common.SunriseConfigurationException;
import com.commercetools.sunrise.common.template.engine.handlebars.HandlebarsFactory;
import com.commercetools.sunrise.common.template.engine.handlebars.HandlebarsTemplateEngine;
import com.google.inject.Provider;
import play.Configuration;

import javax.inject.Inject;
import java.util.Optional;

public final class TemplateEngineProvider implements Provider<TemplateEngine> {

    public static final String CONFIG_HANDLEBARS = "handlebars";
    @Inject
    private HandlebarsFactory handlebarsFactory;
    @Inject
    private Configuration configuration;

    @Override
    public TemplateEngine get() {
        return Optional.ofNullable(configuration.getConfig(CONFIG_HANDLEBARS))
                .map(config -> HandlebarsTemplateEngine.of(handlebarsFactory.create(config)))
                .orElseThrow(() -> new SunriseConfigurationException("Could not initialize HandlebarsTemplateEngine due to missing configuration", CONFIG_HANDLEBARS));
    }
}
