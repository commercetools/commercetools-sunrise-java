package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithContent;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.productcatalog.home.HomeReverseRouter;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * Controller for the home page.
 */
public abstract class SunriseHomeController extends SunriseContentController implements WithContent {

    protected SunriseHomeController(final ContentRenderer contentRenderer) {
        super(contentRenderer);
    }

    @EnableHooks
    @SunriseRoute(HomeReverseRouter.HOME_PAGE)
    public CompletionStage<Result> show() {
        return okResultWithPageContent(PageData.of());
    }
}
