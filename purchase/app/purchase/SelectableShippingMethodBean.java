package purchase;

import common.pages.SelectableData;

public class SelectableShippingMethodBean extends SelectableData {
    private String deliveryDays;

    public SelectableShippingMethodBean() {
    }

    public String getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(final String deliveryDays) {
        this.deliveryDays = deliveryDays;
    }
}
