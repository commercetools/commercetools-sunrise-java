package com.commercetools.sunrise.shoppingcart;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.queries.PagedResult;
import io.sphere.sdk.zones.Location;
import io.sphere.sdk.zones.Zone;
import io.sphere.sdk.zones.queries.ZoneQuery;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class ZoneFinderByCart implements ZoneFinder<Cart> {

    @Inject
    private SphereClient sphereClient;

    @Override
    public CompletionStage<Optional<Zone>> findZone(final Cart cart) {
        return Optional.ofNullable(cart.getShippingAddress())
                .map(this::createLocation)
                .map(this::fetchZoneByCountry)
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    private CompletionStage<Optional<Zone>> fetchZoneByCountry(final Location location) {
        final ZoneQuery request = ZoneQuery.of().byLocation(location);
        return sphereClient.execute(request)
                .thenApplyAsync(PagedResult::head);
    }

    private Location createLocation(final Address address) {
        final CountryCode country = address.getCountry();
        final Location location;
        if (address.getState() != null) {
            location = Location.of(country, address.getState());
        } else {
            location = Location.of(country);
        }
        return location;
    }
}
