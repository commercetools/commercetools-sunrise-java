package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.pages.PageContent;
import io.sphere.sdk.client.ClientErrorException;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

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
public interface WithFormFlow<F, I, O> extends WithForm<F>, WithTemplate {

    Logger getLogger();

    default CompletionStage<Result> showFormPage(final I input) {
        final Form<F> form = createNewFilledForm(input);
        return okResultWithPageContent(createPageContent(input, form));
    }

    default CompletionStage<Result> showFormPageWithErrors(final I input, final Form<F> form) {
        return badRequestResultWithPageContent(createPageContent(input, form));
    }

    default CompletionStage<Result> processForm(final I input) {
        return validateForm(input, bindForm()).thenComposeAsync(form -> {
            if (!form.hasErrors()) {
                return handleValidForm(input, form);
            } else {
                return handleInvalidForm(input, form);
            }
        }, HttpExecution.defaultContext());
    }

    default CompletionStage<Form<F>> validateForm(final I input, final Form<F> form) {
        return completedFuture(form);
    }

    default CompletionStage<Result> handleInvalidForm(final I input, final Form<F> form) {
        return badRequestResultWithPageContent(createPageContent(input, form));
    }

    default CompletionStage<Result> handleValidForm(final I input, final Form<F> form) {
        final CompletionStage<Result> resultStage = executeAction(input, form.get())
                .thenComposeAsync(output -> handleSuccessfulAction(output, form.get()), HttpExecution.defaultContext());
        return recoverWith(resultStage, throwable -> handleFailedAction(input, form, throwable), HttpExecution.defaultContext());
    }

    CompletionStage<O> executeAction(final I input, final F formData);

    default CompletionStage<Result> handleFailedAction(final I input, final Form<F> form, final Throwable throwable) {
        final Throwable causeThrowable = throwable.getCause();
        if (causeThrowable instanceof ClientErrorException) {
            return handleClientErrorFailedAction(input, form, (ClientErrorException) causeThrowable);
        }
        return handleGeneralFailedAction(throwable);
    }

    CompletionStage<Result> handleClientErrorFailedAction(final I input, final Form<F> form, final ClientErrorException clientErrorException);

    default CompletionStage<Result> handleGeneralFailedAction(final Throwable throwable) {
        return exceptionallyCompletedFuture(throwable);
    }

    CompletionStage<Result> handleSuccessfulAction(final O output, final F formData);

    PageContent createPageContent(final I input, final Form<F> form);

    default Form<F> createNewFilledForm(final I input) {
        try {
            final F formData = getFormDataClass().getConstructor().newInstance();
            preFillFormData(input, formData);
            final Map<String, String> classFieldValues = BeanUtils.describe(formData);
            final Form<F> filledForm = getFormFactory().form(getFormDataClass()).bind(classFieldValues);
            filledForm.discardErrors();
            return filledForm;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Missing empty constructor for class " + getFormDataClass().getCanonicalName(), e);
        }
    }

    void preFillFormData(final I input, final F formData);

    default void saveFormError(final Form<?> form, final String message) {
        form.reject(message);
    }

    default void saveUnexpectedFormError(final Form<?> form, final Throwable throwable) {
        form.reject("Something went wrong, please try again"); // TODO i18n
        getLogger().error("The CTP request raised an unexpected exception", throwable);
    }
}
