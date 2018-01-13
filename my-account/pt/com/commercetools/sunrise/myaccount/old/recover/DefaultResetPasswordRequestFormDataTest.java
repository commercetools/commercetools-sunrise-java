package com.commercetools.sunrise.myaccount.old.recover;

import com.commercetools.sunrise.myaccount.resetpassword.DefaultResetPasswordRequestFormData;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import play.data.Form;
import play.data.FormFactory;
import play.test.WithApplication;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link DefaultResetPasswordRequestFormData}.
 */
public class DefaultResetPasswordRequestFormDataTest extends WithApplication {
    private Form<DefaultResetPasswordRequestFormData> form;

    @Before
    public void setup() {
        final FormFactory formFactory = app.injector().instanceOf(FormFactory.class);
        form = formFactory.form(DefaultResetPasswordRequestFormData.class);
    }

    @Test
    public void shouldAcceptValidFormData() {
        final Form<DefaultResetPasswordRequestFormData> validFormData =
                form.bind(recoverPasswordFormData("test@example.com"));
        assertThat(validFormData.errors()).isEmpty();
    }

    @Test
    public void shouldReportInvalidEmail() {
        final Form<DefaultResetPasswordRequestFormData> invalidEmail =
                form.bind(recoverPasswordFormData("test@"));
        assertThat(invalidEmail.errors()).hasSize(1);
        assertThat(invalidEmail.error("email")).isNotNull();
    }

    @Test
    public void shouldReportMissingEmail() {
        final Form<DefaultResetPasswordRequestFormData> emailMissing =
                form.bind(recoverPasswordFormData(""));
        assertThat(emailMissing.errors()).hasSize(1);
        assertThat(emailMissing.error("email")).isNotNull();
    }

    private Map<String, String> recoverPasswordFormData(final String email) {
        return ImmutableMap.of("email", email);
    }
}
