package controllers;

import common.controllers.ControllerDependencies;
import common.controllers.SunriseController;
import io.sphere.sdk.client.PlayJavaSphereClient;
import play.libs.F;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Controller for main web pages like index, imprint and contact.
 */
@Singleton
public final class ApplicationController extends SunriseController {

    @Inject
    public ApplicationController(final PlayJavaSphereClient client, final ControllerDependencies controllerDependencies) {
        super(client, controllerDependencies);
    }

    public F.Promise<Result> index() {
        return F.Promise.pure(redirect(productcatalog.controllers.routes.ProductCatalogController.pop()));
    }
}
