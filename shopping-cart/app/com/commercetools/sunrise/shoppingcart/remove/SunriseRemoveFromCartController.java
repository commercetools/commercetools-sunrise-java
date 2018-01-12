package com.commercetools.sunrise.shoppingcart.remove;

import com.commercetools.sunrise.core.controllers.SunriseContentFormController;
import com.commercetools.sunrise.core.controllers.WithContentForm2Flow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.shoppingcart.cart.CartReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.models.BlankPageContent;
import io.sphere.sdk.carts.Cart;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseRemoveFromCartController extends SunriseContentFormController implements WithContentForm2Flow<Void, Cart, RemoveFromCartFormData> {

    private final RemoveFromCartFormData formData;
    private final RemoveFromCartControllerAction removeFromCartControllerAction;

    protected SunriseRemoveFromCartController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                              final RemoveFromCartFormData formData,
                                              final RemoveFromCartControllerAction removeFromCartControllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.removeFromCartControllerAction = removeFromCartControllerAction;
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
        return new BlankPageContent();
    }

    @Override
    public void preFillFormData(final Void input, final RemoveFromCartFormData formData) {
        // Do not prefill anything
    }
}
