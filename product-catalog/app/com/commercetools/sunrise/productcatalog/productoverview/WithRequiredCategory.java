package com.commercetools.sunrise.productcatalog.productoverview;

import io.sphere.sdk.categories.Category;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface WithRequiredCategory {

    CategoryFinder getCategoryFinder();

    default CompletionStage<Result> requireCategory(final String identifier, final Function<Category, CompletionStage<Result>> nextAction) {
        return getCategoryFinder().apply(identifier)
                .thenComposeAsync(categoryOpt -> categoryOpt
                                .map(nextAction)
                                .orElseGet(this::handleNotFoundCategory),
                        HttpExecution.defaultContext());
    }

    CompletionStage<Result> handleNotFoundCategory();
}
