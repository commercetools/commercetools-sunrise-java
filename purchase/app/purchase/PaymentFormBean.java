package purchase;

import common.contexts.UserContext;
import common.pages.ReverseRouter;
import common.pages.SelectableData;

import java.util.Collections;

public class PaymentFormBean {
    private String actionUrl;
    private String csrfToken;
    private PaymentOptionsBean paymentOptions;


    public PaymentFormBean() {
    }

    public PaymentFormBean(final ReverseRouter reverseRouter, final String csrfToken, final UserContext userContext) {
        setActionUrl(reverseRouter.processCheckoutPaymentForm(userContext.locale().getLanguage()).url());
        setCsrfToken(csrfToken);
        final PaymentOptionsBean paymentOptions = new PaymentOptionsBean();
        final SelectableData o = new SelectableData();
        o.setLabel("prepaid");
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

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(final String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public PaymentOptionsBean getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(final PaymentOptionsBean paymentOptions) {
        this.paymentOptions = paymentOptions;
    }
}
