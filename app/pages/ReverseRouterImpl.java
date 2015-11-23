package pages;

import common.pages.ReverseRouter;
import io.sphere.sdk.models.Base;
import play.mvc.Call;
import purchase.routes;

public class ReverseRouterImpl extends Base implements ReverseRouter {
    @Override
    public Call home(final String languageTag) {
        return new Call() {
            @Override
            public String url() {
                return "/";
            }

            @Override
            public String method() {
                return "GET";
            }

            @Override
            public String fragment() {
                return null;
            }
        };
    }

    @Override
    public Call category(final String language, final String slug, final int page) {
        return productcatalog.controllers.routes.ProductOverviewPageController.show(language, slug, page);
    }

    @Override
    public Call processCheckoutShippingForm(final String language) {
        return routes.CheckoutShippingController.process(language);
    }

    @Override
    public Call showCheckoutShippingForm(final String language) {
        return routes.CheckoutShippingController.show(language);
    }
    
    @Override
    public Call processCheckoutPaymentForm(final String language) {
        return routes.CheckoutPaymentController.process(language);
    }

    @Override
    public Call showCheckoutPaymentForm(final String language) {
        return routes.CheckoutPaymentController.show(language);
    }

    @Override
    public Call showCheckoutConfirmationForm(final String language) {
        return routes.CheckoutConfirmationController.show(language);
    }

    @Override
    public Call processCheckoutConfirmationForm(final String language) {
        return routes.CheckoutConfirmationController.process(language);
    }

    @Override
    public Call designAssets(final String file) {
        return controllers.routes.WebJarAssets.at(file);
    }

    @Override
    public Call showCart(final String language) {
        return routes.CartDetailPageController.show(language);
    }
}
