package purchase;

import common.contexts.ProjectContext;
import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.i18n.I18nResolver;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import play.Configuration;

public class CheckoutShippingPageContent extends CheckoutPageContent {
    private CheckoutShippingFormBean shippingForm;


    public CheckoutShippingPageContent() {
    }

    public CheckoutShippingPageContent(final Cart cart, final I18nResolver i18nResolver, final Configuration configuration,
                                       final UserContext userContext, final ProjectContext projectContext, final ShippingMethods shippingMethods,
                                       final ProductDataConfig productDataConfig, final ReverseRouter reverseRouter) {
        fillDefaults(cart, userContext, productDataConfig, i18nResolver, reverseRouter);
        setShippingForm(new CheckoutShippingFormBean(cart, userContext, projectContext, shippingMethods, i18nResolver, configuration));
    }

    public CheckoutShippingPageContent(final CheckoutShippingFormData filledForm, final Cart cart, final I18nResolver i18nResolver,
                                       final Configuration configuration, final UserContext userContext, final ProjectContext projectContext,
                                       final ShippingMethods shippingMethods, final ProductDataConfig productDataConfig,
                                       final ReverseRouter reverseRouter) {
        fillDefaults(cart, userContext, productDataConfig, i18nResolver, reverseRouter);
        setShippingForm(new CheckoutShippingFormBean(filledForm, userContext, projectContext, shippingMethods, i18nResolver, configuration));
    }

    private void fillDefaults(final Cart cart, final UserContext userContext, final ProductDataConfig productDataConfig,
                              final I18nResolver i18nResolver, final ReverseRouter reverseRouter) {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setShippingStepActive(true);
        setStepWidget(stepWidget);
        setCart(new CartOrderBean(cart, productDataConfig, userContext, reverseRouter));
        setAdditionalTitle(i18nResolver.resolve("checkout", "shippingPageTitle", userContext.locales()).orElse(""));
    }

    public CheckoutShippingFormBean getShippingForm() {
        return shippingForm;
    }

    public void setShippingForm(final CheckoutShippingFormBean shippingForm) {
        this.shippingForm = shippingForm;
    }
}
