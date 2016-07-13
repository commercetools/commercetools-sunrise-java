package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.models.FormSelectableOptionBean;
import io.sphere.sdk.models.Base;

import java.util.List;

public class LocalizationSelectorBean extends Base {

    private List<FormSelectableOptionBean> language;
    private List<FormSelectableOptionBean> country;

    public LocalizationSelectorBean() {
    }

    public List<FormSelectableOptionBean> getLanguage() {
        return language;
    }

    public void setLanguage(final List<FormSelectableOptionBean> language) {
        this.language = language;
    }

    public List<FormSelectableOptionBean> getCountry() {
        return country;
    }

    public void setCountry(final List<FormSelectableOptionBean> country) {
        this.country = country;
    }
}
