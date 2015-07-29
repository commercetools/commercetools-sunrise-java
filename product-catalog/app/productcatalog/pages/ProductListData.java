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
    private final String text;
    private final String sale;
    private final String newx;
    private final String quickView;
    private final String wishlist;
    private final String moreColors;

    public ProductListData(final List<ProductProjection> productList, final AppContext context, final PriceFormatter priceFormatter, final String text, final String sale, final String newx, final String quickView, final String wishlist, final String moreColors) {
        this.productList = productList;
        this.context = context;
        this.priceFormatter = priceFormatter;
        this.text = text;
        this.sale = sale;
        this.newx = newx;
        this.quickView = quickView;
        this.wishlist = wishlist;
        this.moreColors = moreColors;
    }

    public String getText() {
        return text;
    }

    public String getSale() {
        return sale;
    }

    public String getNew() {
        return newx;
    }

    public String quickView() {
        return quickView;
    }

    public String getWishlist() {
        return wishlist;
    }

    public String getMoreColors() {
        return moreColors;
    }

    public List<ProductThumbnailData> getList() {
        return productList.stream().map(product -> new ProductThumbnailData(product, product.getMasterVariant(), context, priceFormatter)).collect(toList());
    }
}
