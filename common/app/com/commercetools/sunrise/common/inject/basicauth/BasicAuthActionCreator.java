package com.commercetools.sunrise.common.inject.basicauth;

import play.Logger;
import play.http.DefaultActionCreator;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.mvc.Http.HeaderNames.AUTHORIZATION;
import static play.mvc.Http.HeaderNames.WWW_AUTHENTICATE;

/**
 * Request handler that enables HTTP basic access authentication.
 */
public class BasicAuthActionCreator extends DefaultActionCreator {
    private static final Logger.ALogger LOGGER = Logger.of(BasicAuthActionCreator.class);
    private final Optional<BasicAuth> basicAuth;

    @Inject
    public BasicAuthActionCreator(@Nullable final BasicAuth basicAuth) {
        this.basicAuth = Optional.ofNullable(basicAuth);
    }

    @Override
    public Action createAction(final Http.Request request, final Method actionMethod) {
        if (basicAuth.isPresent()) {
            return authenticate(basicAuth.get());
        } else {
            return super.createAction(request, actionMethod);
        }
    }

    private Action authenticate(final BasicAuth basicAuth) {
        return new Action.Simple() {

            @Override
            public CompletionStage<Result> call(final Http.Context ctx) {
                final boolean isAuthorized;
                final String authorizationHeader = ctx.request().getHeader(AUTHORIZATION);
                if (authorizationHeader != null) {
                    isAuthorized = basicAuth.isAuthorized(authorizationHeader);
                } else {
                    isAuthorized = false;
                    ctx.response().setHeader(WWW_AUTHENTICATE, "Basic realm=\"" + basicAuth.getRealm() + "\"");
                }
                return authenticationResult(ctx, isAuthorized);
            }

            private CompletionStage<Result> authenticationResult(final Http.Context ctx, final boolean isAuthorized) {
                if (isAuthorized) {
                    LOGGER.debug("Authorized");
                    return delegate.call(ctx);
                } else {
                    LOGGER.info("Unauthorized");
                    return CompletableFuture.completedFuture(unauthorized());
                }
            }
        };
    }
}
