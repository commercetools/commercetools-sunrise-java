package productcatalog.models;

import java.util.List;

public class DeliveryRatesData {
    private List<ShippingMethodData> list;

    public DeliveryRatesData() {
    }

    public List<ShippingMethodData> getList() {
        return list;
    }

    public void setList(final List<ShippingMethodData> list) {
        this.list = list;
    }
}
