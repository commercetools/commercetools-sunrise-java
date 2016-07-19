package com.commercetools.sunrise.common.template.engine;

import com.commercetools.sunrise.common.template.engine.handlebars.HandlebarsFactory;
import com.commercetools.sunrise.common.template.engine.handlebars.HandlebarsTemplateEngine;
import com.google.inject.Provider;

import javax.inject.Inject;

public final class TemplateEngineProvider implements Provider<TemplateEngine> {
    @Inject
    private HandlebarsFactory handlebarsFactory;

    @Override
    public TemplateEngine get() {
        return HandlebarsTemplateEngine.of(handlebarsFactory.create());
    }
}
