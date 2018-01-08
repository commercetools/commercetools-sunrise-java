package com.commercetools.sunrise.shoppingcart.remove;

import com.commercetools.sunrise.core.controllers.SunriseContentFormController;
import com.commercetools.sunrise.core.controllers.WithContentFormFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.shoppingcart.cart.CartReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.shoppingcart.content.viewmodels.CartPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseRemoveFromCartController extends SunriseContentFormController implements WithContentFormFlow<Void, Cart, RemoveFromCartFormData> {

    private final RemoveFromCartFormData formData;
    private final RemoveFromCartControllerAction removeFromCartControllerAction;
    private final CartPageContentFactory cartPageContentFactory;

    protected SunriseRemoveFromCartController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                              final RemoveFromCartFormData formData,
                                              final RemoveFromCartControllerAction removeFromCartControllerAction,
                                              final CartPageContentFactory cartPageContentFactory) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.removeFromCartControllerAction = removeFromCartControllerAction;
        this.cartPageContentFactory = cartPageContentFactory;
    }

    @Override
    public final Class<? extends RemoveFromCartFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(CartReverseRouter.REMOVE_LINE_ITEM_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null);
    }

    @Override
    public CompletionStage<Cart> executeAction(final Void input, final RemoveFromCartFormData formData) {
        return removeFromCartControllerAction.apply(formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final RemoveFromCartFormData formData);

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends RemoveFromCartFormData> form) {
        return cartPageContentFactory.create(null);
    }

    @Override
    public void preFillFormData(final Void input, final RemoveFromCartFormData formData) {
        // Do not prefill anything
    }
}
