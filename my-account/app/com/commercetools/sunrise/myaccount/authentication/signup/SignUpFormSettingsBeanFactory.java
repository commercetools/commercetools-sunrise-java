package com.commercetools.sunrise.myaccount.authentication.signup;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.forms.TitleFormFieldBeanFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.myaccount.authentication.AuthenticationControllerData;
import play.data.Form;
import play.data.FormFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;

@RequestScoped
public class SignUpFormSettingsBeanFactory extends ViewModelFactory<SignUpFormSettingsBean, AuthenticationControllerData> {

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
    public final SignUpFormSettingsBean create(final AuthenticationControllerData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final SignUpFormSettingsBean model, final AuthenticationControllerData data) {
        fillTitle(model, data);
    }

    protected void fillTitle(final SignUpFormSettingsBean model, final AuthenticationControllerData data) {
        final Form<?> form = createForm(data.getSignUpForm());
        model.setTitle(titleFormFieldBeanFactory.createWithDefaultOptions(form.field("title")));
    }

    private Form<?> createForm(@Nullable final Form<?> form) {
        return form != null ? form : formFactory.form();
    }
}
