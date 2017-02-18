package com.commercetools.sunrise.shoppingcart.checkout.confirmation;

import com.commercetools.sunrise.common.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.common.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.view.CheckoutConfirmationPageContentFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.orders.Order;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseCheckoutConfirmationController<F extends CheckoutConfirmationFormData> extends SunriseTemplateFormController implements WithTemplateFormFlow<F, Cart, Order>, WithRequiredCart {

    private final CartFinder cartFinder;
    private final CheckoutConfirmationExecutor checkoutConfirmationExecutor;
    private final CheckoutConfirmationPageContentFactory checkoutConfirmationPageContentFactory;

    protected SunriseCheckoutConfirmationController(final TemplateRenderer templateRenderer, final FormFactory formFactory,
                                                    final CartFinder cartFinder,
                                                    final CheckoutConfirmationExecutor checkoutConfirmationExecutor,
                                                    final CheckoutConfirmationPageContentFactory checkoutConfirmationPageContentFactory) {
        super(templateRenderer, formFactory);
        this.cartFinder = cartFinder;
        this.checkoutConfirmationExecutor = checkoutConfirmationExecutor;
        this.checkoutConfirmationPageContentFactory = checkoutConfirmationPageContentFactory;
    }

    @Override
    public CartFinder getCartFinder() {
        return cartFinder;
    }

    @RunRequestStartedHook
    @SunriseRoute("checkoutConfirmationPageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return requireNonEmptyCart(this::showFormPage);
    }

    @RunRequestStartedHook
    @SunriseRoute("checkoutConfirmationProcessFormCall")
    public CompletionStage<Result> process(final String languageTag) {
        return requireNonEmptyCart(this::processForm);
    }

    @Override
    public CompletionStage<Order> executeAction(final Cart cart, final F formData) {
        return checkoutConfirmationExecutor.apply(cart, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Order order, final F formData);

    @Override
    public PageContent createPageContent(final Cart cart, final Form<F> form) {
        return checkoutConfirmationPageContentFactory.create(cart, form);
    }

    @Override
    public void preFillFormData(final Cart cart, final F formData) {
        // Do not pre-fill anything
    }
}
