package com.commercetools.sunrise.shoppingcart.changequantity;

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

public abstract class SunriseChangeQuantityInCartController extends SunriseContentFormController implements WithContentFormFlow<Void, Cart, ChangeQuantityInCartFormData> {

    private final ChangeQuantityInCartFormData formData;
    private final CartPageContentFactory pageContentFactory;
    private final ChangeQuantityInCartControllerAction controllerAction;

    protected SunriseChangeQuantityInCartController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                                    final ChangeQuantityInCartFormData formData,
                                                    final CartPageContentFactory pageContentFactory,
                                                    final ChangeQuantityInCartControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.pageContentFactory = pageContentFactory;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends ChangeQuantityInCartFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(CartReverseRouter.CHANGE_LINE_ITEM_QUANTITY_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null); // TODO it was requiring non-empty cart
    }

    @Override
    public CompletionStage<Cart> executeAction(final Void input, final ChangeQuantityInCartFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final ChangeQuantityInCartFormData formData);

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends ChangeQuantityInCartFormData> form) {
        return pageContentFactory.create(null);
    }

    @Override
    public void preFillFormData(final Void input, final ChangeQuantityInCartFormData formData) {
        // Do not pre-fill anything
    }
}
