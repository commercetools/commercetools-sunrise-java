package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTreeExtended;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.search.PagedSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.F;
import productcatalog.controllers.SearchCriteria;

import javax.inject.Inject;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class ProductServiceImpl implements ProductService {
    private static final Random RANDOM = new Random();
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final PlayJavaSphereClient sphere;

    @Inject
    public ProductServiceImpl(final PlayJavaSphereClient sphere) {
        this.sphere = sphere;
    }

    public F.Promise<PagedSearchResult<ProductProjection>> searchProducts(final int page, final SearchCriteria searchCriteria) {
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
        final F.Promise<PagedSearchResult<ProductProjection>> resultPromise = sphere.execute(request);
        logRequest(request, resultPromise);
        return resultPromise;
    }

    public F.Promise<Optional<ProductProjection>> findProductBySlug(final Locale locale, final String slug) {
        final ProductProjectionQuery request = ProductProjectionQuery.ofCurrent().bySlug(locale, slug);
        final F.Promise<Optional<ProductProjection>> productOptPromise = sphere.execute(request).map(PagedQueryResult::head);
        productOptPromise.onRedeem(productOpt -> {
            if (productOpt.isPresent()) {
                final String productId = productOpt.get().getId();
                LOGGER.trace("Found product for slug {} in locale {} with ID {}.", slug, locale, productId);
            } else {
                LOGGER.trace("No product found for slug {} in locale {}.", slug, locale);
            }
        });
        return productOptPromise;
    }

    @Override
    public F.Promise<List<ProductProjection>> getSuggestions(final ProductProjection product, final CategoryTreeExtended categoryTree,
                                                             final int numSuggestions) {
        final List<Category> categories = product.getCategories().stream()
                .map(c -> categoryTree.findById(c.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
        final List<Category> siblingCategories = categoryTree.getSiblings(categories);
        return getSuggestions(siblingCategories, numSuggestions);
    }

    private F.Promise<List<ProductProjection>> getSuggestions(final List<Category> categories, final int numSuggestions) {
        final List<String> categoryIds = categories.stream()
                .map(Category::getId)
                .collect(toList());
        final ProductProjectionSearch request = ProductProjectionSearch.ofCurrent()
                .withQueryFilters(filter -> filter.categories().id().byAny(categoryIds));
        final F.Promise<PagedSearchResult<ProductProjection>> resultPromise = sphere.execute(request);
        logRequest(request, resultPromise);
        return resultPromise.map(PagedSearchResult::getResults)
                .map(results -> pickRandomElements(results, numSuggestions));
    }

    private <T> List<T> pickRandomElements(final List<T> elements, final int n) {
        if (elements.size() < n) {
            return pickRandomElements(elements, elements.size());
        } else {
            final List<T> picked = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                final int index = RANDOM.nextInt(elements.size());
                picked.add(elements.get(index));
                elements.remove(index);
            }
            return picked;
        }
    }

    private void logRequest(final ProductProjectionSearch request, final F.Promise<PagedSearchResult<ProductProjection>> resultPromise) {
        resultPromise.onRedeem(result -> LOGGER.debug("Fetched {} out of {} products with request {}",
                result.size(),
                result.getTotal(),
                request.httpRequestIntent().getPath()));
    }
}
