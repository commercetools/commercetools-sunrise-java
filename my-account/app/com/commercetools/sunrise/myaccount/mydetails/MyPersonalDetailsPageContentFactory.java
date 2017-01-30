package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;

import javax.inject.Inject;

public class MyPersonalDetailsPageContentFactory extends PageContentFactory<MyPersonalDetailsPageContent, MyPersonalDetailsControllerData> {

    private final PageTitleResolver pageTitleResolver;
    private final CustomerInfoBeanFactory customerInfoBeanFactory;
    private final MyPersonalDetailsFormSettingsBeanFactory myPersonalDetailsFormSettingsBeanFactory;

    @Inject
    public MyPersonalDetailsPageContentFactory(final PageTitleResolver pageTitleResolver, final CustomerInfoBeanFactory customerInfoBeanFactory,
                                               final MyPersonalDetailsFormSettingsBeanFactory myPersonalDetailsFormSettingsBeanFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.customerInfoBeanFactory = customerInfoBeanFactory;
        this.myPersonalDetailsFormSettingsBeanFactory = myPersonalDetailsFormSettingsBeanFactory;
    }

    @Override
    protected MyPersonalDetailsPageContent getViewModelInstance() {
        return new MyPersonalDetailsPageContent();
    }

    @Override
    public final MyPersonalDetailsPageContent create(final MyPersonalDetailsControllerData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final MyPersonalDetailsPageContent model, final MyPersonalDetailsControllerData data) {
        super.initialize(model, data);
        fillCustomer(model, data);
        fillPersonalDetailsForm(model, data);
        fillPersonalDetailsFormSettings(model, data);
    }

    @Override
    protected void fillTitle(final MyPersonalDetailsPageContent model, final MyPersonalDetailsControllerData data) {
        model.setTitle(pageTitleResolver.getOrEmpty("myAccount:myPersonalDetailsPage.title"));
    }

    protected void fillCustomer(final MyPersonalDetailsPageContent model, final MyPersonalDetailsControllerData data) {
        model.setCustomerInfo(customerInfoBeanFactory.create(data.getCustomer()));
    }

    protected void fillPersonalDetailsForm(final MyPersonalDetailsPageContent model, final MyPersonalDetailsControllerData data) {
        model.setPersonalDetailsForm(data.getForm());
    }

    protected void fillPersonalDetailsFormSettings(final MyPersonalDetailsPageContent model, final MyPersonalDetailsControllerData data) {
        model.setPersonalDetailsFormSettings(myPersonalDetailsFormSettingsBeanFactory.create(data));
    }
}