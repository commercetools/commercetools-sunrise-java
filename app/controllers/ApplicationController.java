package controllers;

import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaClient;
import io.sphere.sdk.products.Product;
import io.sphere.sdk.queries.PagedQueryResult;
import play.libs.F;
import play.mvc.*;

import views.html.*;

import java.util.List;

public final class ApplicationController extends SunriseController {

    public ApplicationController(final PlayJavaClient client, final CategoryTree categoryTree) {
        super(client, categoryTree);
    }

    public F.Promise<Result> index() {
        return client().execute(Product.query()).map(this::showProducts);
    }

    private Result showProducts(PagedQueryResult<Product> productPagedQueryResult) {
        final List<Product> products = productPagedQueryResult.getResults();
        return ok(index.render(data().products(products).build()));
    }
}
