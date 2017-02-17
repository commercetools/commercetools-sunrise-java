package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.common.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.view.CheckoutShippingPageContentFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.models.Reference;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class SunriseCheckoutShippingController<F extends CheckoutShippingFormData> extends SunriseTemplateFormController implements WithTemplateFormFlow<F, ShippingMethodsWithCart, Cart>, WithRequiredCart {

    private final CartFinder cartFinder;
    private final CheckoutShippingExecutor checkoutShippingExecutor;
    private final CheckoutShippingPageContentFactory checkoutShippingPageContentFactory;
    private final ShippingSettings shippingSettings;

    protected SunriseCheckoutShippingController(final ComponentRegistry componentRegistry, final TemplateRenderer templateRenderer,
                                                final FormFactory formFactory, final CartFinder cartFinder,
                                                final CheckoutShippingExecutor checkoutShippingExecutor,
                                                final CheckoutShippingPageContentFactory checkoutShippingPageContentFactory,
                                                final ShippingSettings shippingSettings) {
        super(componentRegistry, templateRenderer, formFactory);
        this.cartFinder = cartFinder;
        this.checkoutShippingExecutor = checkoutShippingExecutor;
        this.checkoutShippingPageContentFactory = checkoutShippingPageContentFactory;
        this.shippingSettings = shippingSettings;
    }

    @Inject
    private void registerThemeLinks(final CheckoutShippingThemeLinksControllerComponent themeLinksControllerComponent) {
        register(themeLinksControllerComponent);
    }

    @Override
    public String getTemplateName() {
        return "checkout-shipping";
    }

    @Override
    public CartFinder getCartFinder() {
        return cartFinder;
    }

    @SunriseRoute("checkoutShippingPageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return requireShippingMethodsWithCart(this::showFormPage);
    }

    @SunriseRoute("checkoutShippingProcessFormCall")
    public CompletionStage<Result> process(final String languageTag) {
        return requireShippingMethodsWithCart(this::processForm);
    }

    @Override
    public CompletionStage<Cart> executeAction(final ShippingMethodsWithCart shippingMethodsWithCart, final F formData) {
        return checkoutShippingExecutor.apply(shippingMethodsWithCart, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final ShippingMethodsWithCart shippingMethodsWithCart, final Form<F> form, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return showFormPageWithErrors(shippingMethodsWithCart, form);
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

    protected final CompletionStage<Result> requireShippingMethodsWithCart(final Function<ShippingMethodsWithCart, CompletionStage<Result>> nextAction) {
        return requireNonEmptyCart(cart -> shippingSettings.getShippingMethods(cart)
                .thenApply(shippingMethods -> ShippingMethodsWithCart.of(shippingMethods, cart))
                .thenComposeAsync(nextAction, HttpExecution.defaultContext()));
    }

    protected final Optional<String> findShippingMethodId(final Cart cart) {
        return Optional.ofNullable(cart.getShippingInfo())
                .flatMap(info -> Optional.ofNullable(info.getShippingMethod()).map(Reference::getId));
    }
}