package com.commercetools.sunrise.myaccount.login;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.forms.ErrorsBean;
import com.commercetools.sunrise.common.models.TitleFormFieldBean;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import io.sphere.sdk.models.Base;
import play.Configuration;

import javax.annotation.Nullable;

public class SignUpFormBean extends Base {

    private ErrorsBean errors;
    private TitleFormFieldBean titleFormField;
    private String firstName;
    private String lastName;
    private String email;
    private boolean agreeToTerms;

    public SignUpFormBean() {
    }

    public SignUpFormBean(final @Nullable String title, final @Nullable String firstName, final @Nullable String lastName,
                          final @Nullable String email, final boolean agreeToTerms, final UserContext userContext,
                          final I18nResolver i18nResolver, final Configuration configuration) {
        this.titleFormField = new TitleFormFieldBean(title, userContext, i18nResolver, configuration);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.agreeToTerms = agreeToTerms;
    }

    public SignUpFormBean(final UserContext userContext, final I18nResolver i18nResolver, final Configuration configuration) {
        this(null, null, null, null, false, userContext, i18nResolver, configuration);
    }

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(final ErrorsBean errors) {
        this.errors = errors;
    }

    public TitleFormFieldBean getSalutations() {
        return titleFormField;
    }

    public void setSalutations(final TitleFormFieldBean titleFormField) {
        this.titleFormField = titleFormField;
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
