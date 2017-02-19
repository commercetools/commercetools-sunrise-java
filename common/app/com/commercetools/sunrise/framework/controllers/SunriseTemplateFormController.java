package com.commercetools.sunrise.framework.controllers;

import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import play.data.FormFactory;

public abstract class SunriseTemplateFormController extends SunriseTemplateController {

    private final FormFactory formFactory;

    protected SunriseTemplateFormController(final TemplateRenderer templateRenderer, final FormFactory formFactory) {
        super(templateRenderer);
        this.formFactory = formFactory;
    }

    public FormFactory getFormFactory() {
        return formFactory;
    }
}
