package com.commercetools.sunrise.framework.checkout.address.viewmodels;

import com.commercetools.sunrise.common.forms.FormFieldWithOptions;
import com.commercetools.sunrise.common.models.FormViewModelFactory;
import com.commercetools.sunrise.common.models.addresses.CountryFormFieldViewModelFactory;
import com.commercetools.sunrise.common.models.addresses.TitleFormFieldViewModelFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.checkout.address.CheckoutAddressFormData;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.Cart;
import play.data.Form;

import javax.inject.Inject;

import static java.util.Collections.singletonList;

@RequestScoped
public class CheckoutAddressFormSettingsViewModelFactory extends FormViewModelFactory<CheckoutAddressFormSettingsViewModel, Cart, CheckoutAddressFormData> {

    private final CountryCode country;
    private final CountryFormFieldViewModelFactory countryFormFieldViewModelFactory;
    private final TitleFormFieldViewModelFactory titleFormFieldViewModelFactory;

    @Inject
    public CheckoutAddressFormSettingsViewModelFactory(final CountryCode country, final CountryFormFieldViewModelFactory countryFormFieldViewModelFactory,
                                                       final TitleFormFieldViewModelFactory titleFormFieldViewModelFactory) {
        this.country = country;
        this.countryFormFieldViewModelFactory = countryFormFieldViewModelFactory;
        this.titleFormFieldViewModelFactory = titleFormFieldViewModelFactory;
    }

    @Override
    protected CheckoutAddressFormSettingsViewModel getViewModelInstance() {
        return new CheckoutAddressFormSettingsViewModel();
    }

    @Override
    public final CheckoutAddressFormSettingsViewModel create(final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        return super.create(cart, form);
    }

    @Override
    protected final void initialize(final CheckoutAddressFormSettingsViewModel viewModel, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        fillCountriesShipping(viewModel, cart, form);
        fillCountriesBilling(viewModel, cart, form);
        fillTitleShipping(viewModel, cart, form);
        fillTitleBilling(viewModel, cart, form);
    }

    protected void fillTitleBilling(final CheckoutAddressFormSettingsViewModel model, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        model.setTitleBilling(titleFormFieldViewModelFactory.createWithDefaultOptions(form.field("titleBilling")));
    }

    protected void fillTitleShipping(final CheckoutAddressFormSettingsViewModel model, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        model.setTitleShipping(titleFormFieldViewModelFactory.createWithDefaultOptions(form.field("titleShipping")));
    }

    protected void fillCountriesBilling(final CheckoutAddressFormSettingsViewModel model, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        model.setCountriesBilling(countryFormFieldViewModelFactory.createWithDefaultOptions(form.field("countryBilling")));
    }

    protected void fillCountriesShipping(final CheckoutAddressFormSettingsViewModel model, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        final FormFieldWithOptions<CountryCode> formFieldWithOptions = new FormFieldWithOptions<>(form.field("countryShipping"), singletonList(country));
        model.setCountriesShipping(countryFormFieldViewModelFactory.create(formFieldWithOptions));
    }
}
