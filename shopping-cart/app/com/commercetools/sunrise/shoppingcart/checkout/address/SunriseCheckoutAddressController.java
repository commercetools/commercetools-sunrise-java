package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
import com.commercetools.sunrise.shoppingcart.common.WithCartPreconditions;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetBillingAddress;
import io.sphere.sdk.carts.commands.updateactions.SetCountry;
import io.sphere.sdk.carts.commands.updateactions.SetCustomerEmail;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.models.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Call;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@RequestScoped
@IntroducingMultiControllerComponents(SunriseCheckoutAddressHeroldComponent.class)
public abstract class SunriseCheckoutAddressController extends SunriseFrameworkCartController implements WithTemplateName, WithFormFlow<CheckoutAddressFormData, Cart, Cart>, WithCartPreconditions {

    private static final Logger logger = LoggerFactory.getLogger(SunriseCheckoutAddressController.class);

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("checkout", "checkout-address"));
    }

    @Override
    public String getTemplateName() {
        return "checkout-address";
    }

    @Override
    public Class<? extends CheckoutAddressFormData> getFormDataClass() {
        return DefaultCheckoutAddressFormData.class;
    }

    @SunriseRoute("checkoutAddressesPageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> loadCartWithPreconditions().thenComposeAsync(this::showForm, HttpExecution.defaultContext()));
    }

    @SunriseRoute("checkoutAddressesProcessFormCall")
    @SuppressWarnings("unused")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> loadCartWithPreconditions().thenComposeAsync(this::validateForm, HttpExecution.defaultContext()));
    }

    @Override
    public CompletionStage<Cart> loadCartWithPreconditions() {
        return requiringNonEmptyCart();
    }

    @Override
    public CompletionStage<? extends Cart> doAction(final CheckoutAddressFormData formData, final Cart cart) {
        return setAddressToCart(cart, formData.toShippingAddress(), formData.toBillingAddress());
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<? extends CheckoutAddressFormData> form, final Cart cart, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException, logger);
        return asyncBadRequest(renderPage(form, cart, null));
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final CheckoutAddressFormData formData, final Cart oldCart, final Cart updatedCart) {
        final Call call = injector().getInstance(CheckoutReverseRouter.class).checkoutShippingPageCall(userContext().languageTag());
        return completedFuture(redirect(call));
    }

    @Override
    public CompletionStage<Html> renderPage(final Form<? extends CheckoutAddressFormData> form, final Cart cart, @Nullable final Cart updatedCart) {
        final Cart cartToRender = Optional.ofNullable(updatedCart).orElse(cart);
        final CheckoutAddressPageContent pageContent = injector().getInstance(CheckoutAddressPageContentFactory.class).create(form, cartToRender);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    @Override
    public Form<? extends CheckoutAddressFormData> createForm() {
        return isBillingDifferent()
                ? formFactory().form(getFormDataClass(), BillingAddressDifferentToShippingAddressGroup.class)
                : formFactory().form(getFormDataClass());
    }

    @Override
    public void fillFormData(final CheckoutAddressFormData formData, final Cart cart) {
        formData.setData(cart);
    }

    protected boolean isBillingDifferent() {
        final String flagFieldName = "billingAddressDifferentToBillingAddress";
        final String fieldValue = formFactory().form().bindFromRequest().get(flagFieldName);
        return "true".equals(fieldValue);
    }

    private CompletionStage<Cart> setAddressToCart(final Cart cart, final Address shippingAddress, @Nullable final Address billingAddress) {
        final List<UpdateAction<Cart>> updateActions = new ArrayList<>();
        updateActions.add(SetCountry.of(shippingAddress.getCountry()));
        updateActions.add(SetShippingAddress.of(shippingAddress));
        updateActions.add(SetBillingAddress.of(billingAddress));
        Optional.ofNullable(shippingAddress.getEmail())
                .ifPresent(email -> updateActions.add(SetCustomerEmail.of(email)));
        final CartUpdateCommand cmd = CartUpdateCommand.of(cart, updateActions);
        return executeCartUpdateCommandWithHooks(cmd);
    }
}