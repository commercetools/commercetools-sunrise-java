package com.commercetools.sunrise.sessions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.api.mvc.Session;
import play.mvc.Http;
import scala.Option;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.Optional;

/**
 * Uses a session cookie to store information about the user.
 */
@Singleton
public class CookieSessionStrategy implements SessionStrategy {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> findValueByKey(final String key) {
        final Optional<String> value = context()
                .flatMap(ctx -> findValueInHttpContext(key, ctx)
                        .map(Http.Cookie::value));
        if (value.isPresent()) {
            logger.debug("Loaded from session \"{}\" = {}", key, value.get());
        } else {
            logger.debug("Not found in session \"{}\"", key);
        }
        return value;
    }

    private Optional<Http.Cookie> findValueInHttpContext(final String key, final Http.Context ctx) {
        final Http.Cookie nullableCookie = ctx.response().cookie(key)
                .orElseGet(() -> ctx.request().cookie(key));
        return Optional.ofNullable(nullableCookie)
                .filter(this::isCookieValid);
    }

    private boolean isCookieValid(final Http.Cookie cookie) {
        return !Integer.valueOf(play.api.mvc.Cookie.DiscardedMaxAge()).equals(cookie.maxAge());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void overwriteValueByKey(final String key, @Nullable final String value) {
        if (value != null) {
            context().ifPresent(ctx -> {
                final Http.Cookie cookie = Http.Cookie.builder(key, value)
                        .withPath(cookiePath())
                        .withDomain(cookieDomain())
                        .withSecure(false)
                        .build();
                ctx.response().setCookie(cookie);
                logger.debug("Saved in session \"{}\" = {}", key, value);
            });
        } else {
            removeValueByKey(key);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeValueByKey(final String key) {
        context().ifPresent(ctx -> {
            ctx.response().discardCookie(key, cookiePath(), cookieDomain(), false);
            logger.debug("Removed cookie \"{}\"", key);
        });
    }

    private Optional<Http.Context> context() {
        return Optional.ofNullable(Http.Context.current.get());
    }

    private static String cookiePath() {
        return Session.path();
    }

    private static String cookieDomain() {
        final Option<String> domain = Session.domain();
        return domain.isDefined() ? domain.get() : null;
    }
}