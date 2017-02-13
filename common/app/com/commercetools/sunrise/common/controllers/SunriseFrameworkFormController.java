package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RequestHookContext;
import play.data.FormFactory;

public abstract class SunriseFrameworkFormController extends SunriseFrameworkController {

    private final FormFactory formFactory;

    protected SunriseFrameworkFormController(final TemplateRenderer templateRenderer, final RequestHookContext hookContext, final FormFactory formFactory) {
        super(templateRenderer, hookContext);
        this.formFactory = formFactory;
    }

    public FormFactory getFormFactory() {
        return formFactory;
    }
}
