package com.commercetools.sunrise.common.controllers;

import play.data.Form;
import play.data.FormFactory;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public interface FormBindingTrait<T> {

    default CompletionStage<Form<? extends T>> bindForm() {
        final Form<? extends T> form = bindFormFromRequest();
        return asyncValidation(form);
    }

    default CompletionStage<Form<? extends T>> asyncValidation(final Form<? extends T> filledForm) {
        return completedFuture(filledForm);
    }

    default Form<? extends T> bindFormFromRequest() {
        return formFactory().form(getFormDataClass()).bindFromRequest();
    }

    Class<? extends T> getFormDataClass();

    FormFactory formFactory();
}
