package com.commercetools.sunrise.sessions;

import io.sphere.sdk.models.Base;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.test.WithApplication;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.invokeWithContext;

public class SerializableObjectStoringSessionCookieStrategyTest extends WithApplication {


    private static final SomeObject SOME_OBJECT = new SomeObject("hello", 2);
    private static final SomeObject SOME_OTHER_OBJECT = new SomeObject("world", 4);
    private static final String JSON_SOME_OBJECT = Json.stringify(Json.toJson(SOME_OBJECT));

    @Test
    public void findsWhenInSession() throws Exception {
        invokeWithContext(fakeRequest().session(singletonMap("some-key", JSON_SOME_OBJECT)), () ->
                assertThat(strategy().findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT));
    }

    @Test
    public void doesNotFindWhenNotInSession() throws Exception {
        invokeWithContext(fakeRequest(), () ->
                assertThat(strategy().findObjectByKey("some-key", SomeObject.class)).isEmpty());
    }

    @Test
    public void doesNotFindDifferentClassFound() throws Exception {
        invokeWithContext(fakeRequest().session(singletonMap("some-key", JSON_SOME_OBJECT)), () ->
                assertThat(strategy().findObjectByKey("some-key", Long.class)).isEmpty());
    }

    @Test
    public void createsWhenNotFound() throws Exception {
        invokeWithContext(fakeRequest(), () -> {
            final ObjectStoringSessionStrategy strategy = strategy();
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).isEmpty();
            strategy.overwriteObjectByKey("some-key", SOME_OBJECT);
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
            return strategy;
        });
    }

    @Test
    public void replacesValue() throws Exception {
        invokeWithContext(fakeRequest().session(singletonMap("some-key", JSON_SOME_OBJECT)), () -> {
            final ObjectStoringSessionStrategy strategy = strategy();
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
            strategy.overwriteObjectByKey("some-key", SOME_OTHER_OBJECT);
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OTHER_OBJECT);
            return strategy;
        });
    }

    @Test
    public void removesValue() throws Exception {
        final Http.RequestBuilder request = fakeRequest().session(singletonMap("some-key", JSON_SOME_OBJECT));
        invokeWithContext(request, () -> {
            final ObjectStoringSessionStrategy strategy = strategy();
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
            strategy.removeObjectByKey("some-key");
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).isEmpty();
            assertThat(Http.Context.current().session().containsKey("some-key")).isFalse();
            return strategy;
        });
    }

    private ObjectStoringSessionStrategy strategy() {
        return new SerializableObjectStoringSessionCookieStrategy();
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
