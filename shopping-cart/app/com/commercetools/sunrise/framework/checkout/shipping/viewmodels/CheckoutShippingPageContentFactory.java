package com.commercetools.sunrise.framework.checkout.shipping.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.FormPageContentFactory;
import com.commercetools.sunrise.framework.viewmodels.PageTitleResolver;
import com.commercetools.sunrise.framework.viewmodels.content.carts.CartViewModelFactory;
import com.commercetools.sunrise.framework.checkout.shipping.CheckoutShippingFormData;
import com.commercetools.sunrise.framework.checkout.shipping.ShippingMethodsWithCart;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutShippingPageContentFactory extends FormPageContentFactory<CheckoutShippingPageContent, ShippingMethodsWithCart, CheckoutShippingFormData> {

    private final PageTitleResolver pageTitleResolver;
    private final CartViewModelFactory cartViewModelFactory;
    private final CheckoutShippingFormSettingsViewModelFactory checkoutShippingFormSettingsViewModelFactory;

    @Inject
    public CheckoutShippingPageContentFactory(final PageTitleResolver pageTitleResolver, final CartViewModelFactory cartViewModelFactory,
                                              final CheckoutShippingFormSettingsViewModelFactory checkoutShippingFormSettingsViewModelFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.cartViewModelFactory = cartViewModelFactory;
        this.checkoutShippingFormSettingsViewModelFactory = checkoutShippingFormSettingsViewModelFactory;
    }

    protected final PageTitleResolver getPageTitleResolver() {
        return pageTitleResolver;
    }

    protected final CartViewModelFactory getCartViewModelFactory() {
        return cartViewModelFactory;
    }

    protected final CheckoutShippingFormSettingsViewModelFactory getCheckoutShippingFormSettingsViewModelFactory() {
        return checkoutShippingFormSettingsViewModelFactory;
    }

    @Override
    protected CheckoutShippingPageContent newViewModelInstance(final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        return new CheckoutShippingPageContent();
    }

    @Override
    public final CheckoutShippingPageContent create(final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        return super.create(shippingMethodsWithCart, form);
    }

    @Override
    protected final void initialize(final CheckoutShippingPageContent viewModel, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        super.initialize(viewModel, shippingMethodsWithCart, form);
        fillCart(viewModel, shippingMethodsWithCart, form);
        fillForm(viewModel, shippingMethodsWithCart, form);
        fillFormSettings(viewModel, shippingMethodsWithCart, form);
    }

    @Override
    protected void fillTitle(final CheckoutShippingPageContent viewModel, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        viewModel.setTitle(pageTitleResolver.getOrEmpty("checkout:shippingPage.title"));
    }

    protected void fillCart(final CheckoutShippingPageContent viewModel, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        viewModel.setCart(cartViewModelFactory.create(shippingMethodsWithCart.getCart()));
    }

    protected void fillForm(final CheckoutShippingPageContent viewModel, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        viewModel.setShippingForm(form);
    }

    protected void fillFormSettings(final CheckoutShippingPageContent viewModel, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        viewModel.setShippingFormSettings(checkoutShippingFormSettingsViewModelFactory.create(shippingMethodsWithCart, form));
    }
}
