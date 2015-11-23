package productcatalog.models;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProductListData extends Base {
    private List<ProductData> list;

    public ProductListData() {
    }

    public List<ProductData> getList() {
        return list;
    }

    public void setList(final List<ProductData> list) {
        this.list = list;
    }

    public ProductListData(final UserContext userContext, final ReverseRouter reverseRouter,
                           final CategoryTree categoryTree, final List<ProductProjection> productList) {
        this.list = productList.stream()
                .map(product -> new ProductData(userContext, reverseRouter, categoryTree, product, product.getMasterVariant()))
                .collect(toList());
    }
}
