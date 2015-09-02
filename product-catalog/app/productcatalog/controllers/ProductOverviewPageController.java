package productcatalog.controllers;

import common.cms.CmsPage;
import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.pages.ProductThumbnailData;
import common.pages.ProductThumbnailDataFactory;
import common.pages.SunrisePageData;
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
        final UserContext userContext = userContext("en");
        final F.Promise<CmsPage> cmsPagePromise = cmsService().getPage(userContext.locale(), "pdp");
        final F.Promise<List<ProductProjection>> productListPromise = productService.searchProducts(page, pageSize);

        return productListPromise.flatMap(result ->
                        cmsPagePromise.map(cms -> {
                            final ProductOverviewPageContent content = getPopPageData(userContext, result);
                            final SunrisePageData pageData = pageData(userContext, content);
                            return ok(templateService().renderToHtml("pop", pageData));
                        })
        );
    }

    private ProductOverviewPageContent getPopPageData(final UserContext userContext, final List<ProductProjection> products) {
        final ProductThumbnailDataFactory thumbnailDataFactory = ProductThumbnailDataFactory.of(userContext);

        final String additionalTitle = "";
        final List<ProductThumbnailData> productList = products.stream().map(thumbnailDataFactory::create).collect(toList());

        return new ProductOverviewPageContent(additionalTitle, productList);
    }

}