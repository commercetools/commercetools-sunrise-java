package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.WithOverridablePageContent;
import com.commercetools.sunrise.common.errors.ErrorsBean;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.shoppingcart.CartLikeBeanFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.data.Form;

import javax.inject.Inject;
import java.util.List;

import static com.commercetools.sunrise.common.utils.FormUtils.extractFormField;

public class CheckoutShippingPageContentFactory extends Base implements WithOverridablePageContent<CheckoutShippingPageContent> {
    @Inject
    private CheckoutShippingFormBeanFactory checkoutShippingFormBeanFactory;
    @Inject
    private UserContext userContext;
    @Inject
    private I18nResolver i18nResolver;
    @com.google.inject.Inject
    protected CartLikeBeanFactory cartLikeBeanFactory;

    public CheckoutShippingPageContent create(final Cart cart, final List<ShippingMethod> shippingMethods) {
        final CheckoutShippingPageContent pageContent = createPageContent();
        final CheckoutShippingFormBean shippingFormBean = checkoutShippingFormBeanFactory.create(cart, shippingMethods);
        pageContent.setShippingForm(shippingFormBean);
        fillTitle(pageContent);
        pageContent.setCart(cartLikeBeanFactory.create(cart));
        return pageContent;
    }

    public CheckoutShippingPageContent create(final Cart cart, final List<ShippingMethod> shippingMethods, final ErrorsBean errors, final Form<CheckoutShippingFormData> shippingForm) {
        final CheckoutShippingPageContent pageContent = createPageContent();
        final String selectedShippingMethodId = extractFormField(shippingForm, "shippingMethodId");
        final CheckoutShippingFormBean formBean = checkoutShippingFormBeanFactory.create(shippingMethods, selectedShippingMethodId);
        formBean.setErrors(errors);
        pageContent.setShippingForm(formBean);
        fillTitle(pageContent);
        pageContent.setCart(cartLikeBeanFactory.create(cart));
        return pageContent;
    }

    @Override
    public CheckoutShippingPageContent createPageContent() {
        return new CheckoutShippingPageContent();
    }

    protected void fillTitle(final PageContent pageContent) {
        pageContent.setTitle(i18nResolver.getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:shippingPage.title")));
    }
}
