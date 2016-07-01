package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.common.forms.TitleFormFieldBeanFactory;
import io.sphere.sdk.customers.Customer;
import play.data.Form;

import javax.inject.Inject;

public class MyPersonalDetailsPageContentFactory {

    @Inject
    private TitleFormFieldBeanFactory titleFormFieldBeanFactory;

    public MyPersonalDetailsPageContent create(final Form<?> form, final Customer customer) {
        final MyPersonalDetailsPageContent content = new MyPersonalDetailsPageContent();
        fillCustomer(content, customer);
        fillForm(content, form);
        return content;
    }

    protected void fillCustomer(final MyPersonalDetailsPageContent content, final Customer customer) {
        final CustomerInfoBean bean = new CustomerInfoBean();
        bean.setCustomer(customer);
        content.setCustomerInfo(bean);
    }

    protected void fillForm(final MyPersonalDetailsPageContent content, final Form<?> form) {
        content.setPersonalDetailsFormSettings(createFormSettings(form));
        content.setPersonalDetailsForm(form);
    }

    protected MyPersonalDetailsFormSettingsBean createFormSettings(final Form<?> form) {
        final MyPersonalDetailsFormSettingsBean bean = new MyPersonalDetailsFormSettingsBean();
        bean.setTitle(titleFormFieldBeanFactory.createWithDefaultTitles(form, "title"));
        return bean;
    }
}