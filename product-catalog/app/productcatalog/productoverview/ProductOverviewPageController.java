package productcatalog.productoverview;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;
import productcatalog.common.ProductCatalogController;
import productcatalog.productoverview.search.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static common.utils.LogUtils.logProductRequest;
import static common.utils.UrlUtils.getQueryString;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Singleton
public class ProductOverviewPageController extends ProductCatalogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductOverviewPageController.class);
    private final int paginationDisplayedPages;

    @Inject
    private SearchConfig searchConfig;
    @Inject
    private ProductOverviewPageContentFactory productOverviewPageContentFactory;

    @Inject
    public ProductOverviewPageController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
        this.paginationDisplayedPages = configuration().getInt("pop.pagination.displayedPages", 6);
    }

    /* Controller actions */

    public CompletionStage<Result> show(final String languageTag, final int page, final String categorySlug) {
        final UserContext userContext = userContext(languageTag);
        final Optional<Category> categoryOpt = categoryTree().findBySlug(userContext.locale(), categorySlug);
        if (categoryOpt.isPresent()) {
            final SearchCriteria searchCriteria = getSearchCriteria(page, singletonList(categoryOpt.get()), userContext);
            return searchProducts(searchCriteria).thenApplyAsync(searchResult ->
                    renderCategoryPage(categoryOpt.get(), searchResult, searchCriteria, userContext), HttpExecution.defaultContext());
        } else {
            return CompletableFuture.completedFuture(notFound("Category not found: " + categorySlug));
        }
    }

    public CompletionStage<Result> search(final String languageTag, final int page) {
        final UserContext userContext = userContext(languageTag);
        final SearchCriteria searchCriteria = getSearchCriteria(page, emptyList(), userContext);
        if (searchCriteria.getSearchBox().getSearchTerm().isPresent()) {
            final CompletionStage<PagedSearchResult<ProductProjection>> searchResultStage = searchProducts(searchCriteria);
            return searchResultStage.thenApplyAsync(searchResult ->
                    renderSearchPage(searchResult, searchCriteria, userContext), HttpExecution.defaultContext());
        } else {
            return CompletableFuture.completedFuture(badRequest("Search term missing"));
        }
    }

    protected SearchCriteria getSearchCriteria(final int page, final List<Category> selectedCategories, final UserContext userContext) {
        final Map<String, List<String>> queryString = getQueryString(request());
        final SearchBox searchBox = SearchBoxFactory.of(searchConfig, queryString, userContext).create();
        final ProductsPerPageSelector productsPerPageSelector = ProductsPerPageSelectorFactory.of(searchConfig.getProductsPerPageConfig(), queryString).create();
        final SortSelector sortSelector = SortSelectorFactory.of(searchConfig.getSortConfig(), queryString, userContext).create();
        final List<FacetSelector> facetSelectors = FacetSelectorsFactory.of(searchConfig.getFacetConfigList(), queryString, selectedCategories, userContext, categoryTree()).create();
        return SearchCriteria.of(page, searchBox, productsPerPageSelector, sortSelector, facetSelectors);
    }

    /**
     * Gets a list of Products from a PagedQueryResult
     * @param searchCriteria all information regarding the request parameters
     * @return A CompletionStage of a paged result of the search request
     */
    protected CompletionStage<PagedSearchResult<ProductProjection>> searchProducts(final SearchCriteria searchCriteria) {
        final int pageSize = searchCriteria.getProductsPerPageSelector().getSelectedPageSize();
        final int offset = (searchCriteria.getPage() - 1) * pageSize;
        final ProductProjectionSearch baseRequest = ProductProjectionSearch.ofCurrent()
                .withFacetedSearch(searchCriteria.getFacetSelectors().stream().map(FacetSelector::getFacetedSearchExpression).collect(toList()))
                .withSort(searchCriteria.getSortSelector().getSelectedSortExpressions())
                .withOffset(offset)
                .withLimit(pageSize);
        final ProductProjectionSearch request = searchCriteria.getSearchBox().getSearchTerm()
                .map(baseRequest::withText)
                .orElse(baseRequest);
        return sphere().execute(request)
                .whenCompleteAsync((result, t) -> logProductRequest(LOGGER, request, result), HttpExecution.defaultContext());
    }

    /* Page rendering methods */

    protected Result renderCategoryPage(final Category category, final PagedSearchResult<ProductProjection> searchResult,
                                        final SearchCriteria searchCriteria, final UserContext userContext) {
        final ProductOverviewPageContent content = productOverviewPageContentFactory.create(category, searchResult, searchCriteria, categoryTreeInNew());
        fillPageContent(content, searchResult, searchCriteria);
        return ok(renderPage(userContext, content));
    }

    protected Result renderSearchPage(final PagedSearchResult<ProductProjection> searchResult,
                                      final SearchCriteria searchCriteria, final UserContext userContext) {
        final ProductOverviewPageContent content = productOverviewPageContentFactory.create(searchResult, searchCriteria, categoryTreeInNew());
        fillPageContent(content, searchResult, searchCriteria);
        return ok(renderPage(userContext, content));
    }

    protected void fillPageContent(final ProductOverviewPageContent content,
                                   final PagedSearchResult<ProductProjection> searchResult,
                                   final SearchCriteria searchCriteria) {
        content.setFilterProductsUrl(request().path());
        content.setPagination(new PaginationBean(requestContext(request()), searchResult, searchCriteria.getPage(), searchCriteria.getProductsPerPageSelector().getSelectedPageSize(), paginationDisplayedPages));
    }

    protected Html renderPage(final UserContext userContext, final ProductOverviewPageContent content) {
        final SunrisePageData pageData = pageData(userContext, content, ctx(), session());
        return templateEngine().renderToHtml("pop", pageData, userContext.locales());
    }
}