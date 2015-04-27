package products.controllers;

import controllers.SunriseController;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;
import play.Configuration;
import play.mvc.Result;

import static play.mvc.Results.ok;

public class ProductController extends SunriseController {

    public ProductController(final PlayJavaSphereClient client, final CategoryTree categoryTree, final Configuration configuration) {
        super(client, categoryTree, configuration);
    }

    public Result index() {
        return ok("Good!");
    }
}
