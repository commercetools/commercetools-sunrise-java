package productcatalog.controllers;

import common.cms.CmsPage;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.pages.ProductThumbnailDataFactory;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.facets.*;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.products.search.ProductProjectionSearchModel;
import io.sphere.sdk.search.*;
import play.Configuration;
import play.Logger;
import play.libs.F;
import play.mvc.Result;
import productcatalog.pages.*;
import productcatalog.services.ProductProjectionService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.function.Function;

import static io.sphere.sdk.facets.DefaultFacetType.HIERARCHICAL_SELECT;
import static io.sphere.sdk.facets.DefaultFacetType.SORTED_SELECT;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Singleton
public class ProductOverviewPageController extends SunriseController {
    private static final StringSearchModel<ProductProjection, ?> BRAND_SEARCH_MODEL = ProductProjectionSearchModel.of().allVariants().attribute().ofEnum("designer").label();
    private static final StringSearchModel<ProductProjection, ?> COLOR_SEARCH_MODEL = ProductProjectionSearchModel.of().allVariants().attribute().ofLocalizableEnum("color").key();
    private static final StringSearchModel<ProductProjection, ?> SIZE_SEARCH_MODEL = ProductProjectionSearchModel.of().allVariants().attribute().ofEnum("commonSize").label();
    private static final StringSearchModel<ProductProjection, ?> CATEGORY_SEARCH_MODEL = ProductProjectionSearchModel.of().categories().id();
    private final int pageSize;
    private final ProductProjectionService productService;

    @Inject
    public ProductOverviewPageController(final Configuration configuration, final ControllerDependency controllerDependency,
                                         final ProductProjectionService productService) {
        super(controllerDependency);
        this.productService = productService;
        this.pageSize = configuration.getInt("pop.pageSize");
    }

    public F.Promise<Result> showSubcategory(final String language, final String categorySlug1, final String categorySlug2, final int page) {
        final Locale locale = locale(language);
        final Optional<Category> category1 = categories().findBySlug(locale, categorySlug1);
        final Optional<Category> category2 = categories().findBySlug(locale, categorySlug2);
        if (category1.isPresent() && category2.isPresent()) {
            final List<Category> category1Children = categories().findChildren(category1.get());
            if (isRootCategory(category1.get()) && category1Children.contains(category2.get())) {
                return show(locale, category2.get(), page);
            }
        }
        return F.Promise.pure(notFound("Categories not found: " + categorySlug1 + "/" + categorySlug2));
    }

    public F.Promise<Result> showCategory(final String language, final String categorySlug, final int page) {
        final Locale locale = locale(language);
        final Optional<Category> category = categories().findBySlug(locale, categorySlug);
        if (category.isPresent()) {
            if (isRootCategory(category.get())) {
                return show(locale, category.get(), page);
            }
        }
        return F.Promise.pure(notFound("Category not found: " + categorySlug));
    }

    private F.Promise<Result> show(final Locale locale, final Category category, final int page) {
        final List<Category> childrenCategories = categories().findChildren(category);
        final List<Facet<ProductProjection>> boundFacets = boundFacetList(locale, childrenCategories);
        final F.Promise<PagedSearchResult<ProductProjection>> searchResultPromise = searchProducts(category, boundFacets, page);
        final F.Promise<CmsPage> cmsPromise = getCmsPage("pop");
        return searchResultPromise.flatMap(searchResult ->
                        cmsPromise.flatMap(cms -> {
                            final ProductOverviewPageContent content = getPopPageData(cms, searchResult, boundFacets);
                            return renderPage(view -> ok(view.productOverviewPage(content)));
                        })
        );
    }

    private List<Facet<ProductProjection>> boundFacetList(final Locale locale, final List<Category> childrenCategories) {
        final List<Category> subcategories = getCategoriesAsFlatList(categories(), childrenCategories);
        final FacetOptionMapper categoryHierarchyMapper = HierarchicalCategoryFacetOptionMapper.of(subcategories, singletonList(locale));
        final FacetOptionMapper sortedColorFacetOptionMapper = SortedFacetOptionMapper.of(emptyList());
        final FacetOptionMapper sortedSizeFacetOptionMapper = SortedFacetOptionMapper.of(emptyList());
        final List<Facet<ProductProjection>> facets = asList(
                FlexibleSelectFacetBuilder.of("productType", "Product Type", HIERARCHICAL_SELECT, CATEGORY_SEARCH_MODEL, categoryHierarchyMapper).build(),
                FlexibleSelectFacetBuilder.of("size", "Size", SORTED_SELECT, SIZE_SEARCH_MODEL, sortedSizeFacetOptionMapper).build(),
                FlexibleSelectFacetBuilder.of("color", "Color", SORTED_SELECT, COLOR_SEARCH_MODEL, sortedColorFacetOptionMapper).build(),
                SelectFacetBuilder.of("brands", "Brands", BRAND_SEARCH_MODEL).build());
        return bindFacetsWithRequest(facets);
    }

    private ProductOverviewPageContent getPopPageData(final CmsPage cms, final PagedSearchResult<ProductProjection> searchResult,
                                                      final List<Facet<ProductProjection>> boundFacets) {
        final String additionalTitle = "";
        final ProductListData productListData = getProductListData(searchResult.getResults());
        final FilterListData filterListData = getFilterListData(searchResult, boundFacets);
        return new ProductOverviewPageContent(additionalTitle, productListData, filterListData);
    }

    private F.Promise<Result> renderPage(final Function<ProductCatalogView, Result> pageRenderer) {
        return getCommonCmsPage().map(cms -> {
            final ProductCatalogView view = new ProductCatalogView(templateService(), context(), cms);
            return pageRenderer.apply(view);
        });
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
        final ProductProjectionSearch searchRequest = ProductProjectionSearch.ofCurrent()
                .withQueryFilters(model -> model.categories().id().filtered().by(categoriesId))
                .withOffset(offset)
                .withLimit(pageSize);
        final ProductProjectionSearch facetedSearchRequest = getFacetedSearchRequest(searchRequest, boundFacets);
        final F.Promise<PagedSearchResult<ProductProjection>> searchResultPromise = sphere().execute(facetedSearchRequest);
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

    /* Maybe export it to generic FacetedSearch class */

    private static <T, S extends MetaModelSearchDsl<T, S, M, E>, M, E> S getFacetedSearchRequest(final S baseSearchRequest, final List<Facet<T>> facets) {
        S searchRequest = baseSearchRequest;
        for (final Facet<T> facet : facets) {
            searchRequest = getFacetedSearchRequest(searchRequest, facet);
        }
        return searchRequest;
    }

    private static <T, S extends MetaModelSearchDsl<T, S, M, E>, M, E> S getFacetedSearchRequest(final S baseSearchRequest, final Facet<T> facet) {
        final List<FilterExpression<T>> filterExpressions = facet.getFilterExpressions();
        return baseSearchRequest
                .plusFacets(facet.getFacetExpression())
                .plusFacetFilters(filterExpressions)
                .plusResultFilters(filterExpressions);
    }

    /* This will probably be moved to some kind of factory classes */

    private ProductListData getProductListData(final List<ProductProjection> productList) {
        final ProductThumbnailDataFactory thumbnailDataFactory = ProductThumbnailDataFactory.of(translator(), priceFinder(), priceFormatter());
        return new ProductListData(productList.stream().map(thumbnailDataFactory::create).collect(toList()));
    }

    private <T> FilterListData getFilterListData(final PagedSearchResult<T> searchResult, final List<Facet<T>> boundFacets) {
        final List<FacetData> facets = boundFacets.stream()
                .map(facet -> facet.withSearchResult(searchResult))
                .map(FacetData::new)
                .collect(toList());
        return new FilterListData(facets);
    }

    /* Maybe move these convenient methods to SunriseController? */

    private PriceFinder priceFinder() {
        return userContext().priceFinder();
    }

    private PriceFormatter priceFormatter() {
        return userContext().priceFormatter();
    }

    private Translator translator() {
        return userContext().translator();
    }
}