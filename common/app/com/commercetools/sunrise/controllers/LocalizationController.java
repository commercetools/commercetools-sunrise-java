package com.commercetools.sunrise.controllers;

import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.localization.CountryInSession;
import com.neovisionaries.i18n.CountryCode;
import play.i18n.Lang;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Locale;

@Singleton
public final class LocalizationController extends SunriseController {

    private final CountryInSession countryInSession;

    @Inject
    private LocalizationController(final CountryInSession countryInSession) {
        this.countryInSession = countryInSession;
    }

    @EnableHooks
    public Result changeLanguage(final String language) {
        final Lang lang = new Lang(Locale.forLanguageTag(language));
        if (!changeLang(lang)) {
            LOGGER.debug("Could not change language to '{}'", lang);
        }
        return redirect(request().getHeader(Http.HeaderNames.REFERER));
    }

    @EnableHooks
    public Result changeCountry(final String country) {
        final CountryCode countryCode = CountryCode.getByCode(country);
        countryInSession.store(countryCode);
        return redirect(request().getHeader(Http.HeaderNames.REFERER));
    }
}
