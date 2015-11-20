package productcatalog.pages;

import io.sphere.sdk.models.Base;

import java.util.List;

public class ProductListData extends Base {
    private List<ProductData> list;

    public ProductListData() {
    }

    public ProductListData(final List<ProductData> list) {
        this.list = list;
    }

    public List<ProductData> getList() {
        return list;
    }

    public void setList(final List<ProductData> list) {
        this.list = list;
    }
}
