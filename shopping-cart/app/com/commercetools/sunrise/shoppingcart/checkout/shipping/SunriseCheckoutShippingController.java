package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.viewmodels.CheckoutShippingPageContentFactory;
import com.commercetools.sunrise.framework.controllers.SunriseContentFormController;
import com.commercetools.sunrise.framework.controllers.WithContentFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.checkout.CheckoutReverseRouter;
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

public abstract class SunriseCheckoutShippingController extends SunriseContentFormController
        implements WithContentFormFlow<ShippingMethodsWithCart, Cart, CheckoutShippingFormData>, WithRequiredCart {

    private final CheckoutShippingFormData formData;
    private final CartFinder cartFinder;
    private final CheckoutShippingControllerAction controllerAction;
    private final CheckoutShippingPageContentFactory pageContentFactory;
    private final ShippingSettings shippingSettings;

    protected SunriseCheckoutShippingController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                                final CheckoutShippingFormData formData, final CartFinder cartFinder,
                                                final CheckoutShippingControllerAction controllerAction,
                                                final CheckoutShippingPageContentFactory pageContentFactory,
                                                final ShippingSettings shippingSettings) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.cartFinder = cartFinder;
        this.controllerAction = controllerAction;
        this.pageContentFactory = pageContentFactory;
        this.shippingSettings = shippingSettings;
    }

    @Override
    public final Class<? extends CheckoutShippingFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public final CartFinder getCartFinder() {
        return cartFinder;
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_SHIPPING_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return requireNonEmptyCart(cart ->
                findShippingMethods(cart, shippingMethods ->
                        showFormPage(ShippingMethodsWithCart.of(shippingMethods, cart), formData)));
    }

    @EnableHooks
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
    public CompletionStage<Cart> executeAction(final ShippingMethodsWithCart shippingMethodsWithCart, final CheckoutShippingFormData formData) {
        return controllerAction.apply(shippingMethodsWithCart, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final CheckoutShippingFormData formData);

    @Override
    public PageContent createPageContent(final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        return pageContentFactory.create(shippingMethodsWithCart, form);
    }

    @Override
    public void preFillFormData(final ShippingMethodsWithCart shippingMethodsWithCart, final CheckoutShippingFormData formData) {
        final String shippingMethodId = findShippingMethodId(shippingMethodsWithCart.getCart()).orElse(null);
        formData.applyShippingMethod(shippingMethodId);
    }

    protected final Optional<String> findShippingMethodId(final Cart cart) {
        return Optional.ofNullable(cart.getShippingInfo())
                .flatMap(info -> Optional.ofNullable(info.getShippingMethod()).map(Reference::getId));
    }
}