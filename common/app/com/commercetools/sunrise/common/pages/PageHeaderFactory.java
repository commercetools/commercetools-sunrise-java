package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import play.Configuration;

import javax.inject.Inject;

public class PageHeaderFactory extends ViewModelFactory<PageHeader, PageContent> {

    private final String customerServiceNumber;

    @Inject
    public PageHeaderFactory(final Configuration configuration) {
        this.customerServiceNumber = configuration.getString("checkout.customerServiceNumber", "");
    }

    protected final String getCustomerServiceNumber() {
        return customerServiceNumber;
    }

    @Override
    protected final PageHeader getViewModelInstance(final PageContent pageContent) {
        return new PageHeader();
    }

    @Override
    protected final void initialize(final PageHeader viewModel, final PageContent content) {
        fillTitle(viewModel, content);
        fillCustomerServiceNumber(viewModel);
    }

    protected void fillTitle(final PageHeader viewModel, final PageContent content) {
        viewModel.setTitle(content.getTitle());
    }

    protected void fillCustomerServiceNumber(final PageHeader viewModel) {
        viewModel.setCustomerServiceNumber(customerServiceNumber);
    }
}
