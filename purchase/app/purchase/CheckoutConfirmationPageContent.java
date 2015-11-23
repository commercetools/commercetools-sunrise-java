package purchase;

import common.contexts.UserContext;
import common.models.ProductDataConfig;
import common.pages.ReverseRouter;
import common.pages.SelectableData;
import io.sphere.sdk.carts.Cart;
import play.Configuration;
import play.i18n.Messages;

public class CheckoutConfirmationPageContent extends CheckoutPageContent {

    private CheckoutConfirmationFormBean checkoutForm;

    public CheckoutConfirmationPageContent() {
    }

    public CheckoutConfirmationPageContent(final Cart cart, final Messages messages, final Configuration configuration, final ReverseRouter reverseRouter, final UserContext userContext, final ProductDataConfig productDataConfig) {
        setCart(new CartOrderBean(cart, userContext, productDataConfig));
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setConfirmationStepActive(true);
        setStepWidget(stepWidget);
        fillForm(reverseRouter, userContext);
    }

    private void fillForm(final ReverseRouter reverseRouter, final UserContext userContext) {
        final CheckoutConfirmationFormBean checkoutConfirmationFormBean = new CheckoutConfirmationFormBean();
        checkoutConfirmationFormBean.setActionUrl(reverseRouter.processCheckoutConfirmationForm(userContext.locale().getLanguage()).url());
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
