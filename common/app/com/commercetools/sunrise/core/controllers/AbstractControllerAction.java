package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import io.sphere.sdk.client.ClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

import static io.sphere.sdk.utils.CompletableFutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.CompletableFutureUtils.recoverWith;
import static play.mvc.Http.Status.BAD_REQUEST;

public abstract class AbstractControllerAction<F> implements ControllerAction, WithContent {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ControllerAction.class);

    private final FormFactory formFactory;
    private final TemplateEngine templateEngine;

    protected AbstractControllerAction(final FormFactory formFactory, final TemplateEngine templateEngine) {
        this.formFactory = formFactory;
        this.templateEngine = templateEngine;
    }

    @Override
    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    protected final FormFactory getFormFactory() {
        return formFactory;
    }

    protected abstract Class<? extends F> formClass();

    protected abstract String formName();

    @Override
    public CompletionStage<Result> apply(final Supplier<Result> onSuccess) {
        final Form<? extends F> form = formFactory.form(formClass()).bindFromRequest();
        if (form.hasErrors()) {
            return render(BAD_REQUEST, buildPageDataOnBadRequest(form));
        } else {
            final CompletionStage<Result> result = executeRequest(form.get())
                    .thenApplyAsync(unused -> onSuccess.get(), HttpExecution.defaultContext());
            return recoverWith(result, throwable -> handleFailedRequest(form, throwable), HttpExecution.defaultContext());
        }
    }

    protected abstract CompletionStage<?> executeRequest(final F formData);

    protected CompletionStage<Result> handleFailedRequest(final Form<? extends F> form, final Throwable throwable) {
        if (!form.hasErrors() && throwable.getCause() instanceof ClientErrorException) {
            LOGGER.warn("An unknown client error has occurred", throwable);
            form.reject("errors.unknown");
            return render(BAD_REQUEST, buildPageDataOnBadRequest(form));
        }
        return exceptionallyCompletedFuture(throwable);
    }

    protected PageData buildPageDataOnBadRequest(final Form<? extends F> form) {
        return PageData.of().put(formName(), form);
    }
}
