package com.commercetools.sunrise.httpauth;

import akka.stream.Materializer;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.mvc.Http.HeaderNames.AUTHORIZATION;
import static play.mvc.Http.HeaderNames.WWW_AUTHENTICATE;
import static play.mvc.Results.unauthorized;

/**
 * Request filter that enables HTTP Access Authentication.
 */
public class HttpAuthenticationFilter extends Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpAuthenticationFilter.class);

    @Nullable
    @Inject(optional = true)
    private HttpAuthentication httpAuthentication;

    @Inject
    public HttpAuthenticationFilter(final Materializer mat) {
        super(mat);
    }

    @Override
    public CompletionStage<Result> apply(final Function<Http.RequestHeader, CompletionStage<Result>> nextFilter,
                                         final Http.RequestHeader requestHeader) {
        if (httpAuthentication != null && httpAuthentication.isEnabled()) {
            return authenticate(nextFilter, requestHeader, httpAuthentication);
        } else {
            return nextFilter.apply(requestHeader);
        }
    }

    private CompletionStage<Result> authenticate(final Function<Http.RequestHeader, CompletionStage<Result>> nextFilter,
                                                 final Http.RequestHeader requestHeader, final HttpAuthentication httpAuthentication) {
        return findAuthorizationHeader(requestHeader)
                .map(authorizationHeader -> {
                    if (httpAuthentication.isAuthorized(authorizationHeader)) {
                        return successfulAuthentication(nextFilter, requestHeader);
                    } else {
                        return failedAuthentication();
                    }
                }).orElseGet(() -> missingAuthentication(httpAuthentication));
    }

    private CompletionStage<Result> successfulAuthentication(final Function<Http.RequestHeader, CompletionStage<Result>> nextFilter,
                                                             final Http.RequestHeader requestHeader) {
        LOGGER.debug("Authorized");
        return nextFilter.apply(requestHeader);
    }

    private CompletableFuture<Result> missingAuthentication(final HttpAuthentication httpAuthentication) {
        LOGGER.debug("Missing authentication");
        final Result result = unauthorized()
                .withHeader(WWW_AUTHENTICATE, httpAuthentication.getWwwAuthenticateHeaderValue());
        return completedFuture(result);
    }

    private CompletableFuture<Result> failedAuthentication() {
        LOGGER.debug("Failed authentication");
        return completedFuture(unauthorized("Unauthorized"));
    }

    private Optional<String> findAuthorizationHeader(final Http.RequestHeader requestHeader) {
        return Optional.ofNullable(requestHeader.getHeader(AUTHORIZATION));
    }
}
