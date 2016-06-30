package com.commercetools.sunrise.common.controllers;

import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;

public interface SimpleFormBindingControllerTrait<F, T, R> extends FormBindingTrait<F> {

    CompletionStage<Result> showForm(final T data);

    default CompletionStage<Result> validateForm(final T data) {
        return bindForm().thenComposeAsync(form -> {
            if (!form.hasErrors()) {
                return handleValidForm(form.get(), data);
            } else {
                return handleInvalidForm(form, data);
            }
        }, HttpExecution.defaultContext());
    }

    CompletionStage<Result> handleInvalidForm(final Form<? extends F> form, final T data);

    default CompletionStage<Result> handleValidForm(final F formData, final T data) {
        final CompletionStage<Result> resultStage = doAction(formData, data)
                .thenComposeAsync(result -> handleSuccessfulAction(formData, data, result), HttpExecution.defaultContext());
        return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                handleFailedAction(formData, data, throwable));
    }

    CompletionStage<? extends R> doAction(final F formData, final T data);

    CompletionStage<Result> handleFailedAction(final F formData, final T data, final Throwable throwable);

    CompletionStage<Result> handleSuccessfulAction(final F formData, final T data, final R result);
}
