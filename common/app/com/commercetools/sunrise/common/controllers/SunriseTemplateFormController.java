package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RequestHookContext;
import play.data.FormFactory;

public abstract class SunriseTemplateFormController extends SunriseTemplateController {

    private final FormFactory formFactory;

    protected SunriseTemplateFormController(final RequestHookContext hookContext, final TemplateRenderer templateRenderer,
                                            final FormFactory formFactory) {
        super(hookContext, templateRenderer);
        this.formFactory = formFactory;
    }

    public FormFactory getFormFactory() {
        return formFactory;
    }
}
