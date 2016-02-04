package categorytree;

import io.sphere.sdk.categories.CategoryTreeExtended;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Optional;

public class CategoriesRefreshController extends Controller {
    private final Optional<RefreshableCategoryTree> refreshableCategoryTree;

    @Inject
    public CategoriesRefreshController(final CategoryTreeExtended categoryTree) {
        if (categoryTree instanceof RefreshableCategoryTree) {
            this.refreshableCategoryTree = Optional.of((RefreshableCategoryTree) categoryTree);
        } else {
            this.refreshableCategoryTree = Optional.empty();
        }
    }

    public Result refresh() {
        return refreshableCategoryTree.map(tree -> {
            tree.refresh();
            return ok("Fetched " + tree.getAllAsFlatList().size() + " categories");
        }).orElseThrow(() -> new RuntimeException("Not found refreshable category tree"));
    }
}
