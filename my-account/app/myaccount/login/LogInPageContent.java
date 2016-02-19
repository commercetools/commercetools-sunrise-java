package myaccount.login;

import common.controllers.PageContent;

public class LogInPageContent extends PageContent {
    private LogInFormBean logInForm;

    public LogInPageContent() {
    }

    public LogInPageContent(final LogInFormData formData) {
        this.logInForm = new LogInFormBean(formData);
    }

    public LogInFormBean getLogInForm() {
        return logInForm;
    }

    public void setLogInForm(final LogInFormBean logInForm) {
        this.logInForm = logInForm;
    }
}
