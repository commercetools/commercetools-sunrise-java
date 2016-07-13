package com.commercetools.sunrise.common.basicauth;

import akka.stream.Materializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.mvc.Http.HeaderNames.AUTHORIZATION;
import static play.mvc.Http.HeaderNames.WWW_AUTHENTICATE;
import static play.mvc.Results.unauthorized;

/**
 * Request handler that enables HTTP basic access authentication.
 */
public class BasicAuthFilter extends Filter {

    private static final Logger logger = LoggerFactory.getLogger(BasicAuthFilter.class);

    @Nullable
    private final BasicAuth basicAuth;

    @Inject
    public BasicAuthFilter(final Materializer mat, @Nullable final BasicAuth basicAuth) {
        super(mat);
        this.basicAuth = basicAuth;
    }

    @Override
    public CompletionStage<Result> apply(final Function<Http.RequestHeader, CompletionStage<Result>> nextFilter,
                                         final Http.RequestHeader requestHeader) {
        if (basicAuth != null) {
            return authenticate(basicAuth, nextFilter, requestHeader);
        } else {
            return nextFilter.apply(requestHeader);
        }
    }

    private CompletionStage<Result> authenticate(final BasicAuth basicAuth,
                                                 final Function<Http.RequestHeader, CompletionStage<Result>> nextFilter,
                                                 final Http.RequestHeader requestHeader) {
        final String authorizationHeader = requestHeader.getHeader(AUTHORIZATION);
        if (authorizationHeader != null) {
            if (basicAuth.isAuthorized(authorizationHeader)) {
                return successfulAuthentication(nextFilter, requestHeader);
            } else {
                return failedAuthentication();
            }
        } else {
            return missingAuthentication(basicAuth);
        }
    }

    private CompletionStage<Result> successfulAuthentication(final Function<Http.RequestHeader, CompletionStage<Result>> nextFilter, final Http.RequestHeader requestHeader) {
        logger.trace("Authorized");
        return nextFilter.apply(requestHeader);
    }

    private CompletableFuture<Result> missingAuthentication(final BasicAuth basicAuth) {
        logger.debug("Missing authentication");
        return completedFuture(unauthorized()).thenApply(result ->
                result.withHeader(WWW_AUTHENTICATE, "Basic realm=\"" + basicAuth.getRealm() + "\""));
    }

    private CompletableFuture<Result> failedAuthentication() {
        logger.info("Failed authentication");
        return completedFuture(unauthorized());
    }

}
