package com.commercetools.sunrise.productcatalog.productdetail;

import io.sphere.sdk.products.ProductProjection;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface WithRequiredProduct {

    ProductFinder getProductFinder();

    default CompletionStage<Result> requireProduct(final String identifier, final Function<ProductProjection, CompletionStage<Result>> nextAction) {
        return getProductFinder().apply(identifier)
                .thenComposeAsync(productOpt -> productOpt
                                .map(nextAction)
                                .orElseGet(this::handleNotFoundProduct),
                        HttpExecution.defaultContext());
    }

    CompletionStage<Result> handleNotFoundProduct();
}
