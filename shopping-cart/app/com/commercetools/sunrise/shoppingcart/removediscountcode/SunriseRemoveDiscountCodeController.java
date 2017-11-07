package com.commercetools.sunrise.shoppingcart.removediscountcode;

import com.commercetools.sunrise.framework.controllers.SunriseContentFormController;
import com.commercetools.sunrise.framework.controllers.WithContentFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.cart.CartReverseRouter;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.content.viewmodels.CartPageContent;
import com.commercetools.sunrise.shoppingcart.content.viewmodels.CartPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseRemoveDiscountCodeController extends SunriseContentFormController
        implements WithContentFormFlow<Cart, Cart, RemoveDiscountCodeFormData>, WithRequiredCart {

    private final RemoveDiscountCodeFormData formData;
    private final CartFinder cartFinder;
    private final CartPageContentFactory pageContentFactory;
    private final RemoveDiscountCodeControllerAction controllerAction;

    protected SunriseRemoveDiscountCodeController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                                  final RemoveDiscountCodeFormData formData, final CartFinder cartFinder,
                                                  final CartPageContentFactory pageContentFactory,
                                                  final RemoveDiscountCodeControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.cartFinder = cartFinder;
        this.pageContentFactory = pageContentFactory;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends RemoveDiscountCodeFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public final CartFinder getCartFinder() {
        return cartFinder;
    }

    @EnableHooks
    @SunriseRoute(CartReverseRouter.REMOVE_DISCOUNT_CODE_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireNonEmptyCart(this::processForm);
    }

    @Override
    public CompletionStage<Cart> executeAction(final Cart cart, final RemoveDiscountCodeFormData formData) {
        return controllerAction.apply(cart, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final RemoveDiscountCodeFormData formData);

    @Override
    public PageContent createPageContent(final Cart cart, final Form<? extends RemoveDiscountCodeFormData> form) {
        final CartPageContent cartPageContent = pageContentFactory.create(cart);
        return cartPageContent;
    }

    @Override
    public void preFillFormData(final Cart cart, final RemoveDiscountCodeFormData formData) {
        // Do not pre-fill anything
    }
}
