package com.commercetools.sunrise.framework.template;

import com.commercetools.sunrise.framework.reverserouters.common.LocalizationLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.common.WebJarAssetsLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.myaccount.AddressBookLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.myaccount.AuthenticationLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.myaccount.MyOrdersLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.myaccount.MyPersonalDetailsLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.HomeLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.ProductLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CartLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CheckoutLinksControllerComponent;
import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.components.ControllerComponentSupplier;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class TemplateControllerComponentsSupplier implements ControllerComponentSupplier {

    private final List<ControllerComponent> controllerComponents = new ArrayList<>();

    @Inject
    public TemplateControllerComponentsSupplier(final LocalizationLinksControllerComponent localizationLinksControllerComponent,
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
