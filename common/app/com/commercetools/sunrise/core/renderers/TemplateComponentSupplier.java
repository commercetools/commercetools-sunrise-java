package com.commercetools.sunrise.core.renderers;

import com.commercetools.sunrise.core.components.Component;
import com.commercetools.sunrise.core.components.ComponentSupplier;
import com.commercetools.sunrise.core.reverserouters.common.assets.AssetsLinksControllerComponent;
import com.commercetools.sunrise.core.reverserouters.common.localization.LocalizationLinksControllerComponent;
import com.commercetools.sunrise.core.reverserouters.myaccount.addressbook.AddressBookLinksControllerComponent;
import com.commercetools.sunrise.core.reverserouters.myaccount.authentication.AuthenticationLinksControllerComponent;
import com.commercetools.sunrise.core.reverserouters.myaccount.changepassword.ChangePasswordLinksControllerComponent;
import com.commercetools.sunrise.core.reverserouters.myaccount.mydetails.MyPersonalDetailsLinksControllerComponent;
import com.commercetools.sunrise.core.reverserouters.myaccount.myorders.MyOrdersLinksControllerComponent;
import com.commercetools.sunrise.core.reverserouters.productcatalog.home.HomeLinksControllerComponent;
import com.commercetools.sunrise.core.reverserouters.productcatalog.product.ProductLinksControllerComponent;
import com.commercetools.sunrise.core.reverserouters.shoppingcart.cart.CartLinksControllerComponent;
import com.commercetools.sunrise.core.reverserouters.shoppingcart.checkout.CheckoutLinksControllerComponent;
import com.commercetools.sunrise.core.reverserouters.wishlist.WishlistLinksControllerComponent;

import java.util.List;

import static java.util.Arrays.asList;

public class TemplateComponentSupplier implements ComponentSupplier {

    public static List<Class<? extends Component>> get() {
        return asList(
                LocalizationLinksControllerComponent.class,
                AssetsLinksControllerComponent.class,
                AddressBookLinksControllerComponent.class,
                AuthenticationLinksControllerComponent.class,
                ChangePasswordLinksControllerComponent.class,
                MyOrdersLinksControllerComponent.class,
                MyPersonalDetailsLinksControllerComponent.class,
                HomeLinksControllerComponent.class,
                ProductLinksControllerComponent.class,
                CartLinksControllerComponent.class,
                CheckoutLinksControllerComponent.class,
                WishlistLinksControllerComponent.class);
    }
}
