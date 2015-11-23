package purchase;

import common.contexts.UserContext;
import common.models.ProductDataConfig;
import common.pages.ReverseRouter;
import io.sphere.sdk.carts.Cart;

public class CheckoutPaymentPageContent extends CheckoutPageContent {

    private PaymentFormBean paymentForm;

    public CheckoutPaymentPageContent(final Cart cart, final UserContext userContext, final ProductDataConfig productDataConfig, final ReverseRouter reverseRouter, final String csrfToken) {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setPaymentStepActive(true);
        setStepWidget(stepWidget);
        setCart(new CartOrderBean(cart, userContext, productDataConfig));
        setPaymentForm(new PaymentFormBean(reverseRouter, csrfToken, userContext));
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
