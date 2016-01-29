package productcatalog.common;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductVariant;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProductGalleryData extends Base {
    private List<ProductImageData> list;

    public ProductGalleryData() {
    }

    public ProductGalleryData(final ProductVariant variant) {
        this.list = variant.getImages().stream()
                .map(ProductImageData::new)
                .collect(toList());
    }

    public List<ProductImageData> getList() {
        return list;
    }

    public void setList(final List<ProductImageData> list) {
        this.list = list;
    }
}
