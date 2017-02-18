package com.commercetools.sunrise.shoppingcart.cart.changelineitemquantity;

import com.commercetools.sunrise.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CartReverseRouter;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseChangeLineItemQuantityController<F extends ChangeLineItemQuantityFormData> extends SunriseTemplateFormController implements WithTemplateFormFlow<F, Cart, Cart>, WithRequiredCart {

    private final CartFinder cartFinder;
    private final CartDetailPageContentFactory cartDetailPageContentFactory;
    private final ChangeLineItemQuantityExecutor changeLineItemQuantityExecutor;

    protected SunriseChangeLineItemQuantityController(final TemplateRenderer templateRenderer, final FormFactory formFactory,
                                                      final CartFinder cartFinder,
                                                      final CartDetailPageContentFactory cartDetailPageContentFactory,
                                                      final ChangeLineItemQuantityExecutor changeLineItemQuantityExecutor) {
        super(templateRenderer, formFactory);
        this.cartFinder = cartFinder;
        this.cartDetailPageContentFactory = cartDetailPageContentFactory;
        this.changeLineItemQuantityExecutor = changeLineItemQuantityExecutor;
    }

    @Override
    public CartFinder getCartFinder() {
        return cartFinder;
    }

    @RunRequestStartedHook
    @SunriseRoute(CartReverseRouter.CHANGE_LINE_ITEM_QUANTITY_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireNonEmptyCart(this::processForm);
    }

    @Override
    public CompletionStage<Cart> executeAction(final Cart cart, final F formData) {
        return changeLineItemQuantityExecutor.apply(cart, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final F formData);

    @Override
    public PageContent createPageContent(final Cart cart, final Form<F> form) {
        return cartDetailPageContentFactory.create(cart);
    }

    @Override
    public void preFillFormData(final Cart cart, final F formData) {
        // Do not pre-fill anything
    }
}
