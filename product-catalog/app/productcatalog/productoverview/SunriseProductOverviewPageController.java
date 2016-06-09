package productcatalog.productoverview;

import com.google.inject.Injector;
import common.contexts.RequestContext;
import common.contexts.UserContext;
import common.controllers.SunriseFrameworkController;
import common.controllers.SunrisePageData;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;
import productcatalog.productoverview.search.SearchCriteriaImpl;
import productcatalog.productoverview.search.facetedsearch.FacetedSearchSelector;
import productcatalog.productoverview.search.facetedsearch.FacetedSearchSelectorListFactory;
import productcatalog.productoverview.search.pagination.Pagination;
import productcatalog.productoverview.search.pagination.PaginationFactory;
import productcatalog.productoverview.search.productsperpage.ProductsPerPageSelector;
import productcatalog.productoverview.search.productsperpage.ProductsPerPageSelectorFactory;
import productcatalog.productoverview.search.searchbox.SearchBox;
import productcatalog.productoverview.search.searchbox.SearchBoxFactory;
import productcatalog.productoverview.search.sort.SortSelector;
import productcatalog.productoverview.search.sort.SortSelectorFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseProductOverviewPageController extends SunriseFrameworkController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SunriseProductOverviewPageController.class);

    @Inject
    private Injector injector;
    @Inject
    private UserContext userContext;
    @Inject
    private CategoryTree categoryTree;
    @Inject
    private Configuration configuration;
    @Inject
    private ProductOverviewPageContentFactory productOverviewPageContentFactory;
    @Inject
    private PaginationFactory paginationFactory;
    @Inject
    private SearchBoxFactory searchBoxFactory;
    @Inject
    private SortSelectorFactory sortSelectorFactory;
    @Inject
    private ProductsPerPageSelectorFactory productsPerPageSelectorFactory;
    @Inject
    private FacetedSearchSelectorListFactory facetedSearchSelectorListFactory;
    @Inject
    private RequestContext requestContext;
    @Inject
    private ProductSearchByCategorySlugAndSearchCriteria productSearchByCategorySlugAndSearchCriteria;

    @Nullable
    private String categorySlug;
    @Nullable
    private Integer page;

    /* Controller actions */

    public CompletionStage<Result> showProductsByCategorySlug(final String languageTag, final int page, final String categorySlug) {
        this.page = page;
        this.categorySlug = categorySlug;
        final Optional<Category> category = categoryTree.findBySlug(userContext.locale(), categorySlug);
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
        final SearchCriteriaImpl searchCriteria = getSearchCriteria(singletonList(category));
        return productSearchByCategorySlugAndSearchCriteria.searchProducts(categorySlug, searchCriteria)
                .thenApplyAsync(searchResult -> ok(renderPage(createPageContent(category, searchResult.getPagedSearchResult().get(), searchCriteria))), HttpExecution.defaultContext());
    }

    public CompletionStage<Result> search(final String languageTag, final int page) {
        final SearchCriteriaImpl searchCriteria = getSearchCriteria(emptyList());
        if (searchCriteria.getSearchTerm().isPresent()) {
            return productSearchByCategorySlugAndSearchCriteria.searchProducts(categorySlug, searchCriteria)
                    .thenApplyAsync(searchResult -> ok(renderPage(createPageContent(searchResult.getPagedSearchResult().get(), searchCriteria))), HttpExecution.defaultContext());
        } else {
            return completedFuture(badRequest("Search term missing"));
        }
    }

    protected SearchCriteriaImpl getSearchCriteria(final List<Category> selectedCategories) {
        final Pagination pagination = paginationFactory.create();
        final SearchBox searchBox = searchBoxFactory.create();
        final SortSelector sortSelector = sortSelectorFactory.create();
        final ProductsPerPageSelector productsPerPageSelector = productsPerPageSelectorFactory.create();
        final List<FacetedSearchSelector> facetedSearchSelectors = facetedSearchSelectorListFactory.create(selectedCategories);
        return SearchCriteriaImpl.of(pagination, searchBox, sortSelector, productsPerPageSelector, facetedSearchSelectors);
    }

    /* Page rendering methods */

    protected ProductOverviewPageContent createPageContent(final Category category, final PagedSearchResult<ProductProjection> searchResult,
                                                           final SearchCriteriaImpl searchCriteria) {
        final ProductOverviewPageContent content = productOverviewPageContentFactory.create(category, searchResult, searchCriteria);
        fillPageContent(content, searchResult, searchCriteria);
        return content;
    }

    protected ProductOverviewPageContent createPageContent(final PagedSearchResult<ProductProjection> searchResult,
                                                           final SearchCriteriaImpl searchCriteria) {
        final ProductOverviewPageContent content = productOverviewPageContentFactory.create(searchResult, searchCriteria);
        fillPageContent(content, searchResult, searchCriteria);
        return content;
    }

    protected void fillPageContent(final ProductOverviewPageContent content,
                                   final PagedSearchResult<ProductProjection> searchResult,
                                   final SearchCriteriaImpl searchCriteria) {
        content.setFilterProductsUrl(request().path());
        final Integer paginationDisplayedPages = configuration.getInt("pop.pagination.displayedPages", 6);
        content.setPagination(new PaginationBean(requestContext, searchResult, searchCriteria.getPage(), searchCriteria.getPageSize(), paginationDisplayedPages));
    }

    protected Html renderPage(final ProductOverviewPageContent pageContent) {
        final SunrisePageData pageData = pageData(pageContent);
        return templateEngine().renderToHtml("pop", pageData, userContext.locales());
    }
}