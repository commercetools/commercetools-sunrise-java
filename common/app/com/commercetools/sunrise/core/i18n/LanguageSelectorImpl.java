package com.commercetools.sunrise.core.i18n;

import com.commercetools.sunrise.core.viewmodels.ViewModelFactory;
import com.commercetools.sunrise.models.SelectOption;
import play.i18n.Langs;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

@Singleton
final class LanguageSelectorImpl extends ViewModelFactory implements LanguageSelector {

    private final Langs langs;
    private final Provider<Locale> localeProvider;

    @Inject
    LanguageSelectorImpl(final Langs langs, final Provider<Locale> localeProvider) {
        this.langs = langs;
        this.localeProvider = localeProvider;
    }

    @Override
    public List<SelectOption> options() {
        final Locale selectedLocale = localeProvider.get();
        return langs.availables().stream()
                .map(lang -> createOption(lang.toLocale(), selectedLocale))
                .collect(toList());
    }

    private SelectOption createOption(final Locale locale, final Locale selectedlocale) {
        final SelectOption option = new SelectOption();
        option.setLabel(locale.getDisplayName(locale));
        option.setValue(locale.toLanguageTag());
        option.setSelected(locale.equals(selectedlocale));
        return option;
    }
}
