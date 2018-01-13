package com.commercetools.sunrise.core;

import io.sphere.sdk.client.ClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.sphere.sdk.utils.CompletableFutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.CompletableFutureUtils.recoverWith;

public abstract class AbstractFormAction<F> implements FormAction<F> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(FormAction.class);

    private final FormFactory formFactory;

    protected AbstractFormAction(final FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    protected final FormFactory getFormFactory() {
        return formFactory;
    }

    protected abstract Class<? extends F> formClass();

    protected Form<? extends F> createForm() {
        return formFactory.form(formClass());
    }

    @Override
    public CompletionStage<Result> apply(final Supplier<Result> onSuccess, final Function<Form<? extends F>, CompletionStage<Result>> onBadRequest) {
        final Form<? extends F> form = createForm().bindFromRequest();
        if (form.hasErrors()) {
            return onBadRequest.apply(form);
        } else {
            final CompletionStage<Result> result = onValidForm(form.get())
                    .thenApplyAsync(unused -> onSuccess.get(), HttpExecution.defaultContext());
            return recoverWith(result, throwable -> onFailedRequest(form, throwable, onBadRequest), HttpExecution.defaultContext());
        }
    }

    protected abstract CompletionStage<?> onValidForm(final F formData);

    protected CompletionStage<Result> onFailedRequest(final Form<? extends F> form, final Throwable throwable,
                                                      final Function<Form<? extends F>, CompletionStage<Result>> onBadRequest) {
        if (!form.hasErrors() && throwable.getCause() instanceof ClientErrorException) {
            LOGGER.error("An unhandled client error has occurred", throwable);
            form.reject("errors.unknown");
            return onBadRequest.apply(form);
        }
        return exceptionallyCompletedFuture(throwable);
    }
}
