package com.commercetools.sunrise.framework.cart.addlineitem;

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
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseAddLineItemController extends SunriseContentFormController
        implements WithContentFormFlow<Cart, Cart, AddLineItemFormData>, WithRequiredCart {

    private final AddLineItemFormData formData;
    private final CartFinder cartFinder;
    private final CartCreator cartCreator;
    private final AddLineItemControllerAction controllerAction;
    private final CartDetailPageContentFactory pageContentFactory;

    protected SunriseAddLineItemController(final ContentRenderer contentRenderer,
                                           final FormFactory formFactory, final AddLineItemFormData formData,
                                           final CartFinder cartFinder, final CartCreator cartCreator,
                                           final AddLineItemControllerAction controllerAction,
                                           final CartDetailPageContentFactory pageContentFactory) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.cartFinder = cartFinder;
        this.cartCreator = cartCreator;
        this.controllerAction = controllerAction;
        this.pageContentFactory = pageContentFactory;
    }

    @Override
    public final Class<? extends AddLineItemFormData> getFormDataClass() {
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
    public CompletionStage<Cart> executeAction(final Cart cart, final AddLineItemFormData formData) {
        return controllerAction.apply(cart, formData);
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return cartCreator.get()
                .thenComposeAsync(this::processForm, HttpExecution.defaultContext());
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final AddLineItemFormData formData);

    @Override
    public PageContent createPageContent(final Cart cart, final Form<? extends AddLineItemFormData> form) {
        return pageContentFactory.create(cart);
    }

    @Override
    public void preFillFormData(final Cart cart, final AddLineItemFormData formData) {
        // Do not pre-fill with anything
    }
}
