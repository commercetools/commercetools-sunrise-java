package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.SelectableViewModelFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import com.commercetools.sunrise.shoppingcart.CartFinderBySession;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.PagedResult;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.zones.Location;
import io.sphere.sdk.zones.Zone;
import io.sphere.sdk.zones.queries.ZoneQuery;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.common.utils.CartPriceUtils.calculateApplicableShippingCosts;

@RequestScoped
public class ShippingFormSelectableOptionBeanFactory extends SelectableViewModelFactory<ShippingFormSelectableOptionBean, ShippingMethod, String> {

    private final CartFinderBySession cartFinderBySession;
    private final PriceFormatter priceFormatter;
    private final SphereClient sphereClient;

    @Inject
    public ShippingFormSelectableOptionBeanFactory(final CartFinderBySession cartFinderBySession, final PriceFormatter priceFormatter, final SphereClient sphereClient) {
        this.cartFinderBySession = cartFinderBySession;
        this.priceFormatter = priceFormatter;
        this.sphereClient = sphereClient;
    }

    @Override
    protected ShippingFormSelectableOptionBean getViewModelInstance() {
        return new ShippingFormSelectableOptionBean();
    }

    @Override
    public final ShippingFormSelectableOptionBean create(final ShippingMethod option, @Nullable final String selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final ShippingFormSelectableOptionBean model, final ShippingMethod option, @Nullable final String selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
        fillDescription(model, option, selectedValue);
        fillPrice(model, option, selectedValue);
    }

    protected void fillLabel(final ShippingFormSelectableOptionBean model, final ShippingMethod option, @Nullable final String selectedValue) {
        model.setLabel(option.getName());
    }

    protected void fillValue(final ShippingFormSelectableOptionBean model, final ShippingMethod option, @Nullable final String selectedValue) {
        model.setValue(option.getId());
    }

    protected void fillSelected(final ShippingFormSelectableOptionBean model, final ShippingMethod option, @Nullable final String selectedValue) {
        model.setSelected(option.getId().equals(selectedValue));
    }

    protected void fillDescription(final ShippingFormSelectableOptionBean model, final ShippingMethod option, @Nullable final String selectedValue) {
        model.setDescription(option.getDescription());
    }

    protected void fillPrice(final ShippingFormSelectableOptionBean model, final ShippingMethod option, @Nullable final String selectedValue) {
        // Need to do this since zones are not expanded in shipping methods yet (but will be soon)
        // Rather this (even though it's expensive -two requests per shipping method-) but it will mean less breaking changes in the future
        cartFinderBySession.findCart(null)
                .thenAccept(cartOpt -> cartOpt
                        .ifPresent(cart -> findLocation(cart)
                                .ifPresent(location -> fetchZone(location)
                                        .thenAccept(zoneOpt -> zoneOpt
                                                .ifPresent(zone -> findShippingMethodPrice(option, zone, cart)
                                                        .ifPresent(model::setPrice))))));
    }

    private CompletionStage<Optional<Zone>> fetchZone(final Location location) {
        final ZoneQuery request = ZoneQuery.of().byLocation(location);
        return sphereClient.execute(request)
                .thenApplyAsync(PagedResult::head);
    }

    private Optional<Location> findLocation(final Cart cart) {
        return Optional.ofNullable(cart.getShippingAddress())
                .map(address -> Location.of(address.getCountry(), address.getState()));
    }

    private Optional<String> findShippingMethodPrice(final ShippingMethod shippingMethod, final Zone zone, final Cart cart) {
        return shippingMethod.getShippingRatesForZone(zone).stream()
                .filter(shippingRate -> shippingRate.getPrice().getCurrency().equals(cart.getCurrency()))
                .findAny()
                .map(shippingRate -> priceFormatter.format(calculateApplicableShippingCosts(cart, shippingRate)));
    }
}
