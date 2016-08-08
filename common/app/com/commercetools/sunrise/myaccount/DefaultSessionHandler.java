package com.commercetools.sunrise.myaccount;

import play.mvc.Http;

import java.util.Optional;

public abstract class DefaultSessionHandler<T> implements SessionHandler<T> {

    protected Optional<String> findValue(final Http.Session session, final String key) {
        return Optional.ofNullable(session.get(key));
    }

    protected void overwriteValue(final Http.Session session, final String key, final String value) {
        session.put(key, value);
    }

    protected void removeValue(final Http.Session session, final String key) {
        session.remove(key);
    }
}
