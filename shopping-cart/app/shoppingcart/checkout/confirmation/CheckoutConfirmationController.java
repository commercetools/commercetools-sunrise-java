package shoppingcart.checkout.confirmation;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.errors.ErrorsBean;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.orders.OrderFromCartDraft;
import io.sphere.sdk.orders.PaymentState;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;
import org.apache.commons.lang3.RandomStringUtils;
import play.data.Form;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.concurrent.HttpExecution;
import play.mvc.Http;
import play.mvc.Result;
import shoppingcart.CartSessionUtils;
import shoppingcart.common.CartController;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static shoppingcart.CartSessionKeys.LAST_ORDER_ID_KEY;

@Singleton
public class CheckoutConfirmationController extends CartController {
    private final ProductDataConfig productDataConfig;

    @Inject
    public CheckoutConfirmationController(final ControllerDependency controllerDependency, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
    }

    public CompletionStage<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Http.Context ctx = ctx();
        return getOrCreateCart(userContext, session())
                .thenApplyAsync(cart -> renderCheckoutConfirmationPage(userContext, ctx, cart), HttpExecution.defaultContext());
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final CompletionStage<Cart> cartStage = getOrCreateCart(userContext, session());
        final Form<CheckoutConfirmationFormData> filledForm = Form.form(CheckoutConfirmationFormData.class).bindFromRequest(request());
        final CheckoutConfirmationFormData data = filledForm.get();
        // TODO Enable back agreed terms
//        if (!data.isAgreeTerms()) {
//            filledForm.reject("terms need to be agreed");
//        }
        if (filledForm.hasErrors()) {
            return cartStage.thenComposeAsync(cart -> renderErrorResponse(cart, filledForm, ctx(), userContext), HttpExecution.defaultContext());
        } else {
            return cartStage.thenComposeAsync(cart -> createOrder(cart, languageTag), HttpExecution.defaultContext());
        }
    }

    private Result renderCheckoutConfirmationPage(final UserContext userContext, final Http.Context ctx, final Cart cart) {
        final CheckoutConfirmationPageContent content = new CheckoutConfirmationPageContent(cart, userContext, productDataConfig, i18nResolver(), reverseRouter());
        final SunrisePageData pageData = pageData(userContext, content, ctx);
        return ok(templateService().renderToHtml("checkout-confirmation", pageData, userContext.locales()));
    }

    private CompletionStage<Result> renderErrorResponse(final Cart cart, final Form<CheckoutConfirmationFormData> filledForm, final Http.Context ctx, final UserContext userContext) {
        final CheckoutConfirmationPageContent content = new CheckoutConfirmationPageContent(cart, userContext, productDataConfig, i18nResolver(), reverseRouter());
        content.getCheckoutForm().setErrors(new ErrorsBean(filledForm));
        final SunrisePageData pageData = pageData(userContext, content, ctx);
        return CompletableFuture.completedFuture(badRequest(templateService().renderToHtml("checkout-confirmation", pageData, userContext.locales())));
    }

    private CompletionStage<Result> createOrder(final Cart cart, final String languageTag) {
        final String orderNumber = RandomStringUtils.randomNumeric(8);
        return sphere().execute(OrderFromCartCreateCommand.of(OrderFromCartDraft.of(cart, orderNumber, PaymentState.BALANCE_DUE)))
                .thenApplyAsync(order -> {
                    session(LAST_ORDER_ID_KEY, order.getId());
                    CartSessionUtils.removeCart(session());
                    return redirect(reverseRouter().showCheckoutThankYou(languageTag));
                }, HttpExecution.defaultContext());
    }

}
