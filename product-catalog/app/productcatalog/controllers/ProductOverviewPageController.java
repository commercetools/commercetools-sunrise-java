package productcatalog.controllers;

import common.cms.CmsPage;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.pages.ProductThumbnailData;
import common.pages.ProductThumbnailDataFactory;
import common.pages.SunrisePageData;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.products.ProductProjection;
import play.Configuration;
import play.libs.F;
import play.mvc.Result;
import productcatalog.pages.ProductOverviewPageContent;
import productcatalog.services.ProductProjectionService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

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
        return withCms("pop", cms ->
            productService.searchProducts(page, pageSize).flatMap(result -> {
                final ProductOverviewPageContent content = getPopPageData(cms, result);
                return withCommonCms(commonCmsPage -> {
                    final SunrisePageData pageData = SunrisePageData.of(commonCmsPage, context(), content);
                    return ok(templateService().renderToHtml("pop", pageData));
                });
            })
        );
    }

    private ProductOverviewPageContent getPopPageData(final CmsPage cms, final List<ProductProjection> products) {
        final Translator translator = userContext().translator();
        final PriceFormatter priceFormatter = userContext().priceFormatter();
        final PriceFinder priceFinder = userContext().priceFinder();

        final ProductThumbnailDataFactory thumbnailDataFactory = ProductThumbnailDataFactory.of(translator, priceFinder, priceFormatter);

        final String additionalTitle = "";
        final List<ProductThumbnailData> productList = products.stream().map(thumbnailDataFactory::create).collect(toList());

        return new ProductOverviewPageContent(additionalTitle, productList);
    }

}