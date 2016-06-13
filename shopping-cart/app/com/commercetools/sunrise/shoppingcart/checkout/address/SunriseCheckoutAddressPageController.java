package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.pages.SunrisePageData;
import com.commercetools.sunrise.common.errors.ErrorsBean;
import com.commercetools.sunrise.common.hooks.SunrisePageDataHook;
import com.commercetools.sunrise.common.contexts.RequestScoped;
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
import com.commercetools.sunrise.shoppingcart.common.StepWidgetBean;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
import com.commercetools.sunrise.shoppingcart.hooks.CartLoadedHook;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@RequestScoped
public abstract class SunriseCheckoutAddressPageController extends SunriseFrameworkCartController {

    @Inject
    protected CheckoutAddressPageContentFactory checkoutAddressPageContentFactory;
    protected Form<CheckoutShippingAddressFormData> shippingAddressUnboundForm;
    protected Form<CheckoutBillingAddressFormData> billingAddressUnboundForm;
    @Inject
    private CheckoutReverseRouter checkoutReverseRouter;

    @Inject
    public void initializeForms(final FormFactory formFactory) {
        this.shippingAddressUnboundForm = formFactory.form(CheckoutShippingAddressFormData.class);
        this.billingAddressUnboundForm = formFactory.form(CheckoutBillingAddressFormData.class);
    }

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        final CompletionStage<Cart> loadedCart = getOrCreateCart();
        final ExecutionContextExecutor contextExecutor = HttpExecution.defaultContext();
        final CompletionStage<Object> hooksCompletionStage =
                loadedCart.thenComposeAsync(cart -> runAsyncHook(CartLoadedHook.class, hook -> hook.cartLoaded(cart)), contextExecutor);
        return loadedCart.thenCombineAsync(hooksCompletionStage, (cart, loadedCartHooksResult) -> showCheckoutAddressPage(cart), contextExecutor);
    }

    @RequireCSRFCheck
    @SuppressWarnings("unused")
    public CompletionStage<Result> process(final String languageTag) {
        final CompletionStage<Cart> loadedCart = getOrCreateCart();
        final ExecutionContextExecutor executor = HttpExecution.defaultContext();
        final CompletionStage<Object> hooksCompletionStage = loadedCart.thenComposeAsync(cart -> runAsyncHook(CartLoadedHook.class, hook -> hook.cartLoaded(cart)), executor);
        return loadedCart.thenComposeAsync(cart -> hooksCompletionStage.thenComposeAsync(x -> processAddressForm(cart), executor), executor);
    }

    private Result showCheckoutAddressPage(final Cart cart) {
        final CheckoutAddressPageContent pageContent = checkoutAddressPageContentFactory.create(cart);
        return ok(renderCheckoutAddressPage(cart, pageContent));
    }

    private CompletionStage<Result> processAddressForm(final Cart cart) {
        final Form<CheckoutShippingAddressFormData> shippingAddressForm = shippingAddressUnboundForm.bindFromRequest();
        final Form<CheckoutBillingAddressFormData> billingAddressForm = billingAddressUnboundForm.bindFromRequest();
        if (formHasErrors(shippingAddressForm, billingAddressForm)) {
            return handleFormErrors(shippingAddressForm, billingAddressForm, cart);
        } else {
            final Address shippingAddress = shippingAddressForm.get().toAddress();
            final boolean differentBilling = shippingAddressForm.get().isBillingAddressDifferentToBillingAddress();
            final Address billingAddress = differentBilling ? billingAddressForm.get().toAddress() : null;
            final CompletionStage<Result> resultStage = setAddressToCart(cart, shippingAddress, billingAddress)
                    .thenComposeAsync(this::handleSuccessfulSetAddress, HttpExecution.defaultContext());
            return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                    handleSetAddressToCartError(throwable, shippingAddressForm, billingAddressForm, cart));
        }
    }

    protected boolean formHasErrors(final Form<CheckoutShippingAddressFormData> shippingAddressForm,
                                    final Form<CheckoutBillingAddressFormData> billingAddressForm) {
        if (shippingAddressForm.hasErrors()) {
            return true;
        } else {
            final boolean differentBilling = shippingAddressForm.get().isBillingAddressDifferentToBillingAddress();
            return differentBilling && billingAddressForm.hasErrors();
        }
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
        final Call call = checkoutReverseRouter.showCheckoutShippingForm(userContext().locale().toLanguageTag());
        return completedFuture(redirect(call));
    }

    protected CompletionStage<Result> handleFormErrors(final Form<CheckoutShippingAddressFormData> shippingAddressForm,
                                                       final Form<CheckoutBillingAddressFormData> billingAddressForm,
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
                                                                  final Form<CheckoutShippingAddressFormData> shippingAddressForm,
                                                                  final Form<CheckoutBillingAddressFormData> billingAddressForm,
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
        pageContent.setStepWidget(StepWidgetBean.ADDRESS);
        pageContent.setCart(createCartLikeBean(cart, userContext()));
        final SunrisePageData pageData = pageData(pageContent);
        runVoidHook(SunrisePageDataHook.class, sunrisePageDataHook -> sunrisePageDataHook.acceptSunrisePageData(pageData));
        return templateEngine().renderToHtml("checkout-address", pageData, userContext().locales());
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("checkout", "checkout.address"));
    }
}