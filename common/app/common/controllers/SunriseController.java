package common.controllers;

import common.countries.CountryOperations;
import common.models.UserContext;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.play.controllers.ShopController;
import play.Configuration;
import play.mvc.Controller;

/**
 * An application specific controller.
 * Since we want to show a standard web shop it contains categories.
 */
public class SunriseController extends ShopController {
    private final CategoryTree categoryTree;
    private final CountryOperations countryOperations;

    protected SunriseController(final PlayJavaSphereClient client, final CategoryTree categoryTree, final Configuration configuration) {
        super(client);
        this.categoryTree = categoryTree;
        this.countryOperations = CountryOperations.of(configuration);
    }

    protected final CategoryTree categories() {
        return categoryTree;
    }

    protected final UserContext userContext() {
        return UserContext.of(Controller.lang(), countryOperations.country());
    }
}
