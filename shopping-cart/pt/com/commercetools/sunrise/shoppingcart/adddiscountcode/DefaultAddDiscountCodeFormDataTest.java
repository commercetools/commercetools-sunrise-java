package com.commercetools.sunrise.shoppingcart.adddiscountcode;

import org.junit.Before;
import org.junit.Test;
import play.data.Form;
import play.data.FormFactory;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link DefaultAddDiscountCodeFormData}.
 */
public class DefaultAddDiscountCodeFormDataTest extends WithApplication {
    private Form<DefaultAddDiscountCodeFormData> form;

    @Before
    public void setup() {
        final FormFactory formFactory = app.injector().instanceOf(FormFactory.class);
        form = formFactory.form(DefaultAddDiscountCodeFormData.class);
    }

    @Test
    public void shouldAcceptValidFormData() {
        final Form<DefaultAddDiscountCodeFormData> validFormData =
                form.bind(addDiscountCodeFormData("SUNNY"));
        assertThat(validFormData.errors()).isEmpty();
    }

    @Test
    public void shouldReportEmptyDiscountCodeFormData() {
        final Form<DefaultAddDiscountCodeFormData> validFormData =
                form.bind(addDiscountCodeFormData(""));
        assertThat(validFormData.errors()).hasSize(1);
        assertThat(validFormData.error("code")).isNotNull();
    }

    @Test
    public void shouldReportNullDiscountCode() {
        final Form<DefaultAddDiscountCodeFormData> validFormData =
                form.bind(addDiscountCodeFormData(null));
        assertThat(validFormData.errors()).hasSize(1);
        assertThat(validFormData.error("code")).isNotNull();
    }

    private Map<String, String> addDiscountCodeFormData(final String code) {
        final Map<String, String> formData = new HashMap<>();
        formData.put("code", code);
        return formData;
    }
}
