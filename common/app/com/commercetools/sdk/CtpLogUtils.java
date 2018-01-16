package com.commercetools.sdk;

import com.fasterxml.jackson.databind.JsonNode;
import io.sphere.sdk.client.HttpRequestIntent;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.http.FormUrlEncodedHttpRequestBody;
import io.sphere.sdk.http.StringHttpRequestBody;
import io.sphere.sdk.json.SphereJsonUtils;
import play.libs.Json;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.Collectors;

public final class CtpLogUtils {

    private CtpLogUtils() {
    }

    public static <T> String printableResponse(@Nullable final T response) {
        if (response != null) {
            final JsonNode jsonNode = SphereJsonUtils.toJsonNode(response);
            final String prettyResponse = Json.prettyPrint(jsonNode);
            return " with response:\n" + prettyResponse;
        }
        return " without response";
    }

    public static String printableRequest(final SphereRequest<?> request) {
        final HttpRequestIntent httpRequest = request.httpRequestIntent();
        final String requestBody = printableRequestBody(httpRequest)
                .map(body -> " with body:\n \"" + body + "\"")
                .orElse("");
        return String.format("%s %s",
                httpRequest.getHttpMethod(),
                httpRequest.getPath() + requestBody);
    }

    private static Optional<String> printableRequestBody(final HttpRequestIntent httpRequest) {
        return Optional.ofNullable(httpRequest.getBody())
                .map(body -> {
                    final String bodyAsString;
                    if (httpRequest.getBody() instanceof StringHttpRequestBody) {
                        bodyAsString = ((StringHttpRequestBody) httpRequest.getBody()).getSecuredBody();
                    } else if (httpRequest.getBody() instanceof FormUrlEncodedHttpRequestBody) {
                        bodyAsString = ((FormUrlEncodedHttpRequestBody) httpRequest.getBody()).getParameters().stream()
                                .map(pair -> pair.getName() + "=" + pair.getValue())
                                .collect(Collectors.joining("&"));
                    } else {
                        bodyAsString = "**omitted output**";
                    }
                    return bodyAsString;
                });
    }
}
