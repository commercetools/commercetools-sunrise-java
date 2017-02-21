package com.commercetools.sunrise.framework.controllers;

import play.data.Form;
import play.data.FormFactory;

public interface WithForm<T> {

    default Form<? extends T> bindForm() {
        return createForm().bindFromRequest();
    }

    default Form<? extends T> createForm() {
        return getFormFactory().form(getFormDataClass());
    }

    Class<? extends T> getFormDataClass();

    FormFactory getFormFactory();
}
