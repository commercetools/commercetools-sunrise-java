package com.commercetools.sunrise.core.sessions;

import org.junit.Test;
import play.mvc.Http;
import play.test.WithApplication;

import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.invokeWithContext;

public class AbstractCookieStoringStrategyTest extends WithApplication {

    private static final String SOME_KEY = "some-key";
    private static final String SOME_VALUE = "some-value";

    @Test
    public void findsWhenInCookie() throws Exception {
        invokeWithContext(fakeRequest().cookie(Http.Cookie.builder(SOME_KEY, SOME_VALUE).build()), () ->
                assertThat(cookieStrategy().findValue(SOME_KEY)).contains(SOME_VALUE));
    }

    @Test
    public void doesNotFindWhenNotInCookie() throws Exception {
        invokeWithContext(fakeRequest(), () ->
                assertThat(cookieStrategy().findValue(SOME_KEY)).isEmpty());
    }

    @Test
    public void createsWhenNotFound() throws Exception {
        invokeWithContext(fakeRequest(), () -> {
            final StoringStrategy strategy = cookieStrategy();
            assertThat(strategy.findValue(SOME_KEY)).isEmpty();
            strategy.overwriteValue(SOME_KEY, SOME_VALUE);
            assertThat(strategy.findValue(SOME_KEY)).contains(SOME_VALUE);
            return strategy;
        });
    }

    @Test
    public void replacesValue() throws Exception {
        invokeWithContext(fakeRequest().cookie(Http.Cookie.builder(SOME_KEY, SOME_VALUE).build()), () -> {
            final StoringStrategy strategy = cookieStrategy();
            assertThat(strategy.findValue(SOME_KEY)).contains(SOME_VALUE);
            strategy.overwriteValue(SOME_KEY, "some-other-value");
            assertThat(strategy.findValue(SOME_KEY)).contains("some-other-value");
            return strategy;
        });
    }

    @Test
    public void removesKeyOnNullValue() throws Exception {
        invokeWithContext(fakeRequest().cookie(Http.Cookie.builder(SOME_KEY, SOME_VALUE).build()), () -> {
            final StoringStrategy strategy = cookieStrategy();
            assertThat(strategy.findValue(SOME_KEY)).contains(SOME_VALUE);
            strategy.overwriteValue(SOME_KEY, null);
            assertThat(strategy.findValue(SOME_KEY)).isEmpty();
            return strategy;
        });
    }

    @Test
    public void removesValue() throws Exception {
        invokeWithContext(fakeRequest().cookie(Http.Cookie.builder(SOME_KEY, SOME_VALUE).build()), () -> {
            final StoringStrategy strategy = cookieStrategy();
            assertThat(strategy.findValue(SOME_KEY)).contains(SOME_VALUE);
            strategy.removeValue(SOME_KEY);
            assertThat(strategy.findValue(SOME_KEY)).isEmpty();
            assertThat(Http.Context.current().response().cookie(SOME_KEY))
                    .hasValueSatisfying(cookie -> assertThat(cookie.maxAge()).isNegative());
            return strategy;
        });
    }

    private StoringStrategy cookieStrategy() {
        return new AbstractCookieStoringStrategy();
    }
}
