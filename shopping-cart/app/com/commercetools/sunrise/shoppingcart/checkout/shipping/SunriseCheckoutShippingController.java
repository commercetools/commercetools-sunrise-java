package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.controllers.SunriseFrameworkFormController;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithCartFinder;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.view.CheckoutShippingPageContentFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.models.Reference;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(CheckoutShippingThemeLinksControllerComponent.class)
public abstract class SunriseCheckoutShippingController<F extends CheckoutShippingFormData> extends SunriseFrameworkFormController implements WithFormFlow<F, ShippingMethodsWithCart, Cart>, WithCartFinder {

    private final CartFinder cartFinder;
    private final CheckoutShippingExecutor checkoutShippingExecutor;
    private final CheckoutShippingPageContentFactory checkoutShippingPageContentFactory;
    private final ShippingSettings shippingSettings;

    protected SunriseCheckoutShippingController(final TemplateRenderer templateRenderer, final RequestHookContext hookContext,
                                                final CartFinder cartFinder, final FormFactory formFactory,
                                                final CheckoutShippingExecutor checkoutShippingExecutor,
                                                final CheckoutShippingPageContentFactory checkoutShippingPageContentFactory,
                                                final ShippingSettings shippingSettings) {
        super(templateRenderer, hookContext, formFactory);
        this.cartFinder = cartFinder;
        this.checkoutShippingExecutor = checkoutShippingExecutor;
        this.checkoutShippingPageContentFactory = checkoutShippingPageContentFactory;
        this.shippingSettings = shippingSettings;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = new HashSet<>();
        frameworkTags.addAll(asList("checkout", "checkout-shipping"));
        return frameworkTags;
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
        return doRequest(() -> requireShippingMethodsWithCart(this::showFormPage));
    }

    @SunriseRoute("checkoutShippingProcessFormCall")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> requireShippingMethodsWithCart(this::processForm));
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