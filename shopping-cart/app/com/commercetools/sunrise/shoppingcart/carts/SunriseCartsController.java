package com.commercetools.sunrise.shoppingcart.carts;

import com.commercetools.sunrise.core.controllers.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.shoppingcart.cart.CartReverseRouter;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletionStage;

public abstract class SunriseCartsController extends SunriseController {

    private final TemplateEngine templateEngine;
    private final AddToCartFormAction addToCartFormAction;
    private final ChangeQuantityInCartFormAction changeQuantityInCartFormAction;
    private final RemoveFromCartFormAction removeFromCartFormAction;

    protected SunriseCartsController(final TemplateEngine templateEngine,
                                     final AddToCartFormAction addToCartFormAction,
                                     final ChangeQuantityInCartFormAction changeQuantityInCartFormAction,
                                     final RemoveFromCartFormAction removeFromCartFormAction) {
        this.templateEngine = templateEngine;
        this.addToCartFormAction = addToCartFormAction;
        this.changeQuantityInCartFormAction = changeQuantityInCartFormAction;
        this.removeFromCartFormAction = removeFromCartFormAction;
    }

    @EnableHooks
    @SunriseRoute(CartReverseRouter.CART_DETAIL_PAGE)
    public CompletionStage<Result> show() {
        return templateEngine.render("cart")
                .thenApply(Results::ok);
    }

    @EnableHooks
    @SunriseRoute(CartReverseRouter.ADD_LINE_ITEM_PROCESS)
    public CompletionStage<Result> addLineItem() {
        return addToCartFormAction.apply(this::onLineItemAdded,
                form -> {
                    final PageData pageData = PageData.of().put("addToCartForm", form);
                    return templateEngine.render("cart", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    @EnableHooks
    @SunriseRoute(CartReverseRouter.CHANGE_LINE_ITEM_QUANTITY_PROCESS)
    public CompletionStage<Result> changeQuantity() {
        return changeQuantityInCartFormAction.apply(this::onQuantityChanged,
                form -> {
                    final PageData pageData = PageData.of().put("changeQuantityInCartForm", form);
                    return templateEngine.render("cart", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    @EnableHooks
    @SunriseRoute(CartReverseRouter.REMOVE_LINE_ITEM_PROCESS)
    public CompletionStage<Result> removeLineItem() {
        return removeFromCartFormAction.apply(this::onLineItemRemoved,
                form -> {
                    final PageData pageData = PageData.of().put("removeFromCartForm", form);
                    return templateEngine.render("cart", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    protected abstract Result onLineItemAdded();

    protected abstract Result onQuantityChanged();

    protected abstract Result onLineItemRemoved();
}
