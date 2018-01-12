package com.commercetools.sunrise.core.controllers;

import io.sphere.sdk.client.ClientErrorException;
import org.slf4j.Logger;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.CompletableFutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.CompletableFutureUtils.recoverWith;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Approach to handle form data (Template Method Pattern).
 * @param <F> stereotype of the in a form wrapped class
 */
public interface WithForm2Flow<F> extends WithForm2<F> {

    Logger getLogger();

    default CompletionStage<Result> processForm() {
        return validateForm(bindForm()).thenComposeAsync(form -> {
            if (!form.hasErrors()) {
                return handleValidForm(form);
            } else {
                return handleInvalidForm(form);
            }
        }, HttpExecution.defaultContext());
    }

    default CompletionStage<Form<? extends F>> validateForm(final Form<? extends F> form) {
        return completedFuture(form);
    }

    CompletionStage<Result> handleInvalidForm(final Form<? extends F> form);

    default CompletionStage<Result> handleValidForm(final Form<? extends F> form) {
        final CompletionStage<Result> resultStage = executeAction(form.get())
                .thenComposeAsync(output -> handleSuccessfulAction(form.get()), HttpExecution.defaultContext());
        return recoverWith(resultStage, throwable -> handleFailedAction(form, throwable), HttpExecution.defaultContext());
    }

    CompletionStage<O> executeAction(final F formData);

    default CompletionStage<Result> handleFailedAction(final Form<? extends F> form, final Throwable throwable) {
        final Throwable causeThrowable = throwable.getCause();
        if (causeThrowable instanceof ClientErrorException) {
            return handleClientErrorFailedAction(form, (ClientErrorException) causeThrowable);
        }
        return handleGeneralFailedAction(throwable);
    }

    CompletionStage<Result> handleClientErrorFailedAction(final Form<? extends F> form, final ClientErrorException clientErrorException);

    default CompletionStage<Result> handleGeneralFailedAction(final Throwable throwable) {
        return exceptionallyCompletedFuture(throwable);
    }

    CompletionStage<Result> handleSuccessfulAction(final F formData);

    default void saveFormError(final Form<? extends F> form, final String message) {
        form.reject(message);
    }

    default void saveUnexpectedFormError(final Form<? extends F> form, final Throwable throwable) {
        form.reject("Something went wrong, please try again"); // TODO i18n
        getLogger().error("The CTP request raised an unexpected exception", throwable);
    }
}
