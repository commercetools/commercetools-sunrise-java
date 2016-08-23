package com.commercetools.sunrise.common.controllers;

import play.data.Form;
import play.data.FormFactory;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public interface WithForm<T> {

    default CompletionStage<Form<? extends T>> bindForm() {
        final Form<? extends T> form = createForm().bindFromRequest();
        return asyncValidation(form);
    }

    default Form<? extends T> createForm() {
        return formFactory().form(getFormDataClass());
    }

    default CompletionStage<Form<? extends T>> asyncValidation(final Form<? extends T> filledForm) {
        return completedFuture(filledForm);
    }

    Class<? extends T> getFormDataClass();

    FormFactory formFactory();
}
