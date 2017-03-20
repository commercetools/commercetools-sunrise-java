package com.commercetools.sunrise.sessions;

import org.junit.Test;
import play.mvc.Http;

import java.util.Map;
import java.util.function.Consumer;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

public class SessionCookieStrategyTest {

    @Test
    public void findsWhenInSession() throws Exception {
        final Http.Session httpSession = buildHttpSession(singletonMap("some-key", "some-value"));
        testSession(httpSession, session ->
                assertThat(session.findValueByKey("some-key")).contains("some-value"));
    }

    @Test
    public void doesNotFindWhenNotInSession() throws Exception {
        final Http.Session httpSession = buildHttpSession(emptyMap());
        testSession(httpSession, session ->
                assertThat(session.findValueByKey("some-key")).isEmpty());
    }

    @Test
    public void createsWhenNotFound() throws Exception {
        final Http.Session httpSession = buildHttpSession(emptyMap());
        testSession(httpSession, session -> {
            assertThat(session.findValueByKey("some-key")).isEmpty();
            session.overwriteValueByKey("some-key", "some-value");
            assertThat(session.findValueByKey("some-key")).contains("some-value");
        });
    }

    @Test
    public void replacesValue() throws Exception {
        final Http.Session httpSession = buildHttpSession(singletonMap("some-key", "some-value"));
        testSession(httpSession, session -> {
            assertThat(session.findValueByKey("some-key")).contains("some-value");
            session.overwriteValueByKey("some-key", "some-other-value");
            assertThat(session.findValueByKey("some-key")).contains("some-other-value");
        });
    }

    @Test
    public void removesValue() throws Exception {
        final Http.Session httpSession = buildHttpSession(singletonMap("some-key", "some-value"));
        testSession(httpSession, session -> {
            assertThat(session.findValueByKey("some-key")).contains("some-value");
            session.removeValueByKey("some-key");
            assertThat(session.findValueByKey("some-key")).isEmpty();
            assertThat(httpSession.containsKey("some-key")).isFalse();
        });
    }

    private void testSession(final Http.Session session, final Consumer<SessionCookieStrategy> test) {
        final SessionCookieStrategy sessionCookieStrategy = new SessionCookieStrategy(session);
        test.accept(sessionCookieStrategy);
    }

    private static Http.Session buildHttpSession(final Map<String, String> initialSession) {
        return new Http.Session(initialSession);
    }
}
