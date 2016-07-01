package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.common.controllers.WithOverridablePageContent;
import com.commercetools.sunrise.common.forms.UserFeedback;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormBean;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormBeanFactory;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Base;
import play.data.Form;

import javax.inject.Inject;

public class AddAddressPageContentFactory extends Base implements WithOverridablePageContent<AddAddressPageContent> {

    @Inject
    private UserFeedback userFeedback;
    @Inject
    private AddressBookAddressFormBeanFactory addressBookAddressFormBeanFactory;

    public AddAddressPageContent create(final Form<?> form, final Customer customer) {
        final AddAddressPageContent content = createPageContent();
        fillNewAddressForm(content, form);
        return content;
    }

    protected void fillNewAddressForm(final AddAddressPageContent content, final Form<?> form) {
        final AddressBookAddressFormBean bean = addressBookAddressFormBeanFactory.create(form);
        userFeedback.findErrors().ifPresent(bean::setErrors);
        content.setNewAddressForm(bean);
    }


    @Override
    public AddAddressPageContent createPageContent() {
        return new AddAddressPageContent();
    }
}