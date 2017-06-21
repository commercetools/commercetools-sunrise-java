package com.commercetools.sunrise.shoppingcart.remove;

import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.content.viewmodels.CartPageContentFactory;
import com.commercetools.sunrise.framework.controllers.SunriseContentFormController;
import com.commercetools.sunrise.framework.controllers.WithContentFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.cart.CartReverseRouter;
import io.sphere.sdk.carts.Cart;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseRemoveFromCartController extends SunriseContentFormController
        implements WithContentFormFlow<Cart, Cart, RemoveFromCartFormData>, WithRequiredCart {

    private final RemoveFromCartFormData formData;
    private final CartFinder cartFinder;
    private final RemoveFromCartControllerAction removeFromCartControllerAction;
    private final CartPageContentFactory cartPageContentFactory;

    protected SunriseRemoveFromCartController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                              final RemoveFromCartFormData formData, final CartFinder cartFinder,
                                              final RemoveFromCartControllerAction removeFromCartControllerAction,
                                              final CartPageContentFactory cartPageContentFactory) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.cartFinder = cartFinder;
        this.removeFromCartControllerAction = removeFromCartControllerAction;
        this.cartPageContentFactory = cartPageContentFactory;
    }

    @Override
    public final Class<? extends RemoveFromCartFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public final CartFinder getCartFinder() {
        return cartFinder;
    }

    @EnableHooks
    @SunriseRoute(CartReverseRouter.REMOVE_LINE_ITEM_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireNonEmptyCart(this::processForm);
    }

    @Override
    public CompletionStage<Cart> executeAction(final Cart cart, final RemoveFromCartFormData formData) {
        return removeFromCartControllerAction.apply(cart, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final RemoveFromCartFormData formData);

    @Override
    public PageContent createPageContent(final Cart cart, final Form<? extends RemoveFromCartFormData> form) {
        return cartPageContentFactory.create(cart);
    }

    @Override
    public void preFillFormData(final Cart cart, final RemoveFromCartFormData formData) {
        // Do not prefill anything
    }
}
