package com.commercetools.sunrise.framework.i18n;

import com.commercetools.sunrise.framework.i18n.api.SunriseLangs;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;
import java.util.Locale;

public final class I18nModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(play.api.i18n.Langs.class)
                .to(SunriseLangs.class)
                .in(Singleton.class);

        bind(play.i18n.MessagesApi.class)
                .to(com.commercetools.sunrise.framework.i18n.SunriseMessagesApi.class)
                .in(Singleton.class);

        bind(play.api.i18n.MessagesApi.class)
                .to(com.commercetools.sunrise.framework.i18n.api.SunriseMessagesApi.class)
                .in(Singleton.class);

        bind(Locale.class).toProvider(LocaleFromRequestProvider.class);
    }
}
