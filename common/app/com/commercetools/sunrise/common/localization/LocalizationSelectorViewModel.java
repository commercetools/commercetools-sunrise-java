package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.framework.viewmodels.forms.languages.LanguageFormSelectableOptionViewModel;
import com.commercetools.sunrise.framework.viewmodels.ViewModel;
import com.commercetools.sunrise.framework.viewmodels.forms.countries.CountryFormSelectableOptionViewModel;

import java.util.List;

public class LocalizationSelectorViewModel extends ViewModel {

    private List<LanguageFormSelectableOptionViewModel> language;
    private List<CountryFormSelectableOptionViewModel> country;

    public LocalizationSelectorViewModel() {
    }

    public List<LanguageFormSelectableOptionViewModel> getLanguage() {
        return language;
    }

    public void setLanguage(final List<LanguageFormSelectableOptionViewModel> language) {
        this.language = language;
    }

    public List<CountryFormSelectableOptionViewModel> getCountry() {
        return country;
    }

    public void setCountry(final List<CountryFormSelectableOptionViewModel> country) {
        this.country = country;
    }
}
