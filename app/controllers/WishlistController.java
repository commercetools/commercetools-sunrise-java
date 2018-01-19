package controllers;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.wishlist.AddToWishlistFormAction;
import com.commercetools.sunrise.wishlist.RemoveFromWishlistFormAction;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public class WishlistController extends SunriseController {

    private static final String TEMPLATE = "my-account-wishlist";

    private final TemplateEngine templateEngine;
    private final AddToWishlistFormAction addToWishlistFormAction;
    private final RemoveFromWishlistFormAction removeFromWishlistFormAction;

    @Inject
    WishlistController(final TemplateEngine templateEngine,
                       final AddToWishlistFormAction addToWishlistFormAction,
                       final RemoveFromWishlistFormAction removeFromWishlistFormAction) {
        this.templateEngine = templateEngine;
        this.addToWishlistFormAction = addToWishlistFormAction;
        this.removeFromWishlistFormAction = removeFromWishlistFormAction;
    }

    @EnableHooks
    public CompletionStage<Result> show() {
        return templateEngine.render(TEMPLATE).thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> addLineItem() {
        return addToWishlistFormAction.apply(
                () -> routes.WishlistController.show(),
                form -> templateEngine.render(TEMPLATE, PageData.of().put("addToWishlistForm", form)));
    }

    @EnableHooks
    public CompletionStage<Result> removeLineItem() {
        return removeFromWishlistFormAction.apply(
                () -> routes.WishlistController.show(),
                form -> templateEngine.render(TEMPLATE, PageData.of().put("removeFromWishlistForm", form)));
    }
}
