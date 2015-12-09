package purchase;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import play.i18n.Messages;

public class CheckoutPaymentPageContent extends CheckoutPageContent {

    private PaymentFormBean paymentForm;

    public CheckoutPaymentPageContent(final Cart cart, final ProductDataConfig productDataConfig, final UserContext userContext, final Messages messages, final ReverseRouter reverseRouter) {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setPaymentStepActive(true);
        setStepWidget(stepWidget);
        setCart(new CartOrderBean(cart, productDataConfig, userContext, reverseRouter));
        setPaymentForm(PaymentFormBean.ofDummyData());
        setAdditionalTitle(messages.at("checkoutPaymentPageTitle"));
    }

    public PaymentFormBean getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(final PaymentFormBean paymentForm) {
        this.paymentForm = paymentForm;
    }
}
