package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.PageTitleResolver;
import com.commercetools.sunrise.framework.viewmodels.content.FormPageContentFactory;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.RecoverPasswordFormData;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset.ResetPasswordFormData;
import play.data.Form;

import javax.inject.Inject;

/**
 * Creates the page content for resetting the customers password.
 *
 * @see RecoverPasswordPageContent
 * @see ResetPasswordFormData
 */
public class RecoverPasswordPageContentFactory extends FormPageContentFactory<RecoverPasswordPageContent, Void, RecoverPasswordFormData> {

    private final PageTitleResolver pageTitleResolver;

    @Inject
    protected RecoverPasswordPageContentFactory(final PageTitleResolver pageTitleResolver) {
        this.pageTitleResolver = pageTitleResolver;
    }

    protected final PageTitleResolver getPageTitleResolver() {
        return pageTitleResolver;
    }

    @Override
    public final RecoverPasswordPageContent create(final Void input, final Form<? extends RecoverPasswordFormData> form) {
        return super.create(input, form);
    }

    @Override
    protected RecoverPasswordPageContent newViewModelInstance(final Void input, final Form<? extends RecoverPasswordFormData> form) {
        return new RecoverPasswordPageContent();
    }

    @Override
    protected void fillTitle(final RecoverPasswordPageContent viewModel, final Void input, final Form<? extends RecoverPasswordFormData> form) {
        viewModel.setTitle(pageTitleResolver.getOrEmpty("my-account:paswordRecovery.title"));
    }

    public final RecoverPasswordPageContent create(final Form<? extends RecoverPasswordFormData> form) {
        return super.create(null, form);
    }

    @Override
    protected final void initialize(final RecoverPasswordPageContent viewModel, final Void input, final Form<? extends RecoverPasswordFormData> form) {
        super.initialize(viewModel, input, form);
        fillResetPasswordForm(viewModel, input, form);
    }

    protected void fillResetPasswordForm(final RecoverPasswordPageContent viewModel, final Void input, final Form<? extends RecoverPasswordFormData> form) {
        viewModel.setPasswordRecoveryForm(form);
    }
}
