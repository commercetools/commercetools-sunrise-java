package com.commercetools.sunrise.framework.template;

import com.commercetools.sunrise.framework.reverserouters.common.assets.AssetsLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.common.localization.LocalizationLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.myaccount.addressbook.AddressBookLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.myaccount.authentication.AuthenticationLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.myaccount.myorders.MyOrdersLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.myaccount.mydetails.MyPersonalDetailsLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.home.HomeLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.product.ProductLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.cart.CartLinksControllerComponent;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.checkout.CheckoutLinksControllerComponent;
import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.components.controllers.ControllerComponentSupplier;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class TemplateControllerComponentsSupplier implements ControllerComponentSupplier {

    private final List<ControllerComponent> controllerComponents = new ArrayList<>();

    @Inject
    public TemplateControllerComponentsSupplier(final LocalizationLinksControllerComponent localizationLinksControllerComponent,
                                                final AssetsLinksControllerComponent assetsLinksControllerComponent,
                                                final AddressBookLinksControllerComponent addressBookLinksControllerComponent,
                                                final AuthenticationLinksControllerComponent authenticationLinksControllerComponent,
                                                final MyOrdersLinksControllerComponent myOrdersLinksControllerComponent,
                                                final MyPersonalDetailsLinksControllerComponent myPersonalDetailsLinksControllerComponent,
                                                final HomeLinksControllerComponent homeLinksControllerComponent,
                                                final ProductLinksControllerComponent productLinksControllerComponent,
                                                final CartLinksControllerComponent cartLinksControllerComponent,
                                                final CheckoutLinksControllerComponent checkoutLinksControllerComponent) {
        controllerComponents.add(localizationLinksControllerComponent);
        controllerComponents.add(assetsLinksControllerComponent);
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
