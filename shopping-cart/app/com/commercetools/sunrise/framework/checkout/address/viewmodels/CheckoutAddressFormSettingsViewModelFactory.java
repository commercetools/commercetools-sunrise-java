package com.commercetools.sunrise.framework.checkout.address.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.forms.FormFieldWithOptions;
import com.commercetools.sunrise.framework.viewmodels.FormViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.forms.countries.CountryFormFieldViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.forms.titles.TitleFormFieldViewModelFactory;
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

    protected final CountryCode getCountry() {
        return country;
    }

    protected final CountryFormFieldViewModelFactory getCountryFormFieldViewModelFactory() {
        return countryFormFieldViewModelFactory;
    }

    protected final TitleFormFieldViewModelFactory getTitleFormFieldViewModelFactory() {
        return titleFormFieldViewModelFactory;
    }

    @Override
    protected CheckoutAddressFormSettingsViewModel newViewModelInstance(final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
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

    protected void fillTitleBilling(final CheckoutAddressFormSettingsViewModel viewModel, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        viewModel.setTitleBilling(titleFormFieldViewModelFactory.createWithDefaultOptions(form.field("titleBilling")));
    }

    protected void fillTitleShipping(final CheckoutAddressFormSettingsViewModel viewModel, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        viewModel.setTitleShipping(titleFormFieldViewModelFactory.createWithDefaultOptions(form.field("titleShipping")));
    }

    protected void fillCountriesBilling(final CheckoutAddressFormSettingsViewModel viewModel, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        viewModel.setCountriesBilling(countryFormFieldViewModelFactory.createWithDefaultOptions(form.field("countryBilling")));
    }

    protected void fillCountriesShipping(final CheckoutAddressFormSettingsViewModel viewModel, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        final FormFieldWithOptions<CountryCode> formFieldWithOptions = FormFieldWithOptions.of(form.field("countryShipping"), singletonList(country));
        viewModel.setCountriesShipping(countryFormFieldViewModelFactory.create(formFieldWithOptions));
    }
}
