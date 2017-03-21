package com.commercetools.sunrise.sessions;

import io.sphere.sdk.models.Base;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.test.WithApplication;

import java.util.Map;
import java.util.function.Consumer;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.fakeRequest;

public class SerializableObjectStoringSessionCookieStrategyTest extends WithApplication {


    private static final SomeObject SOME_OBJECT = new SomeObject("hello", 2);
    private static final SomeObject SOME_OTHER_OBJECT = new SomeObject("world", 4);
    private static final String JSON_SOME_OBJECT = Json.stringify(Json.toJson(SOME_OBJECT));

    @Test
    public void findsWhenInSession() throws Exception {
        final Http.Context context = buildHttpContext(singletonMap("some-key", JSON_SOME_OBJECT));
        testSession(context, session ->
                assertThat(session.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT));
    }

    @Test
    public void doesNotFindWhenNotInSession() throws Exception {
        final Http.Context context = buildHttpContext(emptyMap());
        testSession(context, session ->
                assertThat(session.findObjectByKey("some-key", SomeObject.class)).isEmpty());
    }

    @Test
    public void doesNotFindDifferentClassFound() throws Exception {
        final Http.Context context = buildHttpContext(singletonMap("some-key", JSON_SOME_OBJECT));
        testSession(context, session ->
                assertThat(session.findObjectByKey("some-key", Long.class)).isEmpty());
    }

    @Test
    public void createsWhenNotFound() throws Exception {
        final Http.Context context = buildHttpContext(emptyMap());
        testSession(context, session -> {
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).isEmpty();
            session.overwriteObjectByKey("some-key", SOME_OBJECT);
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
        });
    }

    @Test
    public void replacesValue() throws Exception {
        final Http.Context context =  buildHttpContext(singletonMap("some-key", JSON_SOME_OBJECT));
        testSession(context, session -> {
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
            session.overwriteObjectByKey("some-key", SOME_OTHER_OBJECT);
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OTHER_OBJECT);
        });
    }

    @Test
    public void removesValue() throws Exception {
        final Http.Context context = buildHttpContext(singletonMap("some-key", JSON_SOME_OBJECT));
        testSession(context, session -> {
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
            session.removeObjectByKey("some-key");
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).isEmpty();
            assertThat(context.session().containsKey("some-key")).isFalse();
        });
    }

    private void testSession(final Http.Context context, final Consumer<SerializableObjectStoringSessionCookieStrategy> test) {
        final SerializableObjectStoringSessionCookieStrategy serializableObjectSession = new SerializableObjectStoringSessionCookieStrategy(context);
        test.accept(serializableObjectSession);
    }

    private Http.Context buildHttpContext(final Map<String, String> session) {
        return new Http.Context(fakeRequest().session(session));
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
