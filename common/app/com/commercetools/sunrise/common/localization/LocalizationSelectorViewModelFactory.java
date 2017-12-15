package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.framework.localization.Countries;
import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.forms.countries.CountryFormSelectableOptionViewModel;
import com.commercetools.sunrise.framework.viewmodels.forms.countries.CountryFormSelectableOptionViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.forms.languages.LanguageFormSelectableOptionViewModel;
import com.commercetools.sunrise.framework.viewmodels.forms.languages.LanguageFormSelectableOptionViewModelFactory;
import com.neovisionaries.i18n.CountryCode;
import play.i18n.Lang;
import play.i18n.Langs;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocalizationSelectorViewModelFactory extends SimpleViewModelFactory<LocalizationSelectorViewModel, Void> {

    private final Locale locale;
    private final CountryCode country;
    private final Langs langs;
    private final Countries countries;
    private final CountryFormSelectableOptionViewModelFactory countryFormSelectableOptionViewModelFactory;
    private final LanguageFormSelectableOptionViewModelFactory languageFormSelectableOptionViewModelFactory;

    @Inject
    public LocalizationSelectorViewModelFactory(final Locale locale, final CountryCode country, final Langs langs, final Countries countries,
                                                final CountryFormSelectableOptionViewModelFactory countryFormSelectableOptionViewModelFactory,
                                                final LanguageFormSelectableOptionViewModelFactory languageFormSelectableOptionViewModelFactory) {
        this.locale = locale;
        this.country = country;
        this.langs = langs;
        this.countries = countries;
        this.countryFormSelectableOptionViewModelFactory = countryFormSelectableOptionViewModelFactory;
        this.languageFormSelectableOptionViewModelFactory = languageFormSelectableOptionViewModelFactory;
    }

    protected final Locale getLocale() {
        return locale;
    }

    protected final CountryCode getCountry() {
        return country;
    }

    protected final Langs getLangs() {
        return langs;
    }

    protected final Countries getCountries() {
        return countries;
    }

    protected final CountryFormSelectableOptionViewModelFactory getCountryFormSelectableOptionViewModelFactory() {
        return countryFormSelectableOptionViewModelFactory;
    }

    protected final LanguageFormSelectableOptionViewModelFactory getLanguageFormSelectableOptionViewModelFactory() {
        return languageFormSelectableOptionViewModelFactory;
    }

    @Override
    protected LocalizationSelectorViewModel newViewModelInstance(final Void input) {
        return new LocalizationSelectorViewModel();
    }

    @Override
    public final LocalizationSelectorViewModel create(final Void input) {
        return super.create(input);
    }

    public final LocalizationSelectorViewModel create() {
        return super.create(null);
    }

    @Override
    protected final void initialize(final LocalizationSelectorViewModel viewModel, final Void input) {
        fillCountry(viewModel);
        fillLanguage(viewModel);
    }

    protected void fillCountry(final LocalizationSelectorViewModel viewModel) {
        final List<CountryFormSelectableOptionViewModel> options = new ArrayList<>();
        final List<CountryCode> availables = countries.availables();
        if (availables.size() > 1) {
            availables.forEach(country ->
                    options.add(countryFormSelectableOptionViewModelFactory.create(country, this.country)));
        }
        viewModel.setCountry(options);
    }

    protected void fillLanguage(final LocalizationSelectorViewModel viewModel) {
        final List<LanguageFormSelectableOptionViewModel> options = new ArrayList<>();
        final List<Lang> availables = langs.availables();
        if (availables.size() > 1) {
            availables.forEach(lang ->
                    options.add(languageFormSelectableOptionViewModelFactory.create(lang.toLocale(), this.locale)));
        }
        viewModel.setLanguage(options);
    }
}
