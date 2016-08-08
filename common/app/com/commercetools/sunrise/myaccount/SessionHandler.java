package com.commercetools.sunrise.myaccount;

import play.mvc.Http;

import java.util.Optional;

public interface SessionHandler<T> {

    Optional<T> findInSession(final Http.Session session);

    void overwriteSession(final Http.Session session, final T value);

    void removeFromSession(final Http.Session session);
}
