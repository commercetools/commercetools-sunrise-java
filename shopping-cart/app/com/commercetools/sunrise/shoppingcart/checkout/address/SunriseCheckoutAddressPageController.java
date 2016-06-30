package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.SimpleFormBindingControllerTrait;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.forms.ErrorsBean;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.hooks.CartUpdateCommandFilterHook;
import com.commercetools.sunrise.hooks.PrimaryCartUpdatedHook;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetBillingAddress;
import io.sphere.sdk.carts.commands.updateactions.SetCountry;
import io.sphere.sdk.carts.commands.updateactions.SetCustomerEmail;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.models.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Call;
import play.mvc.Result;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.libs.concurrent.HttpExecution.defaultContext;

@RequestScoped
public abstract class SunriseCheckoutAddressPageController extends SunriseFrameworkCartController implements WithOverwriteableTemplateName, SimpleFormBindingControllerTrait<CheckoutAddressFormData> {
    protected static final Logger logger = LoggerFactory.getLogger(SunriseCheckoutAddressPageController.class);

    @Inject
    protected CheckoutAddressPageContentFactory pageContentFactory;


    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> getOrCreateCart().thenCompose(cart -> showCheckoutAddressPage(cart)));
    }

    @Override
    public Class<? extends CheckoutAddressFormData> getFormDataClass() {
        return DefaultCheckoutAddressFormData.class;
    }

    @RequireCSRFCheck
    @SuppressWarnings("unused")
    public CompletionStage<Result> process(final String languageTag) {
        return formProcessingAction(this);
    }

    private CompletionStage<Result> showCheckoutAddressPage(final Cart cart) {
        final CheckoutAddressPageContent pageContent = pageContentFactory.create(cart);
        return asyncOk(renderPage(pageContent, getTemplateName()));
    }

    @Override
    public Form<? extends CheckoutAddressFormData> createForm() {
        return isBillingDifferent()
                ? formFactory().form(getFormDataClass(), BillingAddressDifferentToShippingAddressGroup.class)
                : formFactory().form(getFormDataClass());
    }

    @Override
    public CompletionStage<Result> handleValidForm(final Form<? extends CheckoutAddressFormData> form) {
        return getOrCreateCart().thenComposeAsync(cart -> {
            final CheckoutAddressFormData formData = form.get();
            final Address shippingAddress = formData.toShippingAddress();
            final Address billingAddress = formData.toBillingAddress();
            return sendAddressesToCommercetoolsPlatform(cart, this, form, shippingAddress, billingAddress);
        }, defaultContext());
    }

    protected <F extends CheckoutAddressFormData> CompletionStage<Result> sendAddressesToCommercetoolsPlatform(final Cart cart, final SunriseCheckoutAddressPageController controller, final Form<F> filledForm, final Address shippingAddress, final Address billingAddress) {
        final CompletionStage<Result> resultStage = controller.setAddressToCart(cart, shippingAddress, billingAddress)
                .thenComposeAsync(controller::handleSuccessfulSetAddress, defaultContext());
        return recoverWithAsync(resultStage, defaultContext(), throwable ->
                controller.handleSetAddressToCartError(throwable, filledForm, cart));
    }

    private boolean isBillingDifferent() {
        final String flagFieldName = "billingAddressDifferentToBillingAddress";
        final String fieldValue = formFactory().form().bindFromRequest().get(flagFieldName);
        return "true".equals(fieldValue);
    }

    protected CompletionStage<Cart> setAddressToCart(final Cart cart,
                                                     final Address shippingAddress,
                                                     @Nullable final Address billingAddress) {
        final List<UpdateAction<Cart>> updateActions = new ArrayList<>();
        updateActions.add(SetCountry.of(shippingAddress.getCountry()));
        updateActions.add(SetShippingAddress.of(shippingAddress));
        updateActions.add(SetBillingAddress.of(billingAddress));
        Optional.ofNullable(shippingAddress.getEmail())
                .ifPresent(email -> updateActions.add(SetCustomerEmail.of(email)));
        return executeSphereRequestWithHooks(CartUpdateCommand.of(cart, updateActions),
                CartUpdateCommandFilterHook.class, CartUpdateCommandFilterHook::filterCartUpdateCommand,
                PrimaryCartUpdatedHook.class, PrimaryCartUpdatedHook::onPrimaryCartUpdated);
    }

    protected CompletionStage<Result> handleSuccessfulSetAddress(final Cart cart) {
        final Call call = injector().getInstance(CheckoutReverseRouter.class).checkoutShippingPageCall(userContext().languageTag());
        return completedFuture(redirect(call));
    }

    @Override
    public CompletionStage<Result> handleInvalidForm(final Form<? extends CheckoutAddressFormData> shippingAddressForm) {
        return getOrCreateCart().thenComposeAsync(cart -> {
            ErrorsBean errors = null;
            if (shippingAddressForm.hasErrors()) {
                errors = new ErrorsBean(shippingAddressForm);
            }
            final CheckoutAddressPageContent pageContent = pageContentFactory.createWithAddressError(shippingAddressForm, errors, cart);
            return asyncBadRequest(renderPage(pageContent, getTemplateName()));
        }, defaultContext());
    }

    protected CompletionStage<Result> handleSetAddressToCartError(final Throwable throwable,
                                                                  final Form<? extends CheckoutAddressFormData> shippingAddressForm,
                                                                  final Cart cart) {
        return exceptionallyCompletedFuture(throwable);
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