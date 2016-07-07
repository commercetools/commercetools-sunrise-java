package com.commercetools.sunrise.http;

import com.commercetools.sunrise.common.ctp.SphereClientCredentialsException;
import com.google.inject.ProvisionException;
import play.Configuration;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import scala.Option;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class SunriseDefaultHttpErrorHandler extends DefaultHttpErrorHandler {
    private final Option<String> playEditor;

    @Inject
    public SunriseDefaultHttpErrorHandler(final Configuration configuration, final Environment environment, final OptionalSourceMapper sourceMapper, final Provider<Router> routes) {
        super(configuration, environment, sourceMapper, routes);
        this.playEditor = Option.apply(configuration.getString("play.editor"));
    }

    @Override
    protected CompletionStage<Result> onDevServerError(final Http.RequestHeader request, final UsefulException exception) {
        return Optional.ofNullable(exception.getCause())
                .map(Throwable::getCause)
                .filter(e -> e instanceof ProvisionException)
                .map(e -> (ProvisionException) e)
                .filter(e -> e.getErrorMessages().stream().anyMatch(m -> m.getCause() instanceof SphereClientCredentialsException))
                .map(e -> (CompletionStage<Result>) CompletableFuture.completedFuture(Results.internalServerError(views.html.defaultpages.devError.render(playEditor, new SphereCredentialsUsefulException(exception)))))
                .orElseGet(() ->  super.onDevServerError(request, exception));
    }

    private static class SphereCredentialsUsefulException extends UsefulException {

        public static final String TITLE = "The commercetools platform credentials are not complete.";

        public SphereCredentialsUsefulException(final UsefulException exception) {
            super(TITLE, exception);
            this.title = TITLE;
            this.description = "One solution could be to create a file dev.conf or use application.conf to set these properties:\n" +
                    "ctp.projectKey=\"YOUR PROJECT KEY\"\n" +
                    "ctp.clientId=\"YOUR CLIENT ID\"\n" +
                    "ctp.clientSecret=\"YOUR CLIENT SECRET\"\n" +
                    "ctp.authUrl=\"https://auth.sphere.io\"\n" +
                    "ctp.apiUrl=\"https://api.sphere.io\"\n\n" + exception.description;
            this.cause = exception;
        }
    }

}
