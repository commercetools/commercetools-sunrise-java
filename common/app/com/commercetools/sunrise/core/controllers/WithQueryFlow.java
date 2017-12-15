package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * Approach to handle query data (Template Method Pattern).
 * @param <I> type of the input object, the one queried
 */
public interface WithQueryFlow<I> extends WithContent {

    default CompletionStage<Result> showPage(final I input) {
        return okResultWithPageContent(createPageContent(input));
    }

    PageContent createPageContent(final I input);
}
