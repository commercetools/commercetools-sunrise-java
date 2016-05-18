package common.inject;

import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Scopes;
import play.mvc.Http;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*

IMPORTANT: put @ before the route

inspirated by: http://stackoverflow.com/questions/25626264/how-to-use-play-frameworks-request-and-session-scope-in-google-guice/34981902#34981902
 */


/**
 * Allows objects to be bound to Play! Http.Context.current.args with a ThreadLocal fallback.
 */
public class HttpContextScope implements Scope {

    private static final ThreadLocal<Context> httpContextScopeContext = new ThreadLocal<>();

    enum NullableObject {
        INSTANCE
    }

    @Override
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> provider) {
        return new Provider<T>() {
            @Override
            public T get() {
                Http.Context currentContext = Http.Context.current();
                if (currentContext == null) {
                    Context context = httpContextScopeContext.get();
                    if (context != null) {
                        T t = (T) context.map.get(key);
                        if (t == NullableObject.INSTANCE) {
                            return null;
                        }

                        if (t == null) {
                            t = provider.get();
                            if (!Scopes.isCircularProxy(t)) {
                                context.map.put(key, t != null ? t : NullableObject.INSTANCE);
                            }
                        }
                        return t;
                    }
                }

                String name = key.toString();
                synchronized (currentContext) {
                    Object obj = currentContext.args.get(name);
                    if (obj == NullableObject.INSTANCE) {
                        return null;
                    }
                    T t = (T) obj;
                    if (t == null) {
                        t = provider.get();
                        if (!Scopes.isCircularProxy(t)) {
                            currentContext.args.put(name, t != null ? t : NullableObject.INSTANCE);
                        }
                    }
                    return t;
                }
            }
        };
    }

    @Override
    public String toString() {
        return "Http.Context.ARGS";
    }

    private static class Context {
        final Map<Key, Object> map = Maps.newHashMap();
        final Lock lock = new ReentrantLock();

        public Context open() {
            lock.lock();
            final Context previous = httpContextScopeContext.get();
            httpContextScopeContext.set(this);
            return new Context() {
                public void close() {
                    httpContextScopeContext.set(previous);
                    lock.unlock();
                }
            };
        }
    }
}
