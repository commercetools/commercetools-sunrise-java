package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.FormSelectableOptionBean;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.neovisionaries.i18n.CountryCode;
import play.data.Form;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class CountryFormFieldBeanFactory extends ViewModelFactory {

    private final Locale locale;
    private final List<CountryCode> defaultCountries;

    @Inject
    public CountryFormFieldBeanFactory(final Locale locale, final ProjectContext projectContext) {
        this.locale = locale;
        this.defaultCountries = projectContext.countries();
    }

    public CountryFormFieldBean create(final Form<?> form, final String fieldName, final List<CountryCode> availableCountries) {
        final CountryFormFieldBean bean = new CountryFormFieldBean();
        initialize(bean, form, fieldName, availableCountries);
        return bean;
    }

    public CountryFormFieldBean createWithDefaultCountries(final Form<?> form, final String fieldName) {
        return create(form, fieldName, defaultCountries);
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

    @Nullable
    private String getSelectedCountry(final Form<?> form, final String fieldName) {
        return form.field(fieldName).value();
    }

    private FormSelectableOptionBean createFormSelectableOption(final CountryCode country, final @Nullable String selectedCountryCode) {
        final FormSelectableOptionBean bean = new FormSelectableOptionBean();
        final String countryCode = country.getAlpha2();
        bean.setLabel(country.toLocale().getDisplayCountry(locale));
        bean.setValue(countryCode);
        bean.setSelected(countryCode.equals(selectedCountryCode));
        return bean;
    }
}
