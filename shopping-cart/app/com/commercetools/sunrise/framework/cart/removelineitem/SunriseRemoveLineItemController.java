package com.commercetools.sunrise.framework.cart.removelineitem;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.CartFinder;
import com.commercetools.sunrise.framework.WithRequiredCart;
import com.commercetools.sunrise.framework.cart.cartdetail.viewmodels.CartDetailPageContentFactory;
import com.commercetools.sunrise.framework.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.framework.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.cart.CartReverseRouter;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import io.sphere.sdk.carts.Cart;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseRemoveLineItemController extends SunriseTemplateFormController
        implements WithTemplateFormFlow<Cart, Cart, RemoveLineItemFormData>, WithRequiredCart {

    private final RemoveLineItemFormData formData;
    private final CartFinder cartFinder;
    private final RemoveLineItemControllerAction removeLineItemControllerAction;
    private final CartDetailPageContentFactory cartDetailPageContentFactory;

    protected SunriseRemoveLineItemController(final TemplateRenderer templateRenderer, final FormFactory formFactory,
                                              final RemoveLineItemFormData formData, final CartFinder cartFinder,
                                              final RemoveLineItemControllerAction removeLineItemControllerAction,
                                              final CartDetailPageContentFactory cartDetailPageContentFactory) {
        super(templateRenderer, formFactory);
        this.formData = formData;
        this.cartFinder = cartFinder;
        this.removeLineItemControllerAction = removeLineItemControllerAction;
        this.cartDetailPageContentFactory = cartDetailPageContentFactory;
    }

    @Override
    public final Class<? extends RemoveLineItemFormData> getFormDataClass() {
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
    public CompletionStage<Cart> executeAction(final Cart cart, final RemoveLineItemFormData formData) {
        return removeLineItemControllerAction.apply(cart, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final RemoveLineItemFormData formData);

    @Override
    public PageContent createPageContent(final Cart cart, final Form<? extends RemoveLineItemFormData> form) {
        return cartDetailPageContentFactory.create(cart);
    }

    @Override
    public void preFillFormData(final Cart cart, final RemoveLineItemFormData formData) {
        // Do not prefill anything
    }
}
