package shoppingcart.confirmation;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.i18n.I18nResolver;
import common.models.ProductDataConfig;
import common.models.SelectableData;
import io.sphere.sdk.carts.Cart;
import shoppingcart.common.CartOrderBean;
import shoppingcart.common.CheckoutPageContent;
import shoppingcart.common.StepWidgetBean;

public class CheckoutConfirmationPageContent extends CheckoutPageContent {

    private CheckoutConfirmationFormBean checkoutForm;

    public CheckoutConfirmationPageContent() {
    }

    public CheckoutConfirmationPageContent(final Cart cart, final ProductDataConfig productDataConfig,
                                           final UserContext userContext, final ReverseRouter reverseRouter, final I18nResolver i18nResolver) {
        setCart(new CartOrderBean(cart, productDataConfig, userContext, reverseRouter));
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setConfirmationStepActive(true);
        setStepWidget(stepWidget);
        fillForm();
        setAdditionalTitle(i18nResolver.getOrEmpty(userContext.locales(), "checkout", "confirmationPage.title"));
    }

    private void fillForm() {
        final CheckoutConfirmationFormBean checkoutConfirmationFormBean = new CheckoutConfirmationFormBean();
        final SelectableData newsletter = new SelectableData();
        newsletter.setLabel("SUNRISE Newsletter");
        checkoutConfirmationFormBean.setNewsletter(newsletter);
        final SelectableData termsConditions = new SelectableData();
        checkoutConfirmationFormBean.setTermsConditions(termsConditions);
        setCheckoutForm(checkoutConfirmationFormBean);
    }

    public CheckoutConfirmationFormBean getCheckoutForm() {
        return checkoutForm;
    }

    public void setCheckoutForm(final CheckoutConfirmationFormBean checkoutForm) {
        this.checkoutForm = checkoutForm;
    }
}
