package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.ComponentRegistry;

public abstract class SunriseTemplateController extends SunriseController {

    private final TemplateRenderer templateRenderer;

    protected SunriseTemplateController(final ComponentRegistry componentRegistry, final TemplateRenderer templateRenderer) {
        super(componentRegistry);
        this.templateRenderer = templateRenderer;
    }

    public TemplateRenderer getTemplateRenderer() {
        return templateRenderer;
    }
}
