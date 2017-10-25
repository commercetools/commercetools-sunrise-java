package com.commercetools.sunrise.common.template.engine.handlebars;

import com.commercetools.sunrise.common.forms.ErrorBean;
import com.commercetools.sunrise.common.forms.ErrorsBean;
import org.junit.Test;
import org.mockito.Mockito;
import play.data.Form;
import play.data.validation.ValidationError;

import java.util.*;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class PlayJavaFormResolverTest {

    PlayJavaFormResolver formResolver = new PlayJavaFormResolver(singletonList(Locale.ENGLISH), (locales, message, args) ->
            message);

    @Test
    public void extractErrors() throws Exception {
        String errorField1 = "field1";
        String errorField2 = "field2";
        Form form = formWithSomeErrorsForFields(errorField1, errorField2);
        ErrorsBean result = formResolver.extractErrors(form);

        List<ErrorBean> errors = result.getGlobalErrors();
        checkError(errors.get(0), errorField1, "errorMessage1");
        checkError(errors.get(1), errorField1, "errorMessage2");
        checkError(errors.get(2), errorField2, "errorMessage21");
    }

    private void checkError(ErrorBean error, String field, String message) {
        assertThat(error.getField()).isEqualTo(field);
        assertThat(error.getMessage()).isEqualTo(message);

    }

    private Form formWithSomeErrorsForFields(String field1, String field2) {

        Form form = Mockito.mock(Form.class);
        Map<String, List<ValidationError>> errorMap = new HashMap<String, List<ValidationError>>();
        errorMap.put(field1, Arrays.asList(
                new ValidationError("errorkey1", "errorMessage1"),
                new ValidationError("errorkey2", "errorMessage2")));
        errorMap.put(field2, Collections.singletonList(new ValidationError("errorkey21", "errorMessage21")));
        Mockito.when(form.errors()).thenReturn(errorMap);
        return form;
    }
}
