package purchase;

import common.contexts.UserContext;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;

public class CheckoutPaymentPageContent extends CheckoutPageContent {

    private PaymentFormBean paymentForm;

    public CheckoutPaymentPageContent(final Cart cart, final UserContext userContext, final ProductDataConfig productDataConfig) {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setPaymentStepActive(true);
        setStepWidget(stepWidget);
        setCart(new CartOrderBean(cart, userContext, productDataConfig));
        setPaymentForm(PaymentFormBean.ofDummyData());
    }

    public PaymentFormBean getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(final PaymentFormBean paymentForm) {
        this.paymentForm = paymentForm;
    }

    @Override
    public String additionalTitle() {
        return null;
    }
}
