package purchase;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.i18n.I18nResolver;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;

public class CheckoutPaymentPageContent extends CheckoutPageContent {

    private PaymentFormBean paymentForm;

    public CheckoutPaymentPageContent(final Cart cart, final ProductDataConfig productDataConfig, final UserContext userContext,
                                      final I18nResolver i18nResolver, final ReverseRouter reverseRouter) {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setPaymentStepActive(true);
        setStepWidget(stepWidget);
        setCart(new CartOrderBean(cart, productDataConfig, userContext, reverseRouter));
        setPaymentForm(PaymentFormBean.ofDummyData());
        setAdditionalTitle(i18nResolver.getOrEmpty("checkout", "paymentPageTitle", userContext.locales()));
    }

    public PaymentFormBean getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(final PaymentFormBean paymentForm) {
        this.paymentForm = paymentForm;
    }
}
