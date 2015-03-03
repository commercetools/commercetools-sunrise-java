package controllers;

import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.products.Product;
import io.sphere.sdk.products.queries.ProductQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import play.Configuration;
import play.libs.F;
import play.mvc.*;

import views.html.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public final class ApplicationController extends SunriseController {

    @Inject
    public ApplicationController(final PlayJavaSphereClient client, final CategoryTree categoryTree, final Configuration configuration) {
        super(client, categoryTree, configuration);
    }

    public F.Promise<Result> index() {
        return client().execute(ProductQuery.of()).map(this::showProducts);
    }

    private Result showProducts(PagedQueryResult<Product> productPagedQueryResult) {
        final List<Product> products = productPagedQueryResult.getResults();
        return ok(index.render(data().products(products).build()));
    }
}
