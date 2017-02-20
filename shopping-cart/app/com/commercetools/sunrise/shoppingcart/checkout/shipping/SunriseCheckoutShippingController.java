package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.framework.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.framework.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CheckoutReverseRouter;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.viewmodels.CheckoutShippingPageContentFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class SunriseCheckoutShippingController<F extends CheckoutShippingFormData> extends SunriseTemplateFormController implements WithTemplateFormFlow<F, ShippingMethodsWithCart, Cart>, WithRequiredCart {

    private final CartFinder cartFinder;
    private final CheckoutShippingControllerAction checkoutShippingControllerAction;
    private final CheckoutShippingPageContentFactory checkoutShippingPageContentFactory;
    private final ShippingSettings shippingSettings;

    protected SunriseCheckoutShippingController(final TemplateRenderer templateRenderer, final FormFactory formFactory,
                                                final CartFinder cartFinder,
                                                final CheckoutShippingControllerAction checkoutShippingControllerAction,
                                                final CheckoutShippingPageContentFactory checkoutShippingPageContentFactory,
                                                final ShippingSettings shippingSettings) {
        super(templateRenderer, formFactory);
        this.cartFinder = cartFinder;
        this.checkoutShippingControllerAction = checkoutShippingControllerAction;
        this.checkoutShippingPageContentFactory = checkoutShippingPageContentFactory;
        this.shippingSettings = shippingSettings;
    }

    @Override
    public CartFinder getCartFinder() {
        return cartFinder;
    }

    @RunRequestStartedHook
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_SHIPPING_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return requireNonEmptyCart(cart ->
                findShippingMethods(cart, shippingMethods ->
                        showFormPage(ShippingMethodsWithCart.of(shippingMethods, cart))));
    }

    @RunRequestStartedHook
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_SHIPPING_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireNonEmptyCart(cart ->
                findShippingMethods(cart, shippingMethods ->
                        processForm(ShippingMethodsWithCart.of(shippingMethods, cart))));
    }

    protected final CompletionStage<Result> findShippingMethods(final Cart cart, final Function<List<ShippingMethod>, CompletionStage<Result>> nextAction) {
        return shippingSettings.getShippingMethods(cart)
                .thenComposeAsync(nextAction, HttpExecution.defaultContext());
    }

    @Override
    public CompletionStage<Cart> executeAction(final ShippingMethodsWithCart shippingMethodsWithCart, final F formData) {
        return checkoutShippingControllerAction.apply(shippingMethodsWithCart, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final F formData);

    @Override
    public PageContent createPageContent(final ShippingMethodsWithCart shippingMethodsWithCart, final Form<F> form) {
        return checkoutShippingPageContentFactory.create(shippingMethodsWithCart, form);
    }

    @Override
    public void preFillFormData(final ShippingMethodsWithCart shippingMethodsWithCart, final F formData) {
        final String shippingMethodId = findShippingMethodId(shippingMethodsWithCart.getCart()).orElse(null);
        formData.setShippingMethodId(shippingMethodId);
    }

    protected final Optional<String> findShippingMethodId(final Cart cart) {
        return Optional.ofNullable(cart.getShippingInfo())
                .flatMap(info -> Optional.ofNullable(info.getShippingMethod()).map(Reference::getId));
    }
}