package com.commercetools.sunrise.shoppingcart.add;

import com.commercetools.sunrise.core.controllers.SunriseContentFormController;
import com.commercetools.sunrise.core.controllers.WithContentFormFlow;
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

public abstract class SunriseAddToCartController extends SunriseContentFormController implements WithContentFormFlow<Void, Cart, AddToCartFormData> {

    private final AddToCartFormData formData;
    private final AddToCartControllerAction controllerAction;

    protected SunriseAddToCartController(final ContentRenderer contentRenderer,
                                         final FormFactory formFactory, final AddToCartFormData formData,
                                         final AddToCartControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends AddToCartFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(CartReverseRouter.ADD_LINE_ITEM_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null);
    }

    @Override
    public CompletionStage<Cart> executeAction(final Void input, final AddToCartFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final AddToCartFormData formData);

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends AddToCartFormData> form) {
        return new BlankPageContent();
    }

    @Override
    public void preFillFormData(final Void input, final AddToCartFormData formData) {
        // Do not pre-fill with anything
    }
}
