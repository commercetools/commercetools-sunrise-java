package com.commercetools.sunrise.wishlist.content;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithQueryFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.models.BlankPageContent;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * This controller is used to view the current wishlist.
 */
public abstract class SunriseWishlistContentController extends SunriseContentController implements WithQueryFlow<Void> {

    @Inject
    protected SunriseWishlistContentController(final ContentRenderer contentRenderer) {
        super(contentRenderer);
    }

    @EnableHooks
    @SunriseRoute(WishlistReverseRouter.WISHLIST_PAGE)
    public CompletionStage<Result> show() {
        return showPage(null);
    }

    @Override
    public PageContent createPageContent(final Void input) {
        return new BlankPageContent();
    }
}
