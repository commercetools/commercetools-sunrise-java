package productcatalog.productoverview;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
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
import productcatalog.productoverview.search.*;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static common.utils.LogUtils.logProductRequest;
import static common.utils.UrlUtils.getQueryString;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

public abstract class SunriseProductOverviewPageController extends SunriseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SunriseProductOverviewPageController.class);
    private final int paginationDisplayedPages;

    @Inject
    private SearchConfig searchConfig;
    @Inject
    private ProductOverviewPageContentFactory productOverviewPageContentFactory;
    @Inject
    private UserContext userContext;

    @Nullable
    private String categorySlug;
    @Nullable
    private Integer page;

    @Inject
    public SunriseProductOverviewPageController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
        this.paginationDisplayedPages = configuration().getInt("pop.pagination.displayedPages", 6);
    }

    /* Controller actions */

    public CompletionStage<Result> showProductsByCategorySlug(final String languageTag, final int page, final String categorySlug) {
        this.page = page;
        this.categorySlug = categorySlug;
        final Optional<Category> category = categoryTree().findBySlug(userContext.locale(), categorySlug);
        if (category.isPresent()) {
            return handleFoundCategory(category.get());
        } else {
            return handleNotFoundCategory();
        }
    }

    protected Optional<String> getCategorySlug() {
        return Optional.ofNullable(categorySlug);
    }

    protected Optional<Integer> getPage() {
        return Optional.ofNullable(page);
    }

    private CompletionStage<Result> handleNotFoundCategory() {
        return completedFuture(notFoundCategoryResult());
    }

    private Result notFoundCategoryResult() {
        return notFound("Category not found: " + categorySlug);
    }

    private CompletionStage<Result> handleFoundCategory(final Category category) {
        final SearchCriteria searchCriteria = getSearchCriteria(singletonList(category), userContext);
        return searchProducts(searchCriteria).thenApplyAsync(searchResult ->
                ok(renderPage(createPageContent(category, searchResult, searchCriteria))), HttpExecution.defaultContext());
    }

    public CompletionStage<Result> search(final String languageTag, final int page) {
        final UserContext userContext = userContext(languageTag);
        final SearchCriteria searchCriteria = getSearchCriteria(emptyList(), userContext);
        if (searchCriteria.getSearchBox().getSearchTerm().isPresent()) {
            final CompletionStage<PagedSearchResult<ProductProjection>> searchResultStage = searchProducts(searchCriteria);
            return searchResultStage.thenApplyAsync(searchResult ->
                    ok(renderPage(createPageContent(searchResult, searchCriteria))), HttpExecution.defaultContext());
        } else {
            return completedFuture(badRequest("Search term missing"));
        }
    }

    protected SearchCriteria getSearchCriteria(final List<Category> selectedCategories, final UserContext userContext) {
        final Map<String, List<String>> queryString = getQueryString(request());
        final SearchBox searchBox = SearchBoxFactory.of(searchConfig, queryString, userContext).create();
        final ProductsPerPageSelector productsPerPageSelector = ProductsPerPageSelectorFactory.of(searchConfig.getProductsPerPageConfig(), queryString).create();
        final SortSelector sortSelector = SortSelectorFactory.of(searchConfig.getSortConfig(), queryString, userContext).create();
        final List<FacetSelector> facetSelectors = FacetSelectorsFactory.of(searchConfig.getFacetConfigList(), queryString, selectedCategories, userContext, categoryTree()).create();
        return SearchCriteria.of(getPage().orElse(0), searchBox, productsPerPageSelector, sortSelector, facetSelectors);
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

    protected ProductOverviewPageContent createPageContent(final Category category, final PagedSearchResult<ProductProjection> searchResult,
                                                           final SearchCriteria searchCriteria) {
        final ProductOverviewPageContent content = productOverviewPageContentFactory.create(category, searchResult, searchCriteria);
        fillPageContent(content, searchResult, searchCriteria);
        return content;
    }

    protected ProductOverviewPageContent createPageContent(final PagedSearchResult<ProductProjection> searchResult,
                                                           final SearchCriteria searchCriteria) {
        final ProductOverviewPageContent content = productOverviewPageContentFactory.create(searchResult, searchCriteria);
        fillPageContent(content, searchResult, searchCriteria);
        return content;
    }

    protected void fillPageContent(final ProductOverviewPageContent content,
                                   final PagedSearchResult<ProductProjection> searchResult,
                                   final SearchCriteria searchCriteria) {
        content.setFilterProductsUrl(request().path());
        content.setPagination(new PaginationBean(requestContext(request()), searchResult, searchCriteria.getPage(), searchCriteria.getProductsPerPageSelector().getSelectedPageSize(), paginationDisplayedPages));
    }

    protected Html renderPage(final ProductOverviewPageContent pageContent) {
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateEngine().renderToHtml("pop", pageData, userContext.locales());
    }
}