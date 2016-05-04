package myaccount.login;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

import javax.annotation.Nullable;

public class SignUpFormData extends Base {

    private String title;
    @Constraints.Required
    private String firstName;
    @Constraints.Required
    private String lastName;
    @Constraints.Required
    private String email;
    @Constraints.Required
    private String password;
    @Constraints.Required
    private String confirmPassword;
    @Constraints.Required
    private boolean agreeToTerms;

    public String validate() {
        if (!password.equals(confirmPassword)) {
            return "Not matching passwords"; // TODO use i18n version
        }
        if (!agreeToTerms) {
            return "You must agree to terms"; // TODO use i18n version
        }
        return null;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(final String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isAgreeToTerms() {
        return agreeToTerms;
    }

    public void setAgreeToTerms(final boolean agreeToTerms) {
        this.agreeToTerms = agreeToTerms;
    }
}

