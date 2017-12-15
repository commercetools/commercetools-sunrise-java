package com.commercetools.sunrise.framework.i18n;

import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.i18n.Lang;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.test.WithApplication;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Locale.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static play.mvc.Http.HeaderNames.ACCEPT_LANGUAGE;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.invokeWithContext;

public class LocaleFromRequestProviderTest extends WithApplication {

    private LocaleFromRequestProvider localeProvider;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .configure("play.i18n.langs", asList("en", "de", "en-US"))
                .build();
    }

    @Before
    public void setUp() throws Exception {
        localeProvider = app.injector().instanceOf(LocaleFromRequestProvider.class);
    }

    @Test
    public void addsAcceptedLanguages() throws Exception {
        final List<Locale> acceptedLangs = asList(GERMAN, US);
        testWithHttpContext(ENGLISH, acceptedLangs, () ->
                assertThat(localeProvider.get()).isEqualTo(ENGLISH));
    }

    @Test
    public void discardsNotSupportedByProject() throws Exception {
        final List<Locale> acceptedLangs = asList(ITALIAN, US);
        testWithHttpContext(null, acceptedLangs, () ->
                assertThat(localeProvider.get()).isEqualTo(US));
    }

    @Test
    public void ignoresEmptyAcceptedLanguages() throws Exception {
        final List<Locale> acceptedLangs = emptyList();
        testWithHttpContext(null, acceptedLangs, () ->
                assertThat(localeProvider.get()).isEqualTo(ENGLISH));
    }

    @Test
    public void ignoresNotSupportedCurrentLanguage() throws Exception {
        final List<Locale> acceptedLangs = asList(GERMAN, US);
        testWithHttpContext(ITALIAN, acceptedLangs, () ->
                assertThat(localeProvider.get()).isEqualTo(GERMAN));
    }

    @Test
    public void doesNotFailOnHttpContextNotAvailable() throws Exception {
        assertThat(localeProvider.get()).isEqualTo(ENGLISH);
    }

    private void testWithHttpContext(@Nullable final Locale currentLang, final List<Locale> acceptedLangs, final Runnable test) {
        final Http.RequestBuilder requestWithAcceptLanguage = fakeRequest()
                .header(ACCEPT_LANGUAGE, toAcceptLanguageHeader(acceptedLangs));
        invokeWithContext(requestWithAcceptLanguage, () -> {
            if (currentLang != null) {
                Http.Context.current().changeLang(Lang.forCode(currentLang.toLanguageTag()));
            }
            test.run();
            return null;
        });
    }

    private static String toAcceptLanguageHeader(final List<Locale> acceptedLangs) {
        return acceptedLangs.stream()
                    .map(Locale::toLanguageTag)
                    .collect(Collectors.joining(","));
    }
}