package common.suggestion;

import common.contexts.UserContext;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.ProductProjection;
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

import static common.utils.LogUtils.logProductRequest;
import static common.utils.PriceUtils.createPriceSelection;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Default implementation for Sunrise suggestions.
 */
public class SunriseProductRecommendation implements ProductRecommendation {

    private static final Logger LOGGER = LoggerFactory.getLogger(SunriseProductRecommendation.class);
    private final SphereClient sphere;

    @Inject
    public SunriseProductRecommendation(final SphereClient sphere) {
        this.sphere = sphere;
    }

    /**
     * Gets products from the same categories as the given product, excluding the product itself, up to {@code numProducts}.
     * Most expensive products first.
     * @param product the product to get suggestions for
     * @param numProducts the number of products the returned set should contain
     * @param userContext user's context
     * @return the products related to this product
     */
    @Override
    public CompletionStage<Set<ProductProjection>> relatedToProduct(final ProductProjection product, final int numProducts, final UserContext userContext) {
        final Set<String> categoryIds = product.getCategories().stream()
                .map(Reference::getId)
                .collect(toSet());
        return productsFromCategoryIds(categoryIds, numProducts + 1, userContext)
                .thenApply(products -> products.stream()
                        .filter(p -> !p.getId().equals(product.getId()))
                        .limit(numProducts)
                        .collect(toSet()));
    }

    /**
     * Gets products belonging to any of the given categories, up to {@code numProducts}.
     * Most expensive products first.
     * @param categories the categories to get suggestions from
     * @param numProducts the number of products the returned set should contain
     * @param userContext user's context
     * @return the products related to these categories
     */
    @Override
    public CompletionStage<Set<ProductProjection>> relatedToCategories(final List<Category> categories, final int numProducts, final UserContext userContext) {
        if (categories.isEmpty()) {
            return CompletableFuture.completedFuture(emptySet());
        } else {
            final List<String> categoryIds = categories.stream()
                    .map(Category::getId)
                    .collect(toList());
            return productsFromCategoryIds(categoryIds, numProducts, userContext);
        }
    }

    /**
     * Gets the products belonging to the given categories, up to {@code numProducts}, most expensive products first.
     * @param categoryIds the category IDs to get products from
     * @param numProducts the number of products the returned set should contain
     * @return the products related to these categories
     */
    private CompletionStage<Set<ProductProjection>> productsFromCategoryIds(final Iterable<String> categoryIds, final int numProducts, final UserContext userContext) {
        final ProductProjectionSearch request = ProductProjectionSearch.ofCurrent()
                .withLimit(numProducts)
                .withQueryFilters(product -> product.categories().id().containsAny(categoryIds))
                .withSort(product -> product.allVariants().price().desc())
                .withPriceSelection(createPriceSelection(userContext));
        return sphere.execute(request)
                .whenCompleteAsync((result, t) -> logProductRequest(LOGGER, request, result), HttpExecution.defaultContext())
                .thenApply(SunriseProductRecommendation::resultToProductSet);
    }

    private static Set<ProductProjection> resultToProductSet(final PagedSearchResult<ProductProjection> result) {
        return result.getResults().stream().collect(toSet());
    }
}
