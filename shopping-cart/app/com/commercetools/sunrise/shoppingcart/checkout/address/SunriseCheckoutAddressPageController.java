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
import play.Logger;
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

    private static class DefaultFooHelper extends AbstractFooHelper<CheckoutShippingAddressFormData, CheckoutBillingAddressFormData> {

        public DefaultFooHelper() {
            super(CheckoutShippingAddressFormData.class, CheckoutBillingAddressFormData.class);
        }

        //TODO the value is uses multiple times and could be cached
        @Override
        protected boolean isBillingAddressDifferentToBillingAddress(final Form<CheckoutShippingAddressFormData> shippingAddressForm, final Form<CheckoutBillingAddressFormData> billingAddressForm) {
            return !shippingAddressForm.hasErrors() && shippingAddressForm.get().isBillingAddressDifferentToBillingAddress();
        }
    }

    private static abstract class AbstractFooHelper<S extends WithAddressExport, B extends WithAddressExport> {
        @Inject
        private FormFactory formFactory;
        protected final Class<S> shippingAddressFormClass;
        protected final Class<B> billingAddressFormClass;

        protected AbstractFooHelper(final Class<S> shippingAddressFormClass, final Class<B> billingAddressFormClass) {
            this.billingAddressFormClass = billingAddressFormClass;
            this.shippingAddressFormClass = shippingAddressFormClass;
        }

        protected boolean formHasErrors(final Form<S> shippingAddressForm,
                                        final Form<B> billingAddressForm) {
            if (shippingAddressForm.hasErrors()) {
                return true;
            } else {
                final boolean differentBilling = isBillingAddressDifferentToBillingAddress(shippingAddressForm, billingAddressForm);
                return differentBilling && billingAddressForm.hasErrors();
            }
        }

        protected abstract boolean isBillingAddressDifferentToBillingAddress(final Form<S> shippingAddressForm, final Form<B> billingAddressForm);

        public CompletionStage<Result> processAddressForm(final Cart cart, final SunriseCheckoutAddressPageController controller) {
            final Form<S> shippingAddressForm = formFactory.form(shippingAddressFormClass).bindFromRequest();
            final Form<B> billingAddressForm = formFactory.form(billingAddressFormClass).bindFromRequest();
            if (formHasErrors(shippingAddressForm, billingAddressForm)) {
                return controller.handleFormErrors(shippingAddressForm, billingAddressForm, cart);
            } else {
                final Address shippingAddress = shippingAddressForm.get().toAddress();
                final boolean differentBilling = isBillingAddressDifferentToBillingAddress(shippingAddressForm, billingAddressForm);
                final Address billingAddress = differentBilling ? billingAddressForm.get().toAddress() : null;
                final CompletionStage<Result> resultStage = controller.setAddressToCart(cart, shippingAddress, billingAddress)
                        .thenComposeAsync(controller::handleSuccessfulSetAddress, HttpExecution.defaultContext());
                return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                        controller.handleSetAddressToCartError(throwable, shippingAddressForm, billingAddressForm, cart));
            }
        }
    }

    private CompletionStage<Result> processAddressForm(final Cart cart) {
        return injector.getInstance(DefaultFooHelper.class).processAddressForm(cart, this);
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

    protected CompletionStage<Result> handleFormErrors(final Form<? extends WithAddressExport> shippingAddressForm,
                                                       final Form<? extends WithAddressExport> billingAddressForm,
                                                       final Cart cart) {
        final ErrorsBean errors;
        if (shippingAddressForm.hasErrors()) {
            errors = new ErrorsBean(shippingAddressForm);
        } else {
            errors = new ErrorsBean(billingAddressForm);
        }
        final CheckoutAddressPageContent pageContent = checkoutAddressPageContentFactory.createWithAddressError(shippingAddressForm, billingAddressForm, errors);
        return completedFuture(badRequest(renderCheckoutAddressPage(cart, pageContent)));
    }

    protected CompletionStage<Result> handleSetAddressToCartError(final Throwable throwable,
                                                                  final Form<? extends WithAddressExport> shippingAddressForm,
                                                                  final Form<? extends WithAddressExport> billingAddressForm,
                                                                  final Cart cart) {
        if (throwable.getCause() instanceof ErrorResponseException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            Logger.error("The request to set address to cart raised an exception", errorResponseException);
            final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
            final CheckoutAddressPageContent pageContent = checkoutAddressPageContentFactory.createWithAddressError(shippingAddressForm, billingAddressForm, errors);
            return completedFuture(badRequest(renderCheckoutAddressPage(cart, pageContent)));
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
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