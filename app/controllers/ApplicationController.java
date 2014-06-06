package controllers;

import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaClient;
import play.mvc.*;

import views.html.*;

public final class ApplicationController extends SunriseController {

    public ApplicationController(final PlayJavaClient client, final CategoryTree categoryTree) {
        super(client, categoryTree);
    }

    public Result index() {
        return ok(index.render(data().build()));
    }
}
