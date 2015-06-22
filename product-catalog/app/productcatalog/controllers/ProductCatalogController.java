package productcatalog.controllers;

import common.cms.CmsPage;
import common.cms.CmsService;
import common.controllers.SunriseController;
import common.templates.TemplateService;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;
import play.Configuration;
import play.libs.F;
import play.mvc.Result;
import productcatalog.models.ProductOverviewPageContent;
import productcatalog.templates.ProductCatalogView;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.Function;

@Singleton
public class ProductCatalogController extends SunriseController {

    @Inject
    public ProductCatalogController(final PlayJavaSphereClient client, final CategoryTree categoryTree,
                                    final Configuration configuration, final TemplateService templateService, final CmsService cmsService) {
        super(client, categoryTree, configuration, templateService, cmsService);
    }

    public F.Promise<Result> pop() {
        return withCms("pop", cms -> {
            final ProductOverviewPageContent content = new ProductOverviewPageContent(cms);
            return render(view -> ok(view.productOverviewPage(content)));
        });
    }

    private F.Promise<Result> render(final Function<ProductCatalogView, Result> pageRenderer) {
        final F.Promise<CmsPage> commonPage = cmsService().getPage(userContext().locale(), "common");
        return commonPage.map(cms -> {
            final ProductCatalogView view = new ProductCatalogView(templateService(), cms);
            return pageRenderer.apply(view);
        });
    }
}