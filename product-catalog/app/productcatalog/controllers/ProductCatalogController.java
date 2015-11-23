package productcatalog.controllers;

import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.models.ProductDataConfig;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import productcatalog.services.CategoryService;
import productcatalog.services.ProductProjectionService;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class ProductCatalogController extends SunriseController {
    private final CategoryService categoryService;
    private final ProductProjectionService productService;
    private final ProductDataConfig productDataConfig;
    private final String categoryNewExtId;


    public ProductCatalogController(final ControllerDependency controllerDependency, final CategoryService categoryService,
                                    final ProductProjectionService productService, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.categoryService = categoryService;
        this.productService = productService;
        this.productDataConfig = productDataConfig;
        this.categoryNewExtId = controllerDependency.configuration().getString("common.newCategoryExternalId", "");
    }

    protected CategoryTree categoryTreeInNew() {
        final List<Category> categoriesInNew = categories().findById(categoryNewExtId)
                .map(categoryNew -> singletonList(categoryNew))
                .orElse(emptyList());
        return CategoryTree.of(categoriesInNew);
    }

    protected CategoryService categoryService() {
        return categoryService;
    }

    protected ProductProjectionService productService() {
        return productService;
    }

    protected ProductDataConfig productDataConfig() {
        return productDataConfig;
    }
}
