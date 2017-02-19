package com.commercetools.sunrise.framework.controllers;

import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;

public abstract class SunriseTemplateController extends SunriseController {

    private final TemplateRenderer templateRenderer;

    protected SunriseTemplateController(final TemplateRenderer templateRenderer) {
        this.templateRenderer = templateRenderer;
    }

    public TemplateRenderer getTemplateRenderer() {
        return templateRenderer;
    }
}
