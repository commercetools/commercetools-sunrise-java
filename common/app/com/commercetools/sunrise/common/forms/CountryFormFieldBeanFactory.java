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
        final String selectedCountryCode = getSelectedCountry(form, fieldName);
        bean.setList(availableCountries.stream()
                .map(countryOption -> createFormSelectableOption(countryOption, selectedCountryCode))
                .collect(toList()));
    }

    protected FormSelectableOptionBean createFormSelectableOption(final CountryCode country, final @Nullable String selectedCountryCode) {
        final FormSelectableOptionBean bean = new FormSelectableOptionBean();
        initializeFormSelectableOption(bean, country, selectedCountryCode);
        return bean;
    }

    protected final void initializeFormSelectableOption(final FormSelectableOptionBean bean, final CountryCode country, final @Nullable String selectedCountryCode) {
        final String countryCode = country.getAlpha2();
        bean.setLabel(country.toLocale().getDisplayCountry(userContext.locale()));
        bean.setValue(countryCode);
        bean.setSelected(countryCode.equals(selectedCountryCode));
    }

    @Nullable
    protected String getSelectedCountry(final Form<?> form, final String fieldName) {
        return form.field(fieldName).value();
    }

    protected List<CountryCode> getDefaultCountries() {
        return projectContext.countries();
    }
}
