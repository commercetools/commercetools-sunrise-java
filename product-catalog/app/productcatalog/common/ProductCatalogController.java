package productcatalog.common;

import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.models.ProductDataConfig;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import productcatalog.productoverview.search.SearchConfig;
import common.suggestion.ProductRecommendation;

import java.util.Collections;
import java.util.List;

public abstract class ProductCatalogController extends SunriseController {

    private final ProductRecommendation productRecommendation;
    private final ProductDataConfig productDataConfig;
    private final SearchConfig searchConfig;

    public ProductCatalogController(final ControllerDependency controllerDependency,
                                    final ProductRecommendation productRecommendation, final ProductDataConfig productDataConfig,
                                    final SearchConfig searchConfig) {
        super(controllerDependency);
        this.productRecommendation = productRecommendation;
        this.productDataConfig = productDataConfig;
        this.searchConfig = searchConfig;
    }

    protected CategoryTree categoryTreeInNew() {
        final List<Category> categoriesInNew = newCategory()
                .map(Collections::singletonList)
                .orElseGet(Collections::emptyList);
        return categoryTree().getSubtree(categoriesInNew);
    }

    protected ProductRecommendation productRecommendation() {
        return productRecommendation;
    }

    protected ProductDataConfig productDataConfig() {
        return productDataConfig;
    }

    protected SearchConfig searchConfig() {
        return searchConfig;
    }
}
