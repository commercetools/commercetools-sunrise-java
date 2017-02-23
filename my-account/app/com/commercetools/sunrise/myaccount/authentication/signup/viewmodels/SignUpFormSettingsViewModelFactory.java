package com.commercetools.sunrise.myaccount.authentication.signup.viewmodels;

import com.commercetools.sunrise.common.models.FormViewModelFactory;
import com.commercetools.sunrise.common.models.addresses.TitleFormFieldViewModelFactory;
import com.commercetools.sunrise.myaccount.authentication.signup.SignUpFormData;
import play.data.Form;
import play.data.FormFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class SignUpFormSettingsViewModelFactory extends FormViewModelFactory<SignUpFormSettingsViewModel, Void, SignUpFormData> {

    private final FormFactory formFactory;
    private final TitleFormFieldViewModelFactory titleFormFieldViewModelFactory;

    @Inject
    public SignUpFormSettingsViewModelFactory(final FormFactory formFactory, final TitleFormFieldViewModelFactory titleFormFieldViewModelFactory) {
        this.formFactory = formFactory;
        this.titleFormFieldViewModelFactory = titleFormFieldViewModelFactory;
    }

    @Override
    protected SignUpFormSettingsViewModel getViewModelInstance(final Void input) {
        return new SignUpFormSettingsViewModel();
    }

    @Override
    public final SignUpFormSettingsViewModel create(final Void input, @Nullable final Form<? extends SignUpFormData> form) {
        return super.create(input, form);
    }

    public final SignUpFormSettingsViewModel create(@Nullable final Form<? extends SignUpFormData> form) {
        return super.create(null, form);
    }

    @Override
    protected final void initialize(final SignUpFormSettingsViewModel viewModel, final Void input, @Nullable final Form<? extends SignUpFormData> form) {
        fillTitle(viewModel, form);
    }

    protected void fillTitle(final SignUpFormSettingsViewModel viewModel, @Nullable final Form<? extends SignUpFormData> form) {
        final Form<?> nonNullForm = createForm(form);
        viewModel.setTitle(titleFormFieldViewModelFactory.createWithDefaultOptions(nonNullForm.field("title")));
    }

    private Form<?> createForm(@Nullable final Form<?> form) {
        return form != null ? form : formFactory.form();
    }
}
