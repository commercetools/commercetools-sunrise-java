package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.errors.ErrorsBean;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.hooks.CartLoadedHook;
import com.commercetools.sunrise.shoppingcart.common.StepWidgetBean;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
import com.google.inject.Injector;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetBillingAddress;
import io.sphere.sdk.carts.commands.updateactions.SetCountry;
import io.sphere.sdk.carts.commands.updateactions.SetCustomerEmail;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;
import io.sphere.sdk.client.ErrorResponseException;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.models.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.concurrent.HttpExecution;
import play.mvc.Call;
import play.mvc.Result;
import play.twirl.api.Html;
import scala.concurrent.ExecutionContextExecutor;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@RequestScoped
public abstract class SunriseCheckoutAddressPageController extends SunriseFrameworkCartController implements WithOverwriteableTemplateName {
    protected static final Logger logger = LoggerFactory.getLogger(SunriseCheckoutAddressPageController.class);

    @Inject
    protected CheckoutAddressPageContentFactory checkoutAddressPageContentFactory;
    @Inject
    private CheckoutReverseRouter checkoutReverseRouter;
    @Inject
    private Injector injector;

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> {
            final CompletionStage<Cart> loadedCart = getOrCreateCart();
            final ExecutionContextExecutor contextExecutor = HttpExecution.defaultContext();
            final CompletionStage<Object> hooksCompletionStage =
                    loadedCart.thenComposeAsync(cart -> runAsyncHook(CartLoadedHook.class, hook -> hook.cartLoaded(cart)), contextExecutor);
            return loadedCart.thenCombineAsync(hooksCompletionStage, (cart, loadedCartHooksResult) -> showCheckoutAddressPage(cart), contextExecutor);
        });
    }

    @RequireCSRFCheck
    @SuppressWarnings("unused")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> {
            final CompletionStage<Cart> loadedCart = getOrCreateCart();
            final ExecutionContextExecutor executor = HttpExecution.defaultContext();
            final CompletionStage<Object> hooksCompletionStage = loadedCart.thenComposeAsync(cart -> runAsyncHook(CartLoadedHook.class, hook -> hook.cartLoaded(cart)), executor);
            return loadedCart.thenComposeAsync(cart -> hooksCompletionStage.thenComposeAsync(x -> processAddressForm(cart), executor), executor);
        });
    }

    private Result showCheckoutAddressPage(final Cart cart) {
        final CheckoutAddressPageContent pageContent = checkoutAddressPageContentFactory.create(cart);
        return ok(renderCheckoutAddressPage(cart, pageContent));
    }

    private static class DefaultCheckoutAddressFormDelegate extends CheckoutAddressFormDelegate<CheckoutAddressFormData> {

        public DefaultCheckoutAddressFormDelegate() {
            super(CheckoutAddressFormData.class);
        }
    }

    //make class replaceable with others
    private static abstract class CheckoutAddressFormDelegate<S extends CheckoutAddressFormData> {
        @Inject
        private FormFactory formFactory;
        protected final Class<S> shippingAddressFormClass;

        protected CheckoutAddressFormDelegate(final Class<S> shippingAddressFormClass) {
            this.shippingAddressFormClass = shippingAddressFormClass;
        }

        public CompletionStage<Result> processAddressForm(final Cart cart, final SunriseCheckoutAddressPageController controller) {
            final boolean billingDifferent = askFormBillingAddressDifferentToShippingAddress();
            final Form<S> filledForm = bindForm(billingDifferent);
            if (filledForm.hasErrors()) {
                return controller.handleFormErrors(filledForm, cart);
            } else {
                final S formData = filledForm.get();
                final Address shippingAddress = formData.toShippingAddress();
                final Address billingAddress = billingDifferent ? formData.toBillingAddress() : null;
                final CompletionStage<Result> resultStage = controller.setAddressToCart(cart, shippingAddress, billingAddress)
                        .thenComposeAsync(controller::handleSuccessfulSetAddress, HttpExecution.defaultContext());
                return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                        controller.handleSetAddressToCartError(throwable, filledForm, cart));
            }
        }

        private Form<S> bindForm(final boolean billingDifferent) {
            final Form<S> form = billingDifferent
                    ? formFactory.form(shippingAddressFormClass, BillingAddressDifferentToShippingAddressGroup.class)
                    : formFactory.form(shippingAddressFormClass);
            return form.bindFromRequest();
        }

        private boolean askFormBillingAddressDifferentToShippingAddress() {
            final String flagFieldName = "billingAddressDifferentToBillingAddress";
            final String fieldValue = formFactory.form().bindFromRequest().get(flagFieldName);
            return "true".equals(fieldValue);
        }
    }

    private CompletionStage<Result> processAddressForm(final Cart cart) {
        return injector.getInstance(DefaultCheckoutAddressFormDelegate.class).processAddressForm(cart, this);
    }

    protected CompletionStage<Cart> setAddressToCart(final Cart cart, final Address shippingAddress,
                                                     final @Nullable Address billingAddress) {
        final List<UpdateAction<Cart>> updateActions = new ArrayList<>();
        updateActions.add(SetCountry.of(shippingAddress.getCountry()));
        updateActions.add(SetShippingAddress.of(shippingAddress));
        updateActions.add(SetBillingAddress.of(billingAddress));
        Optional.ofNullable(shippingAddress.getEmail())
                .ifPresent(email -> updateActions.add(SetCustomerEmail.of(email)));
        return sphere().execute(CartUpdateCommand.of(cart, updateActions));
    }

    protected CompletionStage<Result> handleSuccessfulSetAddress(final Cart cart) {
        final Call call = checkoutReverseRouter.checkoutShippingPageCall(userContext().locale().toLanguageTag());
        return completedFuture(redirect(call));
    }

    protected CompletionStage<Result> handleFormErrors(final Form<? extends CheckoutAddressFormData> shippingAddressForm, final Cart cart) {
        ErrorsBean errors = null;
        if (shippingAddressForm.hasErrors()) {
            errors = new ErrorsBean(shippingAddressForm);
        }
        final CheckoutAddressPageContent pageContent = checkoutAddressPageContentFactory.createWithAddressError(shippingAddressForm, errors);
        return completedFuture(badRequest(renderCheckoutAddressPage(cart, pageContent)));
    }

    protected CompletionStage<Result> handleSetAddressToCartError(final Throwable throwable,
                                                                  final Form<? extends CheckoutAddressFormData> shippingAddressForm,
                                                                  final Cart cart) {
        if (throwable.getCause() instanceof ErrorResponseException) {
            return handleErrorResponseException(throwable, shippingAddressForm, cart);
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    private CompletionStage<Result> handleErrorResponseException(final Throwable throwable, final Form<? extends CheckoutAddressFormData> shippingAddressForm, final Cart cart) {
        final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
        logger.error("The request to set address to cart raised an exception", errorResponseException);
        final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
        final CheckoutAddressPageContent pageContent = checkoutAddressPageContentFactory.createWithAddressError(shippingAddressForm, errors);
        return completedFuture(badRequest(renderCheckoutAddressPage(cart, pageContent)));
    }

    protected Html renderCheckoutAddressPage(final Cart cart, final CheckoutAddressPageContent pageContent) {
        fill(cart, pageContent);
        return renderPage(pageContent, getTemplateName());
    }

    protected void fill(final Cart cart, final CheckoutAddressPageContent pageContent) {
        pageContent.setStepWidget(StepWidgetBean.ADDRESS);
        pageContent.setCart(createCartLikeBean(cart, userContext()));
    }

    @Override
    public String getTemplateName() {
        return "checkout-address";
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("checkout", "checkout-address"));
    }
}