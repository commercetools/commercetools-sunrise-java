package com.commercetools.sunrise.shoppingcart.add;

import com.commercetools.sunrise.shoppingcart.CartCreator;
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
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseAddToCartController extends SunriseContentFormController
        implements WithContentFormFlow<Cart, Cart, AddToCartFormData>, WithRequiredCart {

    private final AddToCartFormData formData;
    private final CartFinder cartFinder;
    private final CartCreator cartCreator;
    private final AddToCartControllerAction controllerAction;
    private final CartPageContentFactory pageContentFactory;

    protected SunriseAddToCartController(final ContentRenderer contentRenderer,
                                         final FormFactory formFactory, final AddToCartFormData formData,
                                         final CartFinder cartFinder, final CartCreator cartCreator,
                                         final AddToCartControllerAction controllerAction,
                                         final CartPageContentFactory pageContentFactory) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.cartFinder = cartFinder;
        this.cartCreator = cartCreator;
        this.controllerAction = controllerAction;
        this.pageContentFactory = pageContentFactory;
    }

    @Override
    public final Class<? extends AddToCartFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public final CartFinder getCartFinder() {
        return cartFinder;
    }

    public final CartCreator getCartCreator() {
        return cartCreator;
    }

    @EnableHooks
    @SunriseRoute(CartReverseRouter.ADD_LINE_ITEM_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireCart(this::processForm);
    }

    @Override
    public CompletionStage<Cart> executeAction(final Cart cart, final AddToCartFormData formData) {
        return controllerAction.apply(cart, formData);
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return cartCreator.get()
                .thenComposeAsync(this::processForm, HttpExecution.defaultContext());
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final AddToCartFormData formData);

    @Override
    public PageContent createPageContent(final Cart cart, final Form<? extends AddToCartFormData> form) {
        return pageContentFactory.create(cart);
    }

    @Override
    public void preFillFormData(final Cart cart, final AddToCartFormData formData) {
        // Do not pre-fill with anything
    }
}
