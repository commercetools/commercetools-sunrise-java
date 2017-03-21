package com.commercetools.sunrise.sessions;

import org.junit.Test;
import play.mvc.Http;
import play.test.WithApplication;

import java.util.Map;
import java.util.function.Consumer;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.fakeRequest;

public class SessionCookieStrategyTest extends WithApplication {

    @Test
    public void findsWhenInSession() throws Exception {
        final Http.Context context = buildHttpContext(singletonMap("some-key", "some-value"));
        testSession(context, session ->
                assertThat(session.findValueByKey("some-key")).contains("some-value"));
    }

    @Test
    public void doesNotFindWhenNotInSession() throws Exception {
        final Http.Context context = buildHttpContext(emptyMap());
        testSession(context, session ->
                assertThat(session.findValueByKey("some-key")).isEmpty());
    }

    @Test
    public void createsWhenNotFound() throws Exception {
        final Http.Context context = buildHttpContext(emptyMap());
        testSession(context, session -> {
            assertThat(session.findValueByKey("some-key")).isEmpty();
            session.overwriteValueByKey("some-key", "some-value");
            assertThat(session.findValueByKey("some-key")).contains("some-value");
        });
    }

    @Test
    public void replacesValue() throws Exception {
        final Http.Context context = buildHttpContext(singletonMap("some-key", "some-value"));
        testSession(context, session -> {
            assertThat(session.findValueByKey("some-key")).contains("some-value");
            session.overwriteValueByKey("some-key", "some-other-value");
            assertThat(session.findValueByKey("some-key")).contains("some-other-value");
        });
    }

    @Test
    public void removesValue() throws Exception {
        final Http.Context context = buildHttpContext(singletonMap("some-key", "some-value"));
        testSession(context, session -> {
            assertThat(session.findValueByKey("some-key")).contains("some-value");
            session.removeValueByKey("some-key");
            assertThat(session.findValueByKey("some-key")).isEmpty();
            assertThat(context.session().containsKey("some-key")).isFalse();
        });
    }

    private void testSession(final Http.Context context, final Consumer<SessionCookieStrategy> test) {
        final SessionCookieStrategy sessionCookieStrategy = new SessionCookieStrategy(context);
        test.accept(sessionCookieStrategy);
    }

    private Http.Context buildHttpContext(final Map<String, String> session) {
        return new Http.Context(fakeRequest().session(session));
    }
}
