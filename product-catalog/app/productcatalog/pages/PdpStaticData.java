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

    public String getViewDetails() {
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
