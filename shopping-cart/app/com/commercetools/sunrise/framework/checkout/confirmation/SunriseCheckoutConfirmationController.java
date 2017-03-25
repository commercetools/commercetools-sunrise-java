package com.commercetools.sunrise.framework.checkout.confirmation;

import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.CartFinder;
import com.commercetools.sunrise.framework.WithRequiredCart;
import com.commercetools.sunrise.framework.checkout.confirmation.viewmodels.CheckoutConfirmationPageContentFactory;
import com.commercetools.sunrise.framework.controllers.SunriseContentFormController;
import com.commercetools.sunrise.framework.controllers.WithContentFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.checkout.CheckoutReverseRouter;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.orders.Order;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseCheckoutConfirmationController extends SunriseContentFormController
        implements WithContentFormFlow<Cart, Order, CheckoutConfirmationFormData>, WithRequiredCart {

    private final CheckoutConfirmationFormData formData;
    private final CartFinder cartFinder;
    private final CheckoutConfirmationControllerAction controllerAction;
    private final CheckoutConfirmationPageContentFactory pageContentFactory;

    protected SunriseCheckoutConfirmationController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                                    final CheckoutConfirmationFormData formData, final CartFinder cartFinder,
                                                    final CheckoutConfirmationControllerAction controllerAction,
                                                    final CheckoutConfirmationPageContentFactory pageContentFactory) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.cartFinder = cartFinder;
        this.controllerAction = controllerAction;
        this.pageContentFactory = pageContentFactory;
    }

    @Override
    public final Class<? extends CheckoutConfirmationFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public final CartFinder getCartFinder() {
        return cartFinder;
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_CONFIRMATION_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return requireNonEmptyCart(cart -> showFormPage(cart, formData));
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_CONFIRMATION_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireNonEmptyCart(this::processForm);
    }

    @Override
    public CompletionStage<Order> executeAction(final Cart cart, final CheckoutConfirmationFormData formData) {
        return controllerAction.apply(cart, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Order order, final CheckoutConfirmationFormData formData);

    @Override
    public PageContent createPageContent(final Cart cart, final Form<? extends CheckoutConfirmationFormData> form) {
        return pageContentFactory.create(cart, form);
    }

    @Override
    public void preFillFormData(final Cart cart, final CheckoutConfirmationFormData formData) {
        // Do not pre-fill anything
    }
}
