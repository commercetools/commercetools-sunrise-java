package productcatalog.controllers;

import common.cms.CmsPage;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.pages.ProductThumbnailDataFactory;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.facets.FacetOption;
import io.sphere.sdk.facets.MultiSelectFacet;
import io.sphere.sdk.facets.MultiSelectFacetBuilder;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.products.search.ProductProjectionSearchModel;
import io.sphere.sdk.search.PagedSearchResult;
import io.sphere.sdk.search.TermFacetExpression;
import io.sphere.sdk.search.TermFacetResult;
import play.Configuration;
import play.Logger;
import play.libs.F;
import play.mvc.Result;
import productcatalog.pages.*;
import productcatalog.services.ProductProjectionService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Singleton
public class ProductOverviewPageController extends SunriseController {
    private static final TermFacetExpression<ProductProjection, String> FACET_EXPR_SIZE = ProductProjectionSearchModel.of().allVariants().attribute().ofString("size").faceted().byAllTerms();
    private static final TermFacetExpression<ProductProjection, String> FACET_EXPR_COLOR = ProductProjectionSearchModel.of().allVariants().attribute().ofLocalizableEnum("color").label().locale(Locale.ENGLISH).faceted().byAllTerms();
    private final int pageSize;
    private final ProductProjectionService productService;

    @Inject
    public ProductOverviewPageController(final Configuration configuration, final ControllerDependency controllerDependency, final ProductProjectionService productService) {
        super(controllerDependency);
        this.productService = productService;
        this.pageSize = configuration.getInt("pop.pageSize");
    }

    public F.Promise<Result> show(int page) {
        return getCmsPage("pop").flatMap(cms ->
                searchProducts(page).flatMap(searchResult -> {
                    final ProductOverviewPageContent content = getPopPageData(cms, searchResult);
                    return renderPage(view -> ok(view.productOverviewPage(content)));
                })
        );
    }

    private F.Promise<PagedSearchResult<ProductProjection>> searchProducts(final int page) {
        final int offset = (page - 1) * pageSize;
        final ProductProjectionSearch searchRequest = ProductProjectionSearch.ofCurrent()
                .withOffset(offset)
                .withLimit(pageSize)
                .plusFacets(FACET_EXPR_SIZE)
                .plusFacets(FACET_EXPR_COLOR);
        final F.Promise<PagedSearchResult<ProductProjection>> searchResultPromise = sphere().execute(searchRequest);
        searchResultPromise.onRedeem(result -> Logger.trace("Found {} out of {} products with page {} and page size {}",
                result.size(),
                result.getTotal(),
                page,
                pageSize));
        return searchResultPromise;
    }

    private ProductOverviewPageContent getPopPageData(final CmsPage cms, final PagedSearchResult<ProductProjection> searchResult) {
        final String additionalTitle = "";
        final ProductListData productListData = getProductListData(searchResult);
        final FilterListData filterListData = getFilterListData(searchResult);
        return new ProductOverviewPageContent(additionalTitle, productListData, filterListData);
    }

    private F.Promise<Result> renderPage(final Function<ProductCatalogView, Result> pageRenderer) {
        return getCommonCmsPage().map(cms -> {
            final ProductCatalogView view = new ProductCatalogView(templateService(), context(), cms);
            return pageRenderer.apply(view);
        });
    }

    /* This will be moved to some kind of factory classes */

    private ProductListData getProductListData(final PagedSearchResult<ProductProjection> searchResult) {
        final ProductThumbnailDataFactory thumbnailDataFactory = ProductThumbnailDataFactory.of(translator(), priceFinder(), priceFormatter());
        return new ProductListData(searchResult.getResults().stream().map(thumbnailDataFactory::create).collect(toList()));
    }

    private FilterListData getFilterListData(final PagedSearchResult<ProductProjection> searchResult) {
        final SelectFacetData sizeSelectFacetData = getSizeFacet(searchResult);
        final SelectFacetData colorSelectFacetData = getColorFacet(searchResult);
        return new FilterListData(sizeSelectFacetData, colorSelectFacetData);
    }

    private SelectFacetData getSizeFacet(final PagedSearchResult<ProductProjection> searchResult) {
        final List<FacetOption> facetOptions = getSelectFacetData(searchResult, FACET_EXPR_SIZE);
        final MultiSelectFacet facet = MultiSelectFacetBuilder.of("facet-size", "Size", facetOptions, false).build();
        return new SelectFacetData(facet);
    }

    private SelectFacetData getColorFacet(final PagedSearchResult<ProductProjection> searchResult) {
        final List<FacetOption> facetOptions = getSelectFacetData(searchResult, FACET_EXPR_COLOR);
        final MultiSelectFacet facet = MultiSelectFacetBuilder.of("facet-color", "Color", facetOptions, false).build();
        return new SelectFacetData(facet);
    }

    private <T> List<FacetOption> getSelectFacetData(final PagedSearchResult<ProductProjection> searchResult, final TermFacetExpression<ProductProjection, T> facetExpr) {
        final TermFacetResult<T> facetResult = searchResult.getTermFacetResult(facetExpr);
        return FacetOption.ofFacetResult(facetResult, Collections.<T>emptyList());
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