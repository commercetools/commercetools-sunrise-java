package com.commercetools.sunrise.myaccount.authentication.signup.viewmodels;

import com.commercetools.sunrise.common.models.addresses.TitleFormFieldViewModelFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.myaccount.authentication.signup.SignUpFormData;
import play.data.Form;
import play.data.FormFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class SignUpFormSettingsViewModelFactory extends ViewModelFactory<SignUpFormSettingsViewModel, Form<? extends SignUpFormData>> {

    private final FormFactory formFactory;
    private final TitleFormFieldViewModelFactory titleFormFieldViewModelFactory;

    @Inject
    public SignUpFormSettingsViewModelFactory(final FormFactory formFactory, final TitleFormFieldViewModelFactory titleFormFieldViewModelFactory) {
        this.formFactory = formFactory;
        this.titleFormFieldViewModelFactory = titleFormFieldViewModelFactory;
    }

    @Override
    protected SignUpFormSettingsViewModel getViewModelInstance() {
        return new SignUpFormSettingsViewModel();
    }

    @Override
    public final SignUpFormSettingsViewModel create(@Nullable final Form<? extends SignUpFormData> input) {
        return super.create(input);
    }

    @Override
    protected final void initialize(final SignUpFormSettingsViewModel viewModel, @Nullable final Form<? extends SignUpFormData> form) {
        fillTitle(viewModel, form);
    }

    protected void fillTitle(final SignUpFormSettingsViewModel model, @Nullable final Form<? extends SignUpFormData> form) {
        final Form<?> nonNullForm = createForm(form);
        model.setTitle(titleFormFieldViewModelFactory.createWithDefaultOptions(nonNullForm.field("title")));
    }

    private Form<?> createForm(@Nullable final Form<?> form) {
        return form != null ? form : formFactory.form();
    }
}
