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

public class SerializableObjectStoringSessionStrategyTest extends WithApplication {

    private static final SomeObject SOME_OBJECT = new SomeObject("hello", 2);
    private static final SomeObject SOME_OTHER_OBJECT = new SomeObject("world", 4);
    private static final String JSON_SOME_OBJECT = Json.stringify(Json.toJson(SOME_OBJECT));
    private static final String SOME_KEY = "some-key";

    @Test
    public void findsWhenInSession() throws Exception {
        invokeWithContext(fakeRequest().session(singletonMap(SOME_KEY, JSON_SOME_OBJECT)), () ->
                assertThat(strategy().findObjectByKey(SOME_KEY, SomeObject.class)).contains(SOME_OBJECT));
    }

    @Test
    public void doesNotFindWhenNotInSession() throws Exception {
        invokeWithContext(fakeRequest(), () ->
                assertThat(strategy().findObjectByKey(SOME_KEY, SomeObject.class)).isEmpty());
    }

    @Test
    public void doesNotFindDifferentClassFound() throws Exception {
        invokeWithContext(fakeRequest().session(singletonMap(SOME_KEY, JSON_SOME_OBJECT)), () ->
                assertThat(strategy().findObjectByKey(SOME_KEY, Long.class)).isEmpty());
    }

    @Test
    public void createsWhenNotFound() throws Exception {
        invokeWithContext(fakeRequest(), () -> {
            final ObjectStoringSessionStrategy strategy = strategy();
            assertThat(strategy.findObjectByKey(SOME_KEY, SomeObject.class)).isEmpty();
            strategy.overwriteObjectByKey(SOME_KEY, SOME_OBJECT);
            assertThat(strategy.findObjectByKey(SOME_KEY, SomeObject.class)).contains(SOME_OBJECT);
            return strategy;
        });
    }

    @Test
    public void replacesValue() throws Exception {
        invokeWithContext(fakeRequest().session(singletonMap(SOME_KEY, JSON_SOME_OBJECT)), () -> {
            final ObjectStoringSessionStrategy strategy = strategy();
            assertThat(strategy.findObjectByKey(SOME_KEY, SomeObject.class)).contains(SOME_OBJECT);
            strategy.overwriteObjectByKey(SOME_KEY, SOME_OTHER_OBJECT);
            assertThat(strategy.findObjectByKey(SOME_KEY, SomeObject.class)).contains(SOME_OTHER_OBJECT);
            return strategy;
        });
    }

    @Test
    public void removesKeyOnNullValue() throws Exception {
        invokeWithContext(fakeRequest().session(singletonMap(SOME_KEY, JSON_SOME_OBJECT)), () -> {
            final ObjectStoringSessionStrategy strategy = strategy();
            assertThat(strategy.findObjectByKey(SOME_KEY, SomeObject.class)).contains(SOME_OBJECT);
            strategy.overwriteObjectByKey(SOME_KEY, null);
            assertThat(strategy.findObjectByKey(SOME_KEY, SomeObject.class)).isEmpty();
            return strategy;
        });
    }

    @Test
    public void removesValue() throws Exception {
        final Http.RequestBuilder request = fakeRequest().session(singletonMap(SOME_KEY, JSON_SOME_OBJECT));
        invokeWithContext(request, () -> {
            final ObjectStoringSessionStrategy strategy = strategy();
            assertThat(strategy.findObjectByKey(SOME_KEY, SomeObject.class)).contains(SOME_OBJECT);
            strategy.removeObjectByKey(SOME_KEY);
            assertThat(strategy.findObjectByKey(SOME_KEY, SomeObject.class)).isEmpty();
            assertThat(Http.Context.current().session().containsKey(SOME_KEY)).isFalse();
            return strategy;
        });
    }

    private ObjectStoringSessionStrategy strategy() {
        return new SerializableObjectStoringSessionStrategy();
    }

    private static class SomeObject extends Base {

        public String text;
        public int amount;

        public SomeObject() {
        }

        SomeObject(final String text, final int amount) {
            this.text = text;
            this.amount = amount;
        }
    }
}
