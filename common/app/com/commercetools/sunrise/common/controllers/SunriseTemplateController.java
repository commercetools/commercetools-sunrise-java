package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RequestHookContext;

public abstract class SunriseTemplateController extends SunriseController {

    private final TemplateRenderer templateRenderer;

    protected SunriseTemplateController(final RequestHookContext hookContext, final TemplateRenderer templateRenderer) {
        super(hookContext);
        this.templateRenderer = templateRenderer;
    }

    public TemplateRenderer getTemplateRenderer() {
        return templateRenderer;
    }
}
