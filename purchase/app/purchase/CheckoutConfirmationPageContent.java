package purchase;

import common.contexts.UserContext;
import common.models.ProductDataConfig;
import common.pages.SelectableData;
import io.sphere.sdk.carts.Cart;

public class CheckoutConfirmationPageContent extends CheckoutPageContent {

    private CheckoutConfirmationFormBean checkoutForm;

    public CheckoutConfirmationPageContent() {
    }

    public CheckoutConfirmationPageContent(final Cart cart, final UserContext userContext, final ProductDataConfig productDataConfig) {
        setCart(new CartOrderBean(cart, userContext, productDataConfig));
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setConfirmationStepActive(true);
        setStepWidget(stepWidget);
        fillForm();
    }

    private void fillForm() {
        final CheckoutConfirmationFormBean checkoutConfirmationFormBean = new CheckoutConfirmationFormBean();
        final SelectableData newsletter = new SelectableData();
        newsletter.setName("SUNRISE Newsletter");
        checkoutConfirmationFormBean.setNewsletter(newsletter);
        final SelectableData termsConditions = new SelectableData();
        checkoutConfirmationFormBean.setTermsConditions(termsConditions);
        setCheckoutForm(checkoutConfirmationFormBean);
    }

    @Override
    public String additionalTitle() {
        return null;
    }

    public CheckoutConfirmationFormBean getCheckoutForm() {
        return checkoutForm;
    }

    public void setCheckoutForm(final CheckoutConfirmationFormBean checkoutForm) {
        this.checkoutForm = checkoutForm;
    }
}
