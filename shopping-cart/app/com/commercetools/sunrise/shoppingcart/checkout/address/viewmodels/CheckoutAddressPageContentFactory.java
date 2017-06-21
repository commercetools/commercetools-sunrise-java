package com.commercetools.sunrise.shoppingcart.checkout.address.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.FormPageContentFactory;
import com.commercetools.sunrise.framework.viewmodels.content.carts.CartViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.checkout.address.CheckoutAddressFormData;
import io.sphere.sdk.carts.Cart;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutAddressPageContentFactory extends FormPageContentFactory<CheckoutAddressPageContent, Cart, CheckoutAddressFormData> {

    private final PageTitleResolver pageTitleResolver;
    private final CartViewModelFactory cartViewModelFactory;
    private final CheckoutAddressFormSettingsViewModelFactory addressFormSettingsFactory;

    @Inject
    public CheckoutAddressPageContentFactory(final PageTitleResolver pageTitleResolver, final CartViewModelFactory cartViewModelFactory,
                                             final CheckoutAddressFormSettingsViewModelFactory addressFormSettingsFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.cartViewModelFactory = cartViewModelFactory;
        this.addressFormSettingsFactory = addressFormSettingsFactory;
    }

    protected final PageTitleResolver getPageTitleResolver() {
        return pageTitleResolver;
    }

    protected final CartViewModelFactory getCartViewModelFactory() {
        return cartViewModelFactory;
    }

    protected final CheckoutAddressFormSettingsViewModelFactory getAddressFormSettingsFactory() {
        return addressFormSettingsFactory;
    }

    @Override
    protected CheckoutAddressPageContent newViewModelInstance(final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        return new CheckoutAddressPageContent();
    }

    @Override
    public final CheckoutAddressPageContent create(final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        return super.create(cart, form);
    }

    @Override
    protected final void initialize(final CheckoutAddressPageContent viewModel, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        super.initialize(viewModel, cart, form);
        fillCart(viewModel, cart, form);
        fillForm(viewModel, cart, form);
        fillFormSettings(viewModel, cart, form);
    }

    @Override
    protected void fillTitle(final CheckoutAddressPageContent viewModel, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        viewModel.setTitle(pageTitleResolver.getOrEmpty("checkout:shippingPage.title"));
    }

    protected void fillCart(final CheckoutAddressPageContent viewModel, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        viewModel.setCart(cartViewModelFactory.create(cart));
    }

    protected void fillForm(final CheckoutAddressPageContent viewModel, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        viewModel.setAddressForm(form);
    }

    protected void fillFormSettings(final CheckoutAddressPageContent viewModel, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        viewModel.setAddressFormSettings(addressFormSettingsFactory.create(cart, form));
    }
}
