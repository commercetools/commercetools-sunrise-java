package com.commercetools.sunrise.framework.controllers;

import play.data.FormFactory;

public abstract class SunriseFormController extends SunriseController {

    private final FormFactory formFactory;

    protected SunriseFormController(final FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public FormFactory getFormFactory() {
        return formFactory;
    }
}
