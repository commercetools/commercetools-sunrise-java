package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.SelectableOptionViewModelFactory;
import com.neovisionaries.i18n.CountryCode;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class CountryFormSelectableOptionBeanFactory extends SelectableOptionViewModelFactory<CountryFormSelectableOptionBean, CountryCode> {

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
    public final CountryFormSelectableOptionBean create(final CountryCode option, @Nullable final String selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final CountryFormSelectableOptionBean model, final CountryCode option, @Nullable final String selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
    }

    protected void fillLabel(final CountryFormSelectableOptionBean model, final CountryCode option, @Nullable final String selectedValue) {
        model.setLabel(option.toLocale().getDisplayCountry(locale));
    }

    protected void fillValue(final CountryFormSelectableOptionBean model, final CountryCode option, @Nullable final String selectedValue) {
        model.setValue(option.getAlpha2());
    }

    protected void fillSelected(final CountryFormSelectableOptionBean model, final CountryCode option, @Nullable final String selectedValue) {
        model.setSelected(option.getAlpha2().equals(selectedValue));
    }

}
