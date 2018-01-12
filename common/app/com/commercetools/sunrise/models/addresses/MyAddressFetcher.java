package com.commercetools.sunrise.models.addresses;

import com.commercetools.sunrise.core.NotFoundResourceException;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.models.Address;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(MyAddressFetcherImpl.class)
public interface MyAddressFetcher {

    CompletionStage<Optional<Address>> get(String identifier);

    default CompletionStage<Address> require(String identifier) {
        return get(identifier).thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }
}
