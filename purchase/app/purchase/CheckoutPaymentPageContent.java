package purchase;

import common.contexts.UserContext;
import common.models.ProductDataConfig;
import common.controllers.ReverseRouter;
import io.sphere.sdk.carts.Cart;

public class CheckoutPaymentPageContent extends CheckoutPageContent {

    private PaymentFormBean paymentForm;

    public CheckoutPaymentPageContent(final Cart cart, final ProductDataConfig productDataConfig,
                                      final UserContext userContext, final ReverseRouter reverseRouter) {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setPaymentStepActive(true);
        setStepWidget(stepWidget);
        setCart(new CartOrderBean(cart, productDataConfig, userContext, reverseRouter));
        setPaymentForm(PaymentFormBean.ofDummyData());
    }

    public PaymentFormBean getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(final PaymentFormBean paymentForm) {
        this.paymentForm = paymentForm;
    }

    @Override
    public String getAdditionalTitle() {
        return null;
    }
}
