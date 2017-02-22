package com.commercetools.sunrise.myaccount.mydetails.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.addresses.TitleFormFieldViewModelFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.myaccount.mydetails.MyPersonalDetailsFormData;
import play.data.Form;

import javax.inject.Inject;

@RequestScoped
public class MyPersonalDetailsFormSettingsViewModelFactory extends ViewModelFactory<MyPersonalDetailsFormSettingsViewModel, Form<? extends MyPersonalDetailsFormData>> {

    private final TitleFormFieldViewModelFactory titleFormFieldViewModelFactory;

    @Inject
    public MyPersonalDetailsFormSettingsViewModelFactory(final TitleFormFieldViewModelFactory titleFormFieldViewModelFactory) {
        this.titleFormFieldViewModelFactory = titleFormFieldViewModelFactory;
    }

    @Override
    protected MyPersonalDetailsFormSettingsViewModel getViewModelInstance() {
        return new MyPersonalDetailsFormSettingsViewModel();
    }

    @Override
    public final MyPersonalDetailsFormSettingsViewModel create(final Form<? extends MyPersonalDetailsFormData> form) {
        return super.create(form);
    }

    @Override
    protected final void initialize(final MyPersonalDetailsFormSettingsViewModel viewModel, final Form<? extends MyPersonalDetailsFormData> form) {
        fillTitle(viewModel, form);
    }

    protected void fillTitle(final MyPersonalDetailsFormSettingsViewModel viewModel, final Form<? extends MyPersonalDetailsFormData> form) {
        viewModel.setTitle(titleFormFieldViewModelFactory.createWithDefaultOptions(form.field("title")));
    }
}