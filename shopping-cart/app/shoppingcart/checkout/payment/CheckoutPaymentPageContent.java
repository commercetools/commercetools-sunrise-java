package shoppingcart.checkout.payment;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.i18n.I18nResolver;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import shoppingcart.CartOrderBean;
import shoppingcart.checkout.CheckoutPageContent;
import shoppingcart.checkout.StepWidgetBean;

public class CheckoutPaymentPageContent extends CheckoutPageContent {

    private PaymentFormBean paymentForm;

    public CheckoutPaymentPageContent(final Cart cart, final UserContext userContext, final ProductDataConfig productDataConfig,
                                      final I18nResolver i18nResolver, final ReverseRouter reverseRouter) {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setPaymentStepActive(true);
        setStepWidget(stepWidget);
        setCart(new CartOrderBean(cart, userContext, productDataConfig, reverseRouter));
        setPaymentForm(PaymentFormBean.ofDummyData());
        setAdditionalTitle(i18nResolver.getOrEmpty(userContext.locales(), "checkout", "paymentPage.title"));
    }

    public PaymentFormBean getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(final PaymentFormBean paymentForm) {
        this.paymentForm = paymentForm;
    }
}
