package com.commercetools.sunrise.ctp;

import io.sphere.sdk.client.HttpRequestIntent;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.http.FormUrlEncodedHttpRequestBody;
import io.sphere.sdk.http.StringHttpRequestBody;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;

import java.util.Optional;
import java.util.stream.Collectors;

public final class CtpLogUtils {

    private CtpLogUtils() {
    }

    public static String printableProductRequest(final ProductProjectionSearch request, final PagedSearchResult<ProductProjection> result) {
        return String.format("Fetched %s out of %s products with request %s",
                result.getCount(),
                result.getTotal(),
                printableRequest(request));
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
