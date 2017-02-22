package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.models.LanguageFormSelectableOptionViewModel;
import com.commercetools.sunrise.common.models.LanguageFormSelectableOptionViewModelFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.models.addresses.CountryFormSelectableOptionViewModel;
import com.commercetools.sunrise.contexts.ProjectContext;
import com.commercetools.sunrise.common.models.addresses.CountryFormSelectableOptionViewModelFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.neovisionaries.i18n.CountryCode;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequestScoped
public class LocalizationSelectorViewModelFactory extends ViewModelFactory<LocalizationSelectorViewModel, Void> {

    private final Locale locale;
    private final CountryCode country;
    private final ProjectContext projectContext;
    private final CountryFormSelectableOptionViewModelFactory countryFormSelectableOptionViewModelFactory;
    private final LanguageFormSelectableOptionViewModelFactory languageFormSelectableOptionViewModelFactory;

    @Inject
    public LocalizationSelectorViewModelFactory(final Locale locale, final CountryCode country, final ProjectContext projectContext,
                                                final CountryFormSelectableOptionViewModelFactory countryFormSelectableOptionViewModelFactory,
                                                final LanguageFormSelectableOptionViewModelFactory languageFormSelectableOptionViewModelFactory) {
        this.locale = locale;
        this.country = country;
        this.projectContext = projectContext;
        this.countryFormSelectableOptionViewModelFactory = countryFormSelectableOptionViewModelFactory;
        this.languageFormSelectableOptionViewModelFactory = languageFormSelectableOptionViewModelFactory;
    }

    @Override
    protected LocalizationSelectorViewModel getViewModelInstance() {
        return new LocalizationSelectorViewModel();
    }

    @Override
    public final LocalizationSelectorViewModel create(final Void data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final LocalizationSelectorViewModel model, final Void data) {
        fillCountry(model);
        fillLanguage(model);
    }

    protected void fillCountry(final LocalizationSelectorViewModel model) {
        final List<CountryFormSelectableOptionViewModel> options = new ArrayList<>();
        final List<CountryCode> countries = projectContext.countries();
        if (countries.size() > 1) {
            countries.forEach(country ->
                    options.add(countryFormSelectableOptionViewModelFactory.create(country, this.country)));
        }
        model.setCountry(options);
    }

    protected void fillLanguage(final LocalizationSelectorViewModel model) {
        final List<LanguageFormSelectableOptionViewModel> options = new ArrayList<>();
        final List<Locale> locales = projectContext.locales();
        if (locales.size() > 1) {
            locales.forEach(locale ->
                    options.add(languageFormSelectableOptionViewModelFactory.create(locale, this.locale)));
        }
        model.setLanguage(options);
    }
}
