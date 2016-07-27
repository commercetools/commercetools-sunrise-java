package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.FormSelectableOptionBean;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Base;
import play.data.Form;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CountryFormFieldBeanFactory extends Base {

    @Inject
    private UserContext userContext;
    @Inject
    private ProjectContext projectContext;

    public CountryFormFieldBean create(final Form<?> form, final String fieldName, final List<CountryCode> availableCountries) {
        final CountryFormFieldBean bean = new CountryFormFieldBean();
        initialize(bean, form, fieldName, availableCountries);
        return bean;
    }

    public CountryFormFieldBean createWithDefaultCountries(final Form<?> form, final String fieldName) {
        return create(form, fieldName, getDefaultCountries());
    }

    protected final void initialize(final CountryFormFieldBean bean, final Form<?> form, final String fieldName, final List<CountryCode> availableCountries) {
        fillList(bean, form, fieldName, availableCountries);
    }

    protected void fillList(final CountryFormFieldBean bean, final Form<?> form, final String fieldName, final List<CountryCode> availableCountries) {
        final String selectedCountryCode = form.field(fieldName).valueOr(null);
        bean.setList(availableCountries.stream()
                .map(countryOption -> countryToSelectableData(countryOption, selectedCountryCode))
                .collect(toList()));
    }

    protected FormSelectableOptionBean countryToSelectableData(final CountryCode country, final @Nullable String selectedCountryCode) {
        final FormSelectableOptionBean bean = new FormSelectableOptionBean();
        bean.setLabel(country.toLocale().getDisplayCountry(userContext.locale()));
        final String countryCode = country.getAlpha2();
        bean.setValue(countryCode);
        if (countryCode.equals(selectedCountryCode)) {
            bean.setSelected(true);
        }
        return bean;
    }

    protected List<CountryCode> getDefaultCountries() {
        return projectContext.countries();
    }
}
