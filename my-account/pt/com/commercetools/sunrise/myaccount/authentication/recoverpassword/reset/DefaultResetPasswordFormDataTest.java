package com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import play.data.Form;
import play.data.FormFactory;
import play.test.WithApplication;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link DefaultResetPasswordFormData}.
 */
public class DefaultResetPasswordFormDataTest extends WithApplication {

    private Form<DefaultResetPasswordFormData> form;

    @Before
    public void setup() {
        final FormFactory formFactory = app.injector().instanceOf(FormFactory.class);
        form = formFactory.form(DefaultResetPasswordFormData.class);
    }

    @Test
    public void shouldAcceptValidFormData() {
        final Form<DefaultResetPasswordFormData> validResetPasswordFormData =
                form.bind(resetPasswordFormData("password", "password"));
        assertThat(validResetPasswordFormData.errors()).isEmpty();
    }

    @Test
    public void shouldReportThatPasswordsAreNotEqual() {
        final Form<DefaultResetPasswordFormData> passwordsNotEqual =
                form.bind(resetPasswordFormData("password", "p"));
        assertThat(passwordsNotEqual.errors()).hasSize(1);
        assertThat(passwordsNotEqual.error(""))
                .describedAs("Reported with an empty key because it's a cross field validation")
                .isNotNull();
    }

    @Test
    public void shouldReportThatNewPasswordIsRequired() {
        final Form<DefaultResetPasswordFormData> newPasswordIsMissing =
                form.bind(resetPasswordFormData("", "password"));
        assertThat(newPasswordIsMissing.errors()).hasSize(1);
        assertThat(newPasswordIsMissing.error("newPassword")).isNotNull();
    }

    @Test
    public void shouldReportThatConfirmPasswordIsRequired() {
        final Form<DefaultResetPasswordFormData> confirmPasswordIsMissing =
                form.bind(resetPasswordFormData("password", ""));
        assertThat(confirmPasswordIsMissing.errors()).hasSize(1);
        assertThat(confirmPasswordIsMissing.error("confirmPassword")).isNotNull();
    }

    private Map<String, String> resetPasswordFormData(final String newPassword, final String confirmPassword) {
        return ImmutableMap.of("newPassword", newPassword, "confirmPassword", confirmPassword);
    }
}
