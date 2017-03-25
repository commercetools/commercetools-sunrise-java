package com.commercetools.sunrise.sessions;

import org.junit.Test;
import play.mvc.Http;
import play.test.WithApplication;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.invokeWithContext;

public class SessionCookieStrategyTest extends WithApplication {

    @Test
    public void findsWhenInSession() throws Exception {
        invokeWithContext(fakeRequest().session(singletonMap("some-key", "some-value")), () ->
                assertThat(strategy().findValueByKey("some-key")).contains("some-value"));
    }

    @Test
    public void doesNotFindWhenNotInSession() throws Exception {
        invokeWithContext(fakeRequest(), () ->
                assertThat(strategy().findValueByKey("some-key")).isEmpty());
    }

    @Test
    public void createsWhenNotFound() throws Exception {
        invokeWithContext(fakeRequest(), () -> {
            final SessionCookieStrategy strategy = strategy();
            assertThat(strategy.findValueByKey("some-key")).isEmpty();
            strategy.overwriteValueByKey("some-key", "some-value");
            assertThat(strategy.findValueByKey("some-key")).contains("some-value");
            return strategy;
        });
    }

    @Test
    public void replacesValue() throws Exception {
        invokeWithContext(fakeRequest().session(singletonMap("some-key", "some-value")), () -> {
            final SessionCookieStrategy strategy = strategy();
            assertThat(strategy.findValueByKey("some-key")).contains("some-value");
            strategy.overwriteValueByKey("some-key", "some-other-value");
            assertThat(strategy.findValueByKey("some-key")).contains("some-other-value");
            return strategy;
        });
    }

    @Test
    public void removesValue() throws Exception {
        invokeWithContext(fakeRequest().session(singletonMap("some-key", "some-value")), () -> {
            final SessionCookieStrategy strategy = strategy();
            assertThat(strategy.findValueByKey("some-key")).contains("some-value");
            strategy.removeValueByKey("some-key");
            assertThat(strategy.findValueByKey("some-key")).isEmpty();
            assertThat(Http.Context.current().session().containsKey("some-key")).isFalse();
            return strategy;
        });
    }

    private SessionCookieStrategy strategy() {
        return new SessionCookieStrategy();
    }
}
