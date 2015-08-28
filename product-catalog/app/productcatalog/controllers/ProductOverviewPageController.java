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

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Singleton
public class ProductOverviewPageController extends SunriseController {
    private static final ProductProjectionSearchModel SEARCH_MODEL = ProductProjectionSearchModel.of();
    private final int pageSize;
    private final ProductProjectionService productService;

    @Inject
    public ProductOverviewPageController(final Configuration configuration, final ControllerDependency controllerDependency, final ProductProjectionService productService) {
        super(controllerDependency);
        this.productService = productService;
        this.pageSize = configuration.getInt("pop.pageSize");
    }

    public F.Promise<Result> showSubcategory(final String language, final String categorySlug1, final String categorySlug2, final int page) {
        final Locale locale = Locale.forLanguageTag(language);
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
        final Locale locale = Locale.forLanguageTag(language);
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
        final FacetedSearch<ProductProjection> facetedSearch = FacetedSearch.of(request(), facetList(locale, childrenCategories));
        final F.Promise<PagedSearchResult<ProductProjection>> searchResultPromise = searchProducts(category, facetedSearch, page);
        final F.Promise<CmsPage> cmsPromise = getCmsPage("pop");
        return searchResultPromise.flatMap(searchResult ->
                        cmsPromise.flatMap(cms -> {
                            final ProductOverviewPageContent content = getPopPageData(cms, facetedSearch, searchResult);
                            return renderPage(view -> ok(view.productOverviewPage(content)));
                        })
        );
    }

    private boolean isRootCategory(final Category category) {
        return categories().getRoots().contains(category);
    }

    private List<SelectFacet<ProductProjection>> facetList(final Locale locale, final List<Category> childrenCategories) {
        final List<Category> categoriesAsFlatList = getCategoriesAsFlatList(categories(), childrenCategories);
        final CategoryTree subCategoryTree = CategoryTree.of(categoriesAsFlatList);
        return asList(
                HierarchicalSelectFacetBuilder.ofCategories("productType", "Product Type", categories(), childrenCategories, singletonList(locale)).build(),
                SelectFacetBuilder.of("size", "Size", SEARCH_MODEL.allVariants().attribute().ofEnum("commonSize").label()).build(),
                SelectFacetBuilder.of("color", "Color", SEARCH_MODEL.allVariants().attribute().ofLocalizableEnum("color").label().locale(locale)).build(),
                FlexibleSelectFacetBuilder.of("brands", "Brands", SEARCH_MODEL.allVariants().attribute().ofEnum("designer").label()).build());
    }

    /* Move to product service */
    private F.Promise<PagedSearchResult<ProductProjection>> searchProducts(final Category category, final FacetedSearch<ProductProjection> facetedSearch, final int page) {
        final int offset = (page - 1) * pageSize;
        final List<String> categoriesId = getCategoriesAsFlatList(categories(), singletonList(category)).stream().map(Category::getId).collect(toList());
        final ProductProjectionSearch searchRequest = facetedSearch.applyRequest(ProductProjectionSearch.ofCurrent())
                .withQueryFilters(model -> model.categories().id().filtered().by(categoriesId))
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

    private ProductOverviewPageContent getPopPageData(final CmsPage cms, final FacetedSearch<ProductProjection> facetedSearch,
                                                      final PagedSearchResult<ProductProjection> searchResult) {
        final String additionalTitle = "";
        final ProductListData productListData = getProductListData(searchResult.getResults());
        final FilterListData filterListData = getFilterListData(facetedSearch, searchResult);
        return new ProductOverviewPageContent(additionalTitle, productListData, filterListData);
    }

    private F.Promise<Result> renderPage(final Function<ProductCatalogView, Result> pageRenderer) {
        return getCommonCmsPage().map(cms -> {
            final ProductCatalogView view = new ProductCatalogView(templateService(), context(), cms);
            return pageRenderer.apply(view);
        });
    }

    /* This will probably be moved to some kind of factory classes */

    private ProductListData getProductListData(final List<ProductProjection> productList) {
        final ProductThumbnailDataFactory thumbnailDataFactory = ProductThumbnailDataFactory.of(translator(), priceFinder(), priceFormatter());
        return new ProductListData(productList.stream().map(thumbnailDataFactory::create).collect(toList()));
    }

    private FilterListData getFilterListData(final FacetedSearch<ProductProjection> facetedSearch,
                                             final PagedSearchResult<ProductProjection> searchResult) {

        final List<FacetData> facets = facetedSearch.applyResult(searchResult).stream()
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