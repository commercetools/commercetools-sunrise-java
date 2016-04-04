package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.HttpRequestIntent;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.http.FormUrlEncodedHttpRequestBody;
import io.sphere.sdk.http.StringHttpRequestBody;
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
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class ProductServiceImpl implements ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final SphereClient sphere;

    @Inject
    public ProductServiceImpl(final SphereClient sphere) {
        this.sphere = sphere;
    }

    public CompletionStage<PagedSearchResult<ProductProjection>> searchProducts(final int page, final SearchCriteria searchCriteria) {
        final int pageSize = searchCriteria.getDisplayCriteria().getSelectedPageSize();
        final int offset = (page - 1) * pageSize;
        final ProductProjectionSearch baseRequest = ProductProjectionSearch.ofCurrent()
                .withFacetedSearch(searchCriteria.getFacetsCriteria().getFacetedSearchExpressions())
                .withSort(searchCriteria.getSortCriteria().getSelectedSortExpressions())
                .withOffset(offset)
                .withLimit(pageSize);
        final ProductProjectionSearch request = searchCriteria.getSearchTerm()
                .map(baseRequest::withText)
                .orElse(baseRequest);
        return sphere.execute(request)
                .whenCompleteAsync((result, t) -> logRequest(request, result), HttpExecution.defaultContext());
    }

    public CompletionStage<Optional<ProductProjection>> findProductBySlug(final Locale locale, final String slug) {
        final ProductProjectionQuery request = ProductProjectionQuery.ofCurrent().bySlug(locale, slug);
        return sphere.execute(request)
                .thenApplyAsync(PagedQueryResult::head, HttpExecution.defaultContext())
                .whenComplete((productOpt, t) -> {
                    if (productOpt.isPresent()) {
                        final String productId = productOpt.get().getId();
                        LOGGER.trace("Found product for slug {} in locale {} with ID {}.", slug, locale, productId);
                    } else {
                        LOGGER.trace("No product found for slug {} in locale {}.", slug, locale);
                    }
                });
    }

    @Override
    public CompletionStage<List<ProductProjection>> getSuggestions(final ProductProjection product, final CategoryTree categoryTree,
                                                                   final int numSuggestions) {
        final List<Category> categories = product.getCategories().stream()
                .map(c -> categoryTree.findById(c.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
        final List<Category> siblingCategories = categoryTree.findSiblings(categories);
        final List<Category> targetCategories = siblingCategories.isEmpty() ? categories : siblingCategories;
        return getSuggestions(targetCategories, numSuggestions);
    }

    @Override
    public CompletionStage<List<ProductProjection>> getSuggestions(final List<Category> categories, final int numSuggestions) {
        final List<String> categoryIds = categories.stream()
                .map(Category::getId)
                .collect(toList());
        final ProductProjectionSearch request = ProductProjectionSearch.ofCurrent()
                .withLimit(numSuggestions)
                .withQueryFilters(filter -> filter.categories().id().containsAny(categoryIds));
        return sphere.execute(request)
                .whenCompleteAsync((result, t) -> logRequest(request, result), HttpExecution.defaultContext())
                .thenApply(PagedSearchResult::getResults);
    }

    private static void logRequest(final ProductProjectionSearch request, final PagedSearchResult<ProductProjection> result) {
        final HttpRequestIntent httpRequest = request.httpRequestIntent();
        final String requestBody = printableRequestBody(httpRequest)
                .map(body -> " with body {" + body + "}")
                .orElse("");
        LOGGER.debug("Fetched {} out of {} products with request {} {}",
                result.size(),
                result.getTotal(),
                httpRequest.getHttpMethod(),
                httpRequest.getPath() + requestBody);
    }

    private static Optional<String> printableRequestBody(final HttpRequestIntent httpRequest) {
        return Optional.ofNullable(httpRequest.getBody())
                .map(body -> {
                    final String bodyAsString;
                    if (httpRequest.getBody() instanceof StringHttpRequestBody) {
                        bodyAsString = ((StringHttpRequestBody) httpRequest.getBody()).getSecuredBody();
                    } else if (httpRequest.getBody() instanceof FormUrlEncodedHttpRequestBody) {
                        bodyAsString = ((FormUrlEncodedHttpRequestBody) httpRequest.getBody()).getParameters().stream()
                                .map(pair -> pair.getName() + "=" + pair.getValue())
                                .collect(Collectors.joining("&"));
                    } else {
                        bodyAsString = "**omitted output**";
                    }
                    return bodyAsString;
                });
    }
}
