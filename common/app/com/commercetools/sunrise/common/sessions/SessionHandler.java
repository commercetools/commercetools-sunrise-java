package com.commercetools.sunrise.common.sessions;

import play.mvc.Http;

public interface SessionHandler<T> {

    void overwriteSession(final Http.Session session, final T value);

    void removeFromSession(final Http.Session session);
}
