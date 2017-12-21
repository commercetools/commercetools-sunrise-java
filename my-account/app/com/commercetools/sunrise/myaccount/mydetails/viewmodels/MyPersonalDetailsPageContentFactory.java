package com.commercetools.sunrise.myaccount.mydetails.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.FormPageContentFactory;
import com.commercetools.sunrise.myaccount.mydetails.MyPersonalDetailsFormData;
import io.sphere.sdk.customers.Customer;
import play.data.Form;

import javax.inject.Inject;

public class MyPersonalDetailsPageContentFactory extends FormPageContentFactory<MyPersonalDetailsPageContent, Customer, MyPersonalDetailsFormData> {

    private final MyPersonalDetailsFormSettingsViewModelFactory myPersonalDetailsFormSettingsViewModelFactory;

    @Inject
    public MyPersonalDetailsPageContentFactory(final MyPersonalDetailsFormSettingsViewModelFactory myPersonalDetailsFormSettingsViewModelFactory) {
        this.myPersonalDetailsFormSettingsViewModelFactory = myPersonalDetailsFormSettingsViewModelFactory;
    }

    protected final MyPersonalDetailsFormSettingsViewModelFactory getMyPersonalDetailsFormSettingsViewModelFactory() {
        return myPersonalDetailsFormSettingsViewModelFactory;
    }

    @Override
    protected MyPersonalDetailsPageContent newViewModelInstance(final Customer customer, final Form<? extends MyPersonalDetailsFormData> form) {
        return new MyPersonalDetailsPageContent();
    }

    @Override
    public final MyPersonalDetailsPageContent create(final Customer customer, final Form<? extends MyPersonalDetailsFormData> form) {
        return super.create(customer, form);
    }

    @Override
    protected void initialize(final MyPersonalDetailsPageContent viewModel, final Customer customer, final Form<? extends MyPersonalDetailsFormData> form) {
        super.initialize(viewModel, customer, form);
        fillCustomer(viewModel, customer, form);
        fillPersonalDetailsForm(viewModel, customer, form);
        fillPersonalDetailsFormSettings(viewModel, customer, form);
    }

    protected void fillCustomer(final MyPersonalDetailsPageContent viewModel, final Customer customer, final Form<? extends MyPersonalDetailsFormData> form) {
        viewModel.setCustomer(customer);
    }

    protected void fillPersonalDetailsForm(final MyPersonalDetailsPageContent viewModel, final Customer customer, final Form<? extends MyPersonalDetailsFormData> form) {
        viewModel.setPersonalDetailsForm(form);
    }

    protected void fillPersonalDetailsFormSettings(final MyPersonalDetailsPageContent viewModel, final Customer customer, final Form<? extends MyPersonalDetailsFormData> form) {
        viewModel.setPersonalDetailsFormSettings(myPersonalDetailsFormSettingsViewModelFactory.create(form));
    }
}