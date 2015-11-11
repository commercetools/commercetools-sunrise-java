package productcatalog.controllers;

import common.cms.CmsPage;
import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.pages.ProductThumbnailDataFactory;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.facets.*;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionFacetAndFilterSearchModel;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.products.search.ProductProjectionSearchModel;
import io.sphere.sdk.search.*;
import io.sphere.sdk.search.model.TermFacetAndFilterSearchModel;
import play.Configuration;
import play.Logger;
import play.libs.F;
import play.mvc.Result;
import productcatalog.pages.*;
import productcatalog.services.ProductProjectionService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

import static io.sphere.sdk.facets.DefaultFacetType.HIERARCHICAL_SELECT;
import static io.sphere.sdk.facets.DefaultFacetType.SORTED_SELECT;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Singleton
public class ProductOverviewPageController extends SunriseController {
    public static final ProductProjectionFacetAndFilterSearchModel FACETED_SEARCH = ProductProjectionSearchModel.of().facetedSearch();
    private static final TermFacetAndFilterSearchModel<ProductProjection, ?> BRAND_SEARCH_MODEL = FACETED_SEARCH.allVariants().attribute().ofEnum("designer").label();
    private static final TermFacetAndFilterSearchModel<ProductProjection, ?> COLOR_SEARCH_MODEL = FACETED_SEARCH.allVariants().attribute().ofLocalizableEnum("color").key();
    private static final TermFacetAndFilterSearchModel<ProductProjection, ?> SIZE_SEARCH_MODEL = FACETED_SEARCH.allVariants().attribute().ofEnum("commonSize").label();
//    private static final TermFacetAndFilterSearchModel<ProductProjection, ?> CATEGORY_SEARCH_MODEL = TermFacetAndFilterExpression.of(TermFacetExpression.of("variants.categories.id"), singletonList(FilterExpression.of("variants.categories.id")));
    private final int pageSize;
    private final ProductProjectionService productService;

    @Inject
    public ProductOverviewPageController(final Configuration configuration, final ControllerDependency controllerDependency,
                                         final ProductProjectionService productService) {
        super(controllerDependency);
        this.productService = productService;
        this.pageSize = configuration.getInt("pop.pageSize");
    }

    public F.Promise<Result> show(final String language, final String categorySlug, final int page) {
        final UserContext userContext = userContext(language);
        final Optional<Category> category = categories().findBySlug(userContext.locale(), categorySlug);
        if (category.isPresent()) {
            final List<Category> childrenCategories = categories().findChildren(category.get());
            final List<Facet<ProductProjection>> boundFacets = boundFacetList(userContext.locale(), childrenCategories);
            final F.Promise<PagedSearchResult<ProductProjection>> searchResultPromise = searchProducts(category.get(), boundFacets, page);
            final F.Promise<CmsPage> cmsPromise = cmsService().getPage(userContext.locale(), "pop");
            return searchResultPromise.flatMap(searchResult ->
                            cmsPromise.map(cms -> {
                                final ProductOverviewPageContent content = getPopPageData(cms, userContext, searchResult, boundFacets);
                                return ok(templateService().renderToHtml("pop", pageData(userContext, content)));
                            })
            );
        }
        return F.Promise.pure(notFound("Category not found: " + categorySlug));
    }

    private List<Facet<ProductProjection>> boundFacetList(final Locale locale, final List<Category> childrenCategories) {
        final List<Category> subcategories = getCategoriesAsFlatList(categories(), childrenCategories);
        final FacetOptionMapper categoryHierarchyMapper = HierarchicalCategoryFacetOptionMapper.of(subcategories, singletonList(locale));
        final FacetOptionMapper sortedColorFacetOptionMapper = SortedFacetOptionMapper.of(emptyList());
        final FacetOptionMapper sortedSizeFacetOptionMapper = SortedFacetOptionMapper.of(emptyList());
        final List<Facet<ProductProjection>> facets = asList(
//                SelectFacetBuilder.of("productType", "Product Type", HIERARCHICAL_SELECT, CATEGORY_SEARCH_MODEL, categoryHierarchyMapper).build(),
                SelectFacetBuilder.of("size", "Size", SORTED_SELECT, SIZE_SEARCH_MODEL, sortedSizeFacetOptionMapper).build(),
                SelectFacetBuilder.of("color", "Color", SORTED_SELECT, COLOR_SEARCH_MODEL, sortedColorFacetOptionMapper).build(),
                SelectFacetBuilder.of("brands", "Brands", BRAND_SEARCH_MODEL).build());
        return bindFacetsWithRequest(facets);
    }

    private ProductOverviewPageContent getPopPageData(final CmsPage cms, final UserContext userContext,
                                                      final PagedSearchResult<ProductProjection> searchResult,
                                                      final List<Facet<ProductProjection>> boundFacets) {
        final String additionalTitle = "";
        final ProductListData productListData = getProductListData(searchResult.getResults(), userContext);
        final FilterListData filterListData = getFilterListData(searchResult, boundFacets);
        return new ProductOverviewPageContent(additionalTitle, productListData, filterListData);
    }

    /* Maybe move to some common controller class */

    private boolean isRootCategory(final Category category) {
        return categories().getRoots().contains(category);
    }

    private Locale locale(final String language) {
        return Locale.forLanguageTag(language);
    }

    private static <T> List<Facet<T>> bindFacetsWithRequest(final List<Facet<T>> facets) {
        return facets.stream().map(facet -> {
            final List<String> selectedValues = asList(request().queryString().getOrDefault(facet.getKey(), new String[0]));
            return facet.withSelectedValues(selectedValues);
        }).collect(toList());
    }

    /* Move to product service */

    private F.Promise<PagedSearchResult<ProductProjection>> searchProducts(final Category category, final List<Facet<ProductProjection>> boundFacets, final int page) {
        final int offset = (page - 1) * pageSize;
        final List<String> categoriesId = getCategoriesAsFlatList(categories(), singletonList(category)).stream().map(Category::getId).collect(toList());
        final List<FacetAndFilterExpression<ProductProjection>> facetedSearch = boundFacets.stream().map(Facet::getFacetedSearchExpression).collect(toList());
        final ProductProjectionSearch searchRequest = ProductProjectionSearch.ofCurrent()
                .withQueryFilters(filter -> filter.categories().id().byAny(categoriesId))
                .withFacetedSearch(facetedSearch)
                .withOffset(offset)
                .withLimit(pageSize);
        final F.Promise<PagedSearchResult<ProductProjection>> searchResultPromise = sphere().execute(searchRequest);
        searchResultPromise.onRedeem(result -> Logger.debug("Fetched {} out of {} products with request {}",
                result.size(),
                result.getTotal(),
                searchRequest.httpRequestIntent().getPath()));
        return searchResultPromise;
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
        final ProductThumbnailDataFactory thumbnailDataFactory = ProductThumbnailDataFactory.of(userContext);
        return new ProductListData(productList.stream().map(thumbnailDataFactory::create).collect(toList()));
    }

    private <T> FilterListData getFilterListData(final PagedSearchResult<T> searchResult, final List<Facet<T>> boundFacets) {
        final List<FacetData> facets = boundFacets.stream()
                .map(facet -> facet.withSearchResult(searchResult))
                .map(FacetData::new)
                .collect(toList());
        return new FilterListData(facets);
    }
}