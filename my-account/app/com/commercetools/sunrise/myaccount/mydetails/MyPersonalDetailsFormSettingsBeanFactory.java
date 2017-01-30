package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.forms.TitleFormFieldBeanFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;

import javax.inject.Inject;

@RequestScoped
public class MyPersonalDetailsFormSettingsBeanFactory extends ViewModelFactory<MyPersonalDetailsFormSettingsBean, MyPersonalDetailsControllerData> {

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
    public final MyPersonalDetailsFormSettingsBean create(final MyPersonalDetailsControllerData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final MyPersonalDetailsFormSettingsBean model, final MyPersonalDetailsControllerData data) {
        fillTitle(model, data);
    }

    protected void fillTitle(final MyPersonalDetailsFormSettingsBean model, final MyPersonalDetailsControllerData data) {
        model.setTitle(titleFormFieldBeanFactory.createWithDefaultOptions(data.getForm().field("title")));
    }
}