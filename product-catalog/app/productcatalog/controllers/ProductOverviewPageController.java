package productcatalog.controllers;

import common.cms.CmsPage;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.pages.ProductThumbnailData;
import common.pages.ProductThumbnailDataFactory;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import play.Configuration;
import play.Logger;
import play.libs.F;
import play.mvc.Result;
import productcatalog.pages.ProductCatalogView;
import productcatalog.pages.ProductOverviewPageContent;
import productcatalog.services.ProductProjectionService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Singleton
public class ProductOverviewPageController extends SunriseController {
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
                    final ProductOverviewPageContent content = getPopPageData(cms, searchResult.getResults());
                    return renderPage(view -> ok(view.productOverviewPage(content)));
                })
        );
    }

    private F.Promise<PagedSearchResult<ProductProjection>> searchProducts(final int page) {
        final int offset = (page - 1) * pageSize;
        final ProductProjectionSearch searchRequest = ProductProjectionSearch.ofCurrent()
                .withOffset(offset)
                .withLimit(pageSize);
        final F.Promise<PagedSearchResult<ProductProjection>> searchResultPromise = sphere().execute(searchRequest);
        searchResultPromise.onRedeem(result -> Logger.trace("Found {} out of {} products with page {} and page size {}",
                result.size(),
                result.getTotal(),
                page,
                pageSize));
        return searchResultPromise;
    }

    private ProductOverviewPageContent getPopPageData(final CmsPage cms, final List<ProductProjection> products) {
        final ProductThumbnailDataFactory thumbnailDataFactory = ProductThumbnailDataFactory.of(translator(), priceFinder(), priceFormatter());
        final List<ProductThumbnailData> productList = products.stream().map(thumbnailDataFactory::create).collect(toList());
        final String additionalTitle = "";

        return new ProductOverviewPageContent(additionalTitle, productList);
    }

    private F.Promise<Result> renderPage(final Function<ProductCatalogView, Result> pageRenderer) {
        return getCommonCmsPage().map(cms -> {
            final ProductCatalogView view = new ProductCatalogView(templateService(), context(), cms);
            return pageRenderer.apply(view);
        });
    }

    /* Maybe move these convenient methods to a SunriseController? */

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