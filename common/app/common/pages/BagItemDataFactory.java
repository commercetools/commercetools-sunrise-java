package common.pages;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds BagItemData which is holding a list of selectable items to select
 * the quantity for a LineItem. The Bag is thereby the equivalent to a Cart.
 */
public class BagItemDataFactory {

    private BagItemDataFactory() {

    }

    public static BagItemDataFactory of() {
        return new BagItemDataFactory();
    }

    public BagItemData create(final int numItems) {
        return new BagItemData(createItemList(numItems));
    }

    /**
     * Creates a list of selectable items starting with the default quantity 1,
     * followed by the subsequent quantities up to numItems.s
     * @param numItems the upper boundary for quantity
     * @return the list of selectable items
     */
    public List<SelectableData> createItemList(final int numItems) {
        final List<SelectableData> bagItems = new ArrayList<>(numItems);
        bagItems.add(bagItemDefaultItem());

        for(int i = 2; i <= numItems; i++) {
            bagItems.add(this.bagItemToSelectableItem(i));
        }

        return bagItems;
    }

    /**
     * The default item has the quantity one and is selected in the beginning
     * @return the default item
     */
    private SelectableData bagItemDefaultItem() {
        return new SelectableData("1", "", "", "", true);
    }

    /**
     * Creates a selectable item from a given quantity
     * @param quantity the quantity to be represented by the selectable item
     * @return a selectable item
     */
    private SelectableData bagItemToSelectableItem(final int quantity) {
        return new SelectableData(Integer.toString(quantity), "", "", "", false);
    }
}
