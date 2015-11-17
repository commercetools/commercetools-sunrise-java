package purchase;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.models.ProductDataConfig;
import common.pages.SunrisePageData;
import io.sphere.sdk.carts.Cart;
import play.filters.csrf.CSRF;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Optional;

public class CheckoutShippingController extends CartController {


    private final ShippingMethods shippingMethods;
    private final CSRF.TokenProvider csrfTokenProvider;
    private final ProductDataConfig productDataConfig;

    @Inject
    public CheckoutShippingController(final ControllerDependency controllerDependency, final ShippingMethods shippingMethods, final CSRF.TokenProvider csrfTokenProvider, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.shippingMethods = shippingMethods;
        this.csrfTokenProvider = csrfTokenProvider;
        this.productDataConfig = productDataConfig;
    }

    public F.Promise<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final F.Promise<Cart> cartPromise = getOrCreateCart(userContext, session());
        return cartPromise.map(cart -> {
            final Messages messages = messages(userContext);
            final CheckoutShippingContent content = new CheckoutShippingContent(cart, messages, configuration(), reverseRouter(), userContext, flash(), csrfTokenProvider.generateToken(), shippingMethods, productDataConfig);
            final SunrisePageData pageData = pageData(userContext, content);
            return ok(templateService().renderToHtml("checkout-shipping", pageData, userContext.locales()));
        });
    }

    @RequireCSRFCheck
    public F.Promise<Result> process(final String languageTag) {
        final Optional<CSRF.Token> token = CSRF.getToken(request());
        System.err.println(token);
        return F.Promise.pure(TODO);
    }

}