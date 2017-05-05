package com.commercetools.sunrise.common.categorytree;

import com.commercetools.sunrise.categorytree.CategoryTreeConfiguration;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.home.HomeReverseRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.cache.CacheApi;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public class CategoriesRefreshController extends Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriesRefreshController.class);

    private final CategoryTreeConfiguration configuration;
    private final CacheApi cacheApi;
    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public CategoriesRefreshController(final CategoryTreeConfiguration configuration,
                                       final CacheApi cacheApi, final HomeReverseRouter homeReverseRouter) {
        this.configuration = configuration;
        this.cacheApi = cacheApi;
        this.homeReverseRouter = homeReverseRouter;
    }

    public Result refresh() {
        cacheApi.remove(configuration.cacheKey());
        LOGGER.info("Cached category tree removed");
        return redirect(homeReverseRouter.homePageCall());
    }
}
