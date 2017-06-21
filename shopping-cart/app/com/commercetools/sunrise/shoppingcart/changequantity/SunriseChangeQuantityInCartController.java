package com.commercetools.sunrise.shoppingcart.changequantity;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.content.viewmodels.CartPageContentFactory;
import com.commercetools.sunrise.framework.controllers.SunriseContentFormController;
import com.commercetools.sunrise.framework.controllers.WithContentFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.cart.CartReverseRouter;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import io.sphere.sdk.carts.Cart;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseChangeQuantityInCartController extends SunriseContentFormController
        implements WithContentFormFlow<Cart, Cart, ChangeQuantityInCartFormData>, WithRequiredCart {

    private final ChangeQuantityInCartFormData formData;
    private final CartFinder cartFinder;
    private final CartPageContentFactory pageContentFactory;
    private final ChangeQuantityInCartControllerAction controllerAction;

    protected SunriseChangeQuantityInCartController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                                    final ChangeQuantityInCartFormData formData, final CartFinder cartFinder,
                                                    final CartPageContentFactory pageContentFactory,
                                                    final ChangeQuantityInCartControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.cartFinder = cartFinder;
        this.pageContentFactory = pageContentFactory;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends ChangeQuantityInCartFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public final CartFinder getCartFinder() {
        return cartFinder;
    }

    @EnableHooks
    @SunriseRoute(CartReverseRouter.CHANGE_LINE_ITEM_QUANTITY_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireNonEmptyCart(this::processForm);
    }

    @Override
    public CompletionStage<Cart> executeAction(final Cart cart, final ChangeQuantityInCartFormData formData) {
        return controllerAction.apply(cart, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final ChangeQuantityInCartFormData formData);

    @Override
    public PageContent createPageContent(final Cart cart, final Form<? extends ChangeQuantityInCartFormData> form) {
        return pageContentFactory.create(cart);
    }

    @Override
    public void preFillFormData(final Cart cart, final ChangeQuantityInCartFormData formData) {
        // Do not pre-fill anything
    }
}
