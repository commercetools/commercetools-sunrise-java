package com.commercetools.sunrise.framework.controllers;

import io.sphere.sdk.client.ClientErrorException;
import org.slf4j.Logger;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.CompletableFutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.CompletableFutureUtils.recoverWith;

/**
 * Approach to handle execution of data (Template Method Pattern).
 * @param <I> type of the input data, possibly a parameter object
 * @param <O> type of the output object
 */
public interface WithExecutionFlow<I, O> {

    Logger getLogger();

    default CompletionStage<Result> processRequest(final I input) {
        final CompletionStage<Result> resultStage = executeAction(input)
                .thenComposeAsync(this::handleSuccessfulAction, HttpExecution.defaultContext());
        return recoverWith(resultStage, throwable -> handleFailedAction(input, throwable), HttpExecution.defaultContext());
    }

    CompletionStage<O> executeAction(final I input);

    default CompletionStage<Result> handleFailedAction(final I input, final Throwable throwable) {
        final Throwable causeThrowable = throwable.getCause();
        if (causeThrowable instanceof ClientErrorException) {
            return handleClientErrorFailedAction(input, (ClientErrorException) causeThrowable);
        }
        return handleGeneralFailedAction(throwable);
    }

    CompletionStage<Result> handleClientErrorFailedAction(final I input, final ClientErrorException clientErrorException);

    default CompletionStage<Result> handleGeneralFailedAction(final Throwable throwable) {
        return exceptionallyCompletedFuture(throwable);
    }

    CompletionStage<Result> handleSuccessfulAction(final O output);
}
