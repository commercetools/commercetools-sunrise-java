package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.reverserouter.common.LocalizationLinksControllerComponent;
import com.commercetools.sunrise.common.reverserouter.common.WebJarAssetsLinksControllerComponent;
import com.commercetools.sunrise.common.reverserouter.myaccount.AddressBookLinksControllerComponent;
import com.commercetools.sunrise.common.reverserouter.myaccount.AuthenticationLinksControllerComponent;
import com.commercetools.sunrise.common.reverserouter.myaccount.MyOrdersLinksControllerComponent;
import com.commercetools.sunrise.common.reverserouter.myaccount.MyPersonalDetailsLinksControllerComponent;
import com.commercetools.sunrise.common.reverserouter.productcatalog.HomeLinksControllerComponent;
import com.commercetools.sunrise.common.reverserouter.productcatalog.ProductLinksControllerComponent;
import com.commercetools.sunrise.common.reverserouter.shoppingcart.CartLinksControllerComponent;
import com.commercetools.sunrise.common.reverserouter.shoppingcart.CheckoutLinksControllerComponent;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.framework.ControllerComponentSupplier;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class TemplateLinksComponentSupplier implements ControllerComponentSupplier {

    private final List<ControllerComponent> controllerComponents = new ArrayList<>();

    @Inject
    public TemplateLinksComponentSupplier(final LocalizationLinksControllerComponent localizationLinksControllerComponent,
                                          final WebJarAssetsLinksControllerComponent webJarAssetsLinksControllerComponent,
                                          final AddressBookLinksControllerComponent addressBookLinksControllerComponent,
                                          final AuthenticationLinksControllerComponent authenticationLinksControllerComponent,
                                          final MyOrdersLinksControllerComponent myOrdersLinksControllerComponent,
                                          final MyPersonalDetailsLinksControllerComponent myPersonalDetailsLinksControllerComponent,
                                          final HomeLinksControllerComponent homeLinksControllerComponent,
                                          final ProductLinksControllerComponent productLinksControllerComponent,
                                          final CartLinksControllerComponent cartLinksControllerComponent,
                                          final CheckoutLinksControllerComponent checkoutLinksControllerComponent) {
        controllerComponents.add(localizationLinksControllerComponent);
        controllerComponents.add(webJarAssetsLinksControllerComponent);
        controllerComponents.add(addressBookLinksControllerComponent);
        controllerComponents.add(authenticationLinksControllerComponent);
        controllerComponents.add(myOrdersLinksControllerComponent);
        controllerComponents.add(myPersonalDetailsLinksControllerComponent);
        controllerComponents.add(homeLinksControllerComponent);
        controllerComponents.add(productLinksControllerComponent);
        controllerComponents.add(cartLinksControllerComponent);
        controllerComponents.add(checkoutLinksControllerComponent);
    }

    @Override
    public List<ControllerComponent> get() {
        return controllerComponents;
    }
}
