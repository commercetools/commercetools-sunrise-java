package com.commercetools.sunrise.ctp.graphql;

import com.fasterxml.jackson.databind.JsonNode;
import io.sphere.sdk.client.SphereRequest;

import javax.annotation.Nullable;

public interface GraphQLRequest<T> extends SphereRequest<T> {

    String getQuery();

    @Nullable
    JsonNode getVariables();
}
