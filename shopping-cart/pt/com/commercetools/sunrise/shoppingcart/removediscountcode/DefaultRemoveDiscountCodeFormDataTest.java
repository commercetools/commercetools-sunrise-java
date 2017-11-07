package com.commercetools.sunrise.shoppingcart.removediscountcode;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import play.data.Form;
import play.data.FormFactory;
import play.test.WithApplication;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link DefaultRemoveDiscountCodeFormData}.
 */
public class DefaultRemoveDiscountCodeFormDataTest extends WithApplication {
    private Form<DefaultRemoveDiscountCodeFormData> form;

    @Before
    public void setup() {
        final FormFactory formFactory = app.injector().instanceOf(FormFactory.class);
        form = formFactory.form(DefaultRemoveDiscountCodeFormData.class);
    }

    @Test
    public void shouldAcceptValidFormData() {
        final Form<DefaultRemoveDiscountCodeFormData> validFormData =
                form.bind(removeDiscountCodeFormData(UUID.randomUUID().toString()));
        assertThat(validFormData.errors()).isEmpty();
    }

    @Test
    public void shouldReportEmptyDiscountCodeIdFormData() {
        final Form<DefaultRemoveDiscountCodeFormData> validFormData =
                form.bind(removeDiscountCodeFormData(""));
        assertThat(validFormData.errors()).hasSize(1);
        assertThat(validFormData.error("discountCodeId")).isNotNull();
    }

    private Map<String, String> removeDiscountCodeFormData(final String discountCodeId) {
        return ImmutableMap.of("discountCodeId", discountCodeId);
    }
}
