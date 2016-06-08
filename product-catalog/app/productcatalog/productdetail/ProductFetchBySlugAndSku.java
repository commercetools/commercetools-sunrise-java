package productcatalog.productdetail;

import common.contexts.UserContext;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.queries.PagedQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.UnaryOperator;

public final class ProductFetchBySlugAndSku implements ProductFetch<String, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductFetchBySlugAndSku.class);

    @Inject
    private SphereClient sphereClient;
    @Inject
    private UserContext userContext;

    @Override
    public CompletionStage<ProductFetchResult> findProduct(final String productIdentifier,
                                                           final String variantIdentifier,
                                                           final UnaryOperator<ProductProjectionQuery> queryFilter,
                                                           final UnaryOperator<ProductProjectionSearch> searchFilter) {
        return findProduct(productIdentifier, queryFilter)
                .thenApplyAsync(productOpt -> productOpt
                .map(product -> findVariant(variantIdentifier, product)
                        .map(variant -> ProductFetchResult.of(product, variant))
                        .orElseGet(() -> ProductFetchResult.ofNotFoundVariant(product)))
                .orElseGet(ProductFetchResult::ofNotFoundProduct),
                HttpExecution.defaultContext());
    }

    private CompletionStage<Optional<ProductProjection>> findProduct(final String productIdentifier, final UnaryOperator<ProductProjectionQuery> queryFilter) {
        return findProductBySlug(productIdentifier, userContext.locale(), queryFilter);
    }

    private Optional<ProductVariant> findVariant(final String variantIdentifier, final ProductProjection product) {
        return product.findVariantBySku(variantIdentifier);
    }

    /**
     * Gets a product, uniquely identified by a slug for a given locale.
     * @param slug the product slug
     * @param locale the locale in which you provide the slug
     * @param queryFilter
     * @return A CompletionStage of an optionally found ProductProjection
     */
    private CompletionStage<Optional<ProductProjection>> findProductBySlug(final String slug, final Locale locale, final UnaryOperator<ProductProjectionQuery> queryFilter) {
        final ProductProjectionQuery request = ProductProjectionQuery.ofCurrent().bySlug(locale, slug);
        return sphereClient.execute(queryFilter.apply(request))
                .thenApplyAsync(PagedQueryResult::head, HttpExecution.defaultContext())
                .whenCompleteAsync((productOpt, t) -> {
                    if (productOpt.isPresent()) {
                        final String productId = productOpt.get().getId();
                        LOGGER.trace("Found product for slug {} in locale {} with ID {}.", slug, locale, productId);
                    } else {
                        LOGGER.trace("No product found for slug {} in locale {}.", slug, locale);
                    }
                }, HttpExecution.defaultContext());
    }
}
