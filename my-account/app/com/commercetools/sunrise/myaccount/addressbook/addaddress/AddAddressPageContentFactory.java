package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.common.utils.FormUtils;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormBean;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.Configuration;
import play.data.Form;

import javax.inject.Inject;
import java.util.List;

public class AddAddressPageContentFactory {

    @Inject
    private UserContext userContext;
    @Inject
    private ProjectContext projectContext;
    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private Configuration configuration;

    public AddAddressPageContent create(final Customer customer, final Form<?> form) {
        final AddAddressPageContent content = new AddAddressPageContent();
        fillNewAddressForm(content, form);
        return content;
    }

    protected void fillNewAddressForm(final AddAddressPageContent content, final Form<?> form) {
        final Address address = FormUtils.extractAddress(form);
        final List<CountryCode> countries = projectContext.countries();
        final AddressBookAddressFormBean bean = new AddressBookAddressFormBean(address, countries, userContext, i18nResolver, configuration);
        fillDefaultAddresses(form, bean);
        content.setNewAddressForm(bean);
    }

    private void fillDefaultAddresses(final Form<?> form, final AddressBookAddressFormBean bean) {
        final boolean defaultBillingAddress = FormUtils.extractBooleanFormField(form, "defaultBillingAddress");
        bean.setDefaultBillingAddress(defaultBillingAddress);
        final boolean defaultShippingAddress = FormUtils.extractBooleanFormField(form, "defaultShippingAddress");
        bean.setDefaultShippingAddress(defaultShippingAddress);
    }
}