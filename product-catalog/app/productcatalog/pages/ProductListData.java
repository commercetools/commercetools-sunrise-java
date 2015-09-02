package productcatalog.pages;

import common.pages.ProductThumbnailData;
import io.sphere.sdk.models.Base;

import java.util.List;

public class ProductListData extends Base {
    final List<ProductThumbnailData> productList;

    public ProductListData(final List<ProductThumbnailData> productList) {
        this.productList = productList;
    }

    public List<ProductThumbnailData> getList() {
        return productList;
    }
}
