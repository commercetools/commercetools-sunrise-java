package controllers;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.shoppingcart.carts.AddToCartFormAction;
import com.commercetools.sunrise.shoppingcart.carts.ChangeQuantityInCartFormAction;
import com.commercetools.sunrise.shoppingcart.carts.RemoveFromCartFormAction;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public class CartController extends SunriseController {

    private static final String TEMPLATE = "cart";

    private final TemplateEngine templateEngine;
    private final AddToCartFormAction addToCartFormAction;
    private final ChangeQuantityInCartFormAction changeQuantityInCartFormAction;
    private final RemoveFromCartFormAction removeFromCartFormAction;

    @Inject
    CartController(final TemplateEngine templateEngine,
                   final AddToCartFormAction addToCartFormAction,
                   final ChangeQuantityInCartFormAction changeQuantityInCartFormAction,
                   final RemoveFromCartFormAction removeFromCartFormAction) {
        this.templateEngine = templateEngine;
        this.addToCartFormAction = addToCartFormAction;
        this.changeQuantityInCartFormAction = changeQuantityInCartFormAction;
        this.removeFromCartFormAction = removeFromCartFormAction;
    }

    @EnableHooks
    public CompletionStage<Result> show() {
        return templateEngine.render(TEMPLATE).thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> addLineItem() {
        return addToCartFormAction.apply(
                () -> routes.CartController.show(),
                form -> templateEngine.render(TEMPLATE, PageData.of().put("addToCartForm", form)));
    }

    @EnableHooks
    public CompletionStage<Result> changeQuantity() {
        return changeQuantityInCartFormAction.apply(
                () -> routes.CartController.show(),
                form -> templateEngine.render(TEMPLATE, PageData.of().put("changeQuantityInCartForm", form)));
    }

    @EnableHooks
    public CompletionStage<Result> removeLineItem() {
        return removeFromCartFormAction.apply(
                () -> routes.CartController.show(),
                form -> templateEngine.render(TEMPLATE, PageData.of().put("removeFromCartForm", form)));
    }
}
