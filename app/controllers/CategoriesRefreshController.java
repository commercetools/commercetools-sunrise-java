package controllers;

import io.sphere.sdk.categories.CategoryTree;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public class CategoriesRefreshController extends Controller {
    private final RefreshableCategoryTree refreshableCategoryTree;

    @Inject
    public CategoriesRefreshController(final CategoryTree categoryTree) {
        if (categoryTree instanceof RefreshableCategoryTree) {
            this.refreshableCategoryTree = (RefreshableCategoryTree) categoryTree;
        } else {
            this.refreshableCategoryTree = null;
        }
    }

    public Result refresh() {
        refreshableCategoryTree.refresh();
        return ok("Fetched " + refreshableCategoryTree.getAllAsFlatList().size() + " categories");
    }
}
