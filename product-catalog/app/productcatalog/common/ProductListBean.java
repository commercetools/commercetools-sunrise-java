package productcatalog.common;

import io.sphere.sdk.models.Base;

import java.util.List;

public class ProductListBean extends Base {

    private List<ProductThumbnailBean> list;

    public ProductListBean() {
    }

    public List<ProductThumbnailBean> getList() {
        return list;
    }

    public void setList(final List<ProductThumbnailBean> list) {
        this.list = list;
    }
}
