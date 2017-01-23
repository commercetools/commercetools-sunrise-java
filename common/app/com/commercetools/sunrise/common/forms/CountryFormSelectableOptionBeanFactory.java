package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.FormFieldFactory;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class CountryFormSelectableOptionBeanFactory extends FormFieldFactory<CountryFormSelectableOptionBean, CountryFormSelectableOptionBeanFactory.Data> {

    private final Locale locale;

    @Inject
    public CountryFormSelectableOptionBeanFactory(final Locale locale) {
        this.locale = locale;
    }

    public final CountryFormSelectableOptionBean create(final CountryCode country, @Nullable final String selectedCountryCode) {
        final Data data = new Data(country, selectedCountryCode);
        return initializedViewModel(data);
    }

    @Override
    protected CountryFormSelectableOptionBean getViewModelInstance() {
        return new CountryFormSelectableOptionBean();
    }

    @Override
    protected final void initialize(final CountryFormSelectableOptionBean bean, final Data data) {
        fillLabel(bean, data);
        fillValue(bean, data);
        fillSelected(bean, data);
    }

    protected void fillSelected(final CountryFormSelectableOptionBean bean, final Data data) {
        bean.setSelected(data.country.getAlpha2().equals(data.selectedCountryCode));
    }

    protected void fillValue(final CountryFormSelectableOptionBean bean, final Data data) {
        bean.setValue(data.country.getAlpha2());
    }

    protected void fillLabel(final CountryFormSelectableOptionBean bean, final Data data) {
        bean.setLabel(data.country.toLocale().getDisplayCountry(locale));
    }

    protected static final class Data extends Base {

        public final CountryCode country;
        @Nullable
        public final String selectedCountryCode;

        public Data(final CountryCode country, final String selectedCountryCode) {
            this.country = country;
            this.selectedCountryCode = selectedCountryCode;
        }
    }
}
