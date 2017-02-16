package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.injection.RequestScoped;
import com.commercetools.sunrise.common.models.SelectableViewModelFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class LanguageFormSelectableOptionBeanFactory extends SelectableViewModelFactory<LanguageFormSelectableOptionBean, Locale, Locale> {

    private final Locale locale;

    @Inject
    public LanguageFormSelectableOptionBeanFactory(final Locale locale) {
        this.locale = locale;
    }

    @Override
    protected LanguageFormSelectableOptionBean getViewModelInstance() {
        return new LanguageFormSelectableOptionBean();
    }

    @Override
    public final LanguageFormSelectableOptionBean create(final Locale option, @Nullable final Locale selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final LanguageFormSelectableOptionBean model, final Locale option, @Nullable final Locale selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
    }

    protected void fillLabel(final LanguageFormSelectableOptionBean model, final Locale option, @Nullable final Locale selectedLocale) {
        model.setLabel(option.getDisplayName(locale));
    }

    protected void fillValue(final LanguageFormSelectableOptionBean model, final Locale option, @Nullable final Locale selectedLocale) {
        model.setValue(option.getLanguage());
    }

    protected void fillSelected(final LanguageFormSelectableOptionBean model, final Locale option, @Nullable final Locale selectedLocale) {
        model.setSelected(option.equals(selectedLocale));
    }

}
