package com.commercetools.sunrise.shoppingcart.checkout.confirmation;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.shoppingcart.CartSessionHandler;
import com.commercetools.sunrise.shoppingcart.OrderSessionHandler;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkShoppingCartController;
import com.commercetools.sunrise.shoppingcart.common.WithCartPreconditions;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.OrderFromCartDraft;
import io.sphere.sdk.orders.PaymentState;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.mvc.Call;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.libs.concurrent.HttpExecution.defaultContext;

@RequestScoped
@IntroducingMultiControllerComponents(SunriseCheckoutConfirmationHeroldComponent.class)
public abstract class SunriseCheckoutConfirmationController extends SunriseFrameworkShoppingCartController
        implements WithTemplateName, WithFormFlow<CheckoutConfirmationFormData, Cart, Order>, WithCartPreconditions {

    private static final Logger logger = LoggerFactory.getLogger(SunriseCheckoutConfirmationController.class);

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("checkout", "checkout-confirmation"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "checkout-confirmation";
    }

    @Override
    public Class<? extends CheckoutConfirmationFormData> getFormDataClass() {
        return DefaultCheckoutConfirmationFormData.class;
    }

    @SunriseRoute("checkoutConfirmationPageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> loadCartWithPreconditions().thenComposeAsync(this::showForm, defaultContext()));
    }

    @SunriseRoute("checkoutConfirmationProcessFormCall")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> loadCartWithPreconditions().thenComposeAsync(this::validateForm, defaultContext()));
    }

    @Override
    public CompletionStage<Cart> loadCartWithPreconditions() {
        return requireNonEmptyCart();
    }

    @Override
    public CompletionStage<? extends Order> doAction(final CheckoutConfirmationFormData formData, final Cart cart) {
        return createOrder(cart);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<? extends CheckoutConfirmationFormData> form, final Cart cart, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException, logger);
        return asyncBadRequest(renderPage(form, cart, null));
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final CheckoutConfirmationFormData formData, final Cart cart, final Order order) {
        final Call call = injector().getInstance(CheckoutReverseRouter.class).checkoutThankYouPageCall(userContext().languageTag());
        return completedFuture(redirect(call));
    }

    @Override
    public CompletionStage<Html> renderPage(final Form<? extends CheckoutConfirmationFormData> form, final Cart cart, @Nullable final Order order) {
        final CheckoutConfirmationPageContent pageContent = injector().getInstance(CheckoutConfirmationPageContentFactory.class).create(form, cart);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    @Override
    public void fillFormData(final CheckoutConfirmationFormData formData, final Cart context) {
        // Do nothing
    }

    protected PaymentState orderInitialPaymentState(final Cart cart) {
        return PaymentState.PENDING;
    }

    protected String generateOrderNumber() {
        return RandomStringUtils.randomNumeric(8);
    }

    private CompletionStage<Order> createOrder(final Cart cart) {
        final String orderNumber = generateOrderNumber();
        final OrderFromCartDraft orderDraft = OrderFromCartDraft.of(cart, orderNumber, orderInitialPaymentState(cart));
        return sphere().execute(OrderFromCartCreateCommand.of(orderDraft))
                .thenApplyAsync(order -> {
                    final Http.Session session = session();
                    injector().getInstance(OrderSessionHandler.class).overwriteInSession(session, order);
                    injector().getInstance(CartSessionHandler.class).removeFromSession(session);
                    return order;
                }, defaultContext());
    }
}
