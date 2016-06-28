package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.common.forms.UserFeedback;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormBean;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormBeanFactory;
import io.sphere.sdk.customers.Customer;
import play.data.Form;

import javax.inject.Inject;

public class ChangeAddressPageContentFactory {

    @Inject
    private UserFeedback userFeedback;
    @Inject
    private AddressBookAddressFormBeanFactory addressBookAddressFormBeanFactory;

    public ChangeAddressPageContent create(final Customer customer, final Form<?> form) {
        final ChangeAddressPageContent content = new ChangeAddressPageContent();
        fillEditAddressForm(content, form);
        return content;
    }

    protected void fillEditAddressForm(final ChangeAddressPageContent content, final Form<?> form) {
        final AddressBookAddressFormBean bean = addressBookAddressFormBeanFactory.create(form);
        userFeedback.findErrors().ifPresent(bean::setErrors);
        content.setEditAddressForm(bean);
    }


}