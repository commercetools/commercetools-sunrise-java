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

    @Override
    protected final PageHeader getViewModelInstance() {
        return new PageHeader();
    }

    @Override
    protected final void initialize(final PageHeader model, final PageContent content) {
        fillTitle(model, content);
        fillCustomerServiceNumber(model);
    }

    protected void fillTitle(final PageHeader model, final PageContent content) {
        model.setTitle(content.getTitle());
    }

    protected void fillCustomerServiceNumber(final PageHeader model) {
        model.setCustomerServiceNumber(customerServiceNumber);
    }
}
