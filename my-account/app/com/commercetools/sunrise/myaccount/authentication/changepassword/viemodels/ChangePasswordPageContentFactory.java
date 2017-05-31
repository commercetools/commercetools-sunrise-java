package com.commercetools.sunrise.myaccount.authentication.changepassword.viemodels;

import com.commercetools.sunrise.framework.viewmodels.PageTitleResolver;
import com.commercetools.sunrise.framework.viewmodels.content.FormPageContentFactory;
import com.commercetools.sunrise.myaccount.authentication.changepassword.ChangePasswordFormData;
import play.data.Form;

import javax.inject.Inject;

public class ChangePasswordPageContentFactory extends FormPageContentFactory<ChangePasswordPageContent, Void, ChangePasswordFormData> {

    private final PageTitleResolver pageTitleResolver;

    @Inject
    protected ChangePasswordPageContentFactory(final PageTitleResolver pageTitleResolver) {
        this.pageTitleResolver = pageTitleResolver;
    }

    protected final PageTitleResolver getPageTitleResolver() {
        return pageTitleResolver;
    }

    @Override
    protected ChangePasswordPageContent newViewModelInstance(final Void input, final Form<? extends ChangePasswordFormData> form) {
        return new ChangePasswordPageContent();
    }

    public final ChangePasswordPageContent create(final Form<? extends ChangePasswordFormData> form) {
        return super.create(null, form);
    }

    @Override
    protected final void initialize(final ChangePasswordPageContent viewModel, final Void input, final Form<? extends ChangePasswordFormData> form) {
        super.initialize(viewModel, input, form);
        fillChangePasswordForm(viewModel, form);
    }

    @Override
    protected void fillTitle(final ChangePasswordPageContent viewModel, final Void input, final Form<? extends ChangePasswordFormData> form) {
        viewModel.setTitle(pageTitleResolver.getOrEmpty("my-account:changePassword.changePasswordTitle")); // TODO messages missing in FE templates
    }

    private void fillChangePasswordForm(final ChangePasswordPageContent viewModel, final Form<? extends ChangePasswordFormData> form) {
        viewModel.setChangePasswordForm(form);
    }
}
