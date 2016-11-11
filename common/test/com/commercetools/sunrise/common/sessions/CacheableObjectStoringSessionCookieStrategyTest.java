package com.commercetools.sunrise.common.sessions;

import io.sphere.sdk.models.Base;
import org.junit.Test;
import play.Configuration;
import play.cache.CacheApi;
import play.mvc.Http;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

public class CacheableObjectStoringSessionCookieStrategyTest {

    private static final SomeObject SOME_OBJECT = new SomeObject("hello", 2);
    private static final SomeObject SOME_OTHER_OBJECT = new SomeObject("world", 4);

    @Test
    public void findsWhenInCacheAndSession() throws Exception {
        final Http.Session playSession = buildPlayHttpSession(singletonMap("some-key", "some-cache-key"));
        final CacheApi cache = buildCache(singletonMap("some-cache-key", SOME_OBJECT));
        testSession(playSession, cache, session ->
                assertThat(session.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT));
    }

    @Test
    public void doesNotFindWhenNotInCache() throws Exception {
        final Http.Session playSession = buildPlayHttpSession(singletonMap("some-key", "some-cache-key"));
        final CacheApi cache = buildCache(emptyMap());
        testSession(playSession, cache, session ->
                assertThat(session.findObjectByKey("some-key", SomeObject.class)).isEmpty());
    }

    @Test
    public void doesNotFindWhenNotInSession() throws Exception {
        final Http.Session playSession = buildPlayHttpSession(emptyMap());
        final CacheApi cache = buildCache(singletonMap("some-cache-key", SOME_OBJECT));
        testSession(playSession, cache, session ->
                assertThat(session.findObjectByKey("some-key", SomeObject.class)).isEmpty());
    }

    @Test
    public void doesNotFindWhenNeitherInSessionNorInCache() throws Exception {
        final Http.Session playSession = buildPlayHttpSession(emptyMap());
        final CacheApi cache = buildCache(emptyMap());
        testSession(playSession, cache, session ->
                assertThat(session.findObjectByKey("some-key", SomeObject.class)).isEmpty());
    }

    @Test
    public void doesNotFindDifferentClassFound() throws Exception {
        final Http.Session playSession = buildPlayHttpSession(singletonMap("some-key", "some-cache-key"));
        final CacheApi cache = buildCache(singletonMap("some-cache-key", SOME_OBJECT));
        testSession(playSession, cache, session -> {
            final Optional<Long> objectByKey = session.findObjectByKey("some-key", Long.class);
            assertThat(objectByKey).isEmpty();
        });
    }

    @Test
    public void createsWhenNotFound() throws Exception {
        final Http.Session playSession = buildPlayHttpSession(emptyMap());
        final CacheApi cache = buildCache(emptyMap());
        testSession(playSession, cache, session -> {
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).isEmpty();
            session.overwriteObjectByKey("some-key", SOME_OBJECT);
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
        });
    }

    @Test
    public void createsWhenNotInSession() throws Exception {
        final Http.Session playSession = buildPlayHttpSession(emptyMap());
        final CacheApi cache = buildCache(singletonMap("some-cache-key", SOME_OBJECT));
        testSession(playSession, cache, session -> {
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).isEmpty();
            session.overwriteObjectByKey("some-key", SOME_OBJECT);
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
        });
    }

    @Test
    public void createsWhenNotInCache() throws Exception {
        final Http.Session playSession = buildPlayHttpSession(singletonMap("some-key", "some-cache-key"));
        final CacheApi cache = buildCache(emptyMap());
        testSession(playSession, cache, session -> {
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).isEmpty();
            session.overwriteObjectByKey("some-key", SOME_OBJECT);
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
        });
    }

    @Test
    public void replacesValue() throws Exception {
        final Http.Session playSession = buildPlayHttpSession(singletonMap("some-key", "some-cache-key"));
        final CacheApi cache = buildCache(singletonMap("some-cache-key", SOME_OBJECT));
        testSession(playSession, cache, session -> {
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
            session.overwriteObjectByKey("some-key", SOME_OTHER_OBJECT);
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OTHER_OBJECT);
        });
    }

    @Test
    public void removesValue() throws Exception {
        final Http.Session playSession = buildPlayHttpSession(singletonMap("some-key", "some-cache-key"));
        final CacheApi cache = buildCache(singletonMap("some-cache-key", SOME_OBJECT));
        testSession(playSession, cache, session -> {
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).contains(SOME_OBJECT);
            session.removeObjectByKey("some-key");
            assertThat(session.findObjectByKey("some-key", SomeObject.class)).isEmpty();
            assertThat(playSession.containsKey("some-key")).isFalse();
            assertThat(Optional.ofNullable(cache.get("some-key"))).isEmpty();
        });
    }

    private void testSession(final Http.Session session, final CacheApi cacheApi,
                             final Consumer<CacheableObjectStoringSessionCookieStrategy> test) {
        final Configuration config = new Configuration(emptyMap());
        final CacheableObjectStoringSessionCookieStrategy cachableObjectSession = new CacheableObjectStoringSessionCookieStrategy(session, cacheApi, config);
        test.accept(cachableObjectSession);
    }

    private static Http.Session buildPlayHttpSession(final Map<String, String> initialSession) {
        return new Http.Session(initialSession);
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
