package productcatalog.pages;

import common.contexts.AppContext;
import common.utils.PriceFormatter;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProductListData {
    private final List<ProductProjection> productList;
    private final AppContext context;
    private final PriceFormatter priceFormatter;

    public ProductListData(final List<ProductProjection> productList, final AppContext context, final PriceFormatter priceFormatter) {
        this.productList = productList;
        this.context = context;
        this.priceFormatter = priceFormatter;
    }

    public List<ProductThumbnailData> getList() {
        return productList.stream().map(product -> new ProductThumbnailData(product, product.getMasterVariant(), context, priceFormatter)).collect(toList());
    }
}
