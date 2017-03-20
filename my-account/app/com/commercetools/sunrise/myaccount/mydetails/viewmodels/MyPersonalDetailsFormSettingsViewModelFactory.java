package com.commercetools.sunrise.myaccount.mydetails.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.FormViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.forms.titles.TitleFormFieldViewModelFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.myaccount.mydetails.MyPersonalDetailsFormData;
import play.data.Form;

import javax.inject.Inject;

@RequestScoped
public class MyPersonalDetailsFormSettingsViewModelFactory extends FormViewModelFactory<MyPersonalDetailsFormSettingsViewModel, Void, MyPersonalDetailsFormData> {

    private final TitleFormFieldViewModelFactory titleFormFieldViewModelFactory;

    @Inject
    public MyPersonalDetailsFormSettingsViewModelFactory(final TitleFormFieldViewModelFactory titleFormFieldViewModelFactory) {
        this.titleFormFieldViewModelFactory = titleFormFieldViewModelFactory;
    }

    protected final TitleFormFieldViewModelFactory getTitleFormFieldViewModelFactory() {
        return titleFormFieldViewModelFactory;
    }

    @Override
    protected MyPersonalDetailsFormSettingsViewModel newViewModelInstance(final Void input, final Form<? extends MyPersonalDetailsFormData> form) {
        return new MyPersonalDetailsFormSettingsViewModel();
    }

    @Override
    public final MyPersonalDetailsFormSettingsViewModel create(final Void input, final Form<? extends MyPersonalDetailsFormData> form) {
        return super.create(input, form);
    }

    public final MyPersonalDetailsFormSettingsViewModel create(final Form<? extends MyPersonalDetailsFormData> form) {
        return super.create(null, form);
    }

    @Override
    protected final void initialize(final MyPersonalDetailsFormSettingsViewModel viewModel, final Void input, final Form<? extends MyPersonalDetailsFormData> form) {
        fillTitle(viewModel, form);

    }

    protected void fillTitle(final MyPersonalDetailsFormSettingsViewModel viewModel, final Form<? extends MyPersonalDetailsFormData> form) {
        viewModel.setTitle(titleFormFieldViewModelFactory.createWithDefaultOptions(form.field("title")));
    }
}