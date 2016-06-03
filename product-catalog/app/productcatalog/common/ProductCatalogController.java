package productcatalog.common;

import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;

import java.util.Collections;
import java.util.List;

public abstract class ProductCatalogController extends SunriseController {

    public ProductCatalogController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
    }

    protected CategoryTree categoryTreeInNew() {
        final List<Category> categoriesInNew = newCategory()
                .map(Collections::singletonList)
                .orElseGet(Collections::emptyList);
        return categoryTree().getSubtree(categoriesInNew);
    }
}
