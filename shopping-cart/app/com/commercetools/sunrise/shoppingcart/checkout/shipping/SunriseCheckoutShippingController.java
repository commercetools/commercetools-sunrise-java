package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.view.CheckoutShippingPageContent;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.view.CheckoutShippingPageContentFactory;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkShoppingCartController;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.models.Reference;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

@IntroducingMultiControllerComponents(CheckoutShippingThemeLinksControllerComponent.class)
public abstract class SunriseCheckoutShippingController<F extends CheckoutShippingFormData> extends SunriseFrameworkShoppingCartController implements WithTemplateName, WithFormFlow<F, Cart, Cart> {

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
        return doRequest(() -> requireNonEmptyCart(this::showFormPage));
    }

    @SunriseRoute("checkoutShippingProcessFormCall")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> requireNonEmptyCart(this::processForm));
    }

    @Override
    public CompletionStage<Cart> doAction(final F formData, final Cart cart) {
        return checkoutShippingExecutor.apply(cart, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<F> form, final Cart cart, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return asyncBadRequest(renderPage(form, cart, null));
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final F formData, final Cart oldCart, final Cart updatedCart);

    @Override
    public CompletionStage<Html> renderPage(final Form<F> form, final Cart oldCart, @Nullable final Cart updatedCart) {
        final Cart cart = firstNonNull(updatedCart, oldCart);
        return shippingSettings.getShippingMethods(cart)
                .thenComposeAsync(shippingMethods -> {
                    final ShippingMethodsWithCart shippingMethodsWithCart = new ShippingMethodsWithCart(shippingMethods, cart);
                    final CheckoutShippingPageContent pageContent = checkoutShippingPageContentFactory.create(shippingMethodsWithCart, form);
                    return renderPageWithTemplate(pageContent, getTemplateName());
                }, HttpExecution.defaultContext());
    }

    @Override
    public void preFillFormData(final F formData, final Cart cart) {
        final String shippingMethodId = findShippingMethodId(cart).orElse(null);
        formData.setShippingMethodId(shippingMethodId);
    }

    protected final Optional<String> findShippingMethodId(final Cart cart) {
        return Optional.ofNullable(cart.getShippingInfo())
                .flatMap(info -> Optional.ofNullable(info.getShippingMethod()).map(Reference::getId));
    }
}