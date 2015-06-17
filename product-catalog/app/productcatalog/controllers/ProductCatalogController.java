package productcatalog.controllers;

import common.cms.CmsService;
import common.controllers.SunriseController;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;
import play.Configuration;
import play.libs.F;
import play.mvc.Result;
import productcatalog.models.ProductOverviewPageData;
import productcatalog.templates.ProductCatalogViewService;

import javax.inject.Inject;

public class ProductCatalogController extends SunriseController {
    private final ProductCatalogViewService viewService;

    @Inject
    public ProductCatalogController(final PlayJavaSphereClient client, final CategoryTree categoryTree,
                                    final Configuration configuration, final CmsService cmsService) {
        super(client, categoryTree, configuration, cmsService);
        this.viewService = new ProductCatalogViewService();
    }

    public F.Promise<Result> pop() {
        return cmsService().get(userContext().locale(), "pop").map(cms -> {
            final ProductOverviewPageData pageData = new ProductOverviewPageData(cms);
            return ok(viewService.productOverviewPage(pageData));
        });
    }
}