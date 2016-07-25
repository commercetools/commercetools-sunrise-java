package com.commercetools.sunrise.common.template.engine;

import com.commercetools.sunrise.common.template.engine.handlebars.HandlebarsFactory;
import com.commercetools.sunrise.common.template.engine.handlebars.HandlebarsTemplateEngine;
import com.google.inject.Provider;
import play.Configuration;

import javax.inject.Inject;

public final class TemplateEngineProvider implements Provider<TemplateEngine> {

    @Inject
    private HandlebarsFactory handlebarsFactory;
    @Inject
    private Configuration configuration;

    @Override
    public TemplateEngine get() {
        final Configuration config = configuration.getConfig("handlebars");
        return HandlebarsTemplateEngine.of(handlebarsFactory.create(config));
    }
}
