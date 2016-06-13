package com.commercetools.sunrise.common.contexts;

import com.google.inject.*;
import play.mvc.Http;

/*

IMPORTANT: put @ before the route

inspirated by: http://stackoverflow.com/questions/25626264/how-to-use-play-frameworks-request-and-session-scope-in-google-guice/34981902#34981902
 */


/**
 * Allows objects to be bound to Play! Http.Context.current.args with a ThreadLocal fallback.
 */
public class RequestScope implements Scope {

    private static final TypeLiteral<Http.Context> CONTEXT_TYPE_LITERAL = TypeLiteral.get(Http.Context.class);

    enum NullableObject {
        INSTANCE
    }

    @Override
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> provider) {
        //here is no http context available
        return new HttpContextScopeProvider<>(key, provider);
    }

    @Override
    public String toString() {
        return "Http.Context.args";
    }

    private static class HttpContextScopeProvider<T> implements Provider<T> {
        private final Key<T> key;
        private final Provider<T> provider;

        public HttpContextScopeProvider(final Key<T> key, final Provider<T> provider) {
            this.key = key;
            this.provider = provider;
        }

        @Override
        public T get() {
            final Http.Context currentContext = Http.Context.current();
            if (key.getTypeLiteral().equals(CONTEXT_TYPE_LITERAL)) {
                return (T) currentContext;
            } else {
                final Cache cache = new HttpContextCache(currentContext);
                final String cacheKey = key.toString();
                final Object obj = cache.get(cacheKey);
                final boolean objectIsSupposedToBeNull = obj == NullableObject.INSTANCE;
                if (objectIsSupposedToBeNull) {
                    return null;
                } else if (obj == null) {
                    final T t = provider.get();
                    if (!Scopes.isCircularProxy(t)) {
                        final Object cacheValue = t != null ? t : NullableObject.INSTANCE;
                        cache.put(cacheKey, cacheValue);
                    }
                    return t;
                } else {
                    return (T) obj;
                }
            }
        }
    }

    private interface Cache {
        Object get(final String key);

        void put(final String key, final Object object);
    }

    /**
     * Put dependency objects into {@link play.mvc.Http.Context#args} which has the benefit if the context gets deleted also the cached objects.
     */
    private static class HttpContextCache implements Cache {
        final Http.Context context;

        private HttpContextCache(final Http.Context currentContext) {
            this.context = currentContext;
        }

        public Object get(final String key) {
            return context.args.get(key);
        }

        @Override
        public void put(final String key, final Object object) {
            context.args.put(key, object);
        }
    }
}
