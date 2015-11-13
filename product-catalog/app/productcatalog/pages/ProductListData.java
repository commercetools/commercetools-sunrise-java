package productcatalog.pages;

import io.sphere.sdk.models.Base;

import java.util.List;

public class ProductListData extends Base {
    private final List<ProductData> productDataList;

    public ProductListData(final List<ProductData> productDataList) {
        this.productDataList = productDataList;
    }

    public List<ProductData> getList() {
        return productDataList;
    }
}
