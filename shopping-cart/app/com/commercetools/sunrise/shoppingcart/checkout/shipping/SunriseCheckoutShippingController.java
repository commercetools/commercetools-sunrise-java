package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.common.reverserouter.CheckoutLocalizedReverseRouter;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkShoppingCartController;
import com.commercetools.sunrise.shoppingcart.common.WithCartPreconditions;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetShippingMethod;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Call;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.libs.concurrent.HttpExecution.defaultContext;

@IntroducingMultiControllerComponents(CheckoutShippingThemeLinksControllerComponent.class)
public abstract class SunriseCheckoutShippingController extends SunriseFrameworkShoppingCartController
        implements WithTemplateName, WithFormFlow<CheckoutShippingFormData, Cart, Cart>, WithCartPreconditions {

    private static final Logger LOGGER = LoggerFactory.getLogger(SunriseCheckoutShippingController.class);

    @Inject
    private CheckoutLocalizedReverseRouter checkoutLocalizedReverseRouter;

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("checkout", "checkout-shipping"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "checkout-shipping";
    }

    @Override
    public Class<? extends CheckoutShippingFormData> getFormDataClass() {
        return DefaultCheckoutShippingFormData.class;
    }

    @SunriseRoute("checkoutShippingPageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> loadCartWithPreconditions().thenComposeAsync(this::showForm, defaultContext()));
    }

    @SunriseRoute("checkoutShippingProcessFormCall")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> loadCartWithPreconditions().thenComposeAsync(this::validateForm, defaultContext()));
    }

    @Override
    public CompletionStage<Cart> loadCartWithPreconditions() {
        return requireNonEmptyCart();
    }

    @Override
    public CompletionStage<? extends Cart> doAction(final CheckoutShippingFormData formData, final Cart cart) {
        return setShippingToCart(cart, formData.getShippingMethodId());
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<? extends CheckoutShippingFormData> form, final Cart cart, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException, LOGGER);
        return asyncBadRequest(renderPage(form, cart, null));
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final CheckoutShippingFormData formData, final Cart cart, final Cart updatedCart) {
        return redirectToCheckoutPayment();
    }

    @Override
    public CompletionStage<Html> renderPage(final Form<? extends CheckoutShippingFormData> form, final Cart cart, @Nullable final Cart updatedCart) {
        return getShippingMethods()
                .thenComposeAsync(shippingMethods -> {
                    final CheckoutShippingPageContentFactory pageContentFactory = injector().getInstance(CheckoutShippingPageContentFactory.class);
                    final CheckoutShippingControllerData pageData = new CheckoutShippingControllerData(form, cart, updatedCart, shippingMethods);
                    final CheckoutShippingPageContent pageContent = pageContentFactory.create(pageData);
                    return renderPageWithTemplate(pageContent, getTemplateName());
            }, HttpExecution.defaultContext());
    }

    @Override
    public void fillFormData(final CheckoutShippingFormData formData, final Cart cart) {
        final String shippingMethodId = findShippingMethodId(cart).orElse(null);
        formData.setShippingMethodId(shippingMethodId);
    }

    protected final CompletionStage<Result> redirectToCheckoutPayment() {
        final Call call = checkoutLocalizedReverseRouter.checkoutPaymentPageCall();
        return completedFuture(redirect(call));
    }

    protected final Optional<String> findShippingMethodId(final Cart cart) {
        return Optional.ofNullable(cart.getShippingInfo())
                .flatMap(info -> Optional.ofNullable(info.getShippingMethod()).map(Reference::getId));
    }

    private CompletionStage<Cart> setShippingToCart(final Cart cart, final String shippingMethodId) {
        final Reference<ShippingMethod> shippingMethodRef = ShippingMethod.referenceOfId(shippingMethodId);
        final SetShippingMethod setShippingMethod = SetShippingMethod.of(shippingMethodRef);
        final CartUpdateCommand cmd = CartUpdateCommand.of(cart, setShippingMethod);
        return executeCartUpdateCommandWithHooks(cmd);
    }
}