package com.commercetools.sunrise.framework.cart.changelineitemquantity;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.CartFinder;
import com.commercetools.sunrise.framework.WithRequiredCart;
import com.commercetools.sunrise.framework.cart.cartdetail.viewmodels.CartDetailPageContentFactory;
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

public abstract class SunriseChangeLineItemQuantityController extends SunriseContentFormController
        implements WithContentFormFlow<Cart, Cart, ChangeLineItemQuantityFormData>, WithRequiredCart {

    private final ChangeLineItemQuantityFormData formData;
    private final CartFinder cartFinder;
    private final CartDetailPageContentFactory pageContentFactory;
    private final ChangeLineItemQuantityControllerAction controllerAction;

    protected SunriseChangeLineItemQuantityController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                                      final ChangeLineItemQuantityFormData formData, final CartFinder cartFinder,
                                                      final CartDetailPageContentFactory pageContentFactory,
                                                      final ChangeLineItemQuantityControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.cartFinder = cartFinder;
        this.pageContentFactory = pageContentFactory;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends ChangeLineItemQuantityFormData> getFormDataClass() {
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
    public CompletionStage<Cart> executeAction(final Cart cart, final ChangeLineItemQuantityFormData formData) {
        return controllerAction.apply(cart, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final ChangeLineItemQuantityFormData formData);

    @Override
    public PageContent createPageContent(final Cart cart, final Form<? extends ChangeLineItemQuantityFormData> form) {
        return pageContentFactory.create(cart);
    }

    @Override
    public void preFillFormData(final Cart cart, final ChangeLineItemQuantityFormData formData) {
        // Do not pre-fill anything
    }
}
