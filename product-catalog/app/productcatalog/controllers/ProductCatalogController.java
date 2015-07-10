package productcatalog.controllers;

import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.utils.PriceFormatterImpl;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.SearchDsl;
import play.libs.F;
import play.mvc.Result;
import productcatalog.pages.ProductCatalogView;
import productcatalog.pages.ProductDetailPageContent;
import productcatalog.pages.ProductOverviewPageContent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.Function;

@Singleton
public class ProductCatalogController extends SunriseController {
    private static final int PAGE_SIZE = 9;

    @Inject
    public ProductCatalogController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
    }

    public F.Promise<Result> pop(int page) {
        return withCms("pop", cms ->
            sphere().execute(searchProducts(page)).flatMap(result -> {
                final ProductOverviewPageContent content = new ProductOverviewPageContent(cms, context(), result, PriceFormatterImpl.of());
                return render(view -> ok(view.productOverviewPage(content)));
            })
        );
    }

    public F.Promise<Result> pdp(int page) {
        return withCms("pdp", cms ->
            sphere().execute(searchProducts(page)).flatMap(result -> {
                final ProductDetailPageContent content = new ProductDetailPageContent(cms, context(), result, PriceFormatterImpl.of());
                return render(view -> ok(view.productDetailPage(content)));
            })
        );
    }

    private SearchDsl<ProductProjection> searchProducts(final int page) {
        final int offset = (page - 1) * PAGE_SIZE;
        return ProductProjectionSearch.ofCurrent().withOffset(offset).withLimit(PAGE_SIZE);
    }

    private F.Promise<Result> render(final Function<ProductCatalogView, Result> pageRenderer) {
        return withCommonCms(cms -> {
            final ProductCatalogView view = new ProductCatalogView(templateService(), context(), cms);
            return pageRenderer.apply(view);
        });
    }
}