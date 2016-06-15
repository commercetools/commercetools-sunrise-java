package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.hooks.SunrisePageDataHook;
import com.commercetools.sunrise.common.models.FormSelectableOptionBean;
import com.commercetools.sunrise.common.pages.SunrisePageData;
import com.commercetools.sunrise.framework.ControllerComponent;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class LocationSelectorControllerComponent implements ControllerComponent, SunrisePageDataHook {

    @Inject
    private UserContext userContext;
    @Inject
    private ProjectContext projectContext;

    @Override
    public void acceptSunrisePageData(final SunrisePageData sunrisePageData) {
        final LocalizationSelectorBean bean = createLocalizationSelector();
        sunrisePageData.getHeader().setLocation(bean);
    }

    private LocalizationSelectorBean createLocalizationSelector() {
        final LocalizationSelectorBean bean = new LocalizationSelectorBean();
        bean.setCountry(createCountryFormOptions());
        bean.setLanguage(createLanguageFormOptions());
        return bean;
    }

    private List<FormSelectableOptionBean> createCountryFormOptions() {
        final List<FormSelectableOptionBean> countrySelector = projectContext.countries().stream()
                .map(countryCode -> {
                    final FormSelectableOptionBean bean = new FormSelectableOptionBean();
                    bean.setLabel(countryCode.getName());
                    bean.setValue(countryCode.getAlpha2());
                    bean.setSelected(countryCode.equals(userContext.country()));
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
                    bean.setSelected(locale.equals(userContext.locale()));
                    return bean;
                }).collect(toList());
        return (localeSelector.size() > 1) ? localeSelector : emptyList();
    }
}
