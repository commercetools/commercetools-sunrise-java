package generalpages.controllers;

import common.controllers.SunriseController;
import generalpages.views.html.index;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import play.Configuration;
import play.libs.F;
import play.mvc.*;

import views.html.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

import static io.sphere.sdk.products.ProductProjectionType.CURRENT;

/**
 * Controller for main web pages like index, imprint and contact.
 */
@Singleton
public final class ApplicationController extends SunriseController {

    @Inject
    public ApplicationController(final PlayJavaSphereClient client, final CategoryTree categoryTree, final Configuration configuration) {
        super(client, categoryTree, configuration);
    }

    public F.Promise<Result> index() {
        return client().execute(ProductProjectionQuery.of(CURRENT)).map(this::showProducts);
    }

    private Result showProducts(PagedQueryResult<ProductProjection> productPagedQueryResult) {
        final List<ProductProjection> products = productPagedQueryResult.getResults();
        return ok(index.render(data().products(products).build()));
    }
}
