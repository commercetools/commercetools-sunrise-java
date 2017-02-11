package com.commercetools.sunrise.myaccount.authentication.signup.view;

import com.commercetools.sunrise.common.forms.TitleFormFieldBeanFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.myaccount.authentication.signup.SignUpFormData;
import play.data.Form;
import play.data.FormFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class SignUpFormSettingsBeanFactory extends ViewModelFactory<SignUpFormSettingsBean, Form<? extends SignUpFormData>> {

    private final FormFactory formFactory;
    private final TitleFormFieldBeanFactory titleFormFieldBeanFactory;

    @Inject
    public SignUpFormSettingsBeanFactory(final FormFactory formFactory, final TitleFormFieldBeanFactory titleFormFieldBeanFactory) {
        this.formFactory = formFactory;
        this.titleFormFieldBeanFactory = titleFormFieldBeanFactory;
    }

    @Override
    protected SignUpFormSettingsBean getViewModelInstance() {
        return new SignUpFormSettingsBean();
    }

    @Override
    public final SignUpFormSettingsBean create(@Nullable final Form<? extends SignUpFormData> data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final SignUpFormSettingsBean model, @Nullable final Form<? extends SignUpFormData> form) {
        fillTitle(model, form);
    }

    protected void fillTitle(final SignUpFormSettingsBean model, @Nullable final Form<? extends SignUpFormData> form) {
        final Form<?> nonNullForm = createForm(form);
        model.setTitle(titleFormFieldBeanFactory.createWithDefaultOptions(nonNullForm.field("title")));
    }

    private Form<?> createForm(@Nullable final Form<?> form) {
        return form != null ? form : formFactory.form();
    }
}
