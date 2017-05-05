package com.commercetools.sunrise.ctp.graphql;

import io.sphere.sdk.http.HttpResponse;

public class GraphQLException extends RuntimeException  {

    private static final long serialVersionUID = 0L;

    public GraphQLException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public GraphQLException(final String message, final HttpResponse httpResponse) {
        super(message + "\n" + httpResponse.toString());
    }
}
