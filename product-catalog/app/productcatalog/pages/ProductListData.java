package productcatalog.pages;

import java.util.List;

public class ProductListData {

    private final String text;
    private final String sale;
    private final String isNew;
    private final String quickView;
    private final String wishlist;
    private final String moreColors;
    private final List<ProductThumbnailData> thumbnails;

    public ProductListData(final String text, final String sale, final String isNew, final String quickView, final String wishlist, final String moreColors, final List<ProductThumbnailData> thumbnails) {
        this.text = text;
        this.sale = sale;
        this.isNew = isNew;
        this.quickView = quickView;
        this.wishlist = wishlist;
        this.moreColors = moreColors;
        this.thumbnails = thumbnails;
    }

    public String getText() {
        return text;
    }

    public String getSale() {
        return sale;
    }

    public String getNew() {
        return isNew;
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
        return thumbnails;
    }
}
