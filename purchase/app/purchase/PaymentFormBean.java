package purchase;

import common.contexts.UserContext;
import common.pages.ReverseRouter;
import common.pages.SelectableData;

import java.util.Collections;

public class PaymentFormBean {
    private String actionUrl;
    private PaymentOptionsBean paymentOptions;


    public PaymentFormBean() {
    }

    public PaymentFormBean(final ReverseRouter reverseRouter, final UserContext userContext) {
        setActionUrl(reverseRouter.processCheckoutPaymentForm(userContext.locale().getLanguage()).url());
        final PaymentOptionsBean paymentOptions = new PaymentOptionsBean();
        final SelectableData o = new SelectableData();
        o.setText("prepaid");
        o.setSelected(true);
        paymentOptions.setList(Collections.singletonList(o));
        setPaymentOptions(paymentOptions);
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(final String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public PaymentOptionsBean getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(final PaymentOptionsBean paymentOptions) {
        this.paymentOptions = paymentOptions;
    }
}
