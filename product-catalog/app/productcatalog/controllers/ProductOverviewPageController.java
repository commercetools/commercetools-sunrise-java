package productcatalog.controllers;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.models.ProductDataConfig;
import play.mvc.Http;
import productcatalog.models.*;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.facets.*;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.*;
import play.Configuration;
import play.Logger;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Result;
import productcatalog.services.CategoryService;
import productcatalog.services.ProductProjectionService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Singleton
public class ProductOverviewPageController extends ProductCatalogController {
    private final List<Integer> pageSizeOptions;
    private final int paginationDisplayedPages;

    @Inject
    public ProductOverviewPageController(final ControllerDependency controllerDependency, final ProductProjectionService productService,
                                         final CategoryService categoryService, final ProductDataConfig productDataConfig) {
        super(controllerDependency, categoryService, productService, productDataConfig);
        this.pageSizeOptions = controllerDependency.configuration().getIntList("pop.pageSize.options", asList(9, 24, 99));
        this.paginationDisplayedPages = controllerDependency.configuration().getInt("pop.pagination.displayedPages", 6);
    }

    public F.Promise<Result> show(final String languageTag, final int page, final int pageSize, final String categorySlug) {
        final UserContext userContext = userContext(languageTag);
        final Messages messages = messages(userContext);
        final Optional<Category> category = categories().findBySlug(userContext.locale(), categorySlug);
        final Http.Context ctx = ctx();
        if (category.isPresent()) {
            final SearchOperations searchOps = new SearchOperations(configuration(), request(), messages, userContext.locale());
            final List<Facet<ProductProjection>> facets = searchOps.boundFacets(getCategoriesInFacet(category));
            final List<SortOption<ProductProjection>> sortOptions = searchOps.boundSortOptions();
            return searchProducts(page, pageSize, facets, sortOptions, category.get()).map(searchResult -> {
                final ProductOverviewPageContent content = getPopPageData(userContext, searchResult, facets, sortOptions, page, pageSize, category.get());
                return ok(templateService().renderToHtml("pop", pageData(userContext, content, ctx), userContext.locales()));
            });
        }
        return F.Promise.pure(notFound("Category not found: " + categorySlug));
    }

    public F.Promise<Result> search(final String languageTag, final int page, final int pageSize, final String searchTerm) {
        final UserContext userContext = userContext(languageTag);
        final Messages messages = messages(userContext);
        final SearchOperations searchOps = new SearchOperations(configuration(), request(), messages, userContext.locale());
        final List<Facet<ProductProjection>> facets = searchOps.boundFacets(CategoryTree.of(emptyList()));
        final List<SortOption<ProductProjection>> sortOptions = searchOps.boundSortOptions();
        final F.Promise<PagedSearchResult<ProductProjection>> searchResultPromise = searchProducts(page, pageSize, facets, sortOptions, userContext.locale(), searchTerm);
        final Http.Context ctx = ctx();
        return searchResultPromise.map(searchResult -> {
            final ProductOverviewPageContent content = getPopPageData(userContext, searchResult, facets, sortOptions, page, pageSize, searchTerm);
            return ok(templateService().renderToHtml("pop", pageData(userContext, content, ctx), userContext.locales()));
        });
    }

    private ProductOverviewPageContent getPopPageData(final UserContext userContext,
                                                      final PagedSearchResult<ProductProjection> searchResult,
                                                      final List<Facet<ProductProjection>> facets,
                                                      final List<SortOption<ProductProjection>> sortOptions,
                                                      final int page, final int pageSize, final Category category) {
        final String title = category.getName().find(userContext.locales()).orElse("");
        final ProductOverviewPageContent content = createPageContent(userContext, title, searchResult, sortOptions, facets, page, pageSize);
        content.setBreadcrumb(new BreadcrumbData(category, categories(), userContext, reverseRouter()));
        content.setJumbotron(new JumbotronData(category, userContext, categories()));
        content.setBanner(createBanner(userContext, category));
        content.setSeo(new SeoData(userContext, category));
        return content;
    }

    private ProductOverviewPageContent getPopPageData(final UserContext userContext,
                                                      final PagedSearchResult<ProductProjection> searchResult,
                                                      final List<Facet<ProductProjection>> facets,
                                                      final List<SortOption<ProductProjection>> sortOptions,
                                                      final int page, final int pageSize, final String searchTerm) {
        final String title = searchTerm;
        final ProductOverviewPageContent content = createPageContent(userContext, title, searchResult, sortOptions, facets, page, pageSize);
        content.setBreadcrumb(new BreadcrumbData(searchTerm, userContext, reverseRouter()));
        content.setSearchTerm(searchTerm);
        return content;
    }

    private ProductOverviewPageContent createPageContent(final UserContext userContext, final String title,
                                                         final PagedSearchResult<ProductProjection> searchResult,
                                                         final List<SortOption<ProductProjection>> sortOptions,
                                                         final List<Facet<ProductProjection>> facets,
                                                         final int page, final int pageSize) {
        final ProductOverviewPageContent content = new ProductOverviewPageContent(title);
        content.setFilterProductsUrl(request().path());
        content.setProducts(new ProductListData(searchResult.getResults(), productDataConfig(), userContext, reverseRouter(), categoryTreeInNew()));
        content.setPagination(new PaginationData(requestContext(), searchResult, page, pageSize, paginationDisplayedPages));
        content.setSortSelector(new SortSelector(sortOptions));
        content.setDisplaySelector(new DisplaySelector(pageSizeOptions, pageSize));
        content.setFacets(new FacetListData(searchResult, facets));
        return content;
    }

    /* Move to product service */

    private F.Promise<PagedSearchResult<ProductProjection>> searchProducts(final int page, final int pageSize,
                                                                           final List<Facet<ProductProjection>> facets,
                                                                           final List<SortOption<ProductProjection>> sortOptions,
                                                                           final Category category) {
        final List<String> categoriesId = getCategoriesAsFlatList(categories(), singletonList(category)).stream()
                .map(Category::getId)
                .collect(toList());
        final ProductProjectionSearch request = ProductProjectionSearch.ofCurrent()
                .withQueryFilters(filter -> filter.categories().id().byAny(categoriesId));
        return searchProducts(request, page, pageSize, facets, sortOptions);
    }

    private F.Promise<PagedSearchResult<ProductProjection>> searchProducts(final int page, final int pageSize,
                                                                           final List<Facet<ProductProjection>> facets,
                                                                           final List<SortOption<ProductProjection>> sortOptions,
                                                                           final Locale locale, final String searchTerm) {
        final ProductProjectionSearch request = ProductProjectionSearch.ofCurrent()
                .withText(locale, searchTerm);
        return searchProducts(request, page, pageSize, facets, sortOptions);
    }

    private F.Promise<PagedSearchResult<ProductProjection>> searchProducts(final ProductProjectionSearch baseSearchRequest,
                                                                           final int page, final int pageSize,
                                                                           final List<Facet<ProductProjection>> facets,
                                                                           final List<SortOption<ProductProjection>> sortOptions) {
        final int offset = (page - 1) * pageSize;
        final ProductProjectionSearch searchRequest = baseSearchRequest
                .withFacetedSearch(getFacetedSearchExpressions(facets))
                .withSort(getSortExpressions(sortOptions))
                .withOffset(offset)
                .withLimit(pageSize);
        final F.Promise<PagedSearchResult<ProductProjection>> searchResultPromise = sphere().execute(searchRequest);
        searchResultPromise.onRedeem(result -> Logger.debug("Fetched {} out of {} products with request {}",
                result.size(),
                result.getTotal(),
                searchRequest.httpRequestIntent().getPath()));
        return searchResultPromise;
    }

    private List<FacetAndFilterExpression<ProductProjection>> getFacetedSearchExpressions(final List<Facet<ProductProjection>> facets) {
        return facets.stream()
                .map(Facet::getFacetedSearchExpression)
                .collect(toList());
    }

    private List<SortExpression<ProductProjection>> getSortExpressions(final List<SortOption<ProductProjection>> sortOptions) {
        return sortOptions.stream()
                .filter(SortOption::isSelected)
                .map(SortOption::getSortExpressions)
                .findFirst()
                .orElse(emptyList());
    }

    private static List<Category> getCategoriesAsFlatList(final CategoryTree categoryTree, final List<Category> parentCategories) {
        final List<Category> categories = new ArrayList<>();
        parentCategories.stream().forEach(parent -> {
            categories.add(parent);
            final List<Category> children = categoryTree.findChildren(parent);
            categories.addAll(getCategoriesAsFlatList(categoryTree, children));
        });
        return categories;
    }

    private static BannerData createBanner(final UserContext userContext, final Category category) {
        final BannerData bannerData = new BannerData(userContext, category);
        bannerData.setImageMobile("/assets/img/banner_mobile.jpg"); // TODO obtain from category?
        bannerData.setImageDesktop("/assets/img/banner_desktop.jpg"); // TODO obtain from category?
        return bannerData;
    }

    private CategoryTree getCategoriesInFacet(final Optional<Category> category) {
        final Category rootAncestor = categoryService().getRootAncestor(category.get());
        return categoryService().getSubtree(singletonList(rootAncestor));
    }
}