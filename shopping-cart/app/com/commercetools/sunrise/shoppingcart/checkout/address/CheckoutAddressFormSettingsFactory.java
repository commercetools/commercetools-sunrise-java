package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.forms.CountryFormFieldBeanFactory;
import com.commercetools.sunrise.common.forms.TitleFormFieldBeanFactory;
import com.commercetools.sunrise.common.models.TitleFormFieldBean;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Base;
import play.data.Form;

import javax.inject.Inject;
import java.util.List;

import static com.neovisionaries.i18n.CountryCode.UNDEFINED;
import static java.util.Collections.singletonList;

public class CheckoutAddressFormSettingsFactory extends Base {

    @Inject
    private UserContext userContext;
    @Inject
    private CountryFormFieldBeanFactory countryFormFieldBeanFactory;
    @Inject
    private TitleFormFieldBeanFactory titleFormFieldBeanFactory;

    public CheckoutAddressFormSettings create(final Form<?> form) {
        final CheckoutAddressFormSettings bean = createAddressFormSettings();
        final CountryCode country = userContext.country();
        final List<CountryCode> shippingCountries = singletonList(country);
        bean.setCountriesShipping(countryFormFieldBeanFactory.create(shippingCountries, country));

        final CountryCode selectedCountryBilling = CountryCode.getByCode(form.field("countryBilling").valueOr(UNDEFINED.getAlpha3()));
        bean.setCountriesBilling(countryFormFieldBeanFactory.createWithDefaultCountries(selectedCountryBilling));

        bean.setTitleShipping(createTitleBean(form, "titleShipping"));

        bean.setTitleBilling(createTitleBean(form, "titleBilling"));

        return bean;
    }

    protected TitleFormFieldBean createTitleBean(final Form<?> form, final String fieldName) {
        return titleFormFieldBeanFactory.createWithDefaultTitles(form.field(fieldName).valueOr(null));
    }

    protected CheckoutAddressFormSettings createAddressFormSettings() {
        return new CheckoutAddressFormSettings();
    }
}
