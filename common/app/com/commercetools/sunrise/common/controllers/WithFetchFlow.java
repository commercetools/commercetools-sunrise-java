package com.commercetools.sunrise.common.controllers;

import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;

import java.util.concurrent.CompletionStage;

/**
 * Approach to handle query data (Template Method Pattern).
 * @param <O> type of the output object, the one fetched
 */
public interface WithFetchFlow<O> {

    default CompletionStage<Result> showPage(final O output) {
        return renderPage(output)
                .thenApplyAsync(Results::ok, HttpExecution.defaultContext());
    }

    CompletionStage<Content> renderPage(final O output);
}
