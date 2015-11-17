package pages;

import common.pages.ReverseRouter;
import io.sphere.sdk.models.Base;
import play.mvc.Call;
import purchase.routes;

public class ReverseRouterImpl extends Base implements ReverseRouter {

    @Override
    public Call category(final String language, final String slug, final int page) {
        return productcatalog.controllers.routes.ProductOverviewPageController.show(language, slug, page);
    }

    @Override
    public Call processCheckoutShippingForm(final String language) {
        return routes.CheckoutShippingController.process(language);
    }
}
