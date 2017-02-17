package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.hooks.ComponentRegistry;
import play.data.FormFactory;

public abstract class SunriseFormController extends SunriseController {

    private final FormFactory formFactory;

    protected SunriseFormController(final ComponentRegistry componentRegistry, final FormFactory formFactory) {
        super(componentRegistry);
        this.formFactory = formFactory;
    }

    public FormFactory getFormFactory() {
        return formFactory;
    }
}
