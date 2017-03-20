package com.commercetools.sunrise.framework.controllers;

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
 * @param <I> type of the input data, possibly a parameter object
 * @param <O> type of the output object, normally the updated object if the form is valid
 * @param <F> stereotype of the in a form wrapped class
 */
public interface WithFormFlow<I, O, F> extends WithForm<F> {

    Logger getLogger();

    default CompletionStage<Result> processForm(final I input) {
        return validateForm(input, bindForm()).thenComposeAsync(form -> {
            if (!form.hasErrors()) {
                return handleValidForm(input, form);
            } else {
                return handleInvalidForm(input, form);
            }
        }, HttpExecution.defaultContext());
    }

    default CompletionStage<Form<? extends F>> validateForm(final I input, final Form<? extends F> form) {
        return completedFuture(form);
    }

    CompletionStage<Result> handleInvalidForm(final I input, final Form<? extends F> form);

    default CompletionStage<Result> handleValidForm(final I input, final Form<? extends F> form) {
        final CompletionStage<Result> resultStage = executeAction(input, form.get())
                .thenComposeAsync(output -> handleSuccessfulAction(output, form.get()), HttpExecution.defaultContext());
        return recoverWith(resultStage, throwable -> handleFailedAction(input, form, throwable), HttpExecution.defaultContext());
    }

    CompletionStage<O> executeAction(final I input, final F formData);

    default CompletionStage<Result> handleFailedAction(final I input, final Form<? extends F> form, final Throwable throwable) {
        final Throwable causeThrowable = throwable.getCause();
        if (causeThrowable instanceof ClientErrorException) {
            return handleClientErrorFailedAction(input, form, (ClientErrorException) causeThrowable);
        }
        return handleGeneralFailedAction(throwable);
    }

    CompletionStage<Result> handleClientErrorFailedAction(final I input, final Form<? extends F> form, final ClientErrorException clientErrorException);

    default CompletionStage<Result> handleGeneralFailedAction(final Throwable throwable) {
        return exceptionallyCompletedFuture(throwable);
    }

    CompletionStage<Result> handleSuccessfulAction(final O output, final F formData);

    default void saveFormError(final Form<? extends F> form, final String message) {
        form.reject(message);
    }

    default void saveUnexpectedFormError(final Form<? extends F> form, final Throwable throwable) {
        form.reject("Something went wrong, please try again"); // TODO i18n
        getLogger().error("The CTP request raised an unexpected exception", throwable);
    }
}
