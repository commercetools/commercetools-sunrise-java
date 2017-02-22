package com.commercetools.sunrise.common.models.addresses;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.SelectableViewModelFactory;
import com.neovisionaries.i18n.CountryCode;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class CountryFormSelectableOptionViewModelFactory extends SelectableViewModelFactory<CountryFormSelectableOptionViewModel, CountryCode, CountryCode> {

    private final Locale locale;

    @Inject
    public CountryFormSelectableOptionViewModelFactory(final Locale locale) {
        this.locale = locale;
    }

    @Override
    protected CountryFormSelectableOptionViewModel getViewModelInstance() {
        return new CountryFormSelectableOptionViewModel();
    }

    @Override
    public final CountryFormSelectableOptionViewModel create(final CountryCode option, @Nullable final CountryCode selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final CountryFormSelectableOptionViewModel model, final CountryCode option, @Nullable final CountryCode selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
    }

    protected void fillLabel(final CountryFormSelectableOptionViewModel model, final CountryCode option, @Nullable final CountryCode selectedValue) {
        model.setLabel(option.toLocale().getDisplayCountry(locale));
    }

    protected void fillValue(final CountryFormSelectableOptionViewModel model, final CountryCode option, @Nullable final CountryCode selectedValue) {
        model.setValue(option.getAlpha2());
    }

    protected void fillSelected(final CountryFormSelectableOptionViewModel model, final CountryCode option, @Nullable final CountryCode selectedValue) {
        model.setSelected(option.equals(selectedValue));
    }

}
