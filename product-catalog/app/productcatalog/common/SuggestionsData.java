package productcatalog.common;

import io.sphere.sdk.models.Base;
import productcatalog.common.ProductListData;
import productcatalog.common.ProductThumbnailData;

import java.util.List;

public class SuggestionsData extends Base {
    private List<ProductThumbnailData> list;

    public SuggestionsData() {
    }

    public SuggestionsData(final ProductListData productListData) {
        this.list = productListData.getList();
    }

    public List<ProductThumbnailData> getList() {
        return list;
    }

    public void setList(final List<ProductThumbnailData> list) {
        this.list = list;
    }
}
