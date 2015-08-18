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
import play.Configuration;
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

    public F.Promise<Result> pop(int page) {
        return withCms("pop", cms ->
            productService.searchProducts(page, pageSize).flatMap(result -> {
                final ProductOverviewPageContent content = getPopPageData(cms, result);
                return renderPdp(view -> ok(view.productOverviewPage(content)));
            })
        );
    }

    private ProductOverviewPageContent getPopPageData(final CmsPage cms, final List<ProductProjection> products) {
        final Translator translator = context().translator();
        final PriceFormatter priceFormatter = context().user().priceFormatter();
        final PriceFinder priceFinder = context().user().priceFinder();

        final ProductThumbnailDataFactory thumbnailDataFactory = ProductThumbnailDataFactory.of(translator, priceFinder, priceFormatter);

        final String additionalTitle = "";
        final List<ProductThumbnailData> productList = products.stream().map(thumbnailDataFactory::create).collect(toList());

        return new ProductOverviewPageContent(additionalTitle, productList);
    }

    private F.Promise<Result> renderPdp(final Function<ProductCatalogView, Result> pageRenderer) {
        return withCommonCms(cms -> {
            final ProductCatalogView view = new ProductCatalogView(templateService(), context(), cms);
            return pageRenderer.apply(view);
        });
    }
}