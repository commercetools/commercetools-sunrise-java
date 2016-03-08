package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTreeExtended;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.search.PagedSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;
import productcatalog.productoverview.SearchCriteria;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletionStage;

import static java.util.stream.Collectors.toList;

public class ProductServiceImpl implements ProductService {
    private static final Random RANDOM = new Random();
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final PlayJavaSphereClient sphere;

    @Inject
    public ProductServiceImpl(final PlayJavaSphereClient sphere) {
        this.sphere = sphere;
    }

    public CompletionStage<PagedSearchResult<ProductProjection>> searchProducts(final int page, final SearchCriteria searchCriteria) {
        final int pageSize = searchCriteria.selectedDisplay();
        final int offset = (page - 1) * pageSize;
        final ProductProjectionSearch baseRequest = ProductProjectionSearch.ofCurrent()
                .withFacetedSearch(searchCriteria.selectedFacets())
                .withSort(searchCriteria.selectedSort())
                .withOffset(offset)
                .withLimit(pageSize);
        final ProductProjectionSearch request = searchCriteria.searchTerm()
                .map(baseRequest::withText)
                .orElse(baseRequest);
        final CompletionStage<PagedSearchResult<ProductProjection>> resultStage = sphere.execute(request);
        logRequest(request, resultStage);
        return resultStage;
    }

    public CompletionStage<Optional<ProductProjection>> findProductBySlug(final Locale locale, final String slug) {
        final ProductProjectionQuery request = ProductProjectionQuery.ofCurrent().bySlug(locale, slug);
        final CompletionStage<Optional<ProductProjection>> productOptStage = sphere.execute(request).map(PagedQueryResult::head);
        productOptStage.thenAcceptAsync(productOpt -> {
            if (productOpt.isPresent()) {
                final String productId = productOpt.get().getId();
                LOGGER.trace("Found product for slug {} in locale {} with ID {}.", slug, locale, productId);
            } else {
                LOGGER.trace("No product found for slug {} in locale {}.", slug, locale);
            }
        }, HttpExecution.defaultContext());
        return productOptStage;
    }

    @Override
    public CompletionStage<List<ProductProjection>> getSuggestions(final ProductProjection product, final CategoryTreeExtended categoryTree,
                                                                   final int numSuggestions) {
        final List<Category> categories = product.getCategories().stream()
                .map(c -> categoryTree.findById(c.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
        final List<Category> siblingCategories = categoryTree.getSiblings(categories);
        final CompletionStage<List<ProductProjection>> suggestions;
        if (siblingCategories.isEmpty()) {
            suggestions = getSuggestions(categories, numSuggestions);
        } else {
            suggestions = getSuggestions(siblingCategories, numSuggestions);
        }
        return suggestions;
    }

    @Override
    public CompletionStage<List<ProductProjection>> getSuggestions(final List<Category> categories, final int numSuggestions) {
        final List<String> categoryIds = categories.stream()
                .map(Category::getId)
                .collect(toList());
        final ProductProjectionSearch request = ProductProjectionSearch.ofCurrent()
                .withLimit(numSuggestions)
                .withQueryFilters(filter -> filter.categories().id().byAny(categoryIds));
        final CompletionStage<PagedSearchResult<ProductProjection>> resultStage = sphere.execute(request);
        logRequest(request, resultStage);
        return resultStage.thenApplyAsync(PagedSearchResult::getResults, HttpExecution.defaultContext());
    }

    private void logRequest(final ProductProjectionSearch request, final CompletionStage<PagedSearchResult<ProductProjection>> resultStage) {
        resultStage.thenAcceptAsync(result -> LOGGER.debug("Fetched {} out of {} products with request {}",
                result.size(),
                result.getTotal(),
                request.httpRequestIntent().getPath()));
    }
}
