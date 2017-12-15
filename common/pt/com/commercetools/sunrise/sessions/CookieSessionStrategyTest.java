package com.commercetools.sunrise.sessions;

import org.junit.Test;
import play.mvc.Http;
import play.test.WithApplication;

import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.invokeWithContext;

public class CookieSessionStrategyTest extends WithApplication {

    private static final String SOME_KEY = "some-key";
    private static final String SOME_VALUE = "some-value";

    @Test
    public void findsWhenInCookie() throws Exception {
        invokeWithContext(fakeRequest().cookie(Http.Cookie.builder(SOME_KEY, SOME_VALUE).build()), () ->
                assertThat(cookieStrategy().findValueByKey(SOME_KEY)).contains(SOME_VALUE));
    }

    @Test
    public void doesNotFindWhenNotInCookie() throws Exception {
        invokeWithContext(fakeRequest(), () ->
                assertThat(cookieStrategy().findValueByKey(SOME_KEY)).isEmpty());
    }

    @Test
    public void createsWhenNotFound() throws Exception {
        invokeWithContext(fakeRequest(), () -> {
            final SessionStrategy strategy = cookieStrategy();
            assertThat(strategy.findValueByKey(SOME_KEY)).isEmpty();
            strategy.overwriteValueByKey(SOME_KEY, SOME_VALUE);
            assertThat(strategy.findValueByKey(SOME_KEY)).contains(SOME_VALUE);
            return strategy;
        });
    }

    @Test
    public void replacesValue() throws Exception {
        invokeWithContext(fakeRequest().cookie(Http.Cookie.builder(SOME_KEY, SOME_VALUE).build()), () -> {
            final SessionStrategy strategy = cookieStrategy();
            assertThat(strategy.findValueByKey(SOME_KEY)).contains(SOME_VALUE);
            strategy.overwriteValueByKey(SOME_KEY, "some-other-value");
            assertThat(strategy.findValueByKey(SOME_KEY)).contains("some-other-value");
            return strategy;
        });
    }

    @Test
    public void removesKeyOnNullValue() throws Exception {
        invokeWithContext(fakeRequest().cookie(Http.Cookie.builder(SOME_KEY, SOME_VALUE).build()), () -> {
            final SessionStrategy strategy = cookieStrategy();
            assertThat(strategy.findValueByKey(SOME_KEY)).contains(SOME_VALUE);
            strategy.overwriteValueByKey(SOME_KEY, null);
            assertThat(strategy.findValueByKey(SOME_KEY)).isEmpty();
            return strategy;
        });
    }

    @Test
    public void removesValue() throws Exception {
        invokeWithContext(fakeRequest().cookie(Http.Cookie.builder(SOME_KEY, SOME_VALUE).build()), () -> {
            final SessionStrategy strategy = cookieStrategy();
            assertThat(strategy.findValueByKey(SOME_KEY)).contains(SOME_VALUE);
            strategy.removeValueByKey(SOME_KEY);
            assertThat(strategy.findValueByKey(SOME_KEY)).isEmpty();
            assertThat(Http.Context.current().response().cookie(SOME_KEY))
                    .hasValueSatisfying(cookie -> assertThat(cookie.maxAge()).isNegative());
            return strategy;
        });
    }

    private SessionStrategy cookieStrategy() {
        return new CookieSessionStrategy();
    }
}
