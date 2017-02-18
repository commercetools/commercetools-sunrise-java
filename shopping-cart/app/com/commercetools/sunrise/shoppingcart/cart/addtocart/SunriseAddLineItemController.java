package com.commercetools.sunrise.shoppingcart.cart.addtocart;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CartReverseRouter;
import com.commercetools.sunrise.shoppingcart.CartCreator;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseAddLineItemController<F extends AddProductToCartFormData> extends SunriseTemplateFormController implements WithTemplateFormFlow<F, Cart, Cart>, WithRequiredCart {

    private final CartFinder cartFinder;
    private final CartCreator cartCreator;
    private final AddProductToCartExecutor addProductToCartExecutor;
    private final CartDetailPageContentFactory cartDetailPageContentFactory;

    protected SunriseAddLineItemController(final TemplateRenderer templateRenderer, final FormFactory formFactory,
                                           final CartFinder cartFinder, final CartCreator cartCreator,
                                           final AddProductToCartExecutor addProductToCartExecutor,
                                           final CartDetailPageContentFactory cartDetailPageContentFactory) {
        super(templateRenderer, formFactory);
        this.cartFinder = cartFinder;
        this.cartCreator = cartCreator;
        this.addProductToCartExecutor = addProductToCartExecutor;
        this.cartDetailPageContentFactory = cartDetailPageContentFactory;
    }

    @Override
    public CartFinder getCartFinder() {
        return cartFinder;
    }

    public CartCreator getCartCreator() {
        return cartCreator;
    }

    @RunRequestStartedHook
    @SunriseRoute(CartReverseRouter.ADD_LINE_ITEM_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireCart(this::processForm);
    }

    @Override
    public CompletionStage<Cart> executeAction(final Cart cart, final F formData) {
        return addProductToCartExecutor.apply(cart, formData);
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return cartCreator.get()
                .thenComposeAsync(this::processForm, HttpExecution.defaultContext());
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final F formData);

    @Override
    public PageContent createPageContent(final Cart cart, final Form<F> form) {
        return cartDetailPageContentFactory.create(cart);
    }

    @Override
    public void preFillFormData(final Cart cart, final F formData) {
        // Do not pre-fill with anything
    }
}
