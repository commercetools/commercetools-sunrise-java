package com.commercetools.sunrise.common.localization.changelanguage;

import com.commercetools.sunrise.sessions.language.LanguageInSession;

import javax.inject.Inject;

public final class DefaultChangeLanguageControllerAction implements ChangeLanguageControllerAction {

    private final LanguageInSession languageInSession;

    @Inject
    DefaultChangeLanguageControllerAction(final LanguageInSession languageInSession) {
        this.languageInSession = languageInSession;
    }

    @Override
    public void accept(final ChangeLanguageFormData formData) {
        languageInSession.store(formData.locale());
    }
}
