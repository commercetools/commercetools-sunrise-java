package productcatalog.common;

import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.models.ProductDataConfig;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import productcatalog.services.ProductService;

import java.util.Collections;
import java.util.List;

public abstract class ProductCatalogController extends SunriseController {
    private final ProductService productService;
    private final ProductDataConfig productDataConfig;


    public ProductCatalogController(final ControllerDependency controllerDependency,
                                    final ProductService productService, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.productService = productService;
        this.productDataConfig = productDataConfig;
    }

    protected CategoryTree categoryTreeInNew() {
        final List<Category> categoriesInNew = newCategory()
                .map(Collections::singletonList)
                .orElseGet(Collections::emptyList);
        return categoryTree().getSubtree(categoriesInNew);
    }

    protected ProductService productService() {
        return productService;
    }

    protected ProductDataConfig productDataConfig() {
        return productDataConfig;
    }
}
