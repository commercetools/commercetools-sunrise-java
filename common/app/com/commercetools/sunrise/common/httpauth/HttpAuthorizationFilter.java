package com.commercetools.sunrise.common.httpauth;

import akka.stream.Materializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
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
public class HttpAuthorizationFilter extends Filter {

    private static final Logger logger = LoggerFactory.getLogger(HttpAuthorizationFilter.class);

    @Inject
    private HttpAuthentication httpAuthentication;

    @Inject
    public HttpAuthorizationFilter(final Materializer mat) {
        super(mat);
    }

    @Override
    public CompletionStage<Result> apply(final Function<Http.RequestHeader, CompletionStage<Result>> nextFilter,
                                         final Http.RequestHeader requestHeader) {
        if (httpAuthentication.isEnabled()) {
            return authenticate(nextFilter, requestHeader);
        } else {
            return nextFilter.apply(requestHeader);
        }
    }

    private CompletionStage<Result> authenticate(final Function<Http.RequestHeader, CompletionStage<Result>> nextFilter,
                                                 final Http.RequestHeader requestHeader) {
        return findAuthorizationHeader(requestHeader)
                .map(authorizationHeader -> {
                    if (httpAuthentication.isAuthorized(authorizationHeader)) {
                        return successfulAuthentication(nextFilter, requestHeader);
                    } else {
                        return failedAuthentication();
                    }
                }).orElseGet(this::missingAuthentication);
    }

    private CompletionStage<Result> successfulAuthentication(final Function<Http.RequestHeader, CompletionStage<Result>> nextFilter,
                                                             final Http.RequestHeader requestHeader) {
        logger.trace("Authorized");
        return nextFilter.apply(requestHeader);
    }

    private CompletableFuture<Result> missingAuthentication() {
        logger.debug("Missing authentication");
        final Result result = unauthorized()
                .withHeader(WWW_AUTHENTICATE, httpAuthentication.getWwwAuthenticateHeaderValue());
        return completedFuture(result);
    }

    private CompletableFuture<Result> failedAuthentication() {
        logger.info("Failed authentication");
        return completedFuture(unauthorized("Unauthorized"));
    }

    private Optional<String> findAuthorizationHeader(final Http.RequestHeader requestHeader) {
        return Optional.ofNullable(requestHeader.getHeader(AUTHORIZATION));
    }

}
