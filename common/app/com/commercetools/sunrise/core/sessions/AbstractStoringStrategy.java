package com.commercetools.sunrise.core.sessions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.api.mvc.Session;
import play.mvc.Http;
import scala.Option;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Uses a session cookie to store information about the user.
 */
public abstract class AbstractStoringStrategy implements StoringStrategy {

    protected static final Logger LOGGER = LoggerFactory.getLogger(StoringStrategy.class);

    @Override
    public Optional<String> findInSession(final String key) {
        final Optional<String> value = session().flatMap(session -> Optional.ofNullable(session.get(key)));
        if (value.isPresent()) {
            LOGGER.debug("Loaded from session \"{}\" = {}", key, value.get());
        } else {
            LOGGER.debug("Not found in session \"{}\"", key);
        }
        return value;
    }

    @Override
    public void overwriteInSession(final String key, @Nullable final String value) {
        if (value != null) {
            final Http.Session session = session().orElseThrow(() -> new RuntimeException("missing http context"));
            session.put(key, value);
            LOGGER.debug("Saved in session \"{}\" = {}", key, value);
        } else {
            removeFromSession(key);
        }
    }

    @Override
    public void removeFromSession(final String key) {
        session().ifPresent(session -> {
            final String oldValue = session.remove(key);
            LOGGER.debug("Removed from session \"{}\" = {}", key, oldValue);
        });
    }

    @Override
    public Optional<String> findInCookies(final String key) {
        final Optional<String> value = context().flatMap(ctx -> findValueInHttpContext(key, ctx));
        if (value.isPresent()) {
            LOGGER.debug("Loaded cookie \"{}\" = {}", key, value.get());
        } else {
            LOGGER.debug("Not found cookie \"{}\"", key);
        }
        return value;
    }

    private Optional<String> findValueInHttpContext(final String key, final Http.Context ctx) {
        final Http.Cookie nullableCookie = ctx.response().cookie(key)
                .orElseGet(() -> ctx.request().cookie(key));
        return Optional.ofNullable(nullableCookie)
                .filter(this::isCookieValid)
                .map(Http.Cookie::value);
    }

    private boolean isCookieValid(final Http.Cookie cookie) {
        return !Integer.valueOf(play.api.mvc.Cookie.DiscardedMaxAge()).equals(cookie.maxAge());
    }

    @Override
    public void overwriteInCookies(final String key, @Nullable final String value, final boolean httpOnly, final boolean secure) {
        final Optional<Http.Cookie> cookie = buildCookie(key, value, httpOnly, secure);
        if (cookie.isPresent()) {
            final Http.Context ctx = context().orElseThrow(() -> new RuntimeException("cannot access HTTP context"));
            ctx.response().setCookie(cookie.get());
            LOGGER.debug("Saved cookie \"{}\" = {}", key, cookie);
        } else {
            removeFromCookies(key);
        }
    }

    private Optional<Http.Cookie> buildCookie(final String key, @Nullable final String nullableValue,
                                              final boolean httpOnly, final boolean secure) {
        return Optional.ofNullable(nullableValue).map(value ->
                Http.Cookie.builder(key, value)
                        .withPath(cookiePath())
                        .withDomain(cookieDomain())
                        .withHttpOnly(httpOnly)
                        .withSecure(secure)
                        .build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromCookies(final String key) {
        context().ifPresent(ctx -> {
            ctx.response().discardCookie(key, cookiePath(), cookieDomain(), false);
            LOGGER.debug("Removed cookie \"{}\"", key);
        });
    }

    private String cookiePath() {
        return Session.path();
    }

    @Nullable
    private String cookieDomain() {
        final Option<String> domain = Session.domain();
        return domain.isDefined() ? domain.get() : null;
    }

    private Optional<Http.Context> context() {
        return Optional.ofNullable(Http.Context.current.get());
    }

    private Optional<Http.Session> session() {
        return context().map(Http.Context::session);
    }
}