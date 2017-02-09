package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.forms.CountryFormSelectableOptionBean;
import io.sphere.sdk.models.Base;

import java.util.List;

public class LocalizationSelectorBean extends Base {

    private List<LanguageFormSelectableOptionBean> language;
    private List<CountryFormSelectableOptionBean> country;

    public LocalizationSelectorBean() {
    }

    public List<LanguageFormSelectableOptionBean> getLanguage() {
        return language;
    }

    public void setLanguage(final List<LanguageFormSelectableOptionBean> language) {
        this.language = language;
    }

    public List<CountryFormSelectableOptionBean> getCountry() {
        return country;
    }

    public void setCountry(final List<CountryFormSelectableOptionBean> country) {
        this.country = country;
    }
}
