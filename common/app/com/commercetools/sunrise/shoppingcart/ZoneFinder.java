package com.commercetools.sunrise.shoppingcart;

import io.sphere.sdk.zones.Zone;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface ZoneFinder<Z> {

    CompletionStage<Optional<Zone>> findZone(final Z zoneIdentifier);
}
