package com.commercetools.sunrise.common.models;

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

    @Override
    protected LanguageFormSelectableOptionViewModel getViewModelInstance() {
        return new LanguageFormSelectableOptionViewModel();
    }

    @Override
    public final LanguageFormSelectableOptionViewModel create(final Locale option, @Nullable final Locale selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final LanguageFormSelectableOptionViewModel model, final Locale option, @Nullable final Locale selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
    }

    protected void fillLabel(final LanguageFormSelectableOptionViewModel model, final Locale option, @Nullable final Locale selectedLocale) {
        model.setLabel(option.getDisplayName(locale));
    }

    protected void fillValue(final LanguageFormSelectableOptionViewModel model, final Locale option, @Nullable final Locale selectedLocale) {
        model.setValue(option.getLanguage());
    }

    protected void fillSelected(final LanguageFormSelectableOptionViewModel model, final Locale option, @Nullable final Locale selectedLocale) {
        model.setSelected(option.equals(selectedLocale));
    }

}
