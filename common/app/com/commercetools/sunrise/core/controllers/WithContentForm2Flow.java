package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.viewmodels.PageData;
import io.sphere.sdk.client.ClientErrorException;
import org.apache.commons.beanutils.BeanUtils;
import play.data.Form;
import play.mvc.Result;

import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * Approach to handle form data (Template Method Pattern).
 * @param <I> type of the input data of the form, possibly a parameter object
 * @param <O> type of the output object, normally the updated object if the form is valid
 * @param <F> stereotype of the in a form wrapped class
 */
public interface WithContentForm2Flow<F> extends WithForm2Flow<F>, WithContent {

    default CompletionStage<Result> showFormPage(final F emptyFormData) {
        final Form<? extends F> form = createFilledForm(emptyFormData);
        return okResult(createPageContent(form));
    }

    default CompletionStage<Result> showFormPageWithErrors(final Form<? extends F> form) {
        return badRequestResult(createPageContent(form));
    }

    @Override
    default CompletionStage<Result> handleInvalidForm(final Form<? extends F> form) {
        return showFormPageWithErrors(form);
    }

    @Override
    default CompletionStage<Result> handleClientErrorFailedAction(final Form<? extends F> form, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return showFormPageWithErrors(form);
    }

    PageData createPageContent(final Form<? extends F> form);

    default Form<? extends F> createFilledForm(final F formData) {
        preFillFormData(formData);
        try {
            final Map<String, String> classFieldValues = BeanUtils.describe(formData);
            final Form<? extends F> filledForm = createForm().bind(classFieldValues);
            filledForm.discardErrors();
            return filledForm;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Form cannot be populated for class " + getFormDataClass().getCanonicalName(), e);
        }
    }

    void preFillFormData(final F formData);
}
