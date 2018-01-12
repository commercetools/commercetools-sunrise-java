package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.renderers.TemplateEngine;

public abstract class SunriseContentController extends SunriseController {

    private final TemplateEngine templateEngine;

    protected SunriseContentController(final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }
}
