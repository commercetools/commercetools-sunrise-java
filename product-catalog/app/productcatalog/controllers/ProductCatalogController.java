package productcatalog.controllers;

import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.models.ProductDataConfig;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import productcatalog.services.ProductService;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

public class ProductCatalogController extends SunriseController {
    private final ProductService productService;
    private final ProductDataConfig productDataConfig;
    private final String categoryNewExtId;


    public ProductCatalogController(final ControllerDependency controllerDependency,
                                    final ProductService productService, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.productService = productService;
        this.productDataConfig = productDataConfig;
        this.categoryNewExtId = controllerDependency.configuration().getString("common.newCategoryExternalId", "");
    }

    protected CategoryTree categoryTreeInNew() {
        final List<Category> categoriesInNew = categoryTree().findByExternalId(categoryNewExtId)
                .map(Collections::singletonList)
                .orElse(emptyList());
        return categoryTree().getSubtree(categoriesInNew);
    }

    protected ProductService productService() {
        return productService;
    }

    protected ProductDataConfig productDataConfig() {
        return productDataConfig;
    }
}
