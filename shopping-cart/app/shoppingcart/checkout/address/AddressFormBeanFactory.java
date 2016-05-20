package shoppingcart.checkout.address;

import com.neovisionaries.i18n.CountryCode;
import common.contexts.SunriseDataBeanFactory;
import io.sphere.sdk.models.Address;

import javax.annotation.Nullable;
import java.util.List;

import static java.util.Collections.singletonList;

public class AddressFormBeanFactory extends SunriseDataBeanFactory {
    public AddressFormBean createShippingAddressFormBean(final @Nullable Address shippingAddress) {
        final List<CountryCode> shippingCountries = singletonList(userContext.country());
        return new AddressFormBean(shippingAddress, shippingCountries, userContext, i18nResolver, configuration);
    }

    public AddressFormBean createBillingAddressFormBean(final @Nullable Address billingAddress) {
        final List<CountryCode> billingCountries = projectContext.countries();
        return new AddressFormBean(billingAddress, billingCountries, userContext, i18nResolver, configuration);
    }
}
