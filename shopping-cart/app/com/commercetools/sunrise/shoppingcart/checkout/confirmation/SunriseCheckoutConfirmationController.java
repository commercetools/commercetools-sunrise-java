package com.commercetools.sunrise.shoppingcart.checkout.confirmation;

import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.view.CheckoutConfirmationPageContent;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.view.CheckoutConfirmationPageContentFactory;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkShoppingCartController;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.orders.Order;
import play.data.Form;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(CheckoutConfirmationThemeLinksControllerComponent.class)
public abstract class SunriseCheckoutConfirmationController<F extends CheckoutConfirmationFormData> extends SunriseFrameworkShoppingCartController implements WithTemplateName, WithFormFlow<F, Cart, Order> {

    private final CheckoutConfirmationExecutor checkoutConfirmationExecutor;
    private final CheckoutConfirmationPageContentFactory checkoutConfirmationPageContentFactory;

    protected SunriseCheckoutConfirmationController(final CartFinder cartFinder, final CheckoutConfirmationExecutor checkoutConfirmationExecutor,
                                                    final CheckoutConfirmationPageContentFactory checkoutConfirmationPageContentFactory) {
        super(cartFinder);
        this.checkoutConfirmationExecutor = checkoutConfirmationExecutor;
        this.checkoutConfirmationPageContentFactory = checkoutConfirmationPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("checkout", "checkout-confirmation"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "checkout-confirmation";
    }

    @SunriseRoute("checkoutConfirmationPageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> requireNonEmptyCart(this::showFormPage));
    }

    @SunriseRoute("checkoutConfirmationProcessFormCall")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> requireNonEmptyCart(this::processForm));
    }

    @Override
    public CompletionStage<Order> doAction(final F formData, final Cart cart) {
        return checkoutConfirmationExecutor.apply(cart, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<F> form, final Cart cart, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return asyncBadRequest(renderPage(form, cart, null));
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final F formData, final Cart cart, final Order order);

    @Override
    public CompletionStage<Html> renderPage(final Form<F> form, final Cart cart, @Nullable final Order order) {
        final CheckoutConfirmationPageContent pageContent = checkoutConfirmationPageContentFactory.create(cart, form);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    @Override
    public void preFillFormData(final F formData, final Cart cart) {
        // Do not pre-fill anything
    }
}
