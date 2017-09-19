package com.commercetools.sunrise.common.categorytree;

import com.commercetools.sunrise.ctp.categories.CategoriesSettings;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.home.HomeReverseRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.cache.CacheApi;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public class CategoryTreeRefreshController extends Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryTreeRefreshController.class);

    private final CategoriesSettings categoriesSettings;
    private final CacheApi cacheApi;
    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public CategoryTreeRefreshController(final CategoriesSettings categoriesSettings,
                                         final CacheApi cacheApi, final HomeReverseRouter homeReverseRouter) {
        this.categoriesSettings = categoriesSettings;
        this.cacheApi = cacheApi;
        this.homeReverseRouter = homeReverseRouter;
    }

    public Result refresh() {
        cacheApi.remove(categoriesSettings.cacheKey());
        LOGGER.info("Cached category tree removed");
        return redirect(homeReverseRouter.homePageCall());
    }
}