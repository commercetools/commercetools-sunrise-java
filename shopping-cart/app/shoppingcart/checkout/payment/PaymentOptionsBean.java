package shoppingcart.checkout.payment;

import common.models.SelectableData;

import java.util.List;

public class PaymentOptionsBean {
    private List<SelectableData> list;

    public PaymentOptionsBean() {
     }

    public List<SelectableData> getList() {
        return list;
    }

    public void setList(final List<SelectableData> list) {
        this.list = list;
    }
}
