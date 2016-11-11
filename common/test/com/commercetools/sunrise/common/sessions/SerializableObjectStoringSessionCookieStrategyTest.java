package com.commercetools.sunrise.common.sessions;

import io.sphere.sdk.models.Base;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;

import java.util.Map;
import java.util.function.Consumer;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

public class SerializableObjectStoringSessionCookieStrategyTest {


    private static final SomeObject SOME_OBJECT = new SomeObject("hello", 2);
    private static final SomeObject SOME_OTHER_OBJECT = new SomeObject("world", 4);
    private static final String JSON_SOME_OBJECT = Json.stringify(Json.toJson(SOME_OBJECT));

    @Test
    public void findsWhenInSession() throws Exception {
        final Http.Session httpSession = buildHttpSession(singletonMap("some-key", JSON_SOME_OBJECT));
        testSession(httpSession, session ->
                assertThat(session.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT));
    }

    @Test
    public void doesNotFindWhenNotInSession() throws Exception {
        final Http.Session httpSession = buildHttpSession(emptyMap());
        testSession(httpSession, session ->
                assertThat(session.findObjectByKey("some-key", SomeObject.class)).isEmpty());
    }

    @Test
    public void doesNotFindDifferentClassFound() throws Exception {
        final Http.Session httpSession = buildHttpSession(singletonMap("some-key", JSON_SOME_OBJECT));
        testSession(httpSession, session ->
                assertThat(session.findObjectByKey("some-key", Long.class)).isEmpty());
    }

    @Test
    public void createsWhenNotFound() throws Exception {
        final Http.Session httpSession = buildHttpSession(emptyMap());
        testSession(httpSession, session -> {
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).isEmpty();
            session.overwriteObjectByKey("some-key", SOME_OBJECT);
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
        });
    }

    @Test
    public void replacesValue() throws Exception {
        final Http.Session httpSession = buildHttpSession(singletonMap("some-key", JSON_SOME_OBJECT));
        testSession(httpSession, session -> {
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
            session.overwriteObjectByKey("some-key", SOME_OTHER_OBJECT);
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OTHER_OBJECT);
        });
    }

    @Test
    public void removesValue() throws Exception {
        final Http.Session httpSession = buildHttpSession(singletonMap("some-key", JSON_SOME_OBJECT));
        testSession(httpSession, session -> {
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
            session.removeObjectByKey("some-key");
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).isEmpty();
            assertThat(httpSession.containsKey("some-key")).isFalse();
        });
    }

    private void testSession(final Http.Session session, final Consumer<SerializableObjectStoringSessionCookieStrategy> test) {
        final SerializableObjectStoringSessionCookieStrategy serializableObjectSession = new SerializableObjectStoringSessionCookieStrategy(session);
        test.accept(serializableObjectSession);
    }

    private static Http.Session buildHttpSession(final Map<String, String> initialSession) {
        return new Http.Session(initialSession);
    }

    private static class SomeObject extends Base {

        public String text;
        public int amount;

        public SomeObject() {
        }

        public SomeObject(final String text, final int amount) {
            this.text = text;
            this.amount = amount;
        }
    }
}
