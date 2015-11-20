package productcatalog.controllers;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.models.SelectableData;
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
        this.displayedPages = configuration.getInt("pop.displayedPages", 9);
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
        final ProductOverviewPageContent content = new ProductOverviewPageContent(additionalTitle);
        content.setBreadcrumb(new BreadcrumbData(category, categories(), userContext, reverseRouter()));
        content.setProductListData(getProductListData(searchResult.getResults(), userContext));
        content.setFilterListData(getFilterListData(searchResult, facets, null));
        content.setPaginationData(new PaginationData(requestContext(), searchResult, currentPage, pageSize, displayedPages));
        content.setSortOptions(sortOptions);
        content.setDisplayOptions(getDisplayOptions(pageSize));
        content.setJumbotronData(new JumbotronData(category, userContext, categories()));
        return content;
    }

    private ProductOverviewPageContent getPopPageData(final UserContext userContext, final Messages messages,
                                                      final PagedSearchResult<ProductProjection> searchResult,
                                                      final List<Facet<ProductProjection>> facets,
                                                      final List<SortOption<ProductProjection>> sortOptions,
                                                      final int currentPage, final int pageSize, final String searchTerm) {
        final String additionalTitle = messages.at("pop.searchText", searchTerm);
        final ProductOverviewPageContent content = new ProductOverviewPageContent(additionalTitle);
        content.setBreadcrumb(new BreadcrumbData(searchTerm, userContext, reverseRouter()));
        content.setProductListData(getProductListData(searchResult.getResults(), userContext));
        content.setFilterListData(getFilterListData(searchResult, facets, searchTerm));
        content.setPaginationData(new PaginationData(requestContext(), searchResult, currentPage, pageSize, displayedPages));
        content.setSortOptions(sortOptions);
        content.setDisplayOptions(getDisplayOptions(pageSize));
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

    /* This will probably be moved to some kind of factory classes */

    private ProductListData getProductListData(final List<ProductProjection> productList, final UserContext userContext) {
        final ProductDataFactory productDataFactory = ProductDataFactory.of(userContext, reverseRouter(), categoryService);
        final List<ProductData> productDataList = productList.stream()
                .map(product -> productDataFactory.create(product, product.getMasterVariant()))
                .collect(toList());
        return new ProductListData(productDataList);
    }

    private FilterListData getFilterListData(final PagedSearchResult<ProductProjection> searchResult,
                                             final List<Facet<ProductProjection>> facets, @Nullable final String searchTerm) {
        final FilterListData filterListData = new FilterListData();
        filterListData.setUrl(request().path());
        filterListData.setSearchTerm(searchTerm);
        filterListData.setFacetData(facets.stream()
                .map(facet -> facet.withSearchResult(searchResult))
                .map(FacetData::new)
                .collect(toList()));
        return filterListData;
    }

    private List<SelectableData> getDisplayOptions(final int pageSize) {
        return asList(
                getDisplayOption(9, pageSize),
                getDisplayOption(24, pageSize),
                getDisplayOption(63, pageSize));
    }

    private SelectableData getDisplayOption(final int pageSize, final int currentPageSize) {
        final SelectableData selectableData = new SelectableData(String.valueOf(pageSize), String.valueOf(pageSize));
        if (currentPageSize == pageSize) {
            selectableData.setSelected(true);
        }
        return selectableData;
    }
}