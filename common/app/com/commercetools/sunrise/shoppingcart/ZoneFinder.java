package com.commercetools.sunrise.shoppingcart;

import io.sphere.sdk.zones.Zone;
import io.sphere.sdk.zones.queries.ZoneQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.UnaryOperator;

public interface ZoneFinder<Z> {

    CompletionStage<Optional<Zone>> findZone(final Z zoneIdentifier,
                                             final UnaryOperator<ZoneQuery> runHookOnZoneQuery);
}
