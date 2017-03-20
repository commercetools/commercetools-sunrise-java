package com.commercetools.sunrise.framework.viewmodels.forms.languages;

import com.commercetools.sunrise.framework.viewmodels.forms.SelectableViewModelFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class LanguageFormSelectableOptionViewModelFactory extends SelectableViewModelFactory<LanguageFormSelectableOptionViewModel, Locale, Locale> {

    private final Locale locale;

    @Inject
    public LanguageFormSelectableOptionViewModelFactory(final Locale locale) {
        this.locale = locale;
    }

    protected final Locale getLocale() {
        return locale;
    }

    @Override
    protected LanguageFormSelectableOptionViewModel newViewModelInstance(final Locale option, @Nullable final Locale selectedValue) {
        return new LanguageFormSelectableOptionViewModel();
    }

    @Override
    public final LanguageFormSelectableOptionViewModel create(final Locale option, @Nullable final Locale selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final LanguageFormSelectableOptionViewModel viewModel, final Locale option, @Nullable final Locale selectedValue) {
        fillLabel(viewModel, option, selectedValue);
        fillValue(viewModel, option, selectedValue);
        fillSelected(viewModel, option, selectedValue);
    }

    protected void fillLabel(final LanguageFormSelectableOptionViewModel viewModel, final Locale option, @Nullable final Locale selectedLocale) {
        viewModel.setLabel(option.getDisplayName(locale));
    }

    protected void fillValue(final LanguageFormSelectableOptionViewModel viewModel, final Locale option, @Nullable final Locale selectedLocale) {
        viewModel.setValue(option.getLanguage());
    }

    protected void fillSelected(final LanguageFormSelectableOptionViewModel viewModel, final Locale option, @Nullable final Locale selectedLocale) {
        viewModel.setSelected(option.equals(selectedLocale));
    }

}
