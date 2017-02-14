package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.hooks.RequestHookContext;
import play.data.FormFactory;

public abstract class SunriseTemplatelessFormController extends SunriseController {

    private final FormFactory formFactory;

    protected SunriseTemplatelessFormController(final RequestHookContext hookContext, final FormFactory formFactory) {
        super(hookContext);
        this.formFactory = formFactory;
    }

    public FormFactory getFormFactory() {
        return formFactory;
    }
}
