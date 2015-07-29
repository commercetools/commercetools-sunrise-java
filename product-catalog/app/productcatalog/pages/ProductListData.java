package productcatalog.pages;

import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProductListData {

    private final Translator translator;
    private final PriceFormatter priceFormatter;
    private final PriceFinder priceFinder;

    private final List<ProductProjection> productList;
    private final String text;
    private final String sale;
    private final String newx;
    private final String quickView;
    private final String wishlist;
    private final String moreColors;

    public ProductListData(final Translator translator, final PriceFormatter priceFormatter, final PriceFinder priceFinder, final List<ProductProjection> productList,  final String text, final String sale, final String newx, final String quickView, final String wishlist, final String moreColors) {
        this.translator = translator;
        this.priceFormatter = priceFormatter;
        this.priceFinder = priceFinder;

        this.productList = productList;
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
        return productList.stream().map(product ->
                new ProductThumbnailData(translator, priceFormatter, priceFinder, product, product.getMasterVariant())).collect(toList());
    }
}
