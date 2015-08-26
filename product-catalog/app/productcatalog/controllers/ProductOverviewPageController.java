package productcatalog.controllers;

import common.cms.CmsPage;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.pages.ProductThumbnailDataFactory;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
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
import static java.util.stream.Collectors.toList;

@Singleton
public class ProductOverviewPageController extends SunriseController {
    public static final ProductProjectionSearchModel SEARCH_MODEL = ProductProjectionSearchModel.of();
    private static final List<SelectFacet<ProductProjection>> FACET_LIST = asList(
            MultiSelectFacetBuilder.of("size", "Size", SEARCH_MODEL.allVariants().attribute().ofEnum("commonSize").label()).build(),
            MultiSelectFacetBuilder.of("color", "Color", SEARCH_MODEL.allVariants().attribute().ofLocalizableEnum("color").label().locale(Locale.ENGLISH)).build(),
            MultiSelectFacetBuilder.of("brand", "Brands", SEARCH_MODEL.allVariants().attribute().ofEnum("designer").label()).type(SelectFacetType.LARGE).build());
    private final int pageSize;
    private final ProductProjectionService productService;

    @Inject
    public ProductOverviewPageController(final Configuration configuration, final ControllerDependency controllerDependency, final ProductProjectionService productService) {
        super(controllerDependency);
        this.productService = productService;
        this.pageSize = configuration.getInt("pop.pageSize");
    }

    public F.Promise<Result> show(final int page) {
        final FacetedSearch<ProductProjection> facetedSearch = FacetedSearch.of(request(), FACET_LIST);
        final F.Promise<PagedSearchResult<ProductProjection>> searchResultPromise = searchProducts(facetedSearch, page);
        final F.Promise<CmsPage> cmsPromise = getCmsPage("pop");
        return searchResultPromise.flatMap(searchResult ->
                cmsPromise.flatMap(cms -> {
                    final ProductOverviewPageContent content = getPopPageData(cms, facetedSearch, searchResult);
                    return renderPage(view -> ok(view.productOverviewPage(content)));
                })
        );
    }

    /* Move to product service */
    private F.Promise<PagedSearchResult<ProductProjection>> searchProducts(final FacetedSearch<ProductProjection> facetedSearch, final int page) {
        final int offset = (page - 1) * pageSize;
        final ProductProjectionSearch searchRequest = facetedSearch.applyRequest(ProductProjectionSearch.ofCurrent())
                .withOffset(offset)
                .withLimit(pageSize);
        final F.Promise<PagedSearchResult<ProductProjection>> searchResultPromise = sphere().execute(searchRequest);
        searchResultPromise.onRedeem(result -> Logger.debug("Fetched {} out of {} products with request {}",
                result.size(),
                result.getTotal(),
                searchRequest.httpRequestIntent().getPath()));
        return searchResultPromise;
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
        final List<SelectFacet<ProductProjection>> selectFacets = facetedSearch.applyResult(searchResult);
        final List<SelectFacetData> facetData = selectFacets.stream()
                .map(SelectFacetData::new)
                .collect(toList());
        return new FilterListData(facetData);
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