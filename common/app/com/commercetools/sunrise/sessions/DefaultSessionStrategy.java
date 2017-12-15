package com.commercetools.sunrise.sessions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.Optional;

/**
 * Uses a session cookie to store information about the user.
 */
@Singleton
public class DefaultSessionStrategy implements SessionStrategy {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> findValueByKey(final String key) {
        final Optional<String> value = session().flatMap(session -> Optional.ofNullable(session.get(key)));
        if (value.isPresent()) {
            logger.debug("Loaded from session \"{}\" = {}", key, value.get());
        } else {
            logger.debug("Not found in session \"{}\"", key);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void overwriteValueByKey(final String key, @Nullable final String value) {
        if (value != null) {
            session().ifPresent(session -> {
                session.put(key, value);
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
        session().ifPresent(session -> {
            final String oldValue = session.remove(key);
            logger.debug("Removed from session \"{}\" = {}", key, oldValue);
        });
    }

    private Optional<Http.Session> session() {
        return Optional.ofNullable(Http.Context.current.get()).map(Http.Context::session);
    }
}