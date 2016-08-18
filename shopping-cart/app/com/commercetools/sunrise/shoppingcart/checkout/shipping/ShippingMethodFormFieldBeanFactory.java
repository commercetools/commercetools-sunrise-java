package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.utils.MoneyContext;
import com.commercetools.sunrise.shoppingcart.ZoneFinderByCart;
import com.google.inject.Injector;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.ShippingRate;
import io.sphere.sdk.zones.Zone;
import play.data.Form;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.money.MonetaryAmount;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static io.sphere.sdk.client.SphereClientUtils.blockingWait;
import static java.util.stream.Collectors.toList;

public class ShippingMethodFormFieldBeanFactory extends Base {

    @Inject
    private UserContext userContext;
    @Inject
    private Injector injector;

    protected ShippingMethodFormFieldBean create(final Form<?> form, final String fieldName, final Cart cart,
                                                 final List<ShippingMethod> shippingMethods) {
        final ShippingMethodFormFieldBean bean = new ShippingMethodFormFieldBean();
        initialize(bean, form, fieldName, cart, shippingMethods);
        return bean;
    }

    protected final void initialize(final ShippingMethodFormFieldBean bean, final Form<?> form, final String fieldName,
                                    final Cart cart, final List<ShippingMethod> shippingMethods) {
        fillShippingMethodFormFieldList(bean, form, fieldName, cart, shippingMethods);
    }

    protected void fillShippingMethodFormFieldList(final ShippingMethodFormFieldBean bean, final Form<?> form, final String fieldName,
                                                   final Cart cart, final List<ShippingMethod> shippingMethods) {
        final String selectedShippingMethodId = getSelectedMethodId(form, fieldName);
        bean.setList(shippingMethods.stream()
                .map(shippingMethod -> createShippingFormSelectableOption(cart, shippingMethod, selectedShippingMethodId))
                .collect(toList()));
    }

    protected ShippingFormSelectableOptionBean createShippingFormSelectableOption(final Cart cart, final ShippingMethod shippingMethod,
                                                                                  @Nullable final String selectedShippingMethodId) {
        final ShippingFormSelectableOptionBean bean = new ShippingFormSelectableOptionBean();
        initializeShippingFormSelectableOption(bean, cart, shippingMethod, selectedShippingMethodId);
        return bean;
    }

    protected void initializeShippingFormSelectableOption(final ShippingFormSelectableOptionBean bean, final Cart cart, final ShippingMethod shippingMethod,
                                                          final @Nullable String selectedShippingMethodId) {
        bean.setLabel(shippingMethod.getName());
        bean.setValue(shippingMethod.getId());
        bean.setSelected(shippingMethod.getId().equals(selectedShippingMethodId));
        bean.setDescription(shippingMethod.getDescription());
        findShippingMethodPrice(cart, shippingMethod).ifPresent(bean::setPrice);
    }

    protected Optional<String> findShippingMethodPrice(final Cart cart, final ShippingMethod shippingMethod) {
        return getZone(cart)
                .flatMap(zone -> shippingMethod.getShippingRatesForZone(zone).stream()
                        .filter(shippingRate -> shippingRate.getPrice().getCurrency().equals(userContext.currency()))
                        .findFirst()
                        .map(shippingRate -> {
                            final MoneyContext moneyContext = getMoneyContext();
                            final MonetaryAmount price = isFreeShipping(cart, shippingRate) ? moneyContext.zero() : shippingRate.getPrice();
                            return moneyContext.formatOrZero(price);
                        }));
    }

    protected Optional<Zone> getZone(final Cart cart) {
        // Need to do this since zones are not expanded in shipping methods yet (but will be soon)
        // Rather this (even though it's expensive -one request per shipping method-) but it will mean less breaking changes in the future
        final ZoneFinderByCart zoneFinder = injector.getInstance(ZoneFinderByCart.class);
        return blockingWait(zoneFinder.findZone(cart), Duration.ofMinutes(1));
    }

    protected boolean isFreeShipping(final Cart cart, final ShippingRate shippingRate) {
        if (shippingRate.getFreeAbove() != null) {
            final MonetaryAmount totalCartPriceWithoutShipping = Optional.ofNullable(cart.getShippingInfo())
                    .map(shippingInfo -> cart.getTotalPrice().subtract(shippingInfo.getPrice()))
                    .orElseGet(cart::getTotalPrice);
            return totalCartPriceWithoutShipping.isGreaterThan(shippingRate.getFreeAbove());
        } else {
            return false;
        }
    }

    protected MoneyContext getMoneyContext() {
        return MoneyContext.of(userContext.currency(), userContext.locale());
    }

    @Nullable
    protected String getSelectedMethodId(final Form<?> form, final String fieldName) {
        return form.field(fieldName).value();
    }
}
