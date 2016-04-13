package common.suggestion;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;

/**
 * Provides suggested products.
 */
public interface ProductSuggestion {

    /**
     * Gets a set of products suggestions for the given product, of length {@code numProducts} or less
     * if the requested number is greater than the number of available products.
     * @param product the product to get suggestions for
     * @param numProducts the number of products the returned set should contain
     * @return a {@code CompletionStage} of the set of products of size {@code numProducts} or less
     */
    CompletionStage<Set<ProductProjection>> relatedToProduct(final ProductProjection product, final int numProducts);

    /**
     * Gets a set of products suggestions for the given categories, of length {@code numProducts} or less
     * if the requested number is greater than the number of available products.
     * @param categories the categories to get suggestions from
     * @param numProducts the number of products the returned set should contain
     * @return a {@code CompletionStage} of the set of products of size {@code numProducts} or less
     */
    CompletionStage<Set<ProductProjection>> relatedToCategories(final List<Category> categories, final int numProducts);
}
