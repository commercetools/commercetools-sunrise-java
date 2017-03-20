package com.commercetools.sunrise.myaccount.mydetails.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.FormPageContentFactory;
import com.commercetools.sunrise.framework.viewmodels.content.customers.CustomerInfoViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.PageTitleResolver;
import com.commercetools.sunrise.myaccount.mydetails.MyPersonalDetailsFormData;
import io.sphere.sdk.customers.Customer;
import play.data.Form;

import javax.inject.Inject;

public class MyPersonalDetailsPageContentFactory extends FormPageContentFactory<MyPersonalDetailsPageContent, Customer, MyPersonalDetailsFormData> {

    private final PageTitleResolver pageTitleResolver;
    private final CustomerInfoViewModelFactory customerInfoViewModelFactory;
    private final MyPersonalDetailsFormSettingsViewModelFactory myPersonalDetailsFormSettingsViewModelFactory;

    @Inject
    public MyPersonalDetailsPageContentFactory(final PageTitleResolver pageTitleResolver, final CustomerInfoViewModelFactory customerInfoViewModelFactory,
                                               final MyPersonalDetailsFormSettingsViewModelFactory myPersonalDetailsFormSettingsViewModelFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.customerInfoViewModelFactory = customerInfoViewModelFactory;
        this.myPersonalDetailsFormSettingsViewModelFactory = myPersonalDetailsFormSettingsViewModelFactory;
    }

    protected final PageTitleResolver getPageTitleResolver() {
        return pageTitleResolver;
    }

    protected final CustomerInfoViewModelFactory getCustomerInfoViewModelFactory() {
        return customerInfoViewModelFactory;
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

    @Override
    protected void fillTitle(final MyPersonalDetailsPageContent viewModel, final Customer customer, final Form<? extends MyPersonalDetailsFormData> form) {
        viewModel.setTitle(pageTitleResolver.getOrEmpty("myAccount:myPersonalDetailsPage.title"));
    }

    protected void fillCustomer(final MyPersonalDetailsPageContent viewModel, final Customer customer, final Form<? extends MyPersonalDetailsFormData> form) {
        viewModel.setCustomerInfo(customerInfoViewModelFactory.create(customer));
    }

    protected void fillPersonalDetailsForm(final MyPersonalDetailsPageContent viewModel, final Customer customer, final Form<? extends MyPersonalDetailsFormData> form) {
        viewModel.setPersonalDetailsForm(form);
    }

    protected void fillPersonalDetailsFormSettings(final MyPersonalDetailsPageContent viewModel, final Customer customer, final Form<? extends MyPersonalDetailsFormData> form) {
        viewModel.setPersonalDetailsFormSettings(myPersonalDetailsFormSettingsViewModelFactory.create(form));
    }
}