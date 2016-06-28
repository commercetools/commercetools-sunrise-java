package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.CountryFormFieldBean;
import com.commercetools.sunrise.common.models.FormSelectableOptionBean;
import com.neovisionaries.i18n.CountryCode;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CountryFormFieldBeanFactory {

    @Inject
    private UserContext userContext;
    @Inject
    private ProjectContext projectContext;

    public CountryFormFieldBean create(final List<CountryCode> availableCountries, @Nullable final CountryCode selectedCountry) {
        final CountryFormFieldBean bean = new CountryFormFieldBean();
        fillList(bean, availableCountries, selectedCountry);
        return bean;
    }

    public CountryFormFieldBean createWithDefaultCountries(@Nullable final CountryCode selectedCountry) {
        return create(projectContext.countries(), selectedCountry);
    }

    protected void fillList(final CountryFormFieldBean bean, final List<CountryCode> availableCountries, final @Nullable CountryCode selectedCountry) {
        bean.setList(availableCountries.stream()
                .map(countryOption -> countryToSelectableData(countryOption, selectedCountry))
                .collect(toList()));
    }

    protected FormSelectableOptionBean countryToSelectableData(final CountryCode country, final @Nullable CountryCode selectedCountry) {
        final FormSelectableOptionBean bean = new FormSelectableOptionBean();
        bean.setLabel(country.toLocale().getDisplayCountry(userContext.locale()));
        bean.setValue(country.getAlpha2());
        if (country.equals(selectedCountry)) {
            bean.setSelected(true);
        }
        return bean;
    }

}
