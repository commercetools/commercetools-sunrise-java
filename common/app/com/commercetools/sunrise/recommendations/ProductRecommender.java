package com.commercetools.sunrise.recommendations;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Provides recommendation of products.
 */
@ImplementedBy(DefaultProductRecommender.class)
public interface ProductRecommender {

    /**
     * Gets a distinct list of products recommendations for the given product, of length {@code numProducts} or less
     * if the requested number is greater than the number of available products.
     * @param product the product to get suggestions for
     * @param numProducts the number of products the returned list should contain
     * @return a {@code CompletionStage} of the list of products of size {@code numProducts} or less
     */
    CompletionStage<List<ProductProjection>> relatedToProduct(final ProductProjection product, final int numProducts);

    /**
     * Gets a list of products recommendations for the given categories, of length {@code numProducts} or less
     * if the requested number is greater than the number of available products.
     * @param categories the categories to get suggestions from
     * @param numProducts the number of products the returned list should contain
     * @return a {@code CompletionStage} of the list of products of size {@code numProducts} or less
     */
    CompletionStage<List<ProductProjection>> relatedToCategories(final List<Category> categories, final int numProducts);
}
