package com.commercetools.sunrise.common.categorytree;

import io.sphere.sdk.categories.CategoryTree;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public class CategoriesRefreshController extends Controller {

    @Inject
    private CategoryTree categoryTree;

    public Result refresh() {
        if (categoryTree instanceof RefreshableCategoryTree) {
            ((RefreshableCategoryTree) categoryTree).refresh();
            return ok("Fetched " + categoryTree.getAllAsFlatList().size() + " categories");
        } else 
        throw new RuntimeException("Not found refreshable category tree");
    }
}
