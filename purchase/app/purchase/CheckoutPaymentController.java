package purchase;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.models.ProductDataConfig;
import common.controllers.SunrisePageData;
import io.sphere.sdk.carts.Cart;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Result;

import javax.inject.Inject;

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
            final Messages messages = messages(userContext);
            final CheckoutPaymentPageContent content = new CheckoutPaymentPageContent(cart, userContext, productDataConfig, reverseRouter(), getCsrfToken());
            final SunrisePageData pageData = pageData(userContext, content);
            return ok(templateService().renderToHtml("checkout-payment", pageData, userContext.locales()));
        });
    }

    public Result process(final String language) {
        return redirect(reverseRouter().showCheckoutPaymentForm(language));
    }
}
