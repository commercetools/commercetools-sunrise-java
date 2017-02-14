package com.commercetools.sunrise.productcatalog.productdetail;

import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface WithRequiredProductVariant {

    ProductVariantFinder getProductVariantFinder();

    default CompletionStage<Result> requireProductVariant(final ProductProjection product, final String variantIdentifier,
                                                          final Function<ProductVariant, CompletionStage<Result>> nextAction) {
        return getProductVariantFinder().apply(product, variantIdentifier)
                .thenComposeAsync(variantOpt -> variantOpt
                                .map(nextAction)
                                .orElseGet(() -> handleNotFoundProductVariant(product)),
                        HttpExecution.defaultContext());
    }

    CompletionStage<Result> handleNotFoundProductVariant(final ProductProjection product);
}
