package shoppingcart.checkout.payment;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import shoppingcart.common.CartController;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletionStage;

@Singleton
public class CheckoutPaymentController extends CartController {

    private final ProductDataConfig productDataConfig;

    @Inject
    public CheckoutPaymentController(final ControllerDependency controllerDependency, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
    }

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        return getOrCreateCart(userContext, session())
                .thenApplyAsync(cart -> renderCheckoutPaymentPage(userContext, cart), HttpExecution.defaultContext());
    }

    @RequireCSRFCheck
    public Result process(final String language) {
        return redirect(reverseRouter().showCheckoutConfirmationForm(language));
    }

    private Result renderCheckoutPaymentPage(final UserContext userContext, final Cart cart) {
        final CheckoutPaymentPageContent content = new CheckoutPaymentPageContent(cart, userContext, productDataConfig, i18nResolver(), reverseRouter());
        final SunrisePageData pageData = pageData(userContext, content, ctx(), session());
        return ok(templateService().renderToHtml("checkout-payment", pageData, userContext.locales()));
    }
}
