package com.commercetools.sunrise.common.utils;

import io.sphere.sdk.client.HttpRequestIntent;
import io.sphere.sdk.http.FormUrlEncodedHttpRequestBody;
import io.sphere.sdk.http.StringHttpRequestBody;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.stream.Collectors;

public final class LogUtils {

    private LogUtils() {
    }

    public static void logProductRequest(final Logger logger, final ProductProjectionSearch request, final PagedSearchResult<ProductProjection> result) {
        final HttpRequestIntent httpRequest = request.httpRequestIntent();
        final String requestBody = printableRequestBody(httpRequest)
                .map(body -> " with body {" + body + "}")
                .orElse("");
        logger.debug("Fetched {} out of {} products with request {} {}",
                result.size(),
                result.getTotal(),
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
