package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.models.CommonViewModelFactory;
import com.commercetools.sunrise.common.models.FormSelectableOptionBean;
import com.neovisionaries.i18n.CountryCode;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class LocalizationSelectorBeanFactory extends CommonViewModelFactory<LocalizationSelectorBean> {

    private final Locale locale;
    private final CountryCode country;
    private final ProjectContext projectContext;

    @Inject
    public LocalizationSelectorBeanFactory(final Locale locale, final CountryCode country, final ProjectContext projectContext) {
        this.locale = locale;
        this.country = country;
        this.projectContext = projectContext;
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
        model.setCountry(createCountryFormOptions());
    }

    protected void fillLanguage(final LocalizationSelectorBean model) {
        model.setLanguage(createLanguageFormOptions());
    }

    private List<FormSelectableOptionBean> createCountryFormOptions() {
        final List<FormSelectableOptionBean> countrySelector = projectContext.countries().stream()
                .map(countryCode -> {
                    final FormSelectableOptionBean bean = new FormSelectableOptionBean();
                    bean.setLabel(countryCode.getName());
                    bean.setValue(countryCode.getAlpha2());
                    bean.setSelected(countryCode.equals(this.country));
                    return bean;
                }).collect(toList());
        return (countrySelector.size() > 1) ? countrySelector : emptyList();
    }

    private List<FormSelectableOptionBean> createLanguageFormOptions() {
        final List<FormSelectableOptionBean> localeSelector = projectContext.locales().stream()
                .map(locale -> {
                    final FormSelectableOptionBean bean = new FormSelectableOptionBean();
                    bean.setLabel(locale.getDisplayName());
                    bean.setValue(locale.getLanguage());
                    bean.setSelected(locale.equals(this.locale));
                    return bean;
                }).collect(toList());
        return (localeSelector.size() > 1) ? localeSelector : emptyList();
    }
}
