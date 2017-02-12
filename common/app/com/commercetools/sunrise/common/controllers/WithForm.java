package com.commercetools.sunrise.common.controllers;

import play.data.Form;
import play.data.FormFactory;

public interface WithForm<T> {

    default Form<T> bindForm() {
        return createForm().bindFromRequest();
    }

    default Form<T> createForm() {
        return formFactory().form(getFormDataClass());
    }

    Class<T> getFormDataClass();

    FormFactory formFactory();
}
