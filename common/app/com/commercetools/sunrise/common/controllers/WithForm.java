package com.commercetools.sunrise.common.controllers;

import play.data.Form;
import play.data.FormFactory;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public interface WithForm<T> {

    default CompletionStage<Form<T>> bindForm() {
        final Form<T> form = createForm().bindFromRequest();
        return asyncValidation(form);
    }

    default Form<T> createForm() {
        return formFactory().form(getFormDataClass());
    }

    default CompletionStage<Form<T>> asyncValidation(final Form<T> filledForm) {
        return completedFuture(filledForm);
    }

    Class<T> getFormDataClass();

    FormFactory formFactory();
}
