package com.commercetools.sunrise.framework.controllers;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
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
public interface WithContentFormFlow<I, O, F> extends WithFormFlow<I, O, F>, WithContent {

    default CompletionStage<Result> showFormPage(final I input, final F emptyFormData) {
        final Form<? extends F> form = createFilledForm(input, emptyFormData);
        return okResultWithPageContent(createPageContent(input, form));
    }

    default CompletionStage<Result> showFormPageWithErrors(final I input, final Form<? extends F> form) {
        return badRequestResultWithPageContent(createPageContent(input, form));
    }

    @Override
    default CompletionStage<Result> handleInvalidForm(final I input, final Form<? extends F> form) {
        return showFormPageWithErrors(input, form);
    }

    @Override
    default CompletionStage<Result> handleClientErrorFailedAction(final I input, final Form<? extends F> form, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return showFormPageWithErrors(input, form);
    }

    PageContent createPageContent(final I input, final Form<? extends F> form);

    default Form<? extends F> createFilledForm(final I input, final F formData) {
        preFillFormData(input, formData);
        try {
            final Map<String, String> classFieldValues = BeanUtils.describe(formData);
            final Form<? extends F> filledForm = createForm().bind(classFieldValues);
            filledForm.discardErrors();
            return filledForm;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Form cannot be populated for class " + getFormDataClass().getCanonicalName(), e);
        }
    }

    void preFillFormData(final I input, final F formData);
}
