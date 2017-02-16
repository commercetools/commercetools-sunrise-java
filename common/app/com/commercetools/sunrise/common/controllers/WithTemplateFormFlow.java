package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.pages.PageContent;
import org.apache.commons.beanutils.BeanUtils;
import play.data.Form;
import play.mvc.Result;

import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * Approach to handle form data (Template Method Pattern).
 * @param <F> stereotype of the in a form wrapped class
 * @param <I> type of the input data of the form, possibly a parameter object
 * @param <O> type of the output object, normally the updated object if the form is valid
 */
public interface WithTemplateFormFlow<F, I, O> extends WithFormFlow<F, I, O>, WithTemplate {

    default CompletionStage<Result> showFormPage(final I input) {
        final Form<F> form = createNewFilledForm(input);
        return okResultWithPageContent(createPageContent(input, form));
    }

    default CompletionStage<Result> showFormPageWithErrors(final I input, final Form<F> form) {
        return badRequestResultWithPageContent(createPageContent(input, form));
    }

    @Override
    default CompletionStage<Result> handleInvalidForm(final I input, final Form<F> form) {
        return showFormPageWithErrors(input, form);
    }

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
}
