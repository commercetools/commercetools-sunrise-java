package purchase;

import common.pages.SelectableData;

import java.util.Collections;

public class PaymentFormBean {
    private PaymentOptionsBean paymentOptions;


    public PaymentFormBean() {
    }

    public static PaymentFormBean ofDummyData() {
        final PaymentFormBean paymentFormBean = new PaymentFormBean();
        final PaymentOptionsBean paymentOptions = new PaymentOptionsBean();
        final SelectableData o = new SelectableData();
        o.setText("prepaid");
        o.setSelected(true);
        paymentOptions.setList(Collections.singletonList(o));
        paymentFormBean.setPaymentOptions(paymentOptions);
        return paymentFormBean;
    }

    public PaymentOptionsBean getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(final PaymentOptionsBean paymentOptions) {
        this.paymentOptions = paymentOptions;
    }
}
