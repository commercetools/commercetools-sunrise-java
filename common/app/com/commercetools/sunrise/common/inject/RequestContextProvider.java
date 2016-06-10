package com.commercetools.sunrise.common.inject;

import com.commercetools.sunrise.common.contexts.RequestContext;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class RequestContextProvider implements Provider<RequestContext> {

    @Inject
    private Http.Context context;

    @Override
    public RequestContext get() {
        final Map<String, List<String>> queryStringWithList = new HashMap<>();
        context.request().queryString().forEach((key, arrayValue) -> queryStringWithList.put(key, asList(arrayValue)));
        return RequestContext.of(queryStringWithList, context.request().path());
    }
}
