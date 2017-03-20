package com.commercetools.sunrise.framework.controllers;

import play.data.Form;
import play.data.FormFactory;

public interface WithForm<F> {

    default Form<? extends F> bindForm() {
        return createForm().bindFromRequest();
    }

    default Form<? extends F> createForm() {
        return getFormFactory().form(getFormDataClass());
    }

    Class<? extends F> getFormDataClass();

    FormFactory getFormFactory();
}
