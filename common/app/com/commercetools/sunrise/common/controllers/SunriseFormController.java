package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RequestHookContext;
import play.data.FormFactory;

public abstract class SunriseFormController extends SunriseTemplateController {

    private final FormFactory formFactory;

    protected SunriseFormController(final RequestHookContext hookContext, final TemplateRenderer templateRenderer,
                                    final FormFactory formFactory) {
        super(hookContext, templateRenderer);
        this.formFactory = formFactory;
    }

    public FormFactory getFormFactory() {
        return formFactory;
    }
}
