package shoppingcart.checkout.confirmation;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.template.i18n.I18nIdentifier;
import common.template.i18n.I18nResolver;
import common.models.ProductDataConfig;
import common.models.SelectableData;
import io.sphere.sdk.carts.Cart;
import shoppingcart.checkout.CheckoutPageContent;
import shoppingcart.checkout.StepWidgetBean;
import shoppingcart.common.CartOrderBean;

public class CheckoutConfirmationPageContent extends CheckoutPageContent {

    private CheckoutConfirmationFormBean checkoutForm;

    public CheckoutConfirmationPageContent() {
    }

    public CheckoutConfirmationPageContent(final Cart cart, final UserContext userContext, final ProductDataConfig productDataConfig,
                                           final I18nResolver i18nResolver, final ReverseRouter reverseRouter) {
        setCart(new CartOrderBean(cart, userContext, productDataConfig, reverseRouter));
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setConfirmationStepActive(true);
        setStepWidget(stepWidget);
        fillForm();
        setAdditionalTitle(i18nResolver.getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:confirmationPage.title")));
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
