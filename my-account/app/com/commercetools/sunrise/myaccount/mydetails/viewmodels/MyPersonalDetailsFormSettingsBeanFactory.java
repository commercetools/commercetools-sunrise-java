package com.commercetools.sunrise.myaccount.mydetails.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.addresses.TitleFormFieldBeanFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.myaccount.mydetails.MyPersonalDetailsFormData;
import play.data.Form;

import javax.inject.Inject;

@RequestScoped
public class MyPersonalDetailsFormSettingsBeanFactory extends ViewModelFactory<MyPersonalDetailsFormSettingsBean, Form<? extends MyPersonalDetailsFormData>> {

    private final TitleFormFieldBeanFactory titleFormFieldBeanFactory;

    @Inject
    public MyPersonalDetailsFormSettingsBeanFactory(final TitleFormFieldBeanFactory titleFormFieldBeanFactory) {
        this.titleFormFieldBeanFactory = titleFormFieldBeanFactory;
    }

    @Override
    protected MyPersonalDetailsFormSettingsBean getViewModelInstance() {
        return new MyPersonalDetailsFormSettingsBean();
    }

    @Override
    public final MyPersonalDetailsFormSettingsBean create(final Form<? extends MyPersonalDetailsFormData> form) {
        return super.create(form);
    }

    @Override
    protected final void initialize(final MyPersonalDetailsFormSettingsBean model, final Form<? extends MyPersonalDetailsFormData> form) {
        fillTitle(model, form);
    }

    protected void fillTitle(final MyPersonalDetailsFormSettingsBean model, final Form<? extends MyPersonalDetailsFormData> form) {
        model.setTitle(titleFormFieldBeanFactory.createWithDefaultOptions(form.field("title")));
    }
}