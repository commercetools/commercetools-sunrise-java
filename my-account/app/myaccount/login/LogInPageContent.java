package myaccount.login;

import common.contexts.UserContext;
import common.controllers.PageContent;
import common.i18n.I18nResolver;
import play.Configuration;
import play.data.Form;

public class LogInPageContent extends PageContent {
    private LogInFormBean logInForm;
    private SignUpFormBean signUpForm;

    public LogInPageContent() {
    }

    public LogInPageContent(final Form<LogInFormData> logInForm) {
        this.logInForm = new LogInFormBean(logInForm);
    }

    public LogInPageContent(final Form<SignUpFormData> signUpForm, final UserContext userContext,
                            final I18nResolver i18nResolver, final Configuration configuration) {
        this.signUpForm = new SignUpFormBean(signUpForm, userContext, i18nResolver, configuration);
    }

    public LogInFormBean getLogInForm() {
        return logInForm;
    }

    public void setLogInForm(final LogInFormBean logInForm) {
        this.logInForm = logInForm;
    }

    public SignUpFormBean getSignUpForm() {
        return signUpForm;
    }

    public void setSignUpForm(final SignUpFormBean signUpForm) {
        this.signUpForm = signUpForm;
    }
}
