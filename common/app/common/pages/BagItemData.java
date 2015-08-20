package common.pages;

import io.sphere.sdk.models.Base;

import java.util.List;

/**
 * Represents a list of selectable items to select the quantity for a LineItem.
 * The Bag is thereby the equivalent to a Cart.
 */
public class BagItemData extends Base {

    private final List<SelectableData> bagItems;

    public BagItemData(final List<SelectableData> bagItems) {
        this.bagItems = bagItems;
    }

    public List<SelectableData> getBagItems() {
        return bagItems;
    }
}
