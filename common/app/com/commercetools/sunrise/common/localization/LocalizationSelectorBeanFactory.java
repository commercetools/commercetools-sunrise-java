package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.forms.CountryFormSelectableOptionBean;
import com.commercetools.sunrise.common.forms.CountryFormSelectableOptionBeanFactory;
import com.commercetools.sunrise.common.models.CommonViewModelFactory;
import com.neovisionaries.i18n.CountryCode;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequestScoped
public class LocalizationSelectorBeanFactory extends CommonViewModelFactory<LocalizationSelectorBean> {

    private final Locale locale;
    private final CountryCode country;
    private final ProjectContext projectContext;
    private final CountryFormSelectableOptionBeanFactory countryFormSelectableOptionBeanFactory;
    private final LanguageFormSelectableOptionBeanFactory languageFormSelectableOptionBeanFactory;

    @Inject
    public LocalizationSelectorBeanFactory(final Locale locale, final CountryCode country, final ProjectContext projectContext,
                                           final CountryFormSelectableOptionBeanFactory countryFormSelectableOptionBeanFactory,
                                           final LanguageFormSelectableOptionBeanFactory languageFormSelectableOptionBeanFactory) {
        this.locale = locale;
        this.country = country;
        this.projectContext = projectContext;
        this.countryFormSelectableOptionBeanFactory = countryFormSelectableOptionBeanFactory;
        this.languageFormSelectableOptionBeanFactory = languageFormSelectableOptionBeanFactory;
    }

    @Override
    protected LocalizationSelectorBean getViewModelInstance() {
        return new LocalizationSelectorBean();
    }

    @Override
    public final LocalizationSelectorBean create() {
        return super.create();
    }

    @Override
    protected final void initialize(final LocalizationSelectorBean model) {
        fillCountry(model);
        fillLanguage(model);
    }

    protected void fillCountry(final LocalizationSelectorBean model) {
        final List<CountryFormSelectableOptionBean> options = new ArrayList<>();
        final List<CountryCode> countries = projectContext.countries();
        if (countries.size() > 1) {
            countries.forEach(country ->
                    options.add(countryFormSelectableOptionBeanFactory.create(country, this.country)));
        }
        model.setCountry(options);
    }

    protected void fillLanguage(final LocalizationSelectorBean model) {
        final List<LanguageFormSelectableOptionBean> options = new ArrayList<>();
        final List<Locale> locales = projectContext.locales();
        if (locales.size() > 1) {
            locales.forEach(locale ->
                    options.add(languageFormSelectableOptionBeanFactory.create(locale, this.locale)));
        }
        model.setLanguage(options);
    }
}
