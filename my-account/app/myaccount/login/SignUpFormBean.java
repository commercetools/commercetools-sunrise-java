package myaccount.login;

import common.contexts.UserContext;
import common.errors.ErrorsBean;
import common.template.i18n.I18nResolver;
import common.models.SalutationsFieldsBean;
import io.sphere.sdk.models.Base;
import play.Configuration;
import play.data.Form;

import javax.annotation.Nullable;

public class SignUpFormBean extends Base {
    private ErrorsBean errors;
    private SalutationsFieldsBean salutations;
    private String firstName;
    private String lastName;
    private String email;
    private boolean agreeToTerms;

    public SignUpFormBean() {
    }

    public SignUpFormBean(@Nullable final Form<SignUpFormData> form, final UserContext userContext,
                          final I18nResolver i18nResolver, final Configuration configuration) {
        if (form != null) {
            this.salutations = new SalutationsFieldsBean(form.field("title").value(), userContext, i18nResolver, configuration);
            this.firstName = form.field("firstName").value();
            this.lastName = form.field("lastName").value();
            this.email = form.field("email").value();
            this.agreeToTerms = Boolean.valueOf(form.field("agreeToTerms").value());
        } else {
            this.salutations = new SalutationsFieldsBean(userContext, i18nResolver, configuration);
            this.agreeToTerms = false;
        }
    }

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(final ErrorsBean errors) {
        this.errors = errors;
    }

    public SalutationsFieldsBean getSalutations() {
        return salutations;
    }

    public void setSalutations(final SalutationsFieldsBean salutations) {
        this.salutations = salutations;
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

    public boolean isAgreeToTerms() {
        return agreeToTerms;
    }

    public void setAgreeToTerms(final boolean agreeToTerms) {
        this.agreeToTerms = agreeToTerms;
    }
}
