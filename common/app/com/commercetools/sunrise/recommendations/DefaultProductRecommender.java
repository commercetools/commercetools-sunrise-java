package com.commercetools.sunrise.recommendations;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.ctp.CtpLogUtils.printableProductRequest;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Default implementation for Sunrise suggestions.
 */
public class DefaultProductRecommender implements ProductRecommender {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRecommender.class);

    private final SphereClient sphereClient;
    private final PriceSelection priceSelection;

    @Inject
    protected DefaultProductRecommender(final SphereClient sphereClient, final PriceSelection priceSelection) {
        this.sphereClient = sphereClient;
        this.priceSelection = priceSelection;
    }

    /**
     * Gets products from the same categories as the given product, excluding the product itself, up to {@code numProducts}.
     * Most expensive products first.
     * @param product the product to get suggestions for
     * @param numProducts the number of products the returned set should contain
     * @return the products related to this product
     */
    @Override
    public CompletionStage<List<ProductProjection>> relatedToProduct(final ProductProjection product, final int numProducts) {
        final Set<String> categoryIds = product.getCategories().stream()
                .map(Reference::getId)
                .collect(toSet());
        if (categoryIds.isEmpty()) {
            return CompletableFuture.completedFuture(emptyList());
        } else {
            return productsFromCategoryIds(categoryIds, numProducts + 1)
                    .thenApply(products -> products.stream()
                            .filter(p -> !p.getId().equals(product.getId()))
                            .limit(numProducts)
                            .collect(toList()));
        }
    }

    /**
     * Gets products belonging to any of the given categories, up to {@code numProducts}.
     * Most expensive products first.
     * @param categories the categories to get suggestions from
     * @param numProducts the number of products the returned set should contain
     * @return the products related to these categories
     */
    @Override
    public CompletionStage<List<ProductProjection>> relatedToCategories(final List<Category> categories, final int numProducts) {
        if (categories.isEmpty()) {
            return CompletableFuture.completedFuture(emptyList());
        } else {
            final List<String> categoryIds = categories.stream()
                    .map(Category::getId)
                    .collect(toList());
            return productsFromCategoryIds(categoryIds, numProducts);
        }
    }

    /**
     * Gets the products belonging to the given categories, up to {@code numProducts}, most expensive products first.
     * @param categoryIds the category IDs to get products from
     * @param numProducts the number of products the returned list should contain
     * @return the products related to these categories
     */
    private CompletionStage<List<ProductProjection>> productsFromCategoryIds(final Iterable<String> categoryIds, final int numProducts) {
        final ProductProjectionSearch request = ProductProjectionSearch.ofCurrent()
                .withLimit(numProducts)
                .withQueryFilters(product -> product.categories().id().containsAny(categoryIds))
                .withSort(product -> product.allVariants().price().desc())
                .withMarkingMatchingVariants(false)
                .withPriceSelection(priceSelection);
        return sphereClient.execute(request)
                .whenCompleteAsync((result, t) -> LOGGER.debug(printableProductRequest(request, result)), HttpExecution.defaultContext())
                .thenApply(PagedSearchResult::getResults);
    }
}
