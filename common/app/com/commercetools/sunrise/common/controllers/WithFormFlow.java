package com.commercetools.sunrise.common.controllers;

import io.sphere.sdk.client.ClientErrorException;
import org.apache.commons.beanutils.BeanUtils;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.CompletableFutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.CompletableFutureUtils.recoverWith;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Approach to handle form data (Template Method Pattern).
 * @param <F> stereotype of the in a form wrapped class
 * @param <I> type of the input data of the form, possibly a parameter object
 * @param <O> type of the output object, normally the updated object if the form is valid
 */
public interface WithFormFlow<F, I, O> extends WithForm<F> {

    default CompletionStage<Result> showFormPage(final I input) {
        final Form<F> form = createNewFilledForm(input);
        return renderPage(form, input, null)
                .thenApplyAsync(Results::ok, HttpExecution.defaultContext());
    }

    default CompletionStage<Result> processForm(final I input) {
        return validateForm(input, bindForm()).thenComposeAsync(form -> {
            if (!form.hasErrors()) {
                return handleValidForm(form, input);
            } else {
                return handleInvalidForm(form, input);
            }
        }, HttpExecution.defaultContext());
    }

    default CompletionStage<Form<F>> validateForm(final I input, final Form<F> form) {
        return completedFuture(form);
    }

    default CompletionStage<Result> handleInvalidForm(final Form<F> form, final I input) {
        return renderPage(form, input, null)
                .thenApplyAsync(Results::badRequest, HttpExecution.defaultContext());
    }

    default CompletionStage<Result> handleValidForm(final Form<F> form, final I input) {
        final CompletionStage<Result> resultStage = doAction(form.get(), input)
                .thenComposeAsync(output -> handleSuccessfulAction(form.get(), input, output), HttpExecution.defaultContext());
        return recoverWith(resultStage, throwable -> handleFailedAction(form, input, throwable), HttpExecution.defaultContext());
    }

    CompletionStage<O> doAction(final F formData, final I input);

    default CompletionStage<Result> handleFailedAction(final Form<F> form, final I input, final Throwable throwable) {
        final Throwable causeThrowable = throwable.getCause();
        if (causeThrowable instanceof ClientErrorException) {
            return handleClientErrorFailedAction(form, input, (ClientErrorException) causeThrowable);
        }
        return handleGeneralFailedAction(throwable);
    }

    CompletionStage<Result> handleClientErrorFailedAction(final Form<F> form, final I input, final ClientErrorException clientErrorException);

    default CompletionStage<Result> handleGeneralFailedAction(final Throwable throwable) {
        return exceptionallyCompletedFuture(throwable);
    }

    CompletionStage<Result> handleSuccessfulAction(final F formData, final I input, final O output);

    CompletionStage<Html> renderPage(final Form<F> form, final I input, @Nullable final O output);

    default Form<F> createNewFilledForm(final I input) {
        try {
            final F formData = getFormDataClass().getConstructor().newInstance();
            preFillFormData(formData, input);
            final Map<String, String> classFieldValues = BeanUtils.describe(formData);
            final Form<F> filledForm = formFactory().form(getFormDataClass()).bind(classFieldValues);
            filledForm.discardErrors();
            return filledForm;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Missing empty constructor for class " + getFormDataClass().getCanonicalName(), e);
        }
    }

    void preFillFormData(final F formData, final I input);
}
