package productcatalog.controllers;

import common.cms.CmsService;
import common.controllers.SunriseController;
import common.templates.ViewService;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;
import play.Configuration;
import play.libs.F;
import play.mvc.Result;
import productcatalog.models.ProductOverviewPageContent;
import productcatalog.templates.ProductCatalogView;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProductCatalogController extends SunriseController {

    @Inject
    public ProductCatalogController(final PlayJavaSphereClient client, final CategoryTree categoryTree,
                                    final Configuration configuration, final ViewService viewService, final CmsService cmsService) {
        super(client, categoryTree, configuration, viewService, cmsService);
    }

    public F.Promise<Result> pop() {
        return cmsService().get(userContext().locale(), "pop").flatMap(cms -> {
            final ProductOverviewPageContent pageData = new ProductOverviewPageContent(cms);
            return view().map(view -> ok(view.productOverviewPage(pageData)));
        });
    }

    private F.Promise<ProductCatalogView> view() {
        return cmsService().get(userContext().locale(), "common")
                .map(cms -> new ProductCatalogView(viewService(), cms));
    }
}