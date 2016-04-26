package shoppingcart.checkout.payment;

import common.models.SelectableBean;

import java.util.List;

public class PaymentOptionsBean {
    private List<SelectableBean> list;

    public PaymentOptionsBean() {
     }

    public List<SelectableBean> getList() {
        return list;
    }

    public void setList(final List<SelectableBean> list) {
        this.list = list;
    }
}
