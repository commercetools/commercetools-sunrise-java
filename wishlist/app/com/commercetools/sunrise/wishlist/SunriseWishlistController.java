package com.commercetools.sunrise.wishlist;

import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletionStage;

public abstract class SunriseWishlistController extends SunriseController {

    private final TemplateEngine templateEngine;
    private final AddToWishlistFormAction addToWishlistFormAction;
    private final RemoveFromWishlistFormAction removeFromWishlistFormAction;

    protected SunriseWishlistController(final TemplateEngine templateEngine,
                                        final AddToWishlistFormAction addToWishlistFormAction,
                                        final RemoveFromWishlistFormAction removeFromWishlistFormAction) {
        this.templateEngine = templateEngine;
        this.addToWishlistFormAction = addToWishlistFormAction;
        this.removeFromWishlistFormAction = removeFromWishlistFormAction;
    }

    @EnableHooks
    @SunriseRoute(WishlistReverseRouter.WISHLIST_PAGE)
    public CompletionStage<Result> show() {
        return templateEngine.render("my-account-wishlist")
                .thenApply(Results::ok);
    }

    @EnableHooks
    @SunriseRoute(WishlistReverseRouter.ADD_TO_WISHLIST_PROCESS)
    public CompletionStage<Result> addLineItem() {
        return addToWishlistFormAction.apply(this::onLineItemAdded,
                form -> {
                    final PageData pageData = PageData.of().put("addToWishlistForm", form);
                    return templateEngine.render("my-account-wishlist", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    @EnableHooks
    @SunriseRoute(WishlistReverseRouter.REMOVE_FROM_WISHLIST_PROCESS)
    public CompletionStage<Result> removeLineItem() {
        return removeFromWishlistFormAction.apply(this::onLineItemRemoved,
                form -> {
                    final PageData pageData = PageData.of().put("removeFromWishlistForm", form);
                    return templateEngine.render("my-account-wishlist", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    protected abstract Result onLineItemAdded();

    protected abstract Result onLineItemRemoved();
}
