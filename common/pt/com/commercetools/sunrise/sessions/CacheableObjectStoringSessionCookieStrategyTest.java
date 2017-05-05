package com.commercetools.sunrise.sessions;

import io.sphere.sdk.models.Base;
import org.junit.Test;
import play.Configuration;
import play.cache.CacheApi;
import play.mvc.Http;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.invokeWithContext;

public class CacheableObjectStoringSessionCookieStrategyTest extends WithApplication {

    private static final SomeObject SOME_OBJECT = new SomeObject("hello", 2);
    private static final SomeObject SOME_OTHER_OBJECT = new SomeObject("world", 4);

    @Test
    public void findsWhenInCacheAndSession() throws Exception {
        final CacheApi cache = buildCache(singletonMap("some-cache-key", SOME_OBJECT));
        invokeWithContext(fakeRequest().session(singletonMap("some-key", "some-cache-key")), () ->
                assertThat(strategy(cache).findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT));
    }

    @Test
    public void doesNotFindWhenNotInCache() throws Exception {
        final CacheApi cache = buildCache(emptyMap());
        invokeWithContext(fakeRequest().session(singletonMap("some-key", "some-cache-key")), () ->
                assertThat(strategy(cache).findObjectByKey("some-key", SomeObject.class)).isEmpty());
    }

    @Test
    public void doesNotFindWhenNotInSession() throws Exception {
        final CacheApi cache = buildCache(singletonMap("some-cache-key", SOME_OBJECT));
        invokeWithContext(fakeRequest(), () ->
                assertThat(strategy(cache).findObjectByKey("some-key", SomeObject.class)).isEmpty());
    }

    @Test
    public void doesNotFindWhenNeitherInSessionNorInCache() throws Exception {
        final CacheApi cache = buildCache(emptyMap());
        invokeWithContext(fakeRequest(), () ->
                assertThat(strategy(cache).findObjectByKey("some-key", SomeObject.class)).isEmpty());
    }

    @Test
    public void doesNotFindWhenDifferentClassFound() throws Exception {
        final CacheApi cache = buildCache(singletonMap("some-cache-key", SOME_OBJECT));
        invokeWithContext(fakeRequest().session(singletonMap("some-key", "some-cache-key")), () ->
                assertThat(strategy(cache).findObjectByKey("some-key", Long.class)).isEmpty());
    }

    @Test
    public void createsWhenNotFound() throws Exception {
        final CacheApi cache = buildCache(emptyMap());
        invokeWithContext(fakeRequest(), () -> {
            final ObjectStoringSessionStrategy strategy = strategy(cache);
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).isEmpty();
            strategy.overwriteObjectByKey("some-key", SOME_OBJECT);
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
            return strategy;
        });
    }

    @Test
    public void createsWhenNotInSession() throws Exception {
        final CacheApi cache = buildCache(singletonMap("some-cache-key", SOME_OBJECT));
        invokeWithContext(fakeRequest(), () -> {
            final ObjectStoringSessionStrategy strategy = strategy(cache);
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).isEmpty();
            strategy.overwriteObjectByKey("some-key", SOME_OBJECT);
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
            return strategy;
        });
    }

    @Test
    public void createsWhenNotInCache() throws Exception {
        final CacheApi cache = buildCache(emptyMap());
        invokeWithContext(fakeRequest().session(singletonMap("some-key", "some-cache-key")), () -> {
            final ObjectStoringSessionStrategy strategy = strategy(cache);
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).isEmpty();
            strategy.overwriteObjectByKey("some-key", SOME_OBJECT);
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
            return strategy;
        });
    }

    @Test
    public void replacesValue() throws Exception {
        final CacheApi cache = buildCache(singletonMap("some-cache-key", SOME_OBJECT));
        invokeWithContext(fakeRequest().session(singletonMap("some-key", "some-cache-key")), () -> {
            final ObjectStoringSessionStrategy strategy = strategy(cache);
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
            strategy.overwriteObjectByKey("some-key", SOME_OTHER_OBJECT);
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OTHER_OBJECT);
            return strategy;
        });
    }

    @Test
    public void removesKeyOnNullValue() throws Exception {
        final CacheApi cache = buildCache(singletonMap("some-cache-key", SOME_OBJECT));
        invokeWithContext(fakeRequest().session(singletonMap("some-key", "some-cache-key")), () -> {
            final ObjectStoringSessionStrategy strategy = strategy(cache);
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
            strategy.overwriteObjectByKey("some-key", null);
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).isEmpty();
            return strategy;
        });
    }

    @Test
    public void removesValue() throws Exception {
        final CacheApi cache = buildCache(singletonMap("some-cache-key", SOME_OBJECT));
        invokeWithContext(fakeRequest().session(singletonMap("some-key", "some-cache-key")), () -> {
            final ObjectStoringSessionStrategy strategy = strategy(cache);
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
            strategy.removeObjectByKey("some-key");
            assertThat(strategy.findObjectByKey("some-key", SomeObject.class)).isEmpty();
            assertThat(Http.Context.current().session().containsKey("some-key")).isFalse();
            assertThat(Optional.ofNullable(cache.get("some-key"))).isEmpty();
            return strategy;
        });
    }

    private ObjectStoringSessionStrategy strategy(final CacheApi cacheApi) {
        final Configuration config = new Configuration(emptyMap());
        return new CacheableObjectStoringSessionCookieStrategy(cacheApi, config);
    }

    private static class SomeObject extends Base {

        private final String text;
        private final int amount;

        public SomeObject(final String text, final int amount) {
            this.text = text;
            this.amount = amount;
        }
    }

    private static CacheApi buildCache(final Map<String, Object> initialCache) {

        return new CacheApi() {

            final Map<String, Object> cache = new HashMap<>(initialCache);

            @SuppressWarnings("unchecked")
            @Override
            public <T> T get(final String key) {
                return (T) cache.get(key);
            }

            @Override
            public <T> T getOrElse(final String key, final Callable<T> block, final int expiration) {
                return null;
            }

            @Override
            public <T> T getOrElse(final String key, final Callable<T> block) {
                return null;
            }

            @Override
            public void set(final String key, final Object value, final int expiration) {

            }

            @Override
            public void set(final String key, final Object value) {
                cache.put(key, value);
            }

            @Override
            public void remove(final String key) {
                cache.remove(key);
            }
        };
    }


}
