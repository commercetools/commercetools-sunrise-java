package shoppingcart.confirmation;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.orders.OrderFromCartDraft;
import io.sphere.sdk.orders.PaymentState;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;
import org.apache.commons.lang3.RandomStringUtils;
import play.data.Form;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import shoppingcart.common.CartController;
import shoppingcart.CartSessionUtils;
import shoppingcart.ErrorsBean;

import javax.inject.Inject;
import javax.inject.Singleton;

import static shoppingcart.CartSessionKeys.LAST_ORDER_ID_KEY;

@Singleton
public class CheckoutConfirmationController extends CartController {
    private final ProductDataConfig productDataConfig;

    @Inject
    public CheckoutConfirmationController(final ControllerDependency controllerDependency, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
    }

    public F.Promise<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final F.Promise<Cart> cartPromise = getOrCreateCart(userContext, session());
        final Http.Context ctx = ctx();
        return cartPromise.map(cart -> {
            final CheckoutConfirmationPageContent content = new CheckoutConfirmationPageContent(cart, userContext, productDataConfig, i18nResolver(), reverseRouter());
            final SunrisePageData pageData = pageData(userContext, content, ctx);
            return ok(templateService().renderToHtml("checkout-confirmation", pageData, userContext.locales()));
        });
    }

    @RequireCSRFCheck
    public F.Promise<Result> process(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final F.Promise<Cart> cartPromise = getOrCreateCart(userContext, session());
        final Form<CheckoutConfirmationFormData> filledForm = Form.form(CheckoutConfirmationFormData.class).bindFromRequest(request());
        final CheckoutConfirmationFormData data = filledForm.get();
        // TODO Enable back agreed terms
//        if (!data.isAgreeTerms()) {
//            filledForm.reject("terms need to be agreed");
//        }
        if (filledForm.hasErrors()) {
            return cartPromise.flatMap(cart -> renderErrorResponse(cart, filledForm, ctx(), userContext));
        } else {
            return cartPromise.flatMap(cart -> createOrder(cart, languageTag));
        }
    }

    private F.Promise<Result> renderErrorResponse(final Cart cart, final Form<CheckoutConfirmationFormData> filledForm, final Http.Context ctx, final UserContext userContext) {
        final CheckoutConfirmationPageContent content = new CheckoutConfirmationPageContent(cart, userContext, productDataConfig, i18nResolver(), reverseRouter());
        content.getCheckoutForm().setErrors(new ErrorsBean(filledForm));
        final SunrisePageData pageData = pageData(userContext, content, ctx);
        return F.Promise.pure(badRequest(templateService().renderToHtml("checkout-confirmation", pageData, userContext.locales())));
    }

    private F.Promise<Result> createOrder(final Cart cart, final String languageTag) {
        final String orderNumber = RandomStringUtils.randomNumeric(8);
        return  sphere().execute(OrderFromCartCreateCommand.of(OrderFromCartDraft.of(cart, orderNumber, PaymentState.BALANCE_DUE)))
                .map(order -> {
                    session(LAST_ORDER_ID_KEY, order.getId());
                    CartSessionUtils.removeCart(session());
                    return redirect(reverseRouter().showCheckoutThankyou(languageTag));
                });
    }

}
