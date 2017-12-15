package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.renderers.ContentRenderer;
import play.data.FormFactory;

public abstract class SunriseContentFormController extends SunriseContentController {

    private final FormFactory formFactory;

    protected SunriseContentFormController(final ContentRenderer contentRenderer, final FormFactory formFactory) {
        super(contentRenderer);
        this.formFactory = formFactory;
    }

    public FormFactory getFormFactory() {
        return formFactory;
    }
}
