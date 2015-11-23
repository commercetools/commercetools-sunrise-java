package pages;

import common.controllers.ReverseRouter;
import io.sphere.sdk.models.Base;
import play.Configuration;
import play.mvc.Call;

import static productcatalog.controllers.routes.*;
import static purchase.routes.*;

public class ReverseRouterImpl extends Base implements ReverseRouter {
    private final int pageSizeDefault;

    public ReverseRouterImpl(final Configuration configuration) {
        this.pageSizeDefault = configuration.getInt("pop.pageSize.default");
    }

    @Override
    public Call home(final String languageTag) {
        //return HomeController.show(languageTag);
        return null;
    }

    @Override
    public Call category(final String languageTag, final String categorySlug, final int page) {
        return ProductOverviewPageController.show(languageTag, page, pageSizeDefault, categorySlug);
    }

    @Override
    public Call category(final String languageTag, final String categorySlug) {
        return category(languageTag, categorySlug, 1);
    }

    @Override
    public Call search(final String languageTag, final String searchTerm, final int page) {
        return ProductOverviewPageController.search(languageTag, page, pageSizeDefault, searchTerm);
    }

    @Override
    public Call search(final String languageTag, final String searchTerm) {
        return search(languageTag, searchTerm, 1);
    }

    @Override
    public Call product(final String languageTag, final String productSlug, final String sku) {
        return ProductDetailPageController.show(languageTag, productSlug, sku);
    }

    @Override
    public Call productVariantToCartForm(final String languageTag) {
        return new Call() {
            @Override
            public String url() {
                return "";
            }

            @Override
            public String method() {
                return null;
            }

            @Override
            public String fragment() {
                return null;
            }
        };
    }

    @Override
    public Call showCheckoutShippingForm(final String language) {
        return CheckoutShippingController.show(language);
    }

    @Override
    public Call processCheckoutShippingForm(final String language) {
        return CheckoutShippingController.process(language);
    }

    @Override
    public Call showCheckoutPaymentForm(final String language) {
        return CheckoutPaymentController.show(language);
    }

    @Override
    public Call processCheckoutPaymentForm(final String language) {
        return CheckoutPaymentController.process(language);
    }

    @Override
    public Call showCheckoutConfirmationForm(final String language) {
        return CheckoutPaymentController.show(language);
    }
}
