package productcatalog.productoverview;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.models.ProductDataConfig;
import common.suggestion.ProductRecommendation;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;
import productcatalog.common.BreadcrumbBean;
import productcatalog.common.ProductCatalogController;
import productcatalog.common.ProductListData;
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
    public ProductOverviewPageController(final ControllerDependency controllerDependency, final ProductRecommendation productRecommendation,
                                         final ProductDataConfig productDataConfig, final SearchConfig searchConfig) {
        super(controllerDependency, productRecommendation, productDataConfig, searchConfig);
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
        final SearchConfig searchConfig = searchConfig();
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

    protected ProductOverviewPageContent createPageContent(final PagedSearchResult<ProductProjection> searchResult,
                                                           final SearchCriteria searchCriteria, final UserContext userContext) {
        final ProductOverviewPageContent content = new ProductOverviewPageContent();
        content.setFilterProductsUrl(request().path());
        content.setProducts(new ProductListData(searchResult.getResults(), productDataConfig(), userContext, reverseRouter(), categoryTreeInNew()));
        content.setPagination(new PaginationBean(requestContext(request()), searchResult, searchCriteria.getPage(), searchCriteria.getProductsPerPageSelector().getSelectedPageSize(), paginationDisplayedPages));
        content.setSortSelector(new SortSelectorBean(searchCriteria.getSortSelector(), userContext, i18nResolver()));
        content.setDisplaySelector(new ProductsPerPageSelectorBean(searchCriteria.getProductsPerPageSelector(), userContext, i18nResolver()));
        content.setFacets(new FacetSelectorsBean(searchCriteria.getFacetSelectors(), searchResult, userContext, i18nResolver()));
        return content;
    }

    protected Result renderCategoryPage(final Category category, final PagedSearchResult<ProductProjection> searchResult,
                                        final SearchCriteria searchCriteria, final UserContext userContext) {
        final ProductOverviewPageContent content = createPageContent(searchResult, searchCriteria, userContext);
        content.setAdditionalTitle(category.getName().find(userContext.locales()).orElse(""));
        content.setBreadcrumb(new BreadcrumbBean(category, categoryTree(), userContext, reverseRouter()));
        content.setJumbotron(new JumbotronBean(category, userContext, categoryTree()));
        content.setBanner(createBanner(category, userContext));
        content.setSeo(new SeoBean(userContext, category));
        return ok(renderPage(userContext, content));
    }

    protected Result renderSearchPage(final PagedSearchResult<ProductProjection> searchResult,
                                      final SearchCriteria searchCriteria, final UserContext userContext) {
        final String searchTerm = searchCriteria.getSearchBox().getSearchTerm().get().getValue();
        final ProductOverviewPageContent content = createPageContent(searchResult, searchCriteria, userContext);
        content.setAdditionalTitle(searchTerm);
        content.setBreadcrumb(new BreadcrumbBean(searchTerm));
        content.setSearchTerm(searchTerm);
        return ok(renderPage(userContext, content));
    }

    protected Html renderPage(final UserContext userContext, final ProductOverviewPageContent content) {
        final SunrisePageData pageData = pageData(userContext, content, ctx(), session());
        return templateService().renderToHtml("pop", pageData, userContext.locales());
    }

    protected static BannerBean createBanner(final Category category, final UserContext userContext) {
        final BannerBean bannerBean = new BannerBean(userContext, category);
        bannerBean.setImageMobile("/assets/img/banner_mobile-0a9241da249091a023ecfadde951a53b.jpg"); // TODO obtain from category?
        bannerBean.setImageDesktop("/assets/img/banner_desktop-9ffd148c48068ce2666d6533b4a87d11.jpg"); // TODO obtain from category?
        return bannerBean;
    }

}