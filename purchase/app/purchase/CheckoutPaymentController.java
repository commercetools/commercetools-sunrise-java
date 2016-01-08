package purchase;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.models.ProductDataConfig;
import common.controllers.SunrisePageData;
import io.sphere.sdk.carts.Cart;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CheckoutPaymentController extends CartController {

    private final ProductDataConfig productDataConfig;

    @Inject
    public CheckoutPaymentController(final ControllerDependency controllerDependency, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
    }

    public F.Promise<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final F.Promise<Cart> cartPromise = getOrCreateCart(userContext, session());
        return cartPromise.map(cart -> {
            final CheckoutPaymentPageContent content = new CheckoutPaymentPageContent(cart, productDataConfig, userContext, i18nResolver(), reverseRouter());
            final SunrisePageData pageData = pageData(userContext, content, ctx());
            return ok(templateService().renderToHtml("checkout-payment", pageData, userContext.locales()));
        });
    }

    @RequireCSRFCheck
    public Result process(final String language) {
        return redirect(reverseRouter().showCheckoutConfirmationForm(language));
    }
}
