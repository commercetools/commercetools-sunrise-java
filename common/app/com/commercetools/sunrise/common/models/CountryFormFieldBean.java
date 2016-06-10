package com.commercetools.sunrise.common.models;

import com.neovisionaries.i18n.CountryCode;
import com.commercetools.sunrise.common.contexts.UserContext;

import javax.annotation.Nullable;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CountryFormFieldBean {

    private List<FormSelectableOptionBean> list;


    public CountryFormFieldBean() {
    }

    public CountryFormFieldBean(final List<CountryCode> availableCountries, @Nullable final CountryCode selectedCountry,
                                final UserContext userContext) {
        this.list = availableCountries.stream()
                .map(countryOption -> countryToSelectableData(countryOption, selectedCountry, userContext))
                .collect(toList());
    }

    public List<FormSelectableOptionBean> getList() {
        return list;
    }

    public void setList(final List<FormSelectableOptionBean> list) {
        this.list = list;
    }

    private FormSelectableOptionBean countryToSelectableData(final CountryCode country, final @Nullable CountryCode selectedCountry,
                                                             final UserContext userContext) {
        final FormSelectableOptionBean formSelectableOptionBean = new FormSelectableOptionBean();
        formSelectableOptionBean.setLabel(country.toLocale().getDisplayCountry(userContext.locale()));
        formSelectableOptionBean.setValue(country.getAlpha2());
        if (country.equals(selectedCountry)) {
            formSelectableOptionBean.setSelected(true);
        }
        return formSelectableOptionBean;
    }
}
