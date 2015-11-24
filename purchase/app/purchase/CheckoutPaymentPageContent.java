package purchase;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import play.i18n.Messages;

public class CheckoutPaymentPageContent extends CheckoutPageContent {

    private PaymentFormBean paymentForm;

    public CheckoutPaymentPageContent(final Cart cart, final UserContext userContext, final ProductDataConfig productDataConfig, final Messages messages, final ReverseRouter reverseRouter) {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setPaymentStepActive(true);
        setStepWidget(stepWidget);
        setCart(new CartOrderBean(cart, userContext, productDataConfig, reverseRouter));
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
