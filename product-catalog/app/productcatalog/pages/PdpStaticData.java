package productcatalog.pages;

import common.cms.CmsPage;
import common.pages.BagItemData;
import common.pages.RatingData;
import common.pages.SelectableData;
import io.sphere.sdk.models.Base;

import java.util.List;

public class PdpStaticData extends Base {

    private final List<SelectableData> bagItems;
    private final List<SelectableData> rating;

    private final CmsPage cms;

    public PdpStaticData(final CmsPage cms, final BagItemData bagItems, final RatingData rating) {
        this.cms = cms;
        this.bagItems = bagItems.getBagItems();
        this.rating = rating.getRating();
    }

    public String getRatingText() {
        return cms.getOrEmpty("writeFirstRatingText");
    }

    public String getViewDetailsText() {
        return cms.getOrEmpty("viewDetailsText");
    }

    public String getColorsText() {
        return cms.getOrEmpty("colorsText");
    }

    public String getSizesText() {
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

    public String getShippingRatesText() {
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
        return rating;
    }

    public List<SelectableData> getBagItems() {
        return bagItems;
    }
}
