package reverserouter;

import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.common.reverserouter.*;
import io.sphere.sdk.models.Base;
import play.mvc.Call;
import setupwidget.controllers.SetupReverseRouter;

import static demo.myaccount.routes.*;
import static demo.productcatalog.routes.*;
import static demo.shoppingcart.routes.*;
import static setupwidget.controllers.routes.*;

public class ReverseRouterImpl extends Base implements ReverseRouter, HomeReverseRouter, ProductReverseRouter, CheckoutReverseRouter, SetupReverseRouter {

    @Override
    public Call themeAssets(final String file) {
        return controllers.routes.WebJarAssets.at(file);
    }

    @Override
    public Call homePageCall(final String languageTag) {
        return HomeController.show(languageTag);
    }

    @Override
    public Call productOverviewPageCall(final String languageTag, final String categorySlug) {
        return ProductOverviewController.searchProductsByCategorySlug(languageTag, categorySlug);
    }

    @Override
    public Call productDetailPageCall(final String languageTag, final String productSlug, final String sku) {
        return ProductDetailController.showProductBySlugAndSku(languageTag, productSlug, sku);
    }

    @Override
    public Call processSearchProductsForm(final String languageTag) {
        return ProductOverviewController.searchProductsBySearchTerm(languageTag);
    }

    @Override
    public Call checkoutAddressesPageCall(final String languageTag) {
        return CheckoutAddressController.show(languageTag);
    }

    @Override
    public Call checkoutAddressesProcessFormCall(final String languageTag) {
        return CheckoutAddressController.process(languageTag);
    }

    @Override
    public Call checkoutShippingPageCall(final String languageTag) {
        return CheckoutShippingController.show(languageTag);
    }

    @Override
    public Call checkoutShippingProcessFormCall(final String languageTag) {
        return CheckoutShippingController.process(languageTag);
    }

    @Override
    public Call checkoutPaymentPageCall(final String languageTag) {
        return CheckoutPaymentController.show(languageTag);
    }

    @Override
    public Call checkoutPaymentProcessFormCall(final String languageTag) {
        return CheckoutPaymentController.process(languageTag);
    }

    @Override
    public Call checkoutConfirmationPageCall(final String languageTag) {
        return CheckoutConfirmationController.show(languageTag);
    }

    @Override
    public Call checkoutConfirmationProcessFormCall(final String languageTag) {
        return CheckoutConfirmationController.process(languageTag);
    }

    @Override
    public Call checkoutThankYouPageCall(final String languageTag) {
        return CheckoutThankYouController.show(languageTag);
    }

    @Override
    public Call processSetupFormCall() {
        return SetupController.processForm();
    }
}
