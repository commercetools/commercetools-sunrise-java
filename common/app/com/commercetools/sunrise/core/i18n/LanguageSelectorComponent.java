package com.commercetools.sunrise.core.i18n;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.SelectOption;
import play.i18n.Langs;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

public final class LanguageSelectorComponent implements ControllerComponent, PageDataHook {

    private final Langs langs;
    private final Provider<Locale> localeProvider;

    @Inject
    LanguageSelectorComponent(final Langs langs, final Provider<Locale> localeProvider) {
        this.langs = langs;
        this.localeProvider = localeProvider;
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        return completedFuture(pageData.put("languageOptions", options()));
    }

    private List<SelectOption> options() {
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
