package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.SunriseFrameworkShoppingCartController;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.view.CheckoutShippingPageContent;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.view.CheckoutShippingPageContentFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.models.Reference;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Content;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(CheckoutShippingThemeLinksControllerComponent.class)
public abstract class SunriseCheckoutShippingController<F extends CheckoutShippingFormData> extends SunriseFrameworkShoppingCartController implements WithTemplateName, WithFormFlow<F, ShippingMethodsWithCart, Cart> {

    private final CheckoutShippingExecutor checkoutShippingExecutor;
    private final CheckoutShippingPageContentFactory checkoutShippingPageContentFactory;
    private final ShippingSettings shippingSettings;

    protected SunriseCheckoutShippingController(final CartFinder cartFinder, final CheckoutShippingExecutor checkoutShippingExecutor,
                                                final CheckoutShippingPageContentFactory checkoutShippingPageContentFactory,
                                                final ShippingSettings shippingSettings) {
        super(cartFinder);
        this.checkoutShippingExecutor = checkoutShippingExecutor;
        this.checkoutShippingPageContentFactory = checkoutShippingPageContentFactory;
        this.shippingSettings = shippingSettings;
    }

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

    @SunriseRoute("checkoutShippingPageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> requireShippingMethodsWithCart(this::showFormPage));
    }

    @SunriseRoute("checkoutShippingProcessFormCall")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> requireShippingMethodsWithCart(this::processForm));
    }

    @Override
    public CompletionStage<Cart> doAction(final F formData, final ShippingMethodsWithCart shippingMethodsWithCart) {
        return checkoutShippingExecutor.apply(shippingMethodsWithCart, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<F> form, final ShippingMethodsWithCart shippingMethodsWithCart, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return asyncBadRequest(renderPage(form, shippingMethodsWithCart));
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final F formData, final ShippingMethodsWithCart shippingMethodsWithCart, final Cart updatedCart);

    @Override
    public CompletionStage<Content> renderPage(final Form<F> form, final ShippingMethodsWithCart shippingMethodsWithCart) {
        final CheckoutShippingPageContent pageContent = checkoutShippingPageContentFactory.create(shippingMethodsWithCart, form);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    @Override
    public void preFillFormData(final F formData, final ShippingMethodsWithCart shippingMethodsWithCart) {
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