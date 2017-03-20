package com.commercetools.sunrise.framework.viewmodels.forms.countries;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.viewmodels.forms.SelectableViewModelFactory;
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

    protected final Locale getLocale() {
        return locale;
    }

    @Override
    protected CountryFormSelectableOptionViewModel newViewModelInstance(final CountryCode option, @Nullable final CountryCode selectedValue) {
        return new CountryFormSelectableOptionViewModel();
    }

    @Override
    public final CountryFormSelectableOptionViewModel create(final CountryCode option, @Nullable final CountryCode selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final CountryFormSelectableOptionViewModel viewModel, final CountryCode option, @Nullable final CountryCode selectedValue) {
        fillLabel(viewModel, option, selectedValue);
        fillValue(viewModel, option, selectedValue);
        fillSelected(viewModel, option, selectedValue);
    }

    protected void fillLabel(final CountryFormSelectableOptionViewModel viewModel, final CountryCode option, @Nullable final CountryCode selectedValue) {
        viewModel.setLabel(option.toLocale().getDisplayCountry(locale));
    }

    protected void fillValue(final CountryFormSelectableOptionViewModel viewModel, final CountryCode option, @Nullable final CountryCode selectedValue) {
        viewModel.setValue(option.getAlpha2());
    }

    protected void fillSelected(final CountryFormSelectableOptionViewModel viewModel, final CountryCode option, @Nullable final CountryCode selectedValue) {
        viewModel.setSelected(option.equals(selectedValue));
    }

}
