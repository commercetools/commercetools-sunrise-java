package productcatalog.controllers;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.pages.SelectableData;
import common.pages.SelectableLinkData;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.facets.*;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.*;
import play.Configuration;
import play.Logger;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Result;
import productcatalog.models.SortOption;
import productcatalog.pages.*;
import productcatalog.services.CategoryService;
import productcatalog.services.ProductProjectionService;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Singleton
public class ProductOverviewPageController extends SunriseController {
    private final int displayedPages;
    private final ProductProjectionService productService;
    private final CategoryService categoryService;

    @Inject
    public ProductOverviewPageController(final Configuration configuration, final ControllerDependency controllerDependency,
                                         final ProductProjectionService productService, final CategoryService categoryService) {
        super(controllerDependency);
        this.productService = productService;
        this.categoryService = categoryService;
        this.displayedPages = configuration.getInt("pop.displayedPages");
    }

    public F.Promise<Result> show(final String languageTag, final int page, final int pageSize, final String categorySlug) {
        final UserContext userContext = userContext(languageTag);
        final Messages messages = messages(userContext);
        final Optional<Category> category = categories().findBySlug(userContext.locale(), categorySlug);
        if (category.isPresent()) {
            final SearchOperations searchOps = new SearchOperations(configuration(), request(), messages, userContext.locale());
            final List<Category> categories = category
                    .map(selected -> categories().findChildren(selected))
                    .filter(list -> !list.isEmpty())
                    .orElse(emptyList());
            final List<Facet<ProductProjection>> facets = searchOps.boundFacets(getCategoriesAsFlatList(categories(), categories));
            final List<SortOption<ProductProjection>> sortOptions = searchOps.boundSortOptions();
            return searchProducts(page, pageSize, facets, sortOptions, category.get()).map(searchResult -> {
                final ProductOverviewPageContent content = getPopPageData(userContext, searchResult, facets, sortOptions, page, pageSize, category.get());
                return ok(templateService().renderToHtml("pop", pageData(userContext, content), userContext.locales()));
            });
        }
        return F.Promise.pure(notFound("Category not found: " + categorySlug));
    }

    public F.Promise<Result> search(final String languageTag, final int page, final int pageSize, final String searchTerm) {
        final UserContext userContext = userContext(languageTag);
        final Messages messages = messages(userContext);
        final SearchOperations searchOps = new SearchOperations(configuration(), request(), messages, userContext.locale());
        final List<Facet<ProductProjection>> facets = searchOps.boundFacets(getCategoriesAsFlatList(categories(), emptyList()));
        final List<SortOption<ProductProjection>> sortOptions = searchOps.boundSortOptions();
        final F.Promise<PagedSearchResult<ProductProjection>> searchResultPromise = searchProducts(page, pageSize, facets, sortOptions, userContext.locale(), searchTerm);
        return searchResultPromise.map(searchResult -> {
            final ProductOverviewPageContent content = getPopPageData(userContext, messages, searchResult, facets, sortOptions, page, pageSize, searchTerm);
            return ok(templateService().renderToHtml("pop", pageData(userContext, content), userContext.locales()));
        });
    }

    private ProductOverviewPageContent getPopPageData(final UserContext userContext,
                                                      final PagedSearchResult<ProductProjection> searchResult,
                                                      final List<Facet<ProductProjection>> facets,
                                                      final List<SortOption<ProductProjection>> sortOptions,
                                                      final int currentPage, final int pageSize, final Category category) {
        final String additionalTitle = category.getName().find(userContext.locales()).orElse("");
        final List<SelectableLinkData> breadcrumbData = getBreadcrumbData(userContext, category.toReference());
        final ProductListData productListData = getProductListData(searchResult.getResults(), userContext);
        final FilterListData filterListData = getFilterListData(searchResult, facets, null);
        final PaginationData paginationData = getPaginationData(searchResult, currentPage, pageSize);
        final List<SelectableData> displayOptions = getDisplayOptions(pageSize);
        final JumbotronData jumbotronData = getJumbotronData(userContext, category);
        return new ProductOverviewPageContent(additionalTitle, breadcrumbData, productListData, filterListData, sortOptions, paginationData, displayOptions, jumbotronData);
    }

    private ProductOverviewPageContent getPopPageData(final UserContext userContext, final Messages messages,
                                                      final PagedSearchResult<ProductProjection> searchResult,
                                                      final List<Facet<ProductProjection>> facets,
                                                      final List<SortOption<ProductProjection>> sortOptions,
                                                      final int currentPage, final int pageSize, final String searchTerm) {
        final String additionalTitle = messages.at("pop.searchText", searchTerm);
        final List<SelectableLinkData> breadcrumbData = getSearchBreadCrumbData(messages(userContext), userContext.locale().getLanguage(), searchTerm);
        final ProductListData productListData = getProductListData(searchResult.getResults(), userContext);
        final FilterListData filterListData = getFilterListData(searchResult, facets, searchTerm);
        final PaginationData paginationData = getPaginationData(searchResult, currentPage, pageSize);
        final List<SelectableData> displayOptions = getDisplayOptions(pageSize);
        return new ProductOverviewPageContent(additionalTitle, breadcrumbData, productListData, filterListData, sortOptions, paginationData, displayOptions, null);
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

    /* This will probably be moved to some kind of factory classes */

    private List<SelectableLinkData> getBreadcrumbData(final UserContext userContext, final Reference<Category> category) {
        final BreadcrumbDataFactory breadcrumbDataFactory = BreadcrumbDataFactory.of(reverseRouter(), userContext.locale());
        final List<Category> breadcrumbCategories = categoryService.getBreadCrumbCategories(category);
        return breadcrumbDataFactory.create(breadcrumbCategories);
    }

    private List<SelectableLinkData> getSearchBreadCrumbData(final Messages messages, final String languageTag, final String searchTerm) {
        return asList(
                new SelectableLinkData(messages.at("home.pageName"), reverseRouter().home(languageTag).url(), false),
                new SelectableLinkData(messages.at("search.resultsForText", searchTerm), reverseRouter().search(languageTag, searchTerm, 1).url(), true)
        );
    }

    private ProductListData getProductListData(final List<ProductProjection> productList, final UserContext userContext) {
        final ProductDataFactory productDataFactory = ProductDataFactory.of(userContext, reverseRouter(), categoryService);
        final List<ProductData> productDataList = productList.stream()
                .map(product -> productDataFactory.create(product, product.getMasterVariant()))
                .collect(toList());
        return new ProductListData(productDataList);
    }

    private FilterListData getFilterListData(final PagedSearchResult<ProductProjection> searchResult,
                                             final List<Facet<ProductProjection>> facets, @Nullable final String searchTerm) {
        final List<FacetData> facetData = facets.stream()
                .map(facet -> facet.withSearchResult(searchResult))
                .map(FacetData::new)
                .collect(toList());
        return new FilterListData(request().path(), facetData, searchTerm);
    }

    private PaginationData getPaginationData(final PagedSearchResult<ProductProjection> searchResult, int currentPage, int pageSize) {
        return new PaginationDataFactory(request(), searchResult, currentPage, pageSize, displayedPages).create();
    }

    private JumbotronData getJumbotronData(final UserContext userContext, final Category category) {
        final String parentName = Optional.ofNullable(category.getParent())
                .flatMap(parentRef -> categories().findById(parentRef.getId())
                        .flatMap(parent -> parent.getName().find(userContext.locales())))
                .orElse("");
        final String name = category.getName().find(userContext.locales()).orElse("");
        final String description = Optional.ofNullable(category.getDescription())
                .flatMap(d -> d.find(userContext.locales()))
                .orElse("");
        return new JumbotronData(parentName, name, description);
    }

    private List<SelectableData> getDisplayOptions(final int pageSize) {
        return asList(
                getDisplayOption(9, pageSize),
                getDisplayOption(24, pageSize),
                getDisplayOption(63, pageSize));
    }

    private SelectableData getDisplayOption(final int pageSize, final int currentPageSize) {
        return new SelectableData(String.valueOf(pageSize), String.valueOf(pageSize), null, null, currentPageSize == pageSize);
    }
}