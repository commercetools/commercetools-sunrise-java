package com.commercetools.sunrise.common.models.addresses;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.SelectableViewModelFactory;
import com.neovisionaries.i18n.CountryCode;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class CountryFormSelectableOptionBeanFactory extends SelectableViewModelFactory<CountryFormSelectableOptionBean, CountryCode, CountryCode> {

    private final Locale locale;

    @Inject
    public CountryFormSelectableOptionBeanFactory(final Locale locale) {
        this.locale = locale;
    }

    @Override
    protected CountryFormSelectableOptionBean getViewModelInstance() {
        return new CountryFormSelectableOptionBean();
    }

    @Override
    public final CountryFormSelectableOptionBean create(final CountryCode option, @Nullable final CountryCode selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final CountryFormSelectableOptionBean model, final CountryCode option, @Nullable final CountryCode selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
    }

    protected void fillLabel(final CountryFormSelectableOptionBean model, final CountryCode option, @Nullable final CountryCode selectedValue) {
        model.setLabel(option.toLocale().getDisplayCountry(locale));
    }

    protected void fillValue(final CountryFormSelectableOptionBean model, final CountryCode option, @Nullable final CountryCode selectedValue) {
        model.setValue(option.getAlpha2());
    }

    protected void fillSelected(final CountryFormSelectableOptionBean model, final CountryCode option, @Nullable final CountryCode selectedValue) {
        model.setSelected(option.equals(selectedValue));
    }

}
