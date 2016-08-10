package com.commercetools.sunrise.common.sessions;

import play.mvc.Http;

import javax.annotation.Nullable;

public interface SessionHandler<T> {

    void overwriteInSession(final Http.Session session, @Nullable final T value);

    void removeFromSession(final Http.Session session);
}
