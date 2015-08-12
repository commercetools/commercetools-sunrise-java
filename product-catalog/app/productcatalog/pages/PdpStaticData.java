package productcatalog.pages;

import common.cms.CmsPage;
import common.pages.SelectableData;
import common.pages.BagItemData;
import common.pages.RatingData;

import java.util.List;

public class PdpStaticData {

    private final CmsPage cms;

    public PdpStaticData(final CmsPage cms) {
        this.cms = cms;
    }

    public String getRatingText() {
        return cms.getOrEmpty("writeFirstRatingText");
    }

    public String getViewDetailsText() {
        return cms.getOrEmpty("viewDetailsText");
    }

    public String getColorText() {
        return cms.getOrEmpty("colorsText");
    }

    public String getSizeText() {
        return cms.getOrEmpty("sizesText");
    }

    public String getSizeGuideText() {
        return cms.getOrEmpty("sizeGuideText");
    }

    public String getAddToBagText() {
        return cms.getOrEmpty("addToBagText");
    }

    public String getAddToWishlistText() {
        return cms.getOrEmpty("addToWishlistText");
    }

    public String getAvailableText() {
        return cms.getOrEmpty("availableText");
    }

    public String getProductDetailsText() {
        return cms.getOrEmpty("productDetailsText");
    }

    public String getDeliveryAndReturnsText() {
        return cms.getOrEmpty("deliveryAndReturnsText");
    }

    public String getDeliveryText() {
        return cms.getOrEmpty("deliveryText");
    }

    public String getReturnsText() {
        return cms.getOrEmpty("returnsText");
    }

    public String getYouMayLikeText() {
        return cms.getOrEmpty("youMayLikeText");
    }

    public String getSaleText() {
        return cms.getOrEmpty("saleText");
    }

    public String getNewText() {
        return cms.getOrEmpty("newText");
    }

    public String getQuickViewText() {
        return cms.getOrEmpty("quickViewText");
    }

    public String getWishlistText() {
        return cms.getOrEmpty("wishlistText");
    }

    public String getMoreColorsText() {
        return cms.getOrEmpty("moreColorsText");
    }

    public String getReviewsText() {
        return cms.getOrEmpty("reviewsText");
    }

    public SelectableData getColorDefaultItem() {
        return new SelectableData(cms.getOrEmpty("chooseColorText"), "none", "", "", true);
    }

    public SelectableData getSizeDefaultItem() {
        return new SelectableData(cms.getOrEmpty("chooseSizeText"), "none", "", "", true);
    }

    public List<SelectableData> getRating() {
        return (new RatingData(cms)).getRating();
    }

    public List<SelectableData> getBagItems() {
        return (new BagItemData(100)).getBagItems();
    }
}
