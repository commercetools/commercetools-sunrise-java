package com.commercetools.sunrise.shoppingcart.removediscountcode;

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

public abstract class SunriseRemoveDiscountCodeController extends SunriseContentFormController implements WithContentForm2Flow<Void, Cart, RemoveDiscountCodeFormData> {

    private final RemoveDiscountCodeFormData formData;
    private final RemoveDiscountCodeFormAction controllerAction;

    protected SunriseRemoveDiscountCodeController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                                  final RemoveDiscountCodeFormData formData,
                                                  final RemoveDiscountCodeFormAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends RemoveDiscountCodeFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(CartReverseRouter.REMOVE_DISCOUNT_CODE_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null); // TODO it required non-empty cart
    }

    @Override
    public CompletionStage<Cart> executeAction(final Void input, final RemoveDiscountCodeFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final RemoveDiscountCodeFormData formData);

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends RemoveDiscountCodeFormData> form) {
        return new BlankPageContent();
    }

    @Override
    public void preFillFormData(final Void input, final RemoveDiscountCodeFormData formData) {
        // Do not pre-fill anything
    }
}
