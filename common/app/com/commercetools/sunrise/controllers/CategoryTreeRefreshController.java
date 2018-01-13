package com.commercetools.sunrise.controllers;

import com.commercetools.sunrise.core.reverserouters.productcatalog.home.HomeReverseRouter;
import com.commercetools.sunrise.models.categories.CategorySettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.cache.CacheApi;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public final class CategoryTreeRefreshController extends Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryTreeRefreshController.class);

    private final CategorySettings categorySettings;
    private final CacheApi cacheApi;
    private final HomeReverseRouter homeReverseRouter;

    @Inject
    CategoryTreeRefreshController(final CategorySettings categorySettings,
                                         final CacheApi cacheApi, final HomeReverseRouter homeReverseRouter) {
        this.categorySettings = categorySettings;
        this.cacheApi = cacheApi;
        this.homeReverseRouter = homeReverseRouter;
    }

    public Result refresh() {
        cacheApi.remove(categorySettings.cacheKey());
        LOGGER.info("Cached category tree removed");
        return redirect(homeReverseRouter.homePageCall());
    }
}