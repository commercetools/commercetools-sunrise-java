package productcatalog.controllers;

import common.controllers.ControllerDependencies;
import common.controllers.SunriseController;
import io.sphere.sdk.client.PlayJavaSphereClient;
import play.libs.F;
import play.mvc.Result;
import productcatalog.pages.ProductCatalogView;
import productcatalog.pages.ProductOverviewPageContent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.Function;

@Singleton
public class ProductCatalogController extends SunriseController {

    @Inject
    public ProductCatalogController(final PlayJavaSphereClient client, final ControllerDependencies controllerDependencies) {
        super(client, controllerDependencies);
    }

    public F.Promise<Result> pop() {
        return withCms("pop", cms -> {
            final ProductOverviewPageContent content = new ProductOverviewPageContent(cms);
            return render(view -> ok(view.productOverviewPage(content)));
        });
    }

    private F.Promise<Result> render(final Function<ProductCatalogView, Result> pageRenderer) {
        return withCommonCms(cms -> {
            final ProductCatalogView view = new ProductCatalogView(templateService(), projectContext(), cms);
            return pageRenderer.apply(view);
        });
    }
}