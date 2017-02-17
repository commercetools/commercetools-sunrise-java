package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import play.data.FormFactory;

public abstract class SunriseTemplateFormController extends SunriseTemplateController {

    private final FormFactory formFactory;

    protected SunriseTemplateFormController(final ComponentRegistry componentRegistry, final TemplateRenderer templateRenderer,
                                            final FormFactory formFactory) {
        super(componentRegistry, templateRenderer);
        this.formFactory = formFactory;
    }

    public FormFactory getFormFactory() {
        return formFactory;
    }
}
