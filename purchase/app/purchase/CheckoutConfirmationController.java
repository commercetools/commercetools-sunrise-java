package purchase;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.models.ProductDataConfig;
import common.pages.SunrisePageData;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.orders.OrderFromCartDraft;
import io.sphere.sdk.orders.PaymentState;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;
import org.apache.commons.lang3.RandomStringUtils;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Result;

import javax.inject.Inject;

public class CheckoutConfirmationController extends CartController {
    public static final String LAST_ORDER_ID_KEY = "lastOrderId";
    private final ProductDataConfig productDataConfig;

    @Inject
    public CheckoutConfirmationController(final ControllerDependency controllerDependency, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
    }

    public F.Promise<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final F.Promise<Cart> cartPromise = getOrCreateCart(userContext, session());
        return cartPromise.map(cart -> {
            final Messages messages = messages(userContext);
            final CheckoutConfirmationPageContent content = new CheckoutConfirmationPageContent(cart, messages, configuration(), reverseRouter(), userContext, getCsrfToken(), productDataConfig);
            final SunrisePageData pageData = pageData(userContext, content);
            return ok(templateService().renderToHtml("checkout-confirmation", pageData, userContext.locales()));
        });
    }

    public F.Promise<Result> process(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final F.Promise<Cart> cartPromise = getOrCreateCart(userContext, session());
        final String orderNumber = RandomStringUtils.randomNumeric(8);
        return cartPromise.flatMap(cart -> sphere().execute(OrderFromCartCreateCommand.of(OrderFromCartDraft.of(cart, orderNumber, PaymentState.BALANCE_DUE))))
                .map(order -> {
                    session(LAST_ORDER_ID_KEY, order.getId());
                    CartSessionUtils.removeCart(session());
                    return redirect(reverseRouter().showCheckoutConfirmationForm(languageTag));
                });
    }
}
