package com.commercetools.sunrise.common.controllers;

import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Html;

import java.util.concurrent.CompletionStage;

/**
 * Approach to handle query data (Template Method Pattern).
 * @param <I> type of the input data of the query, possibly a parameter object
 * @param <O> type of the output object, the result of the query
 */
public interface WithQueryFlow<I, O> {

    default CompletionStage<Result> showPage(final I input) {
        return doQuery(input)
                .thenComposeAsync(output -> renderPage(input, output)
                        .thenApplyAsync(Results::ok, HttpExecution.defaultContext()),
                        HttpExecution.defaultContext());
    }

    CompletionStage<O> doQuery(final I input);

    CompletionStage<Html> renderPage(final I input, final O output);
}
