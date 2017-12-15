package com.commercetools.sunrise.common.localization.changelanguage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.i18n.Lang;
import play.mvc.Http;

import java.util.Locale;
import java.util.Optional;

public final class DefaultChangeLanguageControllerAction implements ChangeLanguageControllerAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeLanguageControllerAction.class);

    @Override
    public void accept(final ChangeLanguageFormData formData) {
        Optional.ofNullable(Http.Context.current.get())
                .ifPresent(ctx -> changeLanguage(ctx, formData.locale()));
    }

    private void changeLanguage(final Http.Context ctx, final Locale locale) {
        final Lang lang = new Lang(locale);
        if (!ctx.changeLang(lang)) {
            LOGGER.debug("Could not change language to '{}'", lang);
        }
    }
}
