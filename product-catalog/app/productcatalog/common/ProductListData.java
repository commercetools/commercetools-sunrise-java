package productcatalog.common;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.models.ProductDataConfig;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public class ProductListData extends Base {
    private List<ProductThumbnailData> list;

    public ProductListData() {
    }

    public ProductListData(final Iterable<ProductProjection> productList, final ProductDataConfig productDataConfig,
                           final UserContext userContext, final ReverseRouter reverseRouter, final CategoryTree categoryTreeNew) {
        this.list = StreamSupport.stream(productList.spliterator(), false)
                .map(product -> {
                    final ProductVariant matchingVariant = product.findFirstMatchingVariant()
                            .orElseGet(product::getMasterVariant);
                    return new ProductThumbnailData(product, matchingVariant, productDataConfig, userContext, reverseRouter, categoryTreeNew);
                })
                .collect(toList());
    }

    public List<ProductThumbnailData> getList() {
        return list;
    }

    public void setList(final List<ProductThumbnailData> list) {
        this.list = list;
    }
}
