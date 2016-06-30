package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.common.forms.FormUtils;
import com.commercetools.sunrise.common.forms.TitleFormFieldBeanFactory;
import com.commercetools.sunrise.common.forms.UserFeedback;
import io.sphere.sdk.customers.Customer;
import play.data.Form;

import javax.inject.Inject;
import java.util.function.Function;

public class MyPersonalDetailsPageContentFactory {

    @Inject
    private UserFeedback userFeedback;
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
        final MyPersonalDetailsFormBean formBean = createForm(form);
        userFeedback.findErrors().ifPresent(formBean::setErrors);
        content.setPersonalDetailsForm(formBean);
    }

    protected MyPersonalDetailsFormBean createForm(final Form<?> form) {
        final MyPersonalDetailsFormBean bean = new MyPersonalDetailsFormBean();
        final Function<String, String> formFieldExtractor = FormUtils.formFieldExtractor(form);
        bean.setSalutations(titleFormFieldBeanFactory.createWithDefaultTitles(formFieldExtractor.apply("title")));
        bean.setFirstName(formFieldExtractor.apply("firstName"));
        bean.setLastName(formFieldExtractor.apply("lastName"));
        bean.setEmail(formFieldExtractor.apply("email"));
        return bean;
    }
}