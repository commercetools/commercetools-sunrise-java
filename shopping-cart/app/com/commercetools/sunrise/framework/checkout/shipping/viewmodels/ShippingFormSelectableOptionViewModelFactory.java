package com.commercetools.sunrise.framework.checkout.shipping.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.SelectableViewModelFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import com.commercetools.sunrise.framework.CartFinder;
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
public class ShippingFormSelectableOptionViewModelFactory extends SelectableViewModelFactory<ShippingFormSelectableOptionViewModel, ShippingMethod, String> {

    private final CartFinder cartFinder;
    private final PriceFormatter priceFormatter;
    private final SphereClient sphereClient;

    @Inject
    public ShippingFormSelectableOptionViewModelFactory(final CartFinder cartFinder, final PriceFormatter priceFormatter, final SphereClient sphereClient) {
        this.cartFinder = cartFinder;
        this.priceFormatter = priceFormatter;
        this.sphereClient = sphereClient;
    }

    @Override
    protected ShippingFormSelectableOptionViewModel getViewModelInstance() {
        return new ShippingFormSelectableOptionViewModel();
    }

    @Override
    public final ShippingFormSelectableOptionViewModel create(final ShippingMethod option, @Nullable final String selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final ShippingFormSelectableOptionViewModel model, final ShippingMethod option, @Nullable final String selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
        fillDescription(model, option, selectedValue);
        fillPrice(model, option, selectedValue);
    }

    protected void fillLabel(final ShippingFormSelectableOptionViewModel model, final ShippingMethod option, @Nullable final String selectedValue) {
        model.setLabel(option.getName());
    }

    protected void fillValue(final ShippingFormSelectableOptionViewModel model, final ShippingMethod option, @Nullable final String selectedValue) {
        model.setValue(option.getId());
    }

    protected void fillSelected(final ShippingFormSelectableOptionViewModel model, final ShippingMethod option, @Nullable final String selectedValue) {
        model.setSelected(option.getId().equals(selectedValue));
    }

    protected void fillDescription(final ShippingFormSelectableOptionViewModel model, final ShippingMethod option, @Nullable final String selectedValue) {
        model.setDescription(option.getDescription());
    }

    protected void fillPrice(final ShippingFormSelectableOptionViewModel model, final ShippingMethod option, @Nullable final String selectedValue) {
        // Need to do this since zones are not expanded in shipping methods yet (but will be soon)
        // Rather this (even though it's expensive -two requests per shipping method-) but it will mean less breaking changes in the future
        cartFinder.get()
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
