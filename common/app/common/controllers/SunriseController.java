package common.controllers;

import common.cms.CmsService;
import common.countries.CountryOperations;
import common.models.UserContext;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.play.controllers.ShopController;
import io.sphere.sdk.play.metrics.MetricAction;
import play.Configuration;
import play.mvc.Controller;
import play.mvc.With;

/**
 * An application specific controller.
 * Since we want to show a standard web shop it contains categories.
 */
@With(MetricAction.class)
public abstract class SunriseController extends ShopController {
    private final CategoryTree categoryTree;
    private final CountryOperations countryOperations;
    private final CmsService cmsService;

    protected SunriseController(final PlayJavaSphereClient client, final CategoryTree categoryTree,
                                final Configuration configuration, final CmsService cmsService) {
        super(client);
        this.categoryTree = categoryTree;
        this.countryOperations = CountryOperations.of(configuration);
        this.cmsService = cmsService;
    }

    protected final CategoryTree categories() {
        return categoryTree;
    }

    protected final CmsService cmsService() {
        return cmsService;
    }

    protected final UserContext userContext() {
        return UserContext.of(Controller.lang(), countryOperations.country());
    }
}
